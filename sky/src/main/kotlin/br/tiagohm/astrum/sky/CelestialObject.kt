package br.tiagohm.astrum.sky

import br.tiagohm.astrum.sky.atmosphere.Extinction
import br.tiagohm.astrum.sky.constellations.Constellation
import br.tiagohm.astrum.sky.core.Algorithms
import br.tiagohm.astrum.sky.core.coordinates.*
import br.tiagohm.astrum.sky.core.cos
import br.tiagohm.astrum.sky.core.math.Mat4
import br.tiagohm.astrum.sky.core.math.Triad
import br.tiagohm.astrum.sky.core.sin
import br.tiagohm.astrum.sky.core.tan
import br.tiagohm.astrum.sky.core.units.angle.Angle
import br.tiagohm.astrum.sky.core.units.angle.Radians
import kotlin.math.acos
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sqrt

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
        return Mat4.zrotation(-o.computeSiderealTime()) * computeEquinoxEquatorialPosition(o)
    }

    /**
     * Computes observer-centered hour angle + declination (at current equinox).
     * The frame has its Z axis at the planet's current rotation axis.
     */
    fun computeSiderealPositionApparent(o: Observer): Triad {
        val v = o.altAzToEquinoxEquatorial(computeAltAzPositionApparent(o), false)
        return Mat4.zrotation(-o.computeSiderealTime()) * v
    }

    /**
     * Computes observer-centered galactic coordinates.
     */
    fun computeGalacticPosition(o: Observer): Triad {
        return Algorithms.j2000ToGalactic(computeJ2000EquatorialPosition(o))
    }

    /**
     * Computes observer-centered supergalactic coordinates.
     */
    fun computeSupergalacticPosition(o: Observer): Triad {
        return Algorithms.j2000ToSupergalactic(computeJ2000EquatorialPosition(o))
    }

    /**
     * Computes parallactic angle, which is the deviation between zenith angle and north angle.
     */
    fun parallacticAngle(o: Observer): Radians {
        val phi = o.site.latitude.radians
        val siderealPos = computeSiderealPositionApparent(o)
        var (ha, delta) = Algorithms.rectangularToSphericalCoordinates(siderealPos)

        // We must invert the orientation sense in case of sidereal positions!
        ha = -ha

        // A rare condition! Object exactly in zenith, avoid undefined result.
        return if (ha == Radians.ZERO && (delta - phi) == Radians.ZERO) Radians.ZERO
        else Radians(atan2(sin(ha), tan(phi) * cos(delta) - sin(delta) * cos(ha)))
    }

    /**
     * Checks position an object above mathematical horizon for current location.
     */
    fun isAboveHorizon(o: Observer): Boolean {
        return Algorithms.rectangularToSphericalCoordinates(computeAltAzPositionGeometric(o)).y >= Radians.ZERO
    }

    /**
     * Computes today's time of rise, transit and set in decimal hours for celestial object for current location.
     */
    fun rts(o: Observer, hasAtmosphere: Boolean = true): Triad

    /**
     * Returns object's apparent V magnitude as seen from observer, without including extinction.
     * For Sun, pass Moon instance on extra parameter to take into account solar eclipse.
     */
    fun visualMagnitude(o: Observer, extra: Any? = null): Double

    /**
     * Returns object's apparent V magnitude as seen from observer including extinction.
     * For Sun, pass Moon instance on extra parameter to take into account solar eclipse.
     */
    fun visualMagnitudeWithExtinction(o: Observer, extra: Any? = null): Double

    /**
     * Airmass computation at observer.
     */
    fun airmass(o: Observer): Double {
        val pos = computeAltAzPositionApparent(o)
        val az = Algorithms.rectangularToSphericalCoordinates(pos).y.radians.value
        return if (az > -2.0 * M_PI_180) Extinction.airmass(cos(M_PI_2 - az), true)
        else 0.0
    }

    /**
     * Computes the angular radius of a circle containing the object as seen from the observer
     * with the circle center assumed to be at computeJ2000EquatorialPosition().
     * This value is the apparent angular size of the object, and is independent of the current FOV.
     */
    fun angularSize(o: Observer): Angle

    /**
     * Computes the phase angle for an observer.
     */
    fun phaseAngle(o: Observer): Radians {
        val obsPos = o.computeHeliocentricEclipticPosition()
        val observerRq = obsPos.lengthSquared
        val planetHelioPos = computeHeliocentricEclipticPosition(o)
        val planetRq = planetHelioPos.lengthSquared
        val observerPlanetRq = (obsPos - planetHelioPos).lengthSquared
        return Radians(acos((observerPlanetRq + planetRq - observerRq) / (2.0 * sqrt(observerPlanetRq * planetRq))))
    }

    /**
     * Computes the distance in AU to the given observer.
     */
    fun distance(o: Observer): Double {
        val obsHelioPos = o.computeHeliocentricEclipticPosition()
        return (obsHelioPos - computeHeliocentricEclipticPosition(o)).length
    }

    /**
     * Computes the distance from Sun in AU to the given observer.
     */
    fun distanceFromSun(o: Observer) = computeHeliocentricEclipticPosition(o).length

    /**
     * Computes observer-centered equatorial coordinates at equinox J2000.
     */
    fun computeJ2000EquatorialPosition(o: Observer): Triad {
        return MAT_VSOP87_TO_J2000
            .multiplyWithoutTranslation(computeHeliocentricEclipticPosition(o) - o.computeHeliocentricEclipticPosition())
    }

    /**
     * Computes the Planet position in Cartesian ecliptic (J2000) coordinates in AU, centered on the parent
     */
    fun computeEclipticPosition(o: Observer): Triad

    fun computeHeliocentricEclipticPosition(o: Observer): Triad

    fun computeEclipticVelocity(o: Observer): Triad

    fun computeHeliocentricEclipticVelocity(o: Observer): Triad

    fun equatorialJ2000(o: Observer): Equatorial {
        val pos = computeJ2000EquatorialPosition(o)
        val (a, b) = Algorithms.rectangularToSphericalCoordinates(pos)
        return Equatorial(a.normalized, b)
    }

    fun equatorial(o: Observer): Equatorial {
        val pos = computeEquinoxEquatorialPosition(o)
        val (a, b) = Algorithms.rectangularToSphericalCoordinates(pos)
        return Equatorial(a.normalized, b)
    }

    fun hourAngle(
        o: Observer,
        apparent: Boolean = true
    ): HourAngle {
        val pos = if (apparent) computeSiderealPositionApparent(o) else computeSiderealPositionGeometric(o)
        val (a, b) = Algorithms.rectangularToSphericalCoordinates(pos)
        return HourAngle(Radians(M_2_PI - a.radians.value).degrees.normalized / 15.0, b)
    }

    fun horizontal(
        o: Observer,
        southAzimuth: Boolean = false,
        apparent: Boolean = true
    ): Horizontal {
        val pos = if (apparent) computeAltAzPositionApparent(o) else computeAltAzPositionGeometric(o)
        val (a, b) = Algorithms.rectangularToSphericalCoordinates(pos)
        val direction = if (southAzimuth) M_2_PI else M_3_PI // N is zero, E is 90 degrees
        val az = (direction - a.radians.value).let { if (it > M_2_PI) it - M_2_PI else it }
        return Horizontal(Radians(az), b)
    }

    fun galactic(o: Observer): Galactic {
        val pos = computeGalacticPosition(o)
        val (a, b) = Algorithms.rectangularToSphericalCoordinates(pos)
        return Galactic(a, b)
    }

    fun supergalactic(o: Observer): Supergalactic {
        val pos = computeSupergalacticPosition(o)
        val (a, b) = Algorithms.rectangularToSphericalCoordinates(pos)
        return Supergalactic(a, b)
    }

    fun eclipticJ2000(o: Observer): Ecliptic {
        val eclJ2000 = o.home.computeRotObliquity(J2000)
        val (ra, dec) = Algorithms.rectangularToSphericalCoordinates(computeJ2000EquatorialPosition(o))
        val equ = Equatorial(ra, dec)
        var (lambda, beta) = equ.toEcliptic(eclJ2000)
        if (lambda < Radians.ZERO) lambda += Radians.TWO_PI
        return Ecliptic(lambda, beta)
    }

    fun ecliptic(o: Observer): Ecliptic {
        val (ra, dec) = Algorithms.rectangularToSphericalCoordinates(computeEquinoxEquatorialPosition(o))
        val equ = Equatorial(ra, dec)
        var (lambda, beta) = equ.toEcliptic(o.computeEclipticObliquity())
        if (lambda < Radians.ZERO) lambda += Radians.TWO_PI
        return Ecliptic(lambda, beta)
    }

    fun constellation(o: Observer) = Constellation.find(o, computeEquinoxEquatorialPosition(o))!!

    // Returns a key/value map with data about an object's position, magnitude and so on.
    // fun getInfo(o: Observer): Map<String, Any?>
}