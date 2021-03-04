package br.tiagohm.astrum.sky.core.time

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

    fun toCalendar() = DateTime.fromJulianDay(this)

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