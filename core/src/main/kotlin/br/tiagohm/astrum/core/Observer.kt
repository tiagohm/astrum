package br.tiagohm.astrum.core

import br.tiagohm.astrum.core.algorithms.math.Mat4
import br.tiagohm.astrum.core.algorithms.math.Quad
import br.tiagohm.astrum.core.algorithms.math.Triad
import br.tiagohm.astrum.core.algorithms.nutation.Nutation
import br.tiagohm.astrum.core.algorithms.time.DateTime
import br.tiagohm.astrum.core.algorithms.time.MoonSecularAcceleration
import br.tiagohm.astrum.core.algorithms.time.TimeCorrectionType
import br.tiagohm.astrum.core.sky.atmosphere.ApparentMagnitudeAlgorithm
import br.tiagohm.astrum.core.sky.atmosphere.Extinction
import br.tiagohm.astrum.core.sky.atmosphere.Refraction
import br.tiagohm.astrum.core.sky.planets.Sun
import br.tiagohm.astrum.core.sky.planets.major.earth.Earth
import br.tiagohm.astrum.core.sky.planets.major.earth.Moon
import kotlin.math.*

@Suppress("PrivatePropertyName")
data class Observer(
    val home: Earth,
    val site: Location,
    val dateTime: DateTime,
    val deltaTAlgorithm: TimeCorrectionType = TimeCorrectionType.ESPEANAK_MEEUS,
    val pressure: Double = 1013.0,
    val temperature: Celsius = 25.0,
    val extinctionCoefficient: Double = 0.13,
    val useTopocentricCoordinates: Boolean = true,
    val useNutation: Boolean = true,
    val useLightTravelTime: Boolean = true,
    val apparentMagnitudeAlgorithm: ApparentMagnitudeAlgorithm = ApparentMagnitudeAlgorithm.EXPLANATORY_SUPPLEMENT_2013,
) {

    val refraction = Refraction(pressure, temperature)
    val extinction = Extinction(extinctionCoefficient)

    val jde by lazy { JD[0] + JD[1] / 86400.0 }

    val jd by lazy { JD[0] }

    val mjd by lazy { JD[0] - 2400000.5 }

    private val matAltAzToEquinoxEqu: Mat4
    private val matEquinoxEquToAltAz: Mat4
    private val matJ2000ToAltAz: Mat4
    private val matAltAzToJ2000: Mat4
    private val matJ2000ToEquinoxEqu: Mat4
    private val matEquinoxEquDateToJ2000: Mat4
    private val matHeliocentricEclipticToEquinoxEqu: Mat4
    private val matAltAzToHeliocentricEclipticJ2000: Mat4
    private val matHeliocentricEclipticJ2000ToAltAz: Mat4

    private var JD = DoubleArray(2) // JD_UT and DeltaT, DeltaT = TT - UT

    var lightTimeSunPosition = Triad.ZERO
        private set

    init {
        JD[0] = dateTime.jd
        JD[1] = computeDeltaT(dateTime.jd)

        home.update(this)

        matAltAzToEquinoxEqu = computeRotAltAzToEquatorial()
        matEquinoxEquToAltAz = matAltAzToEquinoxEqu.transpose()

        // Multiply static J2000 earth axis tilt (eclipticalJ2000<->equatorialJ2000)
        // in effect, this matrix transforms from VSOP87 ecliptical J2000 to planet-based equatorial coordinates.
        // For earth, matJ2000ToEquinoxEqu is the precession matrix.
        matEquinoxEquDateToJ2000 = MAT_VSOP87_TO_J2000 * computeRotEquatorialToVsop87()
        matJ2000ToEquinoxEqu = matEquinoxEquDateToJ2000.transpose()
        matJ2000ToAltAz = matEquinoxEquToAltAz * matJ2000ToEquinoxEqu
        matAltAzToJ2000 = matJ2000ToAltAz.transpose()

        matHeliocentricEclipticToEquinoxEqu =
            matJ2000ToEquinoxEqu * MAT_VSOP87_TO_J2000 * Mat4.translation(-computeCenterVsop87Position())

        val matAltAzToVsop87 = MAT_J2000_TO_VSOP87 * matEquinoxEquDateToJ2000 * matAltAzToEquinoxEqu

        if (useTopocentricCoordinates) {
            val offset = computeTopographicOffsetFromCenter()
            val sigma = site.latitude.rad - offset[2]
            val rho = offset[3]

            matAltAzToHeliocentricEclipticJ2000 =
                Mat4.translation(computeCenterVsop87Position()) *
                        matAltAzToVsop87 *
                        Mat4.translation(Triad(rho * sin(sigma), 0.0, rho * cos(sigma)))

            matHeliocentricEclipticJ2000ToAltAz =
                Mat4.translation(Triad(-rho * sin(sigma), 0.0, -rho * cos(sigma))) *
                        matAltAzToVsop87.transpose() *
                        Mat4.translation(-computeCenterVsop87Position())
        } else {
            val center = computeCenterVsop87Position()
            matAltAzToHeliocentricEclipticJ2000 = Mat4.translation(center) * matAltAzToVsop87
            matHeliocentricEclipticJ2000ToAltAz = matAltAzToVsop87.transpose() * Mat4.translation(-center)
        }

        if (useLightTravelTime) {
            computeLightTimeSunPosition()
        }
    }

    fun computeDeltaT(jd: Double): Double {
        var deltaT = deltaTAlgorithm.compute(jd)

        if (!deltaTAlgorithm.deltaTdontUseMoon) {
            deltaT += MoonSecularAcceleration.compute(jd, deltaTAlgorithm.deltaTnDot, false)
        }

        return deltaT
    }

    /**
     * Returns the observer heliocentric position
     */
    fun computeHeliocentricEclipticPosition() = Triad(
        matAltAzToHeliocentricEclipticJ2000[12],
        matAltAzToHeliocentricEclipticJ2000[13],
        matAltAzToHeliocentricEclipticJ2000[14]
    )

    /**
     * Computes the position of the home planet center in the heliocentric VSOP87 frame in AU
     */
    fun computeCenterVsop87Position() = home.computeHeliocentricEclipticPosition(this)

    /**
     * Computes the distance between observer and home planet center in AU
     */
    fun computeDistanceFromCenter() = computeTopographicOffsetFromCenter()[3]

    /**
     * Computes the distance between observer and home planet center in meters
     */
    fun computeDistanceFromCenterInMeters() = computeDistanceFromCenter() * AU

    /**
     * Computes the geocentric rectangular coordinates of the observer in AU, plus geocentric latitude
     */
    fun computeTopographicOffsetFromCenter(): Quad {
        if (home.equatorialRadius <= 0.0) {
            return Quad(0.0, 0.0, site.latitude.rad, site.altitude / (1000.0 * AU))
        }

        val a = home.equatorialRadius
        val bByA = home.oneMinusOblateness

        val lat = site.latitude.rad
        val u = atan(bByA * tan(lat))
        val alt = site.altitude / (1000.0 * AU * a)

        val rhoSinPhiPrime = bByA * sin(u) + alt * sin(lat)
        val rhoCosPhiPrime = cos(u) + alt * cos(lat)

        val rho = sqrt(rhoSinPhiPrime * rhoSinPhiPrime + rhoCosPhiPrime * rhoCosPhiPrime)
        val phiPrime = asin(rhoSinPhiPrime / rho)

        return Quad(rhoCosPhiPrime * a, rhoSinPhiPrime * a, phiPrime, rho * a)
    }

    /**
     * Computes rotation matrix for conversion of alt-azimuthal to equatorial coordinates
     * For Earth we need JD(UT), for other planets JDE! To be general, just have both in here!
     */
    fun computeRotAltAzToEquatorial(): Mat4 {
        return Mat4.zrotation(computeSiderealTime()) * Mat4.yrotation((90.0 - site.latitude).rad)
    }

    fun computeRotEquatorialToVsop87() = home.computeRotEquatorialToVsop87()

    /**
     * Computes the sidereal time in radians of the prime meridian (i.e. Rotation Angle) shifted by the observer longitude.
     */
    fun computeSiderealTime(): Radians {
        return (home.computeSiderealTime(jd, jde, useNutation) + site.longitude).rad
    }

    /**
     * Compute the mean sidereal time in hours of current date shifted by the observer longitude
     */
    fun computeMeanSiderealTime(): Double {
        var time = (home.computeSiderealTime(jd, jde, false) + site.longitude) / 15.0
        time = time.pmod(24.0)
        return if (time < 0) time + 24.0 else time
    }

    /**
     * Compute the apparent sidereal time in hours of current date shifted by the observer longitude
     */
    fun computeApparentSiderealTime(): Double {
        var time = (home.computeSiderealTime(jd, jde, true) + site.longitude) / 15.0
        time = time.pmod(24.0)
        return if (time < 0) time + 24.0 else time
    }

    internal fun j2000ToEquinoxEquatorial(a: Triad, refract: Boolean): Triad {
        return if (refract) {
            refraction.forward(a.transform(matJ2000ToEquinoxEqu))
        } else {
            matJ2000ToEquinoxEqu * a
        }
    }

    internal fun equinoxEquatorialToJ2000(a: Triad, refract: Boolean): Triad {
        return if (refract) {
            refraction.backward(a.transform(matEquinoxEquToAltAz))
        } else {
            matEquinoxEquDateToJ2000 * a
        }
    }

    internal fun j2000ToAltAz(a: Triad, refract: Boolean): Triad {
        return if (refract) {
            refraction.forward(a.transform(matJ2000ToAltAz))
        } else {
            matJ2000ToAltAz * a
        }
    }

    internal fun altAzToEquinoxEquatorial(a: Triad, refract: Boolean): Triad {
        return if (refract) {
            refraction.backward(a.transform(matAltAzToEquinoxEqu))
        } else {
            a.transform(matAltAzToEquinoxEqu)
        }
    }

    fun computeEclipticObliquity(): Double {
        return home.computeRotObliquity(jde) + if (useNutation) Nutation.compute(jde).deltaEpsilon else 0.0
    }

    private fun computeLightTimeSunPosition() {
        val obsPosJDE = home.internalComputeHeliocentricEclipticPosition(jde)
        val obsDist = obsPosJDE.length
        val obsPosJDEbefore = home.internalComputeHeliocentricEclipticPosition(jde - obsDist * (AU / (SPEED_OF_LIGHT * 86400.0)))
        lightTimeSunPosition = obsPosJDE - obsPosJDEbefore
    }

    fun computeEclipseFactor(moon: Moon): Double {
        val sun = home.parent!! as Sun
        val lp = lightTimeSunPosition
        val p3 = computeHeliocentricEclipticPosition()
        val RS = sun.equatorialRadius

        val trans = moon.computeShadowMatrix(jde)
        val c = trans * Triad.ZERO
        val radius = moon.equatorialRadius

        var v1 = lp - p3
        var v2 = c - p3
        val L = v1.length
        val l = v2.length

        v1 /= L
        v2 /= l

        val R = RS / L
        val r = radius / l
        val d = (v1 - v2).length

        return when {
            // Distance too far
            d >= R + r -> 1.0
            // Umbra
            d <= r - R -> 0.0
            // Penumbra completely inside
            d <= R - r -> 1.0 - r * r / (R * R)
            // Penumbra partially inside
            else -> {
                val x = (R * R + d * d - r * r) / (2.0 * d)

                val alpha = acos(x / R)
                val beta = acos((d - x) / r)

                val AR = R * R * (alpha - 0.5 * sin(2.0 * alpha))
                val Ar = r * r * (beta - 0.5 * sin(2.0 * beta))
                val AS = R * R * 2.0 * asin(1.0)

                1.0 - (AR + Ar) / AS
            }
        }
    }

    fun eclipseObscuration(moon: Moon) = 100.0 * (1.0 - computeEclipseFactor(moon))
}