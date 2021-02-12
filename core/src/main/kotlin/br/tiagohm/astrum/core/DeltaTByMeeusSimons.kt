package br.tiagohm.astrum.core

object DeltaTByMeeusSimons : DeltaTAlgorithm {

    override fun compute(jd: Double): Double {
        val year = DateTime.fromJulianDay(jd).year
        val ub = (jd - 2451545.0) / 36525.0

        return when {
            year < 1620 -> 0.0
            year < 1690 -> {
                val u = 3.45 + ub
                (((1244.0 * u - 454.0) * u + 50.0) * u - 107.0) * u + 40.3
            }
            year < 1770 -> {
                val u = 2.70 + ub
                (((70.0 * u - 16.0) * u - 1.0) * u + 11.3) * u + 10.2
            }
            year < 1820 -> {
                val u = 2.05 + ub
                (((6.0 * u + 173.0) * u - 22.0) * u - 18.8) * u + 14.7
            }
            year < 1870 -> {
                val u = 1.55 + ub
                (((-1654.0 * u - 534.0) * u + 111) * u + 12.7) * u + 5.7
            }
            year < 1900 -> {
                val u = 1.15 + ub
                (((8234.0 * u + 101.0) * u + 27.0) * u - 14.6) * u - 5.8
            }
            year < 1940 -> {
                val u = 0.80 + ub
                (((4441.0 * u + 19.0) * u - 443.0) * u + 67.0) * u + 21.4
            }
            year < 1990 -> {
                val u = 0.35 + ub
                (((-1883.0 * u - 140.0) * u + 189.0) * u + 74.0) * u + 36.2
            }
            year <= 2000 -> {
                val u = 0.05 + ub
                ((-5034.0 * u - 188.0) * u + 82.0) * u + 60.8
            }
            else -> 0.0
        }
    }
}