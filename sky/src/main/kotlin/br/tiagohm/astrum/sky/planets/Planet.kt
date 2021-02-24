package br.tiagohm.astrum.sky.planets

import br.tiagohm.astrum.sky.*
import br.tiagohm.astrum.sky.core.Algorithms
import br.tiagohm.astrum.sky.core.cos
import br.tiagohm.astrum.sky.core.ephemeris.Elp82b
import br.tiagohm.astrum.sky.core.ephemeris.L12
import br.tiagohm.astrum.sky.core.ephemeris.MarsSat
import br.tiagohm.astrum.sky.core.ephemeris.Vsop87
import br.tiagohm.astrum.sky.core.math.Mat4
import br.tiagohm.astrum.sky.core.math.Triad
import br.tiagohm.astrum.sky.core.orbit.KeplerOrbit
import br.tiagohm.astrum.sky.core.orbit.Orbit
import br.tiagohm.astrum.sky.core.sin
import br.tiagohm.astrum.sky.core.units.angle.Angle
import br.tiagohm.astrum.sky.core.units.angle.Degrees
import br.tiagohm.astrum.sky.core.units.angle.Radians
import br.tiagohm.astrum.sky.core.units.distance.Distance
import br.tiagohm.astrum.sky.planets.major.earth.Moon
import br.tiagohm.astrum.sky.planets.major.jupiter.Jupiter
import br.tiagohm.astrum.sky.planets.major.mars.Mars
import br.tiagohm.astrum.sky.planets.major.neptune.Neptune
import br.tiagohm.astrum.sky.planets.major.saturn.Saturn
import br.tiagohm.astrum.sky.planets.major.uranus.Uranus
import br.tiagohm.astrum.sky.planets.minor.pluto.Pluto
import kotlin.math.*

abstract class Planet internal constructor(
    // English planet name
    final override val id: String,
    // Gets the equator radius of the planet
    val equatorialRadius: Distance,
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
    private val positionCache = HashMap<Double, Pair<Triad, Triad>>()

    val oneMinusOblateness = 1.0 - oblateness

    /**
     * Gets duration of sidereal year
     */
    open val siderealPeriod = if (orbit is KeplerOrbit &&
        type != PlanetType.OBSERVER &&
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
                ?: if (type.ordinal >= PlanetType.ASTEROID.ordinal) orbit!!.semiMajorAxis.au.value
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

    val isAsteroid = type == PlanetType.ASTEROID

    val isComet = type == PlanetType.COMET

    val isDwarfPlanet = type == PlanetType.DWARF_PLANET

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
    open fun computeSiderealTime(jd: Double, jde: Double, useNutation: Boolean) = Degrees.ZERO

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
        axisRotation = computeSiderealTime(jd, jde, useNutation)
        // We have to call with both to correct this for earth with the new model.
        // For Earth, this is sidereal time for Greenwich, i.e. hour angle between meridian and First Point of Aries.
        // Return angle between ascending node of planet's equator and (J2000) ecliptic (?)
        // Store to later compute central meridian data etc.
        rotLocalToParent = internalComputeTransformationMatrix(jd, jde, useNutation)
    }

    internal fun computeEclipticPosition(jde: Double, o: Observer, useLightTravelTime: Boolean): Pair<Triad, Triad> {
        val jd = jde - o.computeDeltaT(jde) / SECONDS_PER_DAY

        return if (useLightTravelTime) {
            val length =
                (internalComputeHeliocentricEclipticPosition(jde) - o.home.internalComputeHeliocentricEclipticPosition(jde)).length
            val lsc = length * (AU / (SPEED_OF_LIGHT * SECONDS_PER_DAY))

            val pos = internalComputePosition(jde - lsc)
            computeTransformationMatrix(jd - lsc, jde - lsc, o.useNutation)
            pos
        } else {
            val pos = internalComputePosition(jde)
            computeTransformationMatrix(jd, jde, o.useNutation)
            pos
        }
    }

    protected open fun internalComputeTransformationMatrix(jd: Double, jde: Double, useNutation: Boolean): Mat4 {
        // return Mat4.zrotation(rotation.ascendingNode) * Mat4.xrotation(rotation.obliquity)
        return Mat4.zrotation(Radians.ZERO) * Mat4.xrotation(Radians.ZERO)
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

    internal fun internalComputeHeliocentricEclipticPosition(jde: Double): Triad {
        var pos = internalComputePosition(jde).first
        var p = parent

        while (p != null) {
            pos += p.internalComputePosition(jde).first
            p = p.parent
        }

        return pos
    }

    override fun computeEclipticPosition(o: Observer): Triad {
        return computeEclipticPosition(o.jde, o, o.useLightTravelTime).first
    }

    override fun computeEclipticVelocity(o: Observer): Triad {
        return computeEclipticPosition(o.jde, o, o.useLightTravelTime).second
    }

    override fun computeHeliocentricEclipticVelocity(o: Observer): Triad {
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
        val radius = (rings?.size ?: equatorialRadius).au.value
        return Radians(atan2(radius, computeJ2000EquatorialPosition(o).length))
    }

    /**
     * Computes the angular radius of the planet spheroid (i.e. without the rings)
     */
    fun spheroidAngularSize(o: Observer): Angle {
        return Radians(atan2(equatorialRadius.au.value, computeJ2000EquatorialPosition(o).length))
    }

    /**
     * Computes the obliquity.
     * For Earth, this is epsilon, the angle between earth's rotational axis and pole of mean ecliptic of date.
     * Details: e.g. Hilton etal, Report on Precession and the Ecliptic, Cel.Mech.Dyn.Astr.94:351-67 (2006), Fig1.
     * For the other planets, it must be the angle between axis and Normal to the VSOP_J2000 coordinate frame.
     * For moons, it may be the obliquity against its planet's equatorial plane.
     */
    open fun computeRotObliquity(jde: Double): Angle = Radians.ZERO

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
        val phi = o.site.latitude.radians
        val coeff = o.home.computeMeanSolarDay() / o.home.siderealDay

        val coord = Algorithms.rectangularToSphericalCoordinates(computeSiderealPositionGeometric(o))
        val ra = M_2_PI - coord.x.radians.value
        val dec = coord.y

        var ha = ra * 12.0 / M_PI
        if (ha > 24.0) ha -= 24.0
        // It seems necessary to have ha in [-12,12]!
        if (ha > 12.0) ha -= 24.0

        val jd = o.jd
        val ct = (jd - jd.toInt()) * 24.0
        var transit = ct - ha * coeff // For Earth: coeff = (360.985647 / 360.0) = 1.0027379083333

        if (ha > 12.0 && ha <= 24.0) transit += 24.0

        transit += o.dateTime.utcOffset + 12.0

        transit = transit.pmod(24.0)

        val cosH = (sin(hz) - sin(phi) * sin(dec)) / (cos(phi) * cos(dec))

        val rise: Double
        val set: Double

        // Circumpolar
        if (cosH < -1.0) {
            rise = 100.0
            set = 100.0
        }
        // Never rises
        else if (cosH > 1.0) {
            rise = -100.0
            set = -100.0
        } else {
            val HC = acos(cosH) * 12.0 * coeff / M_PI
            rise = (transit - HC).pmod(24.0)
            set = (transit + HC).pmod(24.0)
        }

        return Triad(rise, transit, set)
    }

    // Used to compute shadows.
    internal fun computeShadowMatrix(jde: Double): Mat4 {
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
    fun orbitalVelocity(o: Observer) = computeEclipticVelocity(o).length * AU / SECONDS_PER_DAY

    /**
     * Returns the heliocentric velocity in km/s
     */
    fun heliocentricVelocity(o: Observer) = computeHeliocentricEclipticVelocity(o).length * AU / SECONDS_PER_DAY

    override fun visualMagnitude(o: Observer, extra: Any?): Double {
        // Compute the phase angle i
        val observerHelioPos = o.computeHeliocentricEclipticPosition()
        val observerRq = observerHelioPos.lengthSquared
        val planetHelioPos = computeHeliocentricEclipticPosition(o)
        val planetRq = planetHelioPos.lengthSquared
        val observerPlanetRq = (observerHelioPos - planetHelioPos).lengthSquared
        val dr = sqrt(observerPlanetRq * planetRq)
        val cosChi = (observerPlanetRq + planetRq - observerRq) / (2.0 * dr)
        val phaseAngle = Radians(acos(cosChi))
        var shadowFactor = 1.0
        val d = 5.0 * log10(dr)

        // TODO: Testar com luas de Júpiter?
        // Check if the satellite is inside the inner shadow of the parent planet
        if (parent!!.parent != null) {
            val parentHelioPos = parent.computeHeliocentricEclipticPosition(o)
            val parentRq = parentHelioPos.lengthSquared
            val posTimesParentPos = planetHelioPos.dot(parentHelioPos)

            if (posTimesParentPos > parentRq) {
                // The satellite is farther away from the sun than the parent planet.
                val sunRadius = parent.parent!!.equatorialRadius.au.value
                val sunMinusParentRadius = sunRadius - parent.equatorialRadius.au.value
                val quot = posTimesParentPos / parentRq

                // Compute d = distance from satellite center to border of inner shadow.
                // d>0 means inside the shadow cone.
                var ds = sunRadius - sunMinusParentRadius * quot -
                        sqrt((1.0 - sunMinusParentRadius / sqrt(parentRq)) * (planetRq - posTimesParentPos * quot))

                val radius = equatorialRadius.au.value

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

        // TODO: Plutão e Luas de Júpiter

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
        val radius = equatorialRadius.au.value
        val p = (1.0 - phaseAngle.radians.value / M_PI) * cosChi + sqrt(1.0 - cosChi * cosChi) / M_PI
        val F = 2.0 * albedo * radius * radius * p /
                (3.0 * observerPlanetRq * planetRq) * shadowFactor
        return -26.73 - 2.5 * log10(F)
    }

    final override fun visualMagnitudeWithExtinction(o: Observer, extra: Any?): Double {
        val mag = visualMagnitude(o, extra)

        return if (isAboveHorizon(o)) {
            val altAzPos = computeAltAzPositionGeometric(o).normalized
            o.extinction.forward(altAzPos, mag)
        } else {
            mag
        }
    }

    companion object {

        fun computeMoonHeliocentricCoordinates(jd: Double): DoubleArray {
            val xyz6 = DoubleArray(6)
            val c = Elp82b.computeCoordinates(jd)

            xyz6[0] = c[0]
            xyz6[1] = c[1]
            xyz6[2] = c[2]

            // TODO: Some meaningful way to get speed?

            return xyz6
        }

        fun computeEarthHeliocentricCoordinates(jd: Double): DoubleArray {
            val xyz6 = Vsop87.computeCoordinates(jd, 2)
            val moon = Elp82b.computeCoordinates(jd)

            xyz6[0] -= 0.0121505677733761 * moon[0]
            xyz6[1] -= 0.0121505677733761 * moon[1]
            xyz6[2] -= 0.0121505677733761 * moon[2]

            // TODO: HOW TO FIX EARTH SPEED?

            return xyz6
        }

        fun computeMarsSatHeliocentricCoordinates(jd: Double, body: Int): DoubleArray {
            return MarsSat.computeCoordinates(jd, body)
        }

        fun computeJupiterSatHeliocentricCoordinates(jd: Double, body: Int): DoubleArray {
            return L12.computeCoordinates(jd, body)
        }

        fun computePlanetHeliocentricCoordinates(jd: Double, planet: Int): DoubleArray {
            return Vsop87.computeCoordinates(jd, planet)
        }
    }
}