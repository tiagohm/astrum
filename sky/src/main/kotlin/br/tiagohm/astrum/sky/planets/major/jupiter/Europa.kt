package br.tiagohm.astrum.sky.planets.major.jupiter

import br.tiagohm.astrum.sky.Observer
import br.tiagohm.astrum.sky.PlanetType
import br.tiagohm.astrum.sky.core.math.Triad
import br.tiagohm.astrum.sky.core.units.angle.Angle
import br.tiagohm.astrum.sky.core.units.angle.Radians
import br.tiagohm.astrum.sky.core.units.distance.Kilometer
import br.tiagohm.astrum.sky.planets.ApparentMagnitudeAlgorithm
import br.tiagohm.astrum.sky.planets.Planet

class Europa(parent: Jupiter) : Planet(
    "Europa",
    Kilometer(1560.8).au,
    0.0,
    0.67,
    null,
    PlanetType.MOON,
    parent,
) {

    override val siderealDay = 3.55118107917700017204

    override val siderealPeriod = siderealDay

    override val absoluteMagnitude = -1.41

    override val meanOppositionMagnitude = 5.29

    override fun computePosition(jde: Double): Pair<Triad, Triad> {
        val xyz = computeJupiterSatHeliocentricCoordinates(jde, 1)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }

    override fun computeRotObliquity(jde: Double) = Radians(0.03838365303405089)

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
                val mag = -1.41 + d + phaseDeg * (0.0312 - 0.00125 * phaseDeg)
                if (shadowFactor < 1.0) 13.0 * (1.0 - shadowFactor) + mag else mag
            }
            else -> super.computeVisualMagnitude(o, phaseAngle, cosChi, observerRq, planetRq, observerPlanetRq, d, shadowFactor)
        }
    }
}