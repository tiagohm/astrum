package br.tiagohm.astrum.sky.planets.major.earth

import br.tiagohm.astrum.sky.*
import br.tiagohm.astrum.sky.algorithms.Algorithms
import br.tiagohm.astrum.sky.planets.Sun
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class LunarEclipse(
    val penumbralMagnitude: Double = 0.0,
    val umbralMagnitude: Double = 0.0,
) {

    val isEclipsing = penumbralMagnitude > 1E-6 || umbralMagnitude > 1E-6

    companion object {

        /**
         * Computes Penumbral eclipse magnitude and Umbral eclipse magnitude.
         */
        fun compute(
            o: Observer,
            moon: Moon,
        ): LunarEclipse {
            val op = o.copy(useTopocentricCoordinates = false)
            val sun = moon.parent!!.parent!! as Sun
            val mEquPos = moon.computeEquinoxEquatorialPosition(op)
            val sEquPos = sun.computeEquinoxEquatorialPosition(op)
            var (raMoon, decMoon) = Algorithms.rectangularToSphericalCoordinates(mEquPos)
            val (raSun, decSun) = Algorithms.rectangularToSphericalCoordinates(sEquPos)

            // R.A. of Earth's shadow
            val raShadow = (raSun + M_PI).let { if (it < 0) it + M_2_PI else it }
            // Dec. of Earth's shadow
            val decShadow = -decSun

            raMoon = if (raMoon < 0) raMoon + M_2_PI else raMoon

            val raDiff = (raMoon - raShadow).deg.let { if (it < 0) it + 360.0 else it }

            if (raDiff < 3 || raDiff > 357) {
                val sdistanceAu = sEquPos.length
                val mdistanceKm = mEquPos.length * AU
                // Moon's distance in Earth's radius
                val mdistanceER = mdistanceKm / 6378.1366

                // Sun's horizontal parallax
                val sHP = 3600.0 * asin(6378.1366 / (AU * sdistanceAu)).deg
                // Sun's semi-diameter
                val sSD = 959.64 / sdistanceAu
                // Moon's horizontal parallax
                val mHP = 3600.0 * asin(1.0 / mdistanceER).deg
                // Moon's semi-diameter
                // 0.272488 is Moon/Earth's radius
                val mSD = 3600.0 * asin(0.272488 / mdistanceER).deg

                // Besselian elements
                // ref: Explanatory supplement to the astronomical ephemeris
                // and the American ephemeris and nautical almanac (1961)

                val p1 = (1.0 + 1.0 / 85.0 - 1.0 / 594.0) * mHP

                // Danjon's method - used in French almanac and NASA web site.
                // It's the enlargment of Earth's shadows due to Earth's atmosphere
                // and correction for Earth's oblateness at latitude 45 deg.
                // ref: Five Millennium Catalog of Lunar Eclipses: -1999 to +3000 (Fred Espenak, NASA)
                // Note: Astronomical Almanac using different value which create a bit larger shadows.

                val f1 = p1 + sSD + sHP // Radius of umbra at the distance of the Moon
                val f2 = p1 - sSD + sHP // Radius of penumbra at the distance of the Moon

                val x = 3600.0 * asin(cos(decMoon) * sin(raMoon - raShadow)).deg
                val y = 3600.0 * asin(cos(decShadow) * sin(decMoon) - sin(decShadow) * cos(decMoon) * cos(raMoon - raShadow)).deg
                val L1 = f1 + mSD // Distance between center of the Moon and shadow at beginning and end of penumbral eclipse
                val L2 = f2 + mSD // Distance between center of the Moon and shadow at beginning and end of partial eclipse

                val m = sqrt(x * x + y * y)
                val pMag = (L1 - m) / (2.0 * mSD) // Penumbral magnitude
                val uMag = (L2 - m) / (2.0 * mSD) // Umbral magnitude

                if (pMag > 1E-6 || uMag > 1E-6) return LunarEclipse(pMag, uMag)
            }

            return NONE
        }

        val NONE = LunarEclipse()
    }
}