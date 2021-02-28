package br.tiagohm.astrum.sky.planets.major.jupiter

import br.tiagohm.astrum.sky.Observer
import br.tiagohm.astrum.sky.PlanetType
import br.tiagohm.astrum.sky.core.math.Triad
import br.tiagohm.astrum.sky.core.units.angle.Angle
import br.tiagohm.astrum.sky.core.units.angle.Radians
import br.tiagohm.astrum.sky.core.units.distance.Kilometer
import br.tiagohm.astrum.sky.planets.ApparentMagnitudeAlgorithm
import br.tiagohm.astrum.sky.planets.Planet

class Io(parent: Jupiter) : Planet(
    "Io",
    Kilometer(1821.49),
    0.0,
    0.63,
    null,
    PlanetType.MOON,
    parent,
) {

    override val siderealDay = 1.7691377997541211006

    override val siderealPeriod = siderealDay

    override val absoluteMagnitude = -1.68

    override val meanOppositionMagnitude = 5.02

    override fun computePosition(jde: Double): Pair<Triad, Triad> {
        val xyz = computeJupiterSatHeliocentricCoordinates(jde, 0)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }

    override fun computeRotObliquity(jde: Double) = Radians(0.0386258511268327)

    override fun computeRotAscendingNode() = Radians(-0.3890901056071774)

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
                val mag = -1.68 + d + phaseDeg * (0.046 - 0.0010 * phaseDeg)
                if (shadowFactor < 1.0) 13.0 * (1.0 - shadowFactor) + mag else mag
            }
            else -> super.computeVisualMagnitude(o, phaseAngle, cosChi, observerRq, planetRq, observerPlanetRq, d, shadowFactor)
        }
    }
}