package br.tiagohm.astrum.sky.planets.major.jupiter

import br.tiagohm.astrum.sky.Observer
import br.tiagohm.astrum.sky.PlanetType
import br.tiagohm.astrum.sky.core.math.Triad
import br.tiagohm.astrum.sky.core.units.angle.Angle
import br.tiagohm.astrum.sky.core.units.angle.Radians
import br.tiagohm.astrum.sky.core.units.distance.Kilometer
import br.tiagohm.astrum.sky.planets.ApparentMagnitudeAlgorithm
import br.tiagohm.astrum.sky.planets.Planet

class Callisto(parent: Jupiter) : Planet(
    "Callisto",
    Kilometer(2410.3),
    0.0,
    0.17,
    null,
    PlanetType.MOON,
    parent,
) {

    override val siderealDay = 16.68901797483727222359

    override val siderealPeriod = siderealDay

    override val absoluteMagnitude = -1.05

    override val meanOppositionMagnitude = 5.65

    override fun computePosition(jde: Double): Pair<Triad, Triad> {
        val xyz = computeJupiterSatHeliocentricCoordinates(jde, 3)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }

    override fun computeRotObliquity(jde: Double) = Radians(0.03157353427088316)

    override fun computeRotAscendingNode() = Radians(-0.3056868358604381)

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
                val mag = -1.05 + d + phaseDeg * (0.078 - 0.00274 * phaseDeg)
                if (shadowFactor < 1.0) 13.0 * (1.0 - shadowFactor) + mag else mag
            }
            else -> super.computeVisualMagnitude(o, phaseAngle, cosChi, observerRq, planetRq, observerPlanetRq, d, shadowFactor)
        }
    }
}