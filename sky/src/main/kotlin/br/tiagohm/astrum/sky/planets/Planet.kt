package br.tiagohm.astrum.sky.planets

import br.tiagohm.astrum.sky.*
import br.tiagohm.astrum.sky.core.ephemeris.*
import br.tiagohm.astrum.sky.core.math.Mat4
import br.tiagohm.astrum.sky.core.math.Triad
import br.tiagohm.astrum.sky.core.orbit.KeplerOrbit
import br.tiagohm.astrum.sky.core.orbit.Orbit
import br.tiagohm.astrum.sky.core.time.JulianDay
import br.tiagohm.astrum.sky.core.units.angle.Angle
import br.tiagohm.astrum.sky.core.units.angle.Degrees
import br.tiagohm.astrum.sky.core.units.angle.Radians
import br.tiagohm.astrum.sky.core.units.distance.AU
import br.tiagohm.astrum.sky.core.units.distance.Distance
import br.tiagohm.astrum.sky.planets.major.earth.Moon
import br.tiagohm.astrum.sky.planets.major.jupiter.Jupiter
import br.tiagohm.astrum.sky.planets.major.mars.Mars
import br.tiagohm.astrum.sky.planets.major.neptune.Neptune
import br.tiagohm.astrum.sky.planets.major.saturn.Saturn
import br.tiagohm.astrum.sky.planets.major.uranus.Uranus
import br.tiagohm.astrum.sky.planets.minor.MinorPlanet
import br.tiagohm.astrum.sky.planets.minor.pluto.Pluto
import kotlin.math.*

abstract class Planet internal constructor(
    // English planet name
    final override val id: String,
    // Gets the equator radius of the planet
    radius: Distance,
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
    // Rings
    val rings: Ring? = null,
) : CelestialObject {

    private var rotLocalToParent = Mat4.IDENTITY
    private var axisRotation: Angle = Degrees.ZERO
    private val positionCache = HashMap<JulianDay, Pair<Triad, Triad>>()

    /**
     * Radius in AU.
     */
    val radius = radius.au

    val oneMinusOblateness = 1.0 - oblateness

    /**
     * Mass in Solar masses.
     */
    open val mass: Double = 0.0

    /**
     * Gets duration of sidereal year, in earth days.
     */
    open val siderealPeriod = if (orbit is KeplerOrbit &&
        orbit.semiMajorAxis.isPositive &&
        orbit.e < 0.9
    ) orbit.siderealPeriod else 0.0

    /**
     * Gets duration of mean solar day, in earth days.
     */
    open val meanSolarDay: Double
        get() = computeMeanSolarDay()

    /**
     * Get duration of sidereal day, in earth days.
     */
    abstract val siderealDay: Double

    /**
     * Gets the absolute magnitude.
     */
    open val absoluteMagnitude = -99.0

    /**
     * Returns the mean opposition magnitude, defined as V(1,0)+5log10(a(a-1)).
     */
    open val meanOppositionMagnitude: Double
        get() {
            // Source: Explanatory Supplement 2013, Table 10.6 and formula (10.5) with semimajorAxis a from Table 8.7
            // TODO: Testar com luas e asteroides
            if (absoluteMagnitude <= -99.0) return 100.0

            val a = parent?.orbit?.semiMajorAxis?.au?.value
                ?: if (this is MinorPlanet) orbit!!.semiMajorAxis.au.value
                else if (parent is Mars) 1.52371034
                else if (parent is Jupiter) 5.202887
                else if (parent is Saturn) 9.53667594
                else if (parent is Uranus) 19.18916464
                else if (parent is Neptune) 30.06992276
                else if (parent is Pluto) 39.48211675
                else return 100.0

            return if (a > 0.0) absoluteMagnitude + 5.0 * log10(a * (a - 1.0)) else 100.0
        }

    val isStar = type == PlanetType.STAR

    val isMoon = type == PlanetType.MOON

    val isMinorPlanet = type == PlanetType.MINOR_PLANET

    val isPlanet = type == PlanetType.PLANET

    protected fun computeMeanSolarDay(): Double {
        val sday = siderealDay
        val coeff = abs(sday / siderealPeriod)

        // Duration of mean solar day on moon are same as synodic month on this moon
        // TODO: Testar com luas
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
    open fun computeSiderealTime(jd: JulianDay, jde: JulianDay, useNutation: Boolean) = Degrees.ZERO

    protected open fun computePosition(jde: JulianDay): Pair<Triad, Triad> {
        return orbit!!.positionAndVelocityAtTimevInVSOP87Coordinates(jde)
    }

    internal fun internalComputePosition(jde: JulianDay): Pair<Triad, Triad> {
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
    private fun computeTransformationMatrix(jd: JulianDay, jde: JulianDay, useNutation: Boolean) {
        axisRotation = computeSiderealTime(jd, jde, useNutation)
        // We have to call with both to correct this for earth with the new model.
        // For Earth, this is sidereal time for Greenwich, i.e. hour angle between meridian and First Point of Aries.
        // Return angle between ascending node of planet's equator and (J2000) ecliptic (?)
        // Store to later compute central meridian data etc.
        rotLocalToParent = internalComputeTransformationMatrix(jd, jde, useNutation)
    }

    internal fun computeEclipticPosition(jde: JulianDay, o: Observer, useLightTravelTime: Boolean): Pair<Triad, Triad> {
        val jd = JulianDay(jde.value - o.computeDeltaT(jde)) / SECONDS_PER_DAY

        return if (useLightTravelTime) {
            val length =
                (internalComputeHeliocentricEclipticPosition(jde) - o.home.internalComputeHeliocentricEclipticPosition(jde)).length
            val lsc = length * (AU_KM / (SPEED_OF_LIGHT * SECONDS_PER_DAY))

            val pos = internalComputePosition(jde - lsc)
            computeTransformationMatrix(jd - lsc, jde - lsc, o.useNutation)
            pos
        } else {
            val pos = internalComputePosition(jde)
            computeTransformationMatrix(jd, jde, o.useNutation)
            pos
        }
    }

    protected open fun internalComputeTransformationMatrix(jd: JulianDay, jde: JulianDay, useNutation: Boolean): Mat4 {
        return Mat4.zrotation(computeRotAscendingNode()) * Mat4.xrotation(computeRotObliquity(jde))
    }

    fun phaseAngle(o: Observer): Radians {
        val observerHelioPos = o.computeHeliocentricEclipticPosition()
        val observerRq = observerHelioPos.lengthSquared
        val planetHelioPos = computeHeliocentricEclipticPosition(o)
        val planetRq = planetHelioPos.lengthSquared
        val observerPlanetRq = (observerHelioPos - planetHelioPos).lengthSquared
        val cosChi = (observerPlanetRq + planetRq - observerRq) / (2.0 * sqrt(observerPlanetRq * planetRq))
        return Radians(acos(cosChi))
    }

    override fun distance(o: Observer): Distance {
        val obsHelioPos = o.computeHeliocentricEclipticPosition()
        return AU((obsHelioPos - computeHeliocentricEclipticPosition(o)).length)
    }

    fun distanceFromSun(o: Observer) = computeHeliocentricEclipticPosition(o).length

    override fun computeJ2000EquatorialPosition(o: Observer): Triad {
        return MAT_VSOP87_TO_J2000
            .multiplyWithoutTranslation(computeHeliocentricEclipticPosition(o) - o.computeHeliocentricEclipticPosition())
    }

    fun computeHeliocentricEclipticPosition(o: Observer): Triad {
        var pos = computeEclipticPosition(o)
        var p = parent

        while (p != null) {
            pos += p.computeEclipticPosition(o)
            p = p.parent
        }

        return pos
    }

    internal fun internalComputeHeliocentricEclipticPosition(jde: JulianDay): Triad {
        var pos = internalComputePosition(jde).first
        var p = parent

        while (p != null) {
            pos += p.internalComputePosition(jde).first
            p = p.parent
        }

        return pos
    }

    /**
     * Computes the Planet position in Cartesian ecliptic (J2000) coordinates in AU, centered on the parent
     */
    open fun computeEclipticPosition(o: Observer): Triad {
        return computeEclipticPosition(o.jde, o, o.useLightTravelTime).first
    }

    /**
     * Computes the Planet velocity in Cartesian ecliptic (J2000) coordinates in AU, centered on the parent
     */
    open fun computeEclipticVelocity(o: Observer): Triad {
        return computeEclipticPosition(o.jde, o, o.useLightTravelTime).second
    }

    fun computeHeliocentricEclipticVelocity(o: Observer): Triad {
        var pos = computeEclipticVelocity(o)
        var p = parent

        while (p != null) {
            pos += p.computeEclipticVelocity(o)
            p = p.parent
        }

        return pos
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

    /**
     * Computes the angular size.
     */
    override fun angularSize(o: Observer): Angle {
        val radius = (rings?.size ?: radius).au.value
        return Radians(atan2(radius, computeJ2000EquatorialPosition(o).length))
    }

    /**
     * Computes the angular radius of the planet spheroid (i.e. without the rings)
     */
    fun spheroidAngularSize(o: Observer): Angle {
        return Radians(atan2(radius.au.value, computeJ2000EquatorialPosition(o).length))
    }

    /**
     * Computes the tilt of rotation axis w.r.t. ecliptic.
     * For Earth, this is epsilon, the angle between earth's rotational axis and pole of mean ecliptic of date.
     * Details: e.g. Hilton etal, Report on Precession and the Ecliptic, Cel.Mech.Dyn.Astr.94:351-67 (2006), Fig1.
     * For the other planets, it must be the angle between axis and Normal to the VSOP_J2000 coordinate frame.
     * For moons, it may be the obliquity against its planet's equatorial plane.
     */
    abstract fun computeRotObliquity(jde: JulianDay): Angle

    /**
     * Computes the longitude of ascending node of equator on the ecliptic.
     */
    abstract fun computeRotAscendingNode(): Angle

    final override fun rts(o: Observer, hasAtmosphere: Boolean): Triad {
        var hz = Radians.ZERO // Horizon parallax factor

        if (hasAtmosphere) {
            // Canonical refraction at horizon is -34'. Replace by pressure-dependent value here!
            val zero = Triad(1.0, 0.0, 0.0)
            hz += Radians(asin(o.refraction.backward(zero)[2]))
        }

        return internalComputeRTSTime(o, hz, hasAtmosphere)
    }

    internal open fun internalComputeRTSTime(o: Observer, hz: Angle, hasAtmosphere: Boolean): Triad {
        return CelestialObject.computeRTSTime(o, this, hz)
    }

    // Used to compute shadows.
    internal fun computeShadowMatrix(jde: JulianDay): Mat4 {
        var res = Mat4.translation(internalComputePosition(jde).first) * rotLocalToParent
        var p = parent

        while (p?.parent != null) {
            res = Mat4.translation(p.internalComputePosition(jde).first) * res * p.rotLocalToParent
            p = p.parent
        }

        return res * Mat4.zrotation((axisRotation + Degrees.PLUS_90).radians)
    }

    inline val isRotatingRetrograde: Boolean
        get() = siderealDay < 0.0

    /**
     * Computes the elongation angle for an observer.
     */
    fun elongation(o: Observer): Radians {
        val obsPos = o.computeHeliocentricEclipticPosition()
        val observerRq = obsPos.lengthSquared
        val planetHelioPos = computeHeliocentricEclipticPosition(o)
        val planetRq = planetHelioPos.lengthSquared
        val observerPlanetRq = (obsPos - planetHelioPos).lengthSquared
        return Radians(acos((observerPlanetRq + observerRq - planetRq) / (2.0 * sqrt(observerPlanetRq * observerRq))))
    }

    /**
     * Computes the planet phase ([0..1] illuminated fraction of the planet disk) for an observer.
     */
    fun illumination(o: Observer): Double {
        val obsPos = o.computeHeliocentricEclipticPosition()
        val observerRq = obsPos.lengthSquared
        val planetHelioPos = computeHeliocentricEclipticPosition(o)
        val planetRq = planetHelioPos.lengthSquared
        val observerPlanetRq = (obsPos - planetHelioPos).lengthSquared
        val cosChi = (observerPlanetRq + planetRq - observerRq) / (2.0 * sqrt(observerPlanetRq * planetRq))
        return 0.5 * abs(1.0 + cosChi)
    }

    /**
     * Synodic period for major planets in days.
     */
    fun synodicPeriod(o: Observer): Double {
        return if (o.home.siderealPeriod > 0.0 &&
            siderealPeriod > 0.0 &&
            (isPlanet || parent == o.home)
        ) {
            abs(1 / (1 / o.home.siderealPeriod - 1 / siderealPeriod))
        } else {
            0.0
        }
    }

    /**
     * Returns the orbital velocity in km/s
     */
    fun orbitalVelocity(o: Observer) = computeEclipticVelocity(o).length * (AU_KM / SECONDS_PER_DAY)

    /**
     * Returns the heliocentric velocity in km/s
     */
    fun heliocentricVelocity(o: Observer) = computeHeliocentricEclipticVelocity(o).length * (AU_KM / SECONDS_PER_DAY)

    override fun visualMagnitude(o: Observer, extra: Any?): Double {
        // Compute the phase angle i
        val observerHelioPos = o.computeHeliocentricEclipticPosition()
        val observerRq = observerHelioPos.lengthSquared
        val planetHelioPos = computeHeliocentricEclipticPosition(o)
        val planetRq = planetHelioPos.lengthSquared
        val observerPlanetHelioPos = observerHelioPos - planetHelioPos
        val observerPlanetRq = observerPlanetHelioPos.lengthSquared
        val dr = sqrt(observerPlanetRq * planetRq)
        val cosChi = (observerPlanetRq + planetRq - observerRq) / (2.0 * dr)
        val phaseAngle = Radians(acos(cosChi))
        var shadowFactor = 1.0
        val d = 5.0 * log10(dr)

        // TODO: Testar com luas de Júpiter???
        // Check if the satellite is inside the inner shadow of the parent planet
        if (parent!!.parent != null) {
            val parentHelioPos = parent.computeHeliocentricEclipticPosition(o)
            val parentRq = parentHelioPos.lengthSquared
            val posTimesParentPos = planetHelioPos.dot(parentHelioPos)

            if (posTimesParentPos > parentRq) {
                // The satellite is farther away from the sun than the parent planet.
                val sunRadius = parent.parent!!.radius.au.value
                val sunMinusParentRadius = sunRadius - parent.radius.au.value
                val quot = posTimesParentPos / parentRq

                // Compute d = distance from satellite center to border of inner shadow.
                // d>0 means inside the shadow cone.
                var ds = sunRadius - sunMinusParentRadius * quot -
                        sqrt((1.0 - sunMinusParentRadius / sqrt(parentRq)) * (planetRq - posTimesParentPos * quot))

                val radius = radius.au.value

                // The satellite is totally inside the inner shadow.
                if (ds >= radius) {
                    // Fit a more realistic magnitude for the Moon case.
                    // I used some empirical data for fitting. --AW
                    shadowFactor = if (this is Moon) 2.718E-5 else 1E-9
                } else if (ds > -radius) {
                    // The satellite is partly inside the inner shadow,
                    // compute a fantasy value for the magnitude:
                    ds /= radius
                    shadowFactor = (0.5 - (asin(ds) + ds * sqrt(1.0 - ds * ds)) / M_PI)
                }
            }
        }

        // Use empirical formulae for main planets when seen from earth
        return computeVisualMagnitude(
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

    protected open fun computeVisualMagnitude(
        o: Observer,
        phaseAngle: Angle,
        cosChi: Double,
        observerRq: Double,
        planetRq: Double,
        observerPlanetRq: Double,
        d: Double,
        shadowFactor: Double,
    ): Double {
        if (o.apparentMagnitudeAlgorithm == ApparentMagnitudeAlgorithm.EXPLANATORY_SUPPLEMENT_2013 &&
            this !is Moon &&
            absoluteMagnitude != -99.0
        ) {
            return absoluteMagnitude + d
        }

        // This formula source is unknown. But this is actually used even for the Moon!
        val radius = radius.au.value
        val p = (1.0 - phaseAngle.radians.value / M_PI) * cosChi + sqrt(1.0 - cosChi * cosChi) / M_PI
        val F = 2.0 * albedo * radius * radius * p / (3.0 * observerPlanetRq * planetRq) * shadowFactor
        return -26.73 - 2.5 * log10(F)
    }

    companion object {

        /**
         * Computes heliocentric position of Moon.
         */
        fun computeMoonHeliocentricCoordinates(jd: JulianDay): DoubleArray {
            val xyz6 = DoubleArray(6)
            val c = Elp82b.computeCoordinates(jd)

            xyz6[0] = c[0]
            xyz6[1] = c[1]
            xyz6[2] = c[2]

            // TODO: Some meaningful way to get speed?

            return xyz6
        }

        /**
         * Computes heliocentric position of Earth.
         */
        fun computeEarthHeliocentricCoordinates(jd: JulianDay): DoubleArray {
            val xyz6 = Vsop87.computeCoordinates(jd, 2)
            val moon = Elp82b.computeCoordinates(jd)

            xyz6[0] -= 0.0121505677733761 * moon[0]
            xyz6[1] -= 0.0121505677733761 * moon[1]
            xyz6[2] -= 0.0121505677733761 * moon[2]

            // TODO: HOW TO FIX EARTH SPEED?

            return xyz6
        }

        /**
         * Computes heliocentric position of Mars's moons.
         */
        fun computeMarsSatHeliocentricCoordinates(jd: JulianDay, body: Int): DoubleArray {
            return MarsSat.computeCoordinates(jd, body)
        }

        /**
         * Computes heliocentric position of Jupiter's moons.
         */
        fun computeL12HeliocentricCoordinates(jd: JulianDay, body: Int): DoubleArray {
            return L12.computeCoordinates(jd, body)
        }

        /**
         * Computes heliocentric position of Saturn's moons.
         */
        fun computeTass17HeliocentricCoordinates(jd: JulianDay, body: Int): DoubleArray {
            return Tass17.computeCoordinates(jd, body)
        }

        /**
         * Computes heliocentric position of Uranus' moons.
         */
        fun computeGust86HeliocentricCoordinates(jd: JulianDay, body: Int): DoubleArray {
            return Gust86.computeCoordinates(jd, body)
        }

        /**
         * Computes heliocentric position of the [planet].
         */
        fun computePlanetHeliocentricCoordinates(jd: JulianDay, planet: Int): DoubleArray {
            return Vsop87.computeCoordinates(jd, planet)
        }
    }
}