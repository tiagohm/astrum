package br.tiagohm.astrum.sky.planets.minor

import br.tiagohm.astrum.sky.M_PI_180
import br.tiagohm.astrum.sky.Observer
import br.tiagohm.astrum.sky.PlanetType
import br.tiagohm.astrum.sky.core.orbit.KeplerOrbit
import br.tiagohm.astrum.sky.core.time.DateTime
import br.tiagohm.astrum.sky.core.units.angle.Angle
import br.tiagohm.astrum.sky.core.units.angle.Radians
import br.tiagohm.astrum.sky.core.units.distance.AU
import br.tiagohm.astrum.sky.core.units.distance.Distance
import br.tiagohm.astrum.sky.core.units.distance.Kilometer
import br.tiagohm.astrum.sky.planets.Planet
import br.tiagohm.astrum.sky.planets.Sun
import java.io.IOException
import kotlin.math.*

/**
 * Minor Planet.
 *
 * @param q Pericenter distance
 * @param e Eccentricity
 * @param i Inclination
 * @param omega Longitude of ascending node
 * @param w Argument of perihelion
 * @param t0 Time at perihelion (JDE)
 * @param n Mean motion
 */
open class MinorPlanet(
    name: String,
    parent: Sun,
    q: Distance,
    e: Double,
    i: Angle,
    omega: Angle,
    w: Angle,
    t0: Double,
    n: Angle = KeplerOrbit.computeMeanMotion(e, q),
    albedo: Double = 0.15,
    override val absoluteMagnitude: Double = -99.0,
    val slope: Double = -10.0,
    radius: Distance = Kilometer(1.0),
) : Planet(
    name,
    radius,
    0.0,
    albedo,
    KeplerOrbit(q, e, i, omega, w, t0, n, Radians.ZERO, Radians.ZERO, Radians.ZERO),
    PlanetType.MINOR_PLANET,
    parent,
) {

    override val siderealDay by lazy { siderealPeriod }

    override fun computeRotObliquity(jde: Double) = Radians.ZERO

    override fun computeRotAscendingNode() = Radians.ZERO

    override fun visualMagnitude(o: Observer, extra: Any?): Double {
        // If the H-G system is not used, use the default radius/albedo mechanism
        if (slope < -9.99) {
            return super.visualMagnitude(o, extra)
        }

        val observerHelioPos = o.computeHeliocentricEclipticPosition()
        val observerRq = observerHelioPos.lengthSquared
        val planetHelioPos = computeHeliocentricEclipticPosition(o)
        val planetRq = planetHelioPos.lengthSquared
        val observerPlanetRq = (observerHelioPos - planetHelioPos).lengthSquared
        val cosChi = (observerPlanetRq + planetRq - observerRq) / (2.0 * sqrt(observerPlanetRq * planetRq))
        val phaseAngle = acos(cosChi)

        // Calculate reduced magnitude (magnitude without the influence of distance)
        // Source of the formulae: http://www.britastro.org/asteroids/dymock4.pdf
        // Same model as in Explanatory Supplement 2013, p.423
        val tanPhaseAngleHalf = tan(phaseAngle / 2)
        val phi1 = exp(-3.33 * tanPhaseAngleHalf.pow(0.63))
        val phi2 = exp(-1.87 * tanPhaseAngleHalf.pow(1.22))
        val reducedMagnitude = absoluteMagnitude - 2.5 * log10((1.0 - slope) * phi1 + slope * phi2)

        return reducedMagnitude + 5.0 * log10(sqrt(planetRq * observerPlanetRq))
    }

    companion object {

        private val PACKED_EPOCH_REGEX = Regex("^([IJK])(\\d\\d)([1-9A-C])([1-9A-V])\$")

        fun parseMpcOneLine(parent: Sun, line: String): MinorPlanet {
            if (line.isEmpty() || line.length !in 152..202) {
                throw IOException("Invalid line length: $line")
            }

            // Minor planet number or provisional designation
            // val number = line.substring(0 until 7).trim()
            val absoluteMagnitude = line.substring(8 until 8 + 5).trim().toDouble()
            val slope = line.substring(14 until 14 + 5).trim().toDouble()
            val epoch = line.substring(20 until 20 + 5).trim()
            // val meanAnomalyAtEpoch = line.substring(26 until 26 + 9).trim().toDouble()
            val argumentOfPerihelion = line.substring(37 until 37 + 9).trim().toDouble()
            val longitudeOfTheAscendingNode = line.substring(48 until 48 + 9).trim().toDouble()
            val inclination = line.substring(59 until 59 + 9).trim().toDouble()
            val eccentricity = line.substring(70 until 70 + 9).trim().toDouble()
            val meanDailyMotion = line.substring(80 until 80 + 11).trim().toDouble()
            val semiMajorAxis = line.substring(92 until 92 + 11).trim().toDouble()
            val name = line.substring(166 until 166 + 28).trim()
            val q = (1 - eccentricity) * semiMajorAxis

            // Radius and albedo
            // Assume albedo of 0.15 and calculate a radius based on the absolute magnitude
            // as described here: http://www.physics.sfasu.edu/astro/asteroids/sizemagnitude.html
            val albedo = 0.15 // Assumed
            // Original formula is for diameter!
            val radius = ceil(0.5 * (1329 / sqrt(albedo)) * 10.0.pow(-0.2 * absoluteMagnitude))

            val epochM = PACKED_EPOCH_REGEX.matchEntire(epoch) ?: throw IOException("Invalid epoch format: $line")

            fun unpackDayOrMonthNumber(digit: Char) = when (val d = digit.toInt()) {
                in 0..9 -> d
                in 65..86 -> 10 + (d - 65)
                else -> 0
            }

            fun unpackYearNumber(digit: Char) = when (digit) {
                'I' -> 1800
                'J' -> 1900
                else -> 2000
            }

            val year = epochM.groupValues[2].toInt() + unpackYearNumber(epochM.groupValues[1][0])
            val month = unpackDayOrMonthNumber(epochM.groupValues[3][0])
            val day = unpackDayOrMonthNumber(epochM.groupValues[4][0])
            // Epoch is at .0 TT, i.e. midnight
            val epochJD = DateTime.computeJDFromDate(year, month, day, 0, 0, 0, 0, 0.0)

            return MinorPlanet(
                name,
                parent,
                AU(q),
                eccentricity,
                Radians(inclination * M_PI_180),
                Radians(longitudeOfTheAscendingNode * M_PI_180),
                Radians(argumentOfPerihelion * M_PI_180),
                epochJD,
                Radians(meanDailyMotion * M_PI_180),
                albedo,
                absoluteMagnitude,
                slope,
                Kilometer(radius),
            )
        }
    }
}