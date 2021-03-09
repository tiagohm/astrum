package br.tiagohm.astrum.sky

import br.tiagohm.astrum.sky.atmosphere.Extinction
import br.tiagohm.astrum.sky.constellations.Constellation
import br.tiagohm.astrum.sky.core.Algorithms
import br.tiagohm.astrum.sky.core.coordinates.*
import br.tiagohm.astrum.sky.core.math.*
import br.tiagohm.astrum.sky.core.units.angle.Angle
import br.tiagohm.astrum.sky.core.units.angle.Radians
import br.tiagohm.astrum.sky.core.units.distance.AU
import br.tiagohm.astrum.sky.core.units.distance.Distance
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos

interface CelestialObject {

    /**
     * The celestial body type.
     */
    val type: PlanetType

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
    fun rts(o: Observer, hasAtmosphere: Boolean = true): Triad {
        var hz = Radians.ZERO // Horizon parallax factor

        if (hasAtmosphere) {
            // Canonical refraction at horizon is -34'. Replace by pressure-dependent value here!
            val zero = Triad(1.0, 0.0, 0.0)
            hz += Radians(asin(o.refraction.backward(zero)[2]))
        }

        return computeRTSTime(o, this, hz)
    }

    /**
     * Returns object's apparent V magnitude as seen from observer, without including extinction.
     * For Sun, pass Moon instance on extra parameter to take into account solar eclipse.
     */
    fun visualMagnitude(o: Observer, extra: Any? = null): Double

    /**
     * Returns object's apparent V magnitude as seen from observer including extinction.
     * For Sun, pass Moon instance on extra parameter to take into account solar eclipse.
     */
    fun visualMagnitudeWithExtinction(o: Observer, extra: Any? = null): Double {
        val mag = visualMagnitude(o, extra)

        return if (isAboveHorizon(o)) {
            val altAzPos = computeAltAzPositionGeometric(o).normalized
            o.extinction.forward(altAzPos, mag)
        } else {
            mag
        }
    }

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
     * Computes the distance from this object to observer.
     */
    fun distance(o: Observer): Distance = AU.ZERO

    /**
     * Computes observer-centered equatorial coordinates at equinox J2000.
     */
    fun computeJ2000EquatorialPosition(o: Observer): Triad

    fun equatorialJ2000(o: Observer): EquatorialCoord {
        val pos = computeJ2000EquatorialPosition(o)
        val (a, b) = Algorithms.rectangularToSphericalCoordinates(pos)
        return EquatorialCoord(a.normalized, b)
    }

    fun equatorial(o: Observer): EquatorialCoord {
        val pos = computeEquinoxEquatorialPosition(o)
        val (a, b) = Algorithms.rectangularToSphericalCoordinates(pos)
        return EquatorialCoord(a.normalized, b)
    }

    fun hourAngle(
        o: Observer,
        apparent: Boolean = true
    ): EquatorialCoord {
        val pos = if (apparent) computeSiderealPositionApparent(o) else computeSiderealPositionGeometric(o)
        val (a, b) = Algorithms.rectangularToSphericalCoordinates(pos)
        return EquatorialCoord(Radians(M_2_PI - a.radians.value).degrees.normalized / 15.0, b)
    }

    fun horizontal(
        o: Observer,
        southAzimuth: Boolean = false,
        apparent: Boolean = true
    ): HorizontalCoord {
        val pos = if (apparent) computeAltAzPositionApparent(o) else computeAltAzPositionGeometric(o)
        val (a, b) = Algorithms.rectangularToSphericalCoordinates(pos)
        val direction = if (southAzimuth) M_2_PI else M_3_PI // N is zero, E is 90 degrees
        val az = (direction - a.radians.value).let { if (it > M_2_PI) it - M_2_PI else it }
        return HorizontalCoord(Radians(az), b)
    }

    fun galactic(o: Observer): GalacticCoord {
        val pos = computeGalacticPosition(o)
        val (a, b) = Algorithms.rectangularToSphericalCoordinates(pos)
        return GalacticCoord(a, b)
    }

    fun supergalactic(o: Observer): SupergalacticCoord {
        val pos = computeSupergalacticPosition(o)
        val (a, b) = Algorithms.rectangularToSphericalCoordinates(pos)
        return SupergalacticCoord(a, b)
    }

    fun eclipticJ2000(o: Observer): EclipticCoord {
        val eclJ2000 = o.home.computeRotObliquity(J2000)
        val (ra, dec) = Algorithms.rectangularToSphericalCoordinates(computeJ2000EquatorialPosition(o))
        val equ = EquatorialCoord(ra, dec)
        var (lambda, beta) = equ.toEcliptic(eclJ2000)
        if (lambda < Radians.ZERO) lambda += Radians.TWO_PI
        return EclipticCoord(lambda, beta)
    }

    fun ecliptic(o: Observer): EclipticCoord {
        val (ra, dec) = Algorithms.rectangularToSphericalCoordinates(computeEquinoxEquatorialPosition(o))
        val equ = EquatorialCoord(ra, dec)
        var (lambda, beta) = equ.toEcliptic(o.computeEclipticObliquity())
        if (lambda < Radians.ZERO) lambda += Radians.TWO_PI
        return EclipticCoord(lambda, beta)
    }

    fun constellation(o: Observer) = Constellation.find(o, computeEquinoxEquatorialPosition(o))!!

    companion object {

        fun computeRTSTime(o: Observer, co: CelestialObject, hz: Angle): Triad {
            val phi = o.site.latitude.radians
            val coeff = o.home.meanSolarDay / o.home.siderealDay

            val coord = Algorithms.rectangularToSphericalCoordinates(co.computeSiderealPositionGeometric(o))
            val ra = M_2_PI - coord.x.radians.value
            val dec = coord.y

            var ha = ra * 12.0 / M_PI
            if (ha > 24.0) ha -= 24.0
            // It seems necessary to have ha in [-12,12]!
            if (ha > 12.0) ha -= 24.0

            val jd = o.jd.value
            val ct = (jd - jd.toInt()) * 24.0
            var transit = ct - ha * coeff // For Earth: coeff = (360.985647 / 360.0) = 1.0027379083333

            if (ha > 12.0 && ha <= 24.0) transit += 24.0

            transit += o.utcOffset + 12.0

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
    }
}