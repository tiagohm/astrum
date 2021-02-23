package br.tiagohm.astrum.sky.algorithms.time

import java.time.Instant
import java.time.ZoneOffset
import java.util.*
import kotlin.math.floor

data class DateTime(
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int = 0,
    val minute: Int = 0,
    val second: Int = 0,
    val millisecond: Int = 0,
    val utcOffset: Double = currentUTCOffset,
) {

    val isLepYear by lazy { isLeapYear(year) }

    val jd by lazy { computeJDFromDateTime(this) }

    companion object {

        val currentUTCOffset: Double
            get() = ZoneOffset.systemDefault().rules.getOffset(Instant.now()).totalSeconds / 3600.0

        fun now(): DateTime = from(Calendar.getInstance())

        fun from(c: Calendar): DateTime {
            return DateTime(
                c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DATE),
                c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND),
                c.get(Calendar.MILLISECOND), currentUTCOffset
            )
        }

        fun from(timeInMillis: Long) = from(Calendar.getInstance().also { it.timeInMillis = timeInMillis })

        fun isLeapYear(year: Int): Boolean {
            return if (year > 1582) {
                when {
                    year % 400 == 0 -> true
                    year % 100 == 0 -> false
                    else -> year % 4 == 0
                }
            } else {
                year % 4 == 0
            }
        }

        // Returns a fractional year like YYYY.ddddd
        fun yearAsFraction(year: Int, month: Int, day: Int): Double {
            val d = dayInYear(year, month, 0) + day
            val daysInYear = if (isLeapYear(year)) 366.0 else 365.0
            return year + d / daysInYear
        }

        // Finds day number for date in year
        // Meeus, AA 2nd, 1998, ch.7 p.65
        fun dayInYear(year: Int, month: Int, day: Int): Int {
            val k = if (isLeapYear(year)) 1 else 2
            return (275 * month / 9) - k * ((month + 9) / 12) + day - 30
        }

        fun numberOfDaysInMonthInYear(year: Int, month: Int): Int {
            return when (month) {
                1, 3, 5, 7, 8, 10, 12 -> 31
                4, 6, 9, 11 -> 30
                2 -> if (isLeapYear(year)) 29 else 28
                0 -> numberOfDaysInMonthInYear(year - 1, 12)
                13 -> numberOfDaysInMonthInYear(year + 1, 1)
                else -> 0
            }
        }

        fun computeJDFromDateTime(dt: DateTime): Double {
            return computeJDFromDate(
                dt.year, dt.month, dt.day,
                dt.hour, dt.minute, dt.second, dt.millisecond,
                dt.utcOffset
            )
        }

        fun computeJDFromDate(
            year: Int,
            month: Int,
            day: Int,
            hour: Int,
            minute: Int,
            second: Int,
            millisecond: Int,
            utcOffset: Double,
        ): Double {
            val deltaTime = (hour / 24.0) +
                    (minute / (24.0 * 60.0)) +
                    (second.toDouble() / (24.0 * 60.0 * 60.0)) +
                    (millisecond.toDouble() / (24.0 * 60.0 * 60.0 * 1000.0)) +
                    -0.5

            var jy = year

            val jm = if (month > 2) {
                month + 1
            } else {
                --jy
                month + 13
            }

            var laa = 1461 * jy / 4

            if (jy < 0 && jy % 4 != 0) {
                --laa
            }

            val lbb = 306001 * jm / 10000
            var ljul = laa + lbb + day + 1720995

            if (day + 31L * (month + 12L * year) >= IGREG2) {
                var lcc = jy / 100

                if (jy < 0 && jy % 100 != 0) {
                    --lcc
                }

                var lee = lcc / 4

                if (lcc < 0 && lcc % 4 != 0) {
                    --lee
                }

                ljul += 2 - lcc + lee
            }

            return (ljul + deltaTime) - (utcOffset / 24.0)
        }

        fun computeJDFromBesselianEpoch(epoch: Double): Double {
            return 2400000.5 + (15019.81352 + (epoch - 1900.0) * 365.242198781)
        }

        fun fromJulianDay(jd: Double, hasTime: Boolean = true): DateTime {
            val julian = floor(jd + 0.5).toLong()

            val ta = when {
                julian >= JD_GREG_CAL -> {
                    val jalpha = (4 * (julian - 1867216) - 1) / 146097
                    julian + 1 + jalpha - jalpha / 4
                }
                julian < 0 -> julian + 36525 * (1 - julian / 36525)
                else -> julian
            }

            val tb = ta + 1524
            val tc = (tb * 20 - 2442) / 7305
            val td = 365 * tc + tc / 4
            val te = ((tb - td) * 10000) / 306001

            val day = (tb - td - (306001 * te) / 10000).toInt()

            var month = (te - 1).toInt()

            if (month > 12) {
                month -= 12
            }

            var year = (tc - 4715).toInt()

            if (month > 2) {
                year--
            }

            if (julian < 0) {
                year -= 100 * (1 - julian / 36525).toInt()
            }

            return if (hasTime) {
                val frac = jd - floor(jd)
                val secs = frac * 24.0 * 60.0 * 60.0 + 0.0001 // Add constant to fix floating-point truncation error
                val s = floor(secs).toInt()

                val hour = ((s / (60 * 60)) + 12) % 24
                val minute = (s / (60)) % 60
                val second = s % 60
                val millisecond = floor((secs - floor(secs)) * 1000.0).toInt()

                DateTime(year, month, day, hour, minute, second, millisecond)
            } else {
                DateTime(year, month, day)
            }
        }

        private const val JD_GREG_CAL = 2299161L
        private const val IGREG2 = 15 + 31L * (10 + 12L * 1582)
    }
}