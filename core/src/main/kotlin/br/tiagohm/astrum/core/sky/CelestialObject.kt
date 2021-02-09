package br.tiagohm.astrum.core.sky

import br.tiagohm.astrum.core.*
import br.tiagohm.astrum.core.math.Duad
import br.tiagohm.astrum.core.math.Mat4
import br.tiagohm.astrum.core.math.Triad
import kotlin.math.*

interface CelestialObject {

    val type: PlanetType

    /**
     * Returns a unique identifier for this object.
     */
    val id: String

    /**
     * Computes observer-centered equatorial coordinate at the current equinox.
     * The frame has its Z axis at the planet's current rotation axis.
     * At time 2000-01-01 this frame is almost the same as J2000, but ONLY if the observer is on earth.
     */
    fun computeEquinoxEquatorialPosition(o: Observer): Triad {
        return o.j2000ToEquinoxEquatorial(computeJ2000EquatorialPosition(o), false)
    }

    fun computeEquinoxEquatorialPositionApparent(o: Observer): Triad {
        return o.j2000ToEquinoxEquatorial(computeJ2000EquatorialPosition(o), true)
    }

    /**
     * Computes observer-centered altitude/azimuth position.
     * It is the geometric position, i.e. without taking refraction effect into account.
     * The frame has its Z axis at the zenith.
     */
    fun computeAltAzPositionGeometric(o: Observer): Triad {
        return o.j2000ToAltAz(computeJ2000EquatorialPosition(o), false)
    }

    /**
     * Computes observer-centered altitude/azimuth position.
     * It is the apparent position, i.e. takes refraction effect into account.
     * The frame has its Z axis at the zenith.
     */
    fun computeAltAzPositionApparent(o: Observer): Triad {
        return o.j2000ToAltAz(computeJ2000EquatorialPosition(o), true)
    }

    /**
     * Computes observer-centered hour angle + declination (at current equinox).
     * It is the geometric position, i.e. without taking refraction effect into account.
     * The frame has its Z axis at the planet's current rotation axis.
     */
    fun computeSiderealPositionGeometric(o: Observer): Triad {
        return Mat4.zrotation(-o.computeLocalSiderealTime()) * computeEquinoxEquatorialPosition(o)
    }

    /**
     * Computes observer-centered hour angle + declination (at current equinox).
     * The frame has its Z axis at the planet's current rotation axis.
     */
    fun computeSiderealPositionApparent(o: Observer): Triad {
        val v = o.altAzToEquinoxEquatorial(computeAltAzPositionApparent(o), false)
        return Mat4.zrotation(-o.computeLocalSiderealTime()) * v
    }

    /**
     * Computes observer-centered galactic coordinates.
     */
    fun computeGalacticPosition(o: Observer): Triad {
        return o.j2000ToGalactic(computeJ2000EquatorialPosition(o))
    }

    /**
     * Computes observer-centered supergalactic coordinates.
     */
    fun computeSupergalacticPosition(o: Observer): Triad {
        return o.j2000ToSupergalactic(computeJ2000EquatorialPosition(o))
    }

    /**
     * Computes parallactic angle in radians, which is the deviation between zenith angle and north angle.
     */
    fun parallacticAngle(o: Observer): Double {
        val phi = o.site.latitude * Consts.M_PI_180
        val siderealPos = computeSiderealPositionApparent(o)
        var (ha, delta) = Algorithms.rectangularToSphericalCoordinates(siderealPos)

        // We must invert the orientation sense in case of sidereal positions!
        ha *= -1.0

        // A rare condition! Object exactly in zenith, avoid undefined result.
        return if (ha == 0.0 && (delta - phi) == 0.0) 0.0
        else atan2(sin(ha), tan(phi) * cos(delta) - sin(delta) * cos(ha))
    }

    /**
     * Checks position an object above mathematical horizon for current location.
     */
    fun isAboveHorizon(o: Observer): Boolean {
        return Algorithms.rectangularToSphericalCoordinates(computeAltAzPositionGeometric(o))[1] >= 0.0
    }

    /**
     * Computes today's time of rise, transit and set in decimal hours for celestial object for current location.
     */
    fun rts(o: Observer, hasAtmosphere: Boolean = true): Triad

    /**
     * Returns object's apparent V magnitude as seen from observer, without including extinction.
     */
    fun visualMagnitude(o: Observer): Double

    /**
     * Returns object's apparent V magnitude as seen from observer including extinction.
     */
    fun visualMagnitudeWithExtinction(o: Observer): Double

    fun airmass(o: Observer): Double {
        val pos = computeAltAzPositionApparent(o)
        val (_, az) = Algorithms.rectangularToSphericalCoordinates(pos)

        return if (az > -2.0 * Consts.M_PI_180) {
            o.extinction.airmass(cos(Consts.M_PI_2 - az), true)
        } else {
            0.0
        }
    }

    /**
     * Computes the angular radius in degree of a circle containing the object as seen from the observer
     * with the circle center assumed to be at computeJ2000EquatorialPosition().
     * This value is the apparent angular size of the object, and is independent of the current FOV.
     */
    fun angularSize(o: Observer): Double

    /**
     * Computes the phase angle (radians) for an observer at pos obsPos in heliocentric coordinates (in AU).
     */
    fun phaseAngle(o: Observer): Double {
        val obsPos = o.computeHeliocentricEclipticPosition()
        val observerRq = obsPos.lengthSquared
        val planetHelioPos = computeHeliocentricEclipticPosition(o)
        val planetRq = planetHelioPos.lengthSquared
        val observerPlanetRq = (obsPos - planetHelioPos).lengthSquared
        return acos((observerPlanetRq + planetRq - observerRq) / (2.0 * sqrt(observerPlanetRq * planetRq)))
    }

    /**
     * Computes the distance to the given position in heliocentric coordinate (in AU).
     */
    fun distance(o: Observer): Double {
        val obsHelioPos = o.computeHeliocentricEclipticPosition()
        return (obsHelioPos - computeHeliocentricEclipticPosition(o)).length
    }

    fun distanceFromSun(o: Observer) = computeHeliocentricEclipticPosition(o).length

    /**
     * Computes observer-centered equatorial coordinates at equinox J2000.
     */
    fun computeJ2000EquatorialPosition(o: Observer): Triad {
        return Consts.MAT_VSOP87_TO_J2000
            .multiplyWithoutTranslation(computeHeliocentricEclipticPosition(o) - o.computeHeliocentricEclipticPosition())
    }

    /**
     * Computes the Planet position in Cartesian ecliptic (J2000) coordinates in AU, centered on the parent
     */
    fun computeEclipticPosition(o: Observer): Triad

    fun computeHeliocentricEclipticPosition(o: Observer): Triad

    fun computeEclipticVelocity(o: Observer): Triad

    fun computeHeliocentricEclipticVelocity(o: Observer): Triad

    fun equatorialJ2000(o: Observer): Duad {
        val pos = computeJ2000EquatorialPosition(o)
        val equ = Algorithms.rectangularToSphericalCoordinates(pos)
        return Duad(equ[0].deg.in360, equ[1].deg)
    }

    fun equatorial(o: Observer): Duad {
        val pos = computeEquinoxEquatorialPosition(o)
        val equ = Algorithms.rectangularToSphericalCoordinates(pos)
        return Duad(equ[0].deg.in360, equ[1].deg)
    }

    fun hourAngle(
        o: Observer,
        apparent: Boolean = false
    ): Duad {
        val pos = if (apparent) computeSiderealPositionApparent(o) else computeSiderealPositionGeometric(o)
        val equ = Algorithms.rectangularToSphericalCoordinates(pos)
        return Duad((Consts.M_2_PI - equ[0]).deg.in360 / 15.0, equ[1].deg)
    }

    fun horizontal(
        o: Observer,
        southAzimuth: Boolean = false,
        apparent: Boolean = false
    ): Duad {
        val pos = if (apparent) computeAltAzPositionApparent(o) else computeAltAzPositionGeometric(o)
        val equ = Algorithms.rectangularToSphericalCoordinates(pos)
        val direction = if (southAzimuth) 2.0 else 3.0 // N is zero, E is 90 degrees
        var az = direction * Consts.M_PI - equ[0]
        if (az > Consts.M_2_PI) az -= Consts.M_2_PI
        return Duad(az.deg, equ[1].deg)
    }

    fun galactic(o: Observer): Duad {
        val pos = computeGalacticPosition(o)
        val equ = Algorithms.rectangularToSphericalCoordinates(pos)
        return Duad(equ[0].deg, equ[1].deg)
    }

    fun supergalactic(o: Observer): Duad {
        val pos = computeSupergalacticPosition(o)
        val equ = Algorithms.rectangularToSphericalCoordinates(pos)
        return Duad(equ[0].deg, equ[1].deg)
    }

    fun eclipticJ2000(o: Observer): Duad {
        val eclJ2000 = o.home.computeRotObliquity(Consts.J2000)
        val (ra, dec) = Algorithms.rectangularToSphericalCoordinates(computeJ2000EquatorialPosition(o))
        var (lambda, beta) = Algorithms.equatorialToEcliptic(ra, dec, eclJ2000)
        if (lambda < 0) lambda += Consts.M_2_PI
        return Duad(lambda.deg, beta.deg)
    }

    fun ecliptic(o: Observer): Duad {
        val (ra, dec) = Algorithms.rectangularToSphericalCoordinates(computeEquinoxEquatorialPosition(o))
        var (lambda, beta) = Algorithms.equatorialToEcliptic(ra, dec, o.computeEclipticObliquity())
        if (lambda < 0) lambda += Consts.M_2_PI
        return Duad(lambda.deg, beta.deg)
    }

    // Returns a key/value map with data about an object's position, magnitude and so on.
    // fun getInfo(o: Observer): Map<String, Any?>
}