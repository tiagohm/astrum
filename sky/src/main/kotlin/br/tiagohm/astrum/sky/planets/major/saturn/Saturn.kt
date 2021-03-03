package br.tiagohm.astrum.sky.planets.major.saturn

import br.tiagohm.astrum.sky.Observer
import br.tiagohm.astrum.sky.PlanetType
import br.tiagohm.astrum.sky.core.math.Triad
import br.tiagohm.astrum.sky.core.math.cos
import br.tiagohm.astrum.sky.core.math.sin
import br.tiagohm.astrum.sky.core.units.angle.Angle
import br.tiagohm.astrum.sky.core.units.angle.Degrees
import br.tiagohm.astrum.sky.core.units.angle.Radians
import br.tiagohm.astrum.sky.core.units.distance.Kilometer
import br.tiagohm.astrum.sky.planets.ApparentMagnitudeAlgorithm
import br.tiagohm.astrum.sky.planets.Planet
import br.tiagohm.astrum.sky.planets.Ring
import br.tiagohm.astrum.sky.planets.Sun
import kotlin.math.*

class Saturn(parent: Sun) : Planet(
    "Saturn",
    Kilometer(60268.0),
    0.09796243446,
    0.50,
    null,
    PlanetType.PLANET,
    parent,
    Ring(Kilometer(74510.0), Kilometer(140390.0)),
) {

    override val siderealDay = 0.44400925923884945092

    override val siderealPeriod = 10760.0

    override val absoluteMagnitude = -8.88

    override val meanOppositionMagnitude = 0.67

    override val mass = 1.0 / 3497.901768

    override fun computePosition(jde: Double): Pair<Triad, Triad> {
        val xyz = computePlanetHeliocentricCoordinates(jde, 5)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }

    override fun computeRotObliquity(jde: Double) = Radians(0.4896026430986047)

    override fun computeRotAscendingNode() = Radians(2.9588132951645223)

    override fun computeVisualMagnitude(
        o: Observer,
        phaseAngle: Angle,
        cosChi: Double,
        observerRq: Double,
        planetRq: Double,
        observerPlanetRq: Double,
        d: Double,
        shadowFactor: Double,
    ): Double {
        val phaseDeg = phaseAngle.degrees.value

        return when (o.apparentMagnitudeAlgorithm) {
            ApparentMagnitudeAlgorithm.EXPLANATORY_SUPPLEMENT_2013,
            ApparentMagnitudeAlgorithm.EXPLANATORY_SUPPLEMENT_1992,
            ApparentMagnitudeAlgorithm.ASTRONOMICAL_ALMANAC_1984 -> {
                -8.88 + d + 0.044 * phaseDeg + computeRingsIllumination(o)
            }
            ApparentMagnitudeAlgorithm.MUELLER_1893 -> {
                -8.68 + d + 0.044 * phaseDeg + computeRingsIllumination(o)
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

    fun computeRingsIllumination(o: Observer): Double {
        // Implemented from Meeus, Astr.Alg.1992
        val T = (o.jde - 2451545.0) / 36525.0
        val i = Degrees((0.000004 * T - 0.012998) * T + 28.075216).radians
        val Omega = Degrees((0.000412 * T + 1.394681) * T + 169.508470).radians
        val se = computeHeliocentricEclipticPosition(o) - o.home.computeHeliocentricEclipticPosition(o)
        val lambda = se.longitude
        val beta = atan2(se[2], sqrt(se[0] * se[0] + se[1] * se[1]))
        val sinx = sin(i) * cos(beta) * sin(lambda - Omega) - cos(i) * sin(beta)
        return -2.6 * abs(sinx) + 1.25 * sinx * sinx // ExplSup2013: added term as (10.81)
    }
}