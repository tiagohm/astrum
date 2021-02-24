package br.tiagohm.astrum.sky.planets.major.jupiter

import br.tiagohm.astrum.sky.Observer
import br.tiagohm.astrum.sky.PlanetType
import br.tiagohm.astrum.sky.core.math.Triad
import br.tiagohm.astrum.sky.core.units.angle.Angle
import br.tiagohm.astrum.sky.core.units.angle.Radians
import br.tiagohm.astrum.sky.core.units.distance.Kilometer
import br.tiagohm.astrum.sky.planets.ApparentMagnitudeAlgorithm
import br.tiagohm.astrum.sky.planets.Planet

class Ganymede(parent: Jupiter) : Planet(
    "Ganymede",
    Kilometer(2631.2).au,
    0.0,
    0.43,
    null,
    PlanetType.MOON,
    parent,
) {

    override val siderealDay = 7.15455311954703188684

    override val siderealPeriod = siderealDay

    override val absoluteMagnitude = -2.09

    override val meanOppositionMagnitude = 4.61

    override fun computePosition(jde: Double): Pair<Triad, Triad> {
        val xyz = computeJupiterSatHeliocentricCoordinates(jde, 2)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }

    override fun computeRotObliquity(jde: Double) = Radians(0.03709159998168787)

    override fun computeVisualMagnitude(
        o: Observer,
        phaseAngle: Angle,
        cosChi: Double,
        observerRq: Double,
        planetRq: Double,
        observerPlanetRq: Double,
        d: Double,
        shadowFactor: Double
    ): Double {
        return when (o.apparentMagnitudeAlgorithm) {
            ApparentMagnitudeAlgorithm.EXPLANATORY_SUPPLEMENT_2013 -> {
                val phaseDeg = phaseAngle.degrees.value
                val mag = -2.09 + d + phaseDeg * (0.0323 - 0.00066 * phaseDeg)
                if (shadowFactor < 1.0) 13.0 * (1.0 - shadowFactor) + mag else mag
            }
            else -> super.computeVisualMagnitude(o, phaseAngle, cosChi, observerRq, planetRq, observerPlanetRq, d, shadowFactor)
        }
    }
}