package br.tiagohm.astrum.sky.core.time

import br.tiagohm.astrum.common.JD_HOUR
import br.tiagohm.astrum.common.JD_MINUTE
import br.tiagohm.astrum.common.JD_SECOND
import br.tiagohm.astrum.sky.planets.major.earth.Earth
import java.io.IOException
import java.time.ZonedDateTime

@Suppress("EXPERIMENTAL_FEATURE_WARNING", "RESERVED_MEMBER_INSIDE_INLINE_CLASS", "NOTHING_TO_INLINE")
inline class JulianDay(val value: Double) {

    constructor(
        year: Int,
        month: Int,
        day: Int,
        hour: Int = 0,
        minute: Int = 0,
        second: Int = 0,
        millisecond: Int = 0,
        utcOffset: Double = DateTime.currentUTCOffset(),
    ) : this(computeJDFromDate(year, month, day, hour, minute, second, millisecond, utcOffset))

    constructor(dateTime: ZonedDateTime) : this(
        dateTime.year,
        dateTime.monthValue,
        dateTime.dayOfMonth,
        dateTime.hour,
        dateTime.minute,
        dateTime.second,
        dateTime.nano / 1000000,
        dateTime.offset.totalSeconds / 3600.0,
    )

    fun toDateTime() = DateTime.fromJulianDay(this)

    inline fun addSolarDay(n: Double) = JulianDay(value + n * Earth.MEAN_SOLAR_DAY)

    inline fun addSecond(n: Double = 1.0) = addSolarDay(JD_SECOND * n)

    inline fun addMinute(n: Double = 1.0) = addSolarDay(JD_MINUTE * n)

    inline fun addHour(n: Double = 1.0) = addSolarDay(JD_HOUR * n)

    inline fun addDay(n: Double = 1.0) = addSolarDay(n)

    inline fun addWeek(n: Double = 1.0) = addSolarDay(7.0 * n)

    inline fun addSiderealDay(n: Double = 1.0) = JulianDay(value + n * Earth.SIDEREAL_DAY)

    inline fun addSiderealWeek(n: Double = 1.0) = addSiderealDay(7.0 * n)

    inline fun addSiderealYear(n: Double = 1.0) = addSolarDay(n * Earth.SIDEREAL_PERIOD)

    inline fun addSynodicMonth(n: Double = 1.0) = addSolarDay(n * 29.530588853)

    inline fun addSaros(n: Double = 1.0) = addSynodicMonth(n * 223.0)

    inline fun addDraconicMonth(n: Double = 1.0) = addSolarDay(n * 27.212220817)

    inline fun addMeanTropicalMonth(n: Double = 1.0) = addSolarDay(n * 27.321582241)

    inline fun addAnomalisticMonth(n: Double = 1.0) = addSolarDay(n * 27.554549878)

    inline fun addAnomalisticYear(n: Double = 1.0) = addSolarDay(n * 365.259636)

    inline fun addDraconicYear(n: Double = 1.0) = addSolarDay(n * 346.620075883)

    fun addTropicalYear(n: Double = 1.0): JulianDay {
        // Source: J. Meeus. More Mathematical Astronomy Morsels. 2002, p. 358.
        // Meeus, J. & Savoie, D. The history of the tropical year. Journal of the British Astronomical Association, vol.102, no.1, p.40-42
        // http://articles.adsabs.harvard.edu//full/1992JBAA..102...40M
        val T = (value - 2451545.0) / 365250.0
        return addSolarDay(n * (365.242189623 - T * (0.000061522 - T * (0.0000000609 + T * 0.00000026525))))
    }

    // Source: https://en.wikipedia.org/wiki/Tropical_year#Mean_tropical_year_current_value
    // The mean tropical year on January 1, 2000
    inline fun addMeanTropicalYear(n: Double = 1.0) = addSolarDay(n * 365.2421897)

    inline fun addJulianYear(n: Double = 1.0) = addSolarDay(n * 365.25)

    inline fun addGaussianYear(n: Double = 1.0) = addSolarDay(n * 365.2568983)

    inline fun subtractSolarDay(n: Double) = JulianDay(value - n * Earth.MEAN_SOLAR_DAY)

    inline fun subtractSecond(n: Double = 1.0) = subtractSolarDay(JD_SECOND * n)

    inline fun subtractMinute(n: Double = 1.0) = subtractSolarDay(JD_MINUTE * n)

    inline fun subtractHour(n: Double = 1.0) = subtractSolarDay(JD_HOUR * n)

    inline fun subtractDay(n: Double = 1.0) = subtractSolarDay(n)

    inline fun subtractWeek(n: Double = 1.0) = subtractSolarDay(7.0 * n)

    inline fun subtractSiderealDay(n: Double = 1.0) = JulianDay(value - n * Earth.SIDEREAL_DAY)

    inline fun subtractSiderealWeek(n: Double = 1.0) = subtractSiderealDay(7.0 * n)

    inline fun subtractSiderealYear(n: Double = 1.0) = subtractSolarDay(n * Earth.SIDEREAL_PERIOD)

    inline fun subtractSynodicMonth(n: Double = 1.0) = subtractSolarDay(n * 29.530588853)

    inline fun subtractSaros(n: Double = 1.0) = subtractSynodicMonth(n * 223.0)

    inline fun subtractDraconicMonth(n: Double = 1.0) = subtractSolarDay(n * 27.212220817)

    inline fun subtractMeanTropicalMonth(n: Double = 1.0) = subtractSolarDay(n * 27.321582241)

    inline fun subtractAnomalisticMonth(n: Double = 1.0) = subtractSolarDay(n * 27.554549878)

    inline fun subtractAnomalisticYear(n: Double = 1.0) = subtractSolarDay(n * 365.259636)

    inline fun subtractDraconicYear(n: Double = 1.0) = subtractSolarDay(n * 346.620075883)

    fun subtractTropicalYear(n: Double = 1.0) = addTropicalYear(-n)

    inline fun subtractMeanTropicalYear(n: Double = 1.0) = subtractSolarDay(n * 365.2421897)

    inline fun subtractJulianYear(n: Double = 1.0) = subtractSolarDay(n * 365.25)

    inline fun subtractGaussianYear(n: Double = 1.0) = subtractSolarDay(n * 365.2568983)

    inline operator fun plus(jd: JulianDay) = JulianDay(value + jd.value)

    inline operator fun plus(n: Number) = JulianDay(value + n.toDouble())

    inline operator fun minus(jd: JulianDay) = JulianDay(value - jd.value)

    inline operator fun minus(n: Number) = JulianDay(value - n.toDouble())

    inline operator fun times(n: Number) = JulianDay(value * n.toDouble())

    inline operator fun div(n: Number) = JulianDay(value / n.toDouble())

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as JulianDay

        if (value != other.value) return false

        return true
    }

    override fun hashCode() = value.hashCode()

    companion object {

        private const val IGREG2 = 15 + 31L * (10 + 12L * 1582)

        fun fromBesselianEpoch(epoch: Double) = JulianDay(2400000.5 + (15019.81352 + (epoch - 1900.0) * 365.242198781))

        fun fromUnix(timestampInMs: Long) = JulianDay((timestampInMs / 86400000.0) + 2440587.5)

        fun now() = fromUnix(System.currentTimeMillis())

        private val PACKED_EPOCH_REGEX = Regex("^([IJK])(\\d\\d)([1-9A-C])([1-9A-V])\$")

        fun fromMPCEpoch(epoch: String): JulianDay {
            val epochM = PACKED_EPOCH_REGEX.matchEntire(epoch) ?: throw IOException("Invalid epoch format: $epoch")

            fun unpackDayOrMonthNumber(digit: Char) = when (val d = digit.toInt()) {
                in 0..9 -> d
                in 65..86 -> 10 + (d - 65)
                else -> 0
            }

            fun unpackYearNumber(digit: Char) = when (digit) {
                'I' -> 1800
                'J' -> 1900
                else -> 2000
            }

            val year = epochM.groupValues[2].toInt() + unpackYearNumber(epochM.groupValues[1][0])
            val month = unpackDayOrMonthNumber(epochM.groupValues[3][0])
            val day = unpackDayOrMonthNumber(epochM.groupValues[4][0])

            // Epoch is at .0 TT, i.e. midnight
            return JulianDay(year, month, day, 0, 0, 0, 0, 0.0)
        }

        private fun computeJDFromDate(
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
    }
}