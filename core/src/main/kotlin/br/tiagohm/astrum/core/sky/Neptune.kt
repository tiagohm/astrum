package br.tiagohm.astrum.core.sky

import br.tiagohm.astrum.core.Consts
import br.tiagohm.astrum.core.Observer
import br.tiagohm.astrum.core.algorithms.ApparentMagnitudeAlgorithm
import br.tiagohm.astrum.core.math.Triad

class Neptune(parent: Sun) : Planet(
    "Neptune",
    24764.0 / Consts.AU,
    0.01708124697,
    0.62,
    null,
    PlanetType.PLANET,
    parent,
) {

    override val siderealDay = 0.671249999952453125

    override val siderealPeriod = 60189.0

    override val absoluteMagnitude = -6.87

    override val meanOppositionMagnitude = 7.84

    override fun computePosition(jde: Double): Pair<Triad, Triad> {
        val xyz = computePlanetHeliocentricCoordinates(jde, 7)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }

    override fun computeRotObliquity(jde: Double) = 0.489152978736078

    override fun computeVisualMagnitude(
        o: Observer,
        phaseAngle: Double,
        cosChi: Double,
        observerRq: Double,
        planetRq: Double,
        observerPlanetRq: Double,
        d: Double,
        shadowFactor: Double,
    ): Double {
        return when (o.apparentMagnitudeAlgorithm) {
            ApparentMagnitudeAlgorithm.EXPLANATORY_SUPPLEMENT_2013,
            ApparentMagnitudeAlgorithm.EXPLANATORY_SUPPLEMENT_1992 -> -6.87 + d
            ApparentMagnitudeAlgorithm.MUELLER_1893 -> -7.05 + d
            ApparentMagnitudeAlgorithm.ASTRONOMICAL_ALMANAC_1984 -> -6.87 + d
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