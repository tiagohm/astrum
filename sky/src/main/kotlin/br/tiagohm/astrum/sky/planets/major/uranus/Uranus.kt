package br.tiagohm.astrum.sky.planets.major.uranus

import br.tiagohm.astrum.sky.AU
import br.tiagohm.astrum.sky.Observer
import br.tiagohm.astrum.sky.PlanetType
import br.tiagohm.astrum.sky.algorithms.math.Triad
import br.tiagohm.astrum.sky.deg
import br.tiagohm.astrum.sky.planets.ApparentMagnitudeAlgorithm
import br.tiagohm.astrum.sky.planets.Planet
import br.tiagohm.astrum.sky.planets.Ring
import br.tiagohm.astrum.sky.planets.Sun

class Uranus(parent: Sun) : Planet(
    "Uranus",
    25559.0 / AU,
    0.0229273446,
    0.66,
    null,
    PlanetType.PLANET,
    parent,
    Ring(26840.0 / AU, 97700.0 / AU),
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