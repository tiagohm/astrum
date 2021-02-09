package br.tiagohm.astrum.core.sky

import br.tiagohm.astrum.core.Consts
import br.tiagohm.astrum.core.Observer
import br.tiagohm.astrum.core.algorithms.ApparentMagnitudeAlgorithm
import br.tiagohm.astrum.core.deg
import br.tiagohm.astrum.core.math.Triad

class Uranus(parent: Sun) : Planet(
    "Uranus",
    25559.0 / Consts.AU,
    0.0229273446,
    0.66,
    null,
    PlanetType.PLANET,
    parent,
) {

    override val siderealDay = -0.71833333334397530864

    override val siderealPeriod = 30685.0

    override val absoluteMagnitude = -7.19

    override val meanOppositionMagnitude = 5.52

    override fun computePosition(jde: Double): Pair<Triad, Triad> {
        val xyz = computePlanetHeliocentricCoordinates(jde, 6)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }

    override fun computeRotObliquity(jde: Double) = 1.4360256624251349

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
        val phaseDeg = phaseAngle.deg

        return when (o.apparentMagnitudeAlgorithm) {
            ApparentMagnitudeAlgorithm.EXPLANATORY_SUPPLEMENT_2013 -> {
                -7.19 + d + 0.002 * phaseDeg
            }
            ApparentMagnitudeAlgorithm.EXPLANATORY_SUPPLEMENT_1992 -> {
                -7.19 + d + 0.0028 * phaseDeg
            }
            ApparentMagnitudeAlgorithm.MUELLER_1893 -> -6.85 + d
            ApparentMagnitudeAlgorithm.ASTRONOMICAL_ALMANAC_1984 -> -7.19 + d
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