package br.tiagohm.astrum.sky.core.time

import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import kotlin.math.floor

object DateTime {

    private const val JD_GREG_CAL = 2299161L

    /**
     * Gets the UTC offset from system.
     */
    fun currentUTCOffset() = ZoneOffset.systemDefault().rules.getOffset(Instant.now()).totalSeconds / 3600.0

    /**
     * Checks if [year] is a Leap Year.
     */
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

    /**
     * Returns a fractional year like YYYY.ddddd
     */
    fun yearAsFraction(year: Int, month: Int, day: Int): Double {
        val d = dayInYear(year, month, 0) + day
        val daysInYear = if (isLeapYear(year)) 366.0 else 365.0
        return year + d / daysInYear
    }

    /**
     * Finds day number for date in year
     */
    fun dayInYear(year: Int, month: Int, day: Int): Int {
        // Meeus, AA 2nd, 1998, ch.7 p.65
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

    fun fromJulianDay(jd: JulianDay): ZonedDateTime {
        val julian = floor(jd.value + 0.5).toLong()

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

        val frac = jd.value - floor(jd.value)
        val secs = frac * 24.0 * 60.0 * 60.0 + 0.0001 // Add constant to fix floating-point truncation error
        val s = floor(secs).toInt()

        val hour = ((s / (60 * 60)) + 12) % 24
        val minute = (s / (60)) % 60
        val second = s % 60
        val millisecond = floor((secs - floor(secs)) * 1000.0).toInt()

        return ZonedDateTime.of(
            year,
            month,
            day,
            hour,
            minute,
            second,
            millisecond * 1000000,
            ZoneId.of("UTC"),
        )
    }
}