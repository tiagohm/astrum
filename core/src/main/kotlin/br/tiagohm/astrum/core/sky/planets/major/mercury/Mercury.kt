package br.tiagohm.astrum.core.sky.planets.major.mercury

import br.tiagohm.astrum.core.AU
import br.tiagohm.astrum.core.Observer
import br.tiagohm.astrum.core.Radians
import br.tiagohm.astrum.core.algorithms.math.Triad
import br.tiagohm.astrum.core.deg
import br.tiagohm.astrum.core.sky.PlanetType
import br.tiagohm.astrum.core.sky.atmosphere.ApparentMagnitudeAlgorithm
import br.tiagohm.astrum.core.sky.planets.Planet
import br.tiagohm.astrum.core.sky.planets.Sun

class Mercury(parent: Sun) : Planet(
    "Mercury",
    2440.53 / AU,
    0.0009301258,
    0.06,
    null,
    PlanetType.PLANET,
    parent,
) {

    override val siderealDay = 58.64614590235794649087

    override val siderealPeriod = 87.97

    override val absoluteMagnitude = -0.60

    override fun computePosition(jde: Double): Pair<Triad, Triad> {
        val xyz = computePlanetHeliocentricCoordinates(jde, 0)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }

    override fun computeRotObliquity(jde: Double) = 0.1228178112752234

    override fun computeVisualMagnitude(
        o: Observer,
        phaseAngle: Radians,
        cosChi: Double,
        observerRq: Double,
        planetRq: Double,
        observerPlanetRq: Double,
        d: Double,
        shadowFactor: Double,
    ): Double {
        val phaseDeg = phaseAngle.deg

        return when (o.apparentMagnitudeAlgorithm) {
            ApparentMagnitudeAlgorithm.EXPLANATORY_SUPPLEMENT_2013 -> {
                -0.6 + d + (((3.02E-6 * phaseDeg - 0.000488) * phaseDeg + 0.0498) * phaseDeg)
            }
            ApparentMagnitudeAlgorithm.EXPLANATORY_SUPPLEMENT_1992 -> {
                var f1 = phaseDeg / 100.0
                if (phaseDeg > 150.0) f1 = 1.5
                -0.36 + d + 3.8 * f1 - 2.73 * f1 * f1 + 2 * f1 * f1 * f1
            }
            ApparentMagnitudeAlgorithm.MUELLER_1893 -> {
                val ph50 = phaseDeg - 50.0
                1.16 + d + 0.02838 * ph50 + 0.0001023 * ph50 * ph50
            }
            ApparentMagnitudeAlgorithm.ASTRONOMICAL_ALMANAC_1984 -> {
                -0.42 + d + .038 * phaseDeg - 0.000273 * phaseDeg * phaseDeg + 0.000002 * phaseDeg * phaseDeg * phaseDeg
            }
            // Calculate the visual magnitude from phase angle and albedo of the planet
            else -> super.computeVisualMagnitude(
                o,
                phaseAngle,
                cosChi,
                observerRq,
                planetRq,
                observerPlanetRq,
                d,
                shadowFactor,
            )
        }
    }
}