package br.tiagohm.astrum.core.sky

import br.tiagohm.astrum.core.Observer
import br.tiagohm.astrum.core.math.Triad

interface CelestialObject {

    val type: PlanetType

    // Returns the J2000 Equatorial Position of the object.
    // val pointInRegion: Triplet

    /**
     * Returns a unique identifier for this object.
     */
    val id: String

    /**
     * Gets observer-centered equatorial coordinate at the current equinox.
     * The frame has its Z axis at the planet's current rotation axis.
     * At time 2000-01-01 this frame is almost the same as J2000, but ONLY if the observer is on earth.
     */
    fun computeEquinoxEquatorialPosition(o: Observer): Triad {
        return o.j2000ToEquinoxEquatorial(computeJ2000EquatorialPosition(o))
    }

    // Get observer-centered galactic coordinates
    // fun getGalacticPos(sky: Sky): Triplet

    // Get observer-centered supergalactic coordinates
    // fun getSupergalacticPos(sky: Sky): Triplet

    //! Gets observer-centered hour angle + declination (at current equinox)
    //! It is the geometric position, i.e. without taking refraction effect into account.
    //! The frame has its Z axis at the planet's current rotation axis
    // fun getSiderealPosGeometric(sky: Sky): Triplet

    // Gets observer-centered hour angle + declination (at current equinox)
    // It is the apparent position, i.e. taking the refraction effect into account.
    // The frame has its Z axis at the planet's current rotation axis
    // fun getSiderealPosApparent(sky: Sky): Triplet

    // Gets observer-centered alt/az position
    // It is the geometric position, i.e. without taking refraction effect into account.
    // The frame has its Z axis at the zenith
    // fun getAltAzPosGeometric(sky: Sky): Triplet

    // Gets observer-centered alt/az position
    // It is the apparent position, i.e. taking the refraction effect into account.
    // The frame has its Z axis at the zenith
    // fun getAltAzPosApparent(sky: Sky): Triplet

    // Gets observer-centered alt/az position
    // It is the automatic position, i.e. taking the refraction effect into account if atmosphere is on.
    // The frame has its Z axis at the zenith
    // fun getAltAzPosAuto(sky: Sky): Triplet

    // Gets parallactic angle in radians, which is the deviation between zenith angle and north angle.
    // fun getParallacticAngle(sky: Sky): Double

    // Checks position an object above mathematical horizon for current location.
    // fun isAboveHorizon(sky: Sky): Boolean

    // Checks position an object above real horizon for current location.
    // fun isAboveRealHorizon(sky: Sky): Boolean

    // Gets today's time of rise, transit and set in decimal hours for celestial object for current location.
    // fun getRTSTime(sky: Sky): Triplet

    // Returns object's apparent V magnitude as seen from observer, without including extinction.
    // fun getVMagnitude(sky: Sky): Double

    // Returns object's apparent V magnitude as seen from observer including extinction.
    // fun getVMagnitudeWithExtinction(sky: Sky): Double

    // Returns the angular radius in degree of a circle containing the object as seen from the observer
    // with the circle center assumed to be at getJ2000EquatorialPos().
    // This value is the apparent angular size of the object, and is independent of the current FOV.
    // fun getAngularSize(sky: Sky) = 0.0

    /**
     * Gets the phase angle (radians) for an observer at pos obsPos in heliocentric coordinates (in AU)
     */
    fun computePhaseAngle(o: Observer): Double

    /**
     * Computes the distance to the given position in heliocentric coordinate (in AU)
     */
    fun computeDistance(o: Observer): Double

    fun computeJ2000EquatorialPosition(o: Observer): Triad

    /**
     * Computes the Planet position in Cartesian ecliptic (J2000) coordinates in AU, centered on the parent
     */
    fun computeEclipticPosition(o: Observer): Triad

    fun computeHeliocentricEclipticPosition(o: Observer): Triad

    fun computeHeliocentricEclipticPosition(): Triad

    // Returns a key/value map with data about an object's position, magnitude and so on.
    // fun getInfo(sky: Sky): Map<String, Any?>
}