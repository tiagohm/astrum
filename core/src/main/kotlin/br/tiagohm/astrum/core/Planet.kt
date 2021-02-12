package br.tiagohm.astrum.core

import kotlin.math.*

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
    // Rings
    val rings: Ring? = null,
) : CelestialObject {

    private var rotLocalToParent = Mat4.IDENTITY
    private var axisRotation = 0.0
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
    open val absoluteMagnitude = -99.0

    /**
     * Returns the mean opposition magnitude, defined as V(1,0)+5log10(a(a-1))
     */
    open val meanOppositionMagnitude: Double
        get() {
            // Source: Explanatory Supplement 2013, Table 10.6 and formula (10.5) with semimajorAxis a from Table 8.7
            // TODO: Fixed Mean Opposition Magnitute for some objects.
            // TODO: Testar com luas de Júpiter e asteroides
            if (absoluteMagnitude <= -99.0) return 100.0

            val a = parent?.orbit?.semiMajorAxis
                ?: if (type.ordinal >= PlanetType.ASTEROID.ordinal) orbit!!.semiMajorAxis else return 100.0

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
        axisRotation = computeSiderealTime(jd, jde, useNutation)
        // We have to call with both to correct this for earth with the new model.
        // For Earth, this is sidereal time for Greenwich, i.e. hour angle between meridian and First Point of Aries.
        // Return angle between ascending node of planet's equator and (J2000) ecliptic (?)
        // Store to later compute central meridian data etc.
        rotLocalToParent = internalComputeTransformationMatrix(jd, jde, useNutation)
    }

    internal fun computeEclipticPosition(jde: Double, o: Observer, useLightTravelTime: Boolean): Pair<Triad, Triad> {
        val jd = jde - o.computeDeltaT(jde) / 86400.0

        return if (useLightTravelTime) {
            val length =
                (internalComputeHeliocentricEclipticPosition(jde) - o.home.internalComputeHeliocentricEclipticPosition(jde)).length
            val lsc = length * (AU / (SPEED_OF_LIGHT * 86400.0))

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
     * Computes the angular size in degrees.
     */
    override fun angularSize(o: Observer): Double {
        val radius = rings?.size ?: equatorialRadius
        return atan2(radius, computeJ2000EquatorialPosition(o).length).deg
    }

    /**
     * Computes the angular radius (degrees) of the planet spheroid (i.e. without the rings)
     */
    fun spheroidAngularSize(o: Observer): Double {
        return atan2(equatorialRadius, computeJ2000EquatorialPosition(o).length).deg
    }

    /**
     * Computes the obliquity in radians.
     * For Earth, this is epsilon, the angle between earth's rotational axis and pole of mean ecliptic of date.
     * Details: e.g. Hilton etal, Report on Precession and the Ecliptic, Cel.Mech.Dyn.Astr.94:351-67 (2006), Fig1.
     * For the other planets, it must be the angle between axis and Normal to the VSOP_J2000 coordinate frame.
     * For moons, it may be the obliquity against its planet's equatorial plane.
     */
    open fun computeRotObliquity(jde: Double): Double = 0.0

    final override fun rts(o: Observer, hasAtmosphere: Boolean): Triad {
        var hz = 0.0

        if (hasAtmosphere) {
            // Canonical refraction at horizon is -34'. Replace by pressure-dependent value here!
            val zero = Triad(1.0, 0.0, 0.0)
            hz += asin(o.refraction.backward(zero)[2])
        }

        // TODO: For Moon: hz += (0.7275 * 0.95).rad; // horizon parallax factor
        return internalComputeRTSTime(o, hz, hasAtmosphere)
    }

    // "hz" is in radians
    internal open fun internalComputeRTSTime(o: Observer, hz: Double, hasAtmosphere: Boolean): Triad {
        val phi = o.site.latitude.rad
        val coeff = o.home.computeMeanSolarDay() / o.home.siderealDay

        var (ra, dec) = Algorithms.rectangularToSphericalCoordinates(computeSiderealPositionGeometric(o))
        ra = M_2_PI - ra

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

        return res * Mat4.zrotation((axisRotation + 90.0).rad)
    }

    inline val isRotatingRetrograde: Boolean
        get() = siderealDay < 0.0

    /**
     * Computes the elongation angle (radians) for an observer.
     */
    fun elongation(o: Observer): Double {
        val obsPos = o.computeHeliocentricEclipticPosition()
        val observerRq = obsPos.lengthSquared
        val planetHelioPos = computeHeliocentricEclipticPosition(o)
        val planetRq = planetHelioPos.lengthSquared
        val observerPlanetRq = (obsPos - planetHelioPos).lengthSquared
        return acos((observerPlanetRq + observerRq - planetRq) / (2.0 * sqrt(observerPlanetRq * observerRq)))
    }

    /**
     * Computes the planet phase ([0..1] illuminated fraction of the planet disk) for an observer.
     */
    fun phase(o: Observer): Double {
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
    fun orbitalVelocity(o: Observer) = computeEclipticVelocity(o).length * AU / 86400.0

    /**
     * Returns the heliocentric velocity in km/s
     */
    fun heliocentricVelocity(o: Observer) = computeHeliocentricEclipticVelocity(o).length * AU / 86400.0

    override fun visualMagnitude(o: Observer): Double {
        // Compute the phase angle i
        val observerHelioPos = o.computeHeliocentricEclipticPosition()
        val observerRq = observerHelioPos.lengthSquared
        val planetHelioPos = computeHeliocentricEclipticPosition(o)
        val planetRq = planetHelioPos.lengthSquared
        val observerPlanetRq = (observerHelioPos - planetHelioPos).lengthSquared
        val dr = sqrt(observerPlanetRq * planetRq)
        val cosChi = (observerPlanetRq + planetRq - observerRq) / (2.0 * dr)
        val phaseAngle = acos(cosChi)
        var shadowFactor = 1.0
        val d = 5.0 * log10(dr)

        // TODO: Testar com satélites!!
        // Check if the satellite is inside the inner shadow of the parent planet
        if (parent!!.parent != null) {
            val parentHelioPos = parent.computeHeliocentricEclipticPosition(o)
            val parentRq = parentHelioPos.lengthSquared
            val posTimesParentPos = planetHelioPos.dot(parentHelioPos)

            if (posTimesParentPos > parentRq) {
                // The satellite is farther away from the sun than the parent planet.
                val sunRadius = parent.parent!!.equatorialRadius
                val sunMinusParentRadius = sunRadius - parent.equatorialRadius
                val quot = posTimesParentPos / parentRq

                // Compute d = distance from satellite center to border of inner shadow.
                // d>0 means inside the shadow cone.
                var ds = sunRadius - sunMinusParentRadius * quot -
                        sqrt((1.0 - sunMinusParentRadius / sqrt(parentRq)) * (planetRq - posTimesParentPos * quot))

                // The satellite is totally inside the inner shadow.
                if (ds >= equatorialRadius) {
                    // TODO: Usar is Moon
                    // Fit a more realistic magnitude for the Moon case.
                    // I used some empirical data for fitting. --AW
                    shadowFactor = if (id == "Moon") 2.718e-5 else 1e-9
                } else if (ds > -equatorialRadius) {
                    // The satellite is partly inside the inner shadow,
                    // compute a fantasy value for the magnitude:
                    ds /= equatorialRadius
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

    // phaseAngle is in radians
    protected open fun computeVisualMagnitude(
        o: Observer,
        phaseAngle: Double,
        cosChi: Double,
        observerRq: Double,
        planetRq: Double,
        observerPlanetRq: Double,
        d: Double,
        shadowFactor: Double,
    ): Double {
        if (o.apparentMagnitudeAlgorithm == ApparentMagnitudeAlgorithm.EXPLANATORY_SUPPLEMENT_2013 &&
            id != "Moon" &&
            absoluteMagnitude != -99.0
        ) {
            return absoluteMagnitude + d
        }

        // This formula source is unknown. But this is actually used even for the Moon!
        val p = (1.0 - phaseAngle / M_PI) * cosChi + sqrt(1.0 - cosChi * cosChi) / M_PI
        val F = 2.0 * albedo * equatorialRadius * equatorialRadius * p /
                (3.0 * observerPlanetRq * planetRq) * shadowFactor
        return -26.73 - 2.5 * log10(F)
    }

    final override fun visualMagnitudeWithExtinction(o: Observer): Double {
        val mag = visualMagnitude(o)

        return if (isAboveHorizon(o)) {
            val altAzPos = computeAltAzPositionGeometric(o).normalized
            o.extinction.forward(altAzPos, mag)
        } else {
            mag
        }
    }

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