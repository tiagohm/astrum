package br.tiagohm.astrum.core

object DeltaTByEspenakMeeus : DeltaTAlgorithm {

    override fun compute(jd: Double): Double {
        // Note: the method here is adapted from
        // "Five Millennium Canon of Solar Eclipses" [Espenak and Meeus, 2006]
        // A summary is described here:
        // http://eclipse.gsfc.nasa.gov/SEhelp/deltatpoly2004.html

        val (year, month, day) = DateTime.fromJulianDay(jd)
        val y = DateTime.yearAsFraction(year, month, day)

        // Set the default value for Delta T
        var u = (y - 1820) / 100.0
        val r = -20.0 + 32.0 * u * u

        return when {
            y < -500 -> r
            y < 500 -> {
                u = y / 100
                (((((0.0090316521 * u + 0.022174192) * u - 0.1798452) * u - 5.952053) *
                        u + 33.78311) * u - 1014.41) * u + 10583.6
            }
            y < 1600 -> {
                u = (y - 1000) / 100
                (((((0.0083572073 * u - 0.005050998) * u - 0.8503463) * u + 0.319781) *
                        u + 71.23472) * u - 556.01) * u + 1574.2
            }
            y < 1700 -> {
                val t = y - 1600
                ((t / 7129.0 - 0.01532) * t - 0.9808) * t + 120.0
            }
            y < 1800 -> {
                val t = y - 1700
                (((-t / 1174000.0 + 0.00013336) * t - 0.0059285) * t + 0.1603) * t + 8.83
            }
            y < 1860 -> {
                val t = y - 1800
                ((((((.000000000875 * t - .0000001699) * t + 0.0000121272) * t - 0.00037436) *
                        t + 0.0041116) * t + 0.0068612) * t - 0.332447) * t + 13.72
            }
            y < 1900 -> {
                val t = y - 1860
                ((((t / 233174.0 - 0.0004473624) * t + 0.01680668) * t - 0.251754) * t + 0.5737) * t + 7.62
            }
            y < 1920 -> {
                val t = y - 1900
                (((-0.000197 * t + 0.0061966) * t - 0.0598939) * t + 1.494119) * t - 2.79
            }
            y < 1941 -> {
                val t = y - 1920
                ((0.0020936 * t - 0.076100) * t + 0.84493) * t + 21.20
            }
            y < 1961 -> {
                val t = y - 1950
                ((t / 2547.0 - 1.0 / 233.0) * t + 0.407) * t + 29.07
            }
            y < 1986 -> {
                val t = y - 1975
                ((-t / 718.0 - 1 / 260.0) * t + 1.067) * t + 45.45
            }
            y < 2005 -> {
                val t = y - 2000
                ((((0.00002373599 * t + 0.000651814) * t + 0.0017275) * t - 0.060374) * t + 0.3345) * t + 63.86
            }
            y < 2050 -> {
                val t = y - 2000
                (0.005589 * t + 0.32217) * t + 62.92
            }
            y < 2150 -> {
                // r has been precomputed before, just add the term patching the discontinuity
                r - 0.5628 * (2150.0 - y)
            }
            else -> r
        }
    }
}