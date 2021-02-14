package br.tiagohm.astrum.core

import kotlin.math.*

// TODO: Saturn has rings!!!
class Saturn(parent: Sun) : Planet(
    "Saturn",
    60268.0 / AU,
    0.09796243446,
    0.50,
    null,
    PlanetType.PLANET,
    parent,
    Ring(74510.0 / AU, 140390.0 / AU),
) {

    override val siderealDay = 0.44400925923884945092

    override val siderealPeriod = 10760.0

    override val absoluteMagnitude = -8.88

    override val meanOppositionMagnitude = 0.67

    override fun computePosition(jde: Double): Pair<Triad, Triad> {
        val xyz = computePlanetHeliocentricCoordinates(jde, 5)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }

    override fun computeRotObliquity(jde: Double) = 0.4896026430986047

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
            ApparentMagnitudeAlgorithm.EXPLANATORY_SUPPLEMENT_2013,
            ApparentMagnitudeAlgorithm.EXPLANATORY_SUPPLEMENT_1992,
            ApparentMagnitudeAlgorithm.ASTRONOMICAL_ALMANAC_1984 -> {
                -8.88 + d + 0.044 * phaseDeg + ringsIllumination(o)
            }
            ApparentMagnitudeAlgorithm.MUELLER_1893 -> {
                -8.68 + d + 0.044 * phaseDeg + ringsIllumination(o)
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

    fun ringsIllumination(o: Observer): Double {
        // Implemented from Meeus, Astr.Alg.1992
        val T = (o.jde - 2451545.0) / 36525.0
        val i = ((0.000004 * T - 0.012998) * T + 28.075216).rad
        val Omega = ((0.000412 * T + 1.394681) * T + 169.508470).rad
        val se = computeHeliocentricEclipticPosition(o) - o.home.computeHeliocentricEclipticPosition(o)
        val lambda = atan2(se[1], se[0])
        val beta = atan2(se[2], sqrt(se[0] * se[0] + se[1] * se[1]))
        val sinx = sin(i) * cos(beta) * sin(lambda - Omega) - cos(i) * sin(beta)
        return -2.6 * abs(sinx) + 1.25 * sinx * sinx // ExplSup2013: added term as (10.81)
    }
}