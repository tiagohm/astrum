package br.tiagohm.astrum.core.sky

import br.tiagohm.astrum.core.Consts
import br.tiagohm.astrum.core.KeplerOrbit
import br.tiagohm.astrum.core.Observer
import br.tiagohm.astrum.core.Orbit
import br.tiagohm.astrum.core.algorithms.Elp82b
import br.tiagohm.astrum.core.algorithms.Vsop87
import br.tiagohm.astrum.core.math.Mat4
import br.tiagohm.astrum.core.math.Triad
import kotlin.math.abs
import kotlin.math.atan2

abstract class Planet internal constructor(
    // English planet name
    final override val id: String,
    // Gets the equator radius of the planet in AU
    val equatorialRadius: Double,
    val oblateness: Double,
    // Planet albedo. Used for magnitude computation when no other formula in use
    val albedo: Double,
    // Orbit for positional computations of Minor Planets, Comets and Moons
    // For an "observer", it is GimbalOrbit
    // This remains null for the major planets
    val orbit: Orbit?,
    // Type of body
    final override val type: PlanetType,
    // Parent
    val parent: Planet? = null,
) : CelestialObject {

    private var rotLocalToParent = Mat4.IDENTITY
    private var eclipticPos = Triad.ZERO
    private val positionCache = HashMap<Double, Pair<Triad, Triad>>()

    val oneMinusOblateness = 1.0 - oblateness

    /**
     * Gets duration of sidereal year
     */
    open val siderealPeriod = if (orbit is KeplerOrbit &&
        type != PlanetType.OBSERVER &&
        orbit.semiMajorAxis > 0.0 &&
        orbit.e < 0.9
    ) orbit.siderealPeriod else 0.0

    /**
     * Gets duration of mean solar day, in earth days.
     */
    open val meanSolarDay: Double
        get() = computeMeanSolarDay()

    /**
     * Get duration of sidereal day (earth days)
     */
    abstract val siderealDay: Double

    /**
     * Gets the absolute magnitude
     */
    open val absoluteMagnitude: Double = -99.0

    /**
     * Returns the mean opposition magnitude, defined as V(1,0)+5log10(a(a-1))
     */
    val meanOppositionMagnitude: Double = 0.0

    protected fun computeMeanSolarDay(): Double {
        val sday = siderealDay
        val coeff = abs(sday / siderealPeriod)

        // Duration of mean solar day on moon are same as synodic month on this moon
        return if (type == PlanetType.MOON) {
            val a = parent!!.siderealPeriod / sday
            sday * (a / (a - 1))
        } else {
            val sign = if (isRotatingRetrograde) -1.0 else 1.0
            sign * sday / (1 - sign * coeff)
        }
    }

    internal fun update(o: Observer) {
        parent?.update(o)
        computeTransformationMatrix(o.jd, o.jde, o.useNutation)
    }

    // Compute the axial z rotation (daily rotation around the polar axis) [degrees]
    // to use from equatorial to hour angle based coordinates.
    // On Earth, sidereal time on the other hand is the angle along the planet equator from RA0 to the meridian, i.e. hour angle of the first point of Aries.
    // For Earth (of course) it is sidereal time at Greenwich.
    // For planets and Moons, in this context this is the rotation angle W of the Prime meridian from the ascending node of the planet equator on the ICRF equator.
    open fun computeSiderealTime(jd: Double, jde: Double, useNutation: Boolean) = 0.0

    // Returns angle between axis and normal of ecliptic plane (or, for a moon, equatorial/reference plane defined by parent).
    // For Earth, this is the angle between axis and normal to current ecliptic of date, i.e. the ecliptic obliquity of date JDE.
    // Note: The only place where this is not used for Earth is to build up orbits for planet moons w.r.t. the parent planet orientation.
    // fun getRotObliquity(JDE: Double): Double = 0.0

    protected open fun computePosition(jde: Double): Pair<Triad, Triad> {
        return orbit!!.let { it.positionAtTimevInVSOP87Coordinates(jde) to it.velocity }
    }

    internal fun internalComputePosition(jde: Double): Pair<Triad, Triad> {
        return if (positionCache.containsKey(jde)) {
            positionCache[jde]!!
        } else {
            val pos = computePosition(jde)
            if (positionCache.size >= 100) positionCache.clear()
            positionCache[jde] = pos
            pos
        }
    }

    // Compute the transformation matrix from the local Planet coordinate system to the parent Planet coordinate system.
    // In case of the planets, this makes the axis point to their respective celestial poles.
    // If only old-style rotational elements exist, we use the original algorithm (as of ~2010).
    private fun computeTransformationMatrix(jd: Double, jde: Double, useNutation: Boolean) {
        // We have to call with both to correct this for earth with the new model.
        // For Earth, this is sidereal time for Greenwich, i.e. hour angle between meridian and First Point of Aries.
        // Return angle between ascending node of planet's equator and (J2000) ecliptic (?)
        // Store to later compute central meridian data etc.
        rotLocalToParent = internalComputeTransformationMatrix(jd, jde, useNutation)
    }

    protected open fun internalComputeTransformationMatrix(jd: Double, jde: Double, useNutation: Boolean): Mat4 {
        // return Mat4.zrotation(rotation.ascendingNode) * Mat4.xrotation(rotation.obliquity)
        return Mat4.zrotation(0.0) * Mat4.xrotation(0.0)
    }

    override fun computeHeliocentricEclipticPosition(o: Observer): Triad {
        var pos = computeEclipticPosition(o)
        var p = parent

        while (p != null) {
            pos += p.computeEclipticPosition(o)
            p = p.parent
        }

        return pos
    }

    override fun computeHeliocentricEclipticPosition(): Triad {
        return computeHeliocentricEclipticPosition(eclipticPos)
    }

    internal fun computeHeliocentricEclipticPosition(a: Triad): Triad {
        var pos = a
        var p = parent

        while (p != null) {
            pos += p.eclipticPos
            p = p.parent
        }

        return pos
    }

    override fun computeEclipticPosition(o: Observer): Triad {
        return computeEclipticPosition(o.jde, o, o.useLightTravelTime)
    }

    internal fun computeEclipticPosition(jde: Double, o: Observer, useLightTravelTime: Boolean): Triad {
        val jd = jde - o.computeDeltaT(jde) / 86400.0

        if (useLightTravelTime) {
            val a = internalComputePosition(jde).first
            val b = o.home.internalComputePosition(jde).first

            val length =
                (computeHeliocentricEclipticPosition(a) - o.home.computeHeliocentricEclipticPosition(b)).length
            val lsc = length * (Consts.AU / (Consts.SPEED_OF_LIGHT * 86400.0))

            eclipticPos = internalComputePosition(jde - lsc).first
            computeTransformationMatrix(jd - lsc, jde - lsc, o.useNutation)
        } else {
            eclipticPos = internalComputePosition(jde).first
            computeTransformationMatrix(jd, jde, o.useNutation)
        }

        return eclipticPos
    }

    fun computeRotEquatorialToVsop87(): Mat4 {
        var m = rotLocalToParent

        if (parent != null) {
            var p = parent

            while (p != null) {
                if (p.type != PlanetType.STAR) {
                    m = p.rotLocalToParent * m
                }

                p = p.parent
            }
        }

        return m
    }

    override fun angularSize(o: Observer): Double {
        val radius = equatorialRadius // TODO: Saturn Rings
        return atan2(radius, computeJ2000EquatorialPosition(o).length) * Consts.M_180_PI
    }

    // Gets the elongation angle (radians) for an observer at pos obsPos in heliocentric coordinates (in AU)
    // fun getElongation(obsPos: Triplet): Double

    // Get the angular radius (degrees) of the planet spheroid (i.e. without the rings)
    // fun getSpheroidAngularSize(sky: Sky): Double

    // Get the planet phase (illuminated fraction of the planet disk, [0=dark..1=full]) for an observer at pos obsPos in heliocentric coordinates (in AU)
    // fun getPhase(obsPos: Triplet): Double

    /**
     * Computes the obliquity in radians.
     * For Earth, this is epsilon, the angle between earth's rotational axis and pole of mean ecliptic of date.
     * Details: e.g. Hilton etal, Report on Precession and the Ecliptic, Cel.Mech.Dyn.Astr.94:351-67 (2006), Fig1.
     * For the other planets, it must be the angle between axis and Normal to the VSOP_J2000 coordinate frame.
     * For moons, it may be the obliquity against its planet's equatorial plane.
     */
    open fun computeRotObliquity(jde: Double): Double = 0.0

    inline val isRotatingRetrograde: Boolean
        get() = siderealDay < 0.0

    companion object {

        fun computeEarthHeliocentricCoordinates(jd: Double): DoubleArray {
            val xyz6 = Vsop87.computeCoordinates(jd, 2)
            val moon = Elp82b.computeCoordinates(jd)

            xyz6[0] -= 0.0121505677733761 * moon[0]
            xyz6[1] -= 0.0121505677733761 * moon[1]
            xyz6[2] -= 0.0121505677733761 * moon[2]

            // TODO: HOW TO FIX EARTH SPEED?

            return xyz6
        }

        fun computePlanetHeliocentricCoordinates(jd: Double, planet: Int): DoubleArray {
            return Vsop87.computeCoordinates(jd, planet)
        }
    }
}