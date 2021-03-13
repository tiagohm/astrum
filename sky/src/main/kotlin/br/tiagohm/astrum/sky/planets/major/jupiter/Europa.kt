package br.tiagohm.astrum.sky.planets.major.jupiter

import br.tiagohm.astrum.common.units.angle.Angle
import br.tiagohm.astrum.common.units.angle.Radians
import br.tiagohm.astrum.common.units.distance.Kilometer
import br.tiagohm.astrum.sky.Observer
import br.tiagohm.astrum.sky.PlanetType
import br.tiagohm.astrum.sky.core.math.Triad
import br.tiagohm.astrum.sky.core.time.JulianDay
import br.tiagohm.astrum.sky.planets.ApparentMagnitudeAlgorithm
import br.tiagohm.astrum.sky.planets.Planet

class Europa(parent: Jupiter) : Planet(
    Kilometer(1560.8),
    0.0,
    0.67,
    null,
    PlanetType.MOON,
    parent,
) {

    override val siderealDay = 360 / 101.3747235

    override val siderealPeriod = siderealDay

    override val absoluteMagnitude = -1.41

    override val meanOppositionMagnitude = 5.29

    override fun computePosition(jde: JulianDay): Pair<Triad, Triad> {
        val xyz = computeL12HeliocentricCoordinates(jde, 1)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }

    override fun computeRotObliquity(jde: JulianDay) = Radians(0.03838365303405089)

    override fun computeRotAscendingNode() = Radians(-0.38518526400454545)

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