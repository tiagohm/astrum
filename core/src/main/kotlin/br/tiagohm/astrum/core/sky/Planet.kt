package br.tiagohm.astrum.core.sky

import br.tiagohm.astrum.core.*
import br.tiagohm.astrum.core.algorithms.Elp82b
import br.tiagohm.astrum.core.algorithms.Rotation
import br.tiagohm.astrum.core.algorithms.Vsop87
import br.tiagohm.astrum.core.math.Mat4
import br.tiagohm.astrum.core.math.Triad
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.sqrt

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

    protected var rotation = Rotation()
        private set

    /**
     * Gets duration of sidereal year
     */
    var siderealPeriod: Double = 0.0

    /**
     * Gets duration of mean solar day, in earth days.
     */
    open val meanSolarDay: Double
        get() = computeMeanSolarDay(false)

    /**
     * Get duration of sidereal day (earth days)
     */
    open val siderealDay: Double
        get() = rotation.period

    /**
     * Gets or sets the absolute magnitude
     */
    var absoluteMagnitude: Double = 0.0
        protected set

    /**
     * Returns the mean opposition magnitude, defined as V(1,0)+5log10(a(a-1))
     */
    val meanOppositionMagnitude: Double = 0.0

    init {
        if (
            orbit is KeplerOrbit &&
            type != PlanetType.OBSERVER &&
            orbit.semiMajorAxis > 0.0 &&
            orbit.e < 0.9
        ) {
            siderealPeriod = orbit.siderealPeriod
        }
    }

    protected fun computeMeanSolarDay(isRetrograde: Boolean): Double {
        val sign = if (isRetrograde) -1.0 else 1.0
        val sday = siderealDay
        val coeff = abs(sday / siderealPeriod)

        // Duration of mean solar day on moon are same as synodic month on this moon
        return if (type == PlanetType.MOON) {
            val a = parent!!.siderealPeriod / sday
            sday * (a / (a - 1))
        } else {
            sign * sday / (1 - sign * coeff)
        }
    }

    protected fun setRotation(
        period: Double,
        offset: Double,
        epoch: Double,
        obliquity: Double,
        ascendingNode: Double,
        w0: Double = 0.0,
        w1: Double = 0.0,
    ) {
        rotation = Rotation(
            period,
            offset,
            epoch,
            obliquity,
            ascendingNode,
            w0,
            w1,
        )
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
    open fun computeSiderealTime(jd: Double, jde: Double, useNutation: Boolean): Double {
        val t = jde - rotation.epoch
        // Avoid division by zero (typical case for moons with chaotic period of rotation)
        var rotations = if (rotation.period == 0.0) 1.0 else t / rotation.period
        // Remove full rotations to limit angle
        rotations = remainder(rotations, 1.0)
        return rotations * 360.0 + rotation.offset
    }

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
        return Mat4.zrotation(rotation.ascendingNode) * Mat4.xrotation(rotation.obliquity)
    }

    override fun computeJ2000EquatorialPosition(o: Observer): Triad {
        return Consts.MAT_VSOP87_TO_J2000
            .multiplyWithoutTranslation(computeHeliocentricEclipticPosition(o) - o.computeHeliocentricEclipticPosition())
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
            val b = o.planet.internalComputePosition(jde).first

            val length =
                (computeHeliocentricEclipticPosition(a) - o.planet.computeHeliocentricEclipticPosition(b)).length
            val lsc = length * (Consts.AU / (Consts.SPEED_OF_LIGHT * 86400.0))

            eclipticPos = internalComputePosition(jde - lsc).first
            computeTransformationMatrix(jd - lsc, jde - lsc, o.useNutation)
        } else {
            eclipticPos = internalComputePosition(jde).first
            computeTransformationMatrix(jd, jde, o.useNutation)
        }

        return eclipticPos
    }

    override fun computePhaseAngle(o: Observer): Double {
        val obsPos = o.computeHeliocentricEclipticPosition()
        val observerRq = obsPos.lengthSquared
        val planetHelioPos = computeHeliocentricEclipticPosition(o)
        val planetRq = planetHelioPos.lengthSquared
        val observerPlanetRq = (obsPos - planetHelioPos).lengthSquared
        return acos((observerPlanetRq + planetRq - observerRq) / (2.0 * sqrt(observerPlanetRq * planetRq)))
    }

    override fun computeDistance(o: Observer): Double {
        val obsHelioPos = o.computeHeliocentricEclipticPosition()
        return (obsHelioPos - computeHeliocentricEclipticPosition(o)).length
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

    // Gets the elongation angle (radians) for an observer at pos obsPos in heliocentric coordinates (in AU)
    // fun getElongation(obsPos: Triplet): Double

    // Get the angular radius (degrees) of the planet spheroid (i.e. without the rings)
    // fun getSpheroidAngularSize(sky: Sky): Double

    // Get the planet phase (illuminated fraction of the planet disk, [0=dark..1=full]) for an observer at pos obsPos in heliocentric coordinates (in AU)
    // fun getPhase(obsPos: Triplet): Double

    val isRotatingRetrograde: Boolean
        get() = rotation.w1 < 0.0

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