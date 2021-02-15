package br.tiagohm.astrum.core.sky.planets.major.earth

import br.tiagohm.astrum.core.*
import br.tiagohm.astrum.core.algorithms.Algorithms
import br.tiagohm.astrum.core.algorithms.math.Duad
import br.tiagohm.astrum.core.algorithms.math.Triad
import br.tiagohm.astrum.core.sky.PlanetType
import br.tiagohm.astrum.core.sky.planets.Planet
import br.tiagohm.astrum.core.sky.planets.Sun
import kotlin.math.*

class Moon(parent: Earth) : Planet(
    "Moon",
    1737.4 / AU,
    0.0,
    0.12,
    null,
    PlanetType.MOON,
    parent,
) {

    override val siderealDay = 27.32166171424233789516

    override val siderealPeriod = siderealDay

    override val absoluteMagnitude = 0.21

    override val meanOppositionMagnitude = -12.74

    override fun computePosition(jde: Double): Pair<Triad, Triad> {
        val xyz = computeMoonHeliocentricCoordinates(jde)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }

    override fun computeRotObliquity(jde: Double) = 3.7723828609181886E-4

    override fun internalComputeRTSTime(o: Observer, hz: Radians, hasAtmosphere: Boolean): Triad {
        return super.internalComputeRTSTime(o, hz + 0.7275 * 0.95 * M_PI_180, hasAtmosphere)
    }

    private fun computeAge(o: Observer): Double {
        val op = o.copy(useTopocentricCoordinates = false)
        val eclJDE = parent!!.computeRotObliquity(o.jde)
        val (raMoon, decMoon) = Algorithms.rectangularToSphericalCoordinates(computeEquinoxEquatorialPosition(op))
        val (lambdaMoon) = Algorithms.equatorialToEcliptic(raMoon, decMoon, eclJDE)
        val (raSun, decSun) = Algorithms.rectangularToSphericalCoordinates(parent.parent!!.computeEquinoxEquatorialPosition(op))
        val (lambdaSun) = Algorithms.equatorialToEcliptic(raSun, decSun, eclJDE)
        return (lambdaMoon - lambdaSun).pmod(M_2_PI)
    }

    fun lunarPhase(o: Observer): LunarPhase {
        val delta = computeAge(o).deg

        return when {
            delta < 0.5 || delta > 359.5 -> LunarPhase.NEW_MOON
            delta < 89.5 -> LunarPhase.WAXING_CRESCENT
            delta < 90.5 -> LunarPhase.FIRST_QUARTER
            delta < 179.5 -> LunarPhase.WAXING_GIBBOUS
            delta < 180.5 -> LunarPhase.FULL_MOON
            delta < 269.5 -> LunarPhase.WANING_GIBBOUS
            delta < 270.5 -> LunarPhase.THIRD_QUARTER
            delta < 359.5 -> LunarPhase.WANING_CRESCENT
            else -> throw IllegalStateException("Error in lunar phase")
        }
    }

    /**
     * Computes Penumbral eclipse magnitude and Umbral eclipse magnitude.
     * Returns ZERO if lunar eclipse is not occorring.
     */
    fun lunarEclipse(o: Observer): Duad {
        val op = o.copy(useTopocentricCoordinates = false)
        val sun = parent!!.parent!! as Sun
        val mEquPos = computeEquinoxEquatorialPosition(op)
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

            if (pMag > 1E-6 || uMag > 1E-6) return Duad(pMag, max(0.0, uMag))
        }

        return Duad.ZERO
    }

    fun age(o: Observer) = computeAge(o) * 29.530588853 / M_2_PI
}