package br.tiagohm.astrum.sky.planets.major.earth

import br.tiagohm.astrum.sky.*
import br.tiagohm.astrum.sky.core.Algorithms
import br.tiagohm.astrum.sky.core.coordinates.Equatorial
import br.tiagohm.astrum.sky.core.math.Triad
import br.tiagohm.astrum.sky.core.units.Radians
import br.tiagohm.astrum.sky.planets.Planet

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

    override fun computeRotObliquity(jde: Double) = Radians(3.7723828609181886E-4)

    override fun internalComputeRTSTime(o: Observer, hz: Radians, hasAtmosphere: Boolean): Triad {
        return super.internalComputeRTSTime(o, hz + 0.7275 * 0.95 * M_PI_180, hasAtmosphere)
    }

    private fun computeAge(o: Observer): Radians {
        val op = o.copy(useTopocentricCoordinates = false)
        val eclJDE = parent!!.computeRotObliquity(o.jde)
        val (raMoon, decMoon) = Algorithms.rectangularToSphericalCoordinates(computeEquinoxEquatorialPosition(op))
        val equMoon = Equatorial(raMoon, decMoon)
        val (lambdaMoon) = equMoon.toEcliptic(eclJDE)
        val (raSun, decSun) = Algorithms.rectangularToSphericalCoordinates(parent.parent!!.computeEquinoxEquatorialPosition(op))
        val equSun = Equatorial(raSun, decSun)
        val (lambdaSun) = equSun.toEcliptic(eclJDE)
        return (lambdaMoon - lambdaSun).normalized
    }

    fun lunarPhase(o: Observer): LunarPhase {
        val delta = computeAge(o).degrees.value

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

    fun age(o: Observer) = computeAge(o) * 29.530588853 / M_2_PI
}