package br.tiagohm.astrum.core

import br.tiagohm.astrum.core.algorithms.MoonSecularAcceleration
import br.tiagohm.astrum.core.algorithms.Refraction
import br.tiagohm.astrum.core.math.Mat4
import br.tiagohm.astrum.core.math.Quad
import br.tiagohm.astrum.core.math.Triad
import br.tiagohm.astrum.core.sky.Planet
import br.tiagohm.astrum.core.time.DateTime
import br.tiagohm.astrum.core.time.DeltaTAlgorithmType
import kotlin.math.*

@Suppress("PrivatePropertyName")
data class Observer(
    val planet: Planet,
    val site: ObservationSite,
    val dateTime: DateTime,
    val deltaTAlgorithm: DeltaTAlgorithmType = DeltaTAlgorithmType.ESPEANAK_MEEUS,
    val pressure: Double = 1013.0,
    val temperature: Double = 25.0,
    val useTopocentricCoordinates: Boolean = true,
    val useNutation: Boolean = true,
    val useLightTravelTime: Boolean = true,
) {

    private var JD = DoubleArray(2) // JD_UT and DeltaT, DeltaT = TT - UT
    private val refraction = Refraction(pressure, temperature)

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

    var lightTimeSunPosition = Triad.ZERO
        private set

    init {
        JD[0] = dateTime.jd
        JD[1] = computeDeltaT(dateTime.jd)

        planet.update(this)

        matAltAzToEquinoxEqu = computeRotAltAzToEquatorial()
        matEquinoxEquToAltAz = matAltAzToEquinoxEqu.transpose()

        // Multiply static J2000 earth axis tilt (eclipticalJ2000<->equatorialJ2000)
        // in effect, this matrix transforms from VSOP87 ecliptical J2000 to planet-based equatorial coordinates.
        // For earth, matJ2000ToEquinoxEqu is the precession matrix.
        matEquinoxEquDateToJ2000 = Consts.MAT_VSOP87_TO_J2000 * computeRotEquatorialToVsop87()
        matJ2000ToEquinoxEqu = matEquinoxEquDateToJ2000.transpose()
        matJ2000ToAltAz = matEquinoxEquToAltAz * matJ2000ToEquinoxEqu
        matAltAzToJ2000 = matJ2000ToAltAz.transpose()

        matHeliocentricEclipticToEquinoxEqu =
            matJ2000ToEquinoxEqu * Consts.MAT_VSOP87_TO_J2000 * Mat4.translation(-computeCenterVsop87Position())

        val matAltAzToVsop87 = Consts.MAT_J2000_TO_VSOP87 * matEquinoxEquDateToJ2000 * matAltAzToEquinoxEqu

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
    fun computeCenterVsop87Position() = planet.computeHeliocentricEclipticPosition(this)

    /**
     * Computes the distance between observer and home planet center in AU
     */
    fun computeDistanceFromCenter() = computeTopographicOffsetFromCenter()[3]

    /**
     * Computes the distance between observer and home planet center in meters
     */
    fun computeDistanceFromCenterInMeters() = computeDistanceFromCenter() * Consts.AU

    /**
     * Computes the geocentric rectangular coordinates of the observer in AU, plus geocentric latitude
     */
    fun computeTopographicOffsetFromCenter(): Quad {
        if (planet.equatorialRadius <= 0.0) {
            return Quad(0.0, 0.0, site.latitude.rad, site.altitude / (1000.0 * Consts.AU))
        }

        val a = planet.equatorialRadius
        val bByA = planet.oneMinusOblateness

        val lat = site.latitude.rad
        val u = atan(bByA * tan(lat))
        val alt = site.altitude / (1000.0 * Consts.AU * a)

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
        return Mat4.zrotation(computeLocalSiderealTime()) * Mat4.yrotation((90.0 - site.latitude).rad)
    }

    fun computeRotEquatorialToVsop87() = planet.computeRotEquatorialToVsop87()

    /**
     * Computes the sidereal time of the prime meridian (i.e. Rotation Angle) shifted by the observer longitude.
     */
    fun computeLocalSiderealTime(): Double {
        return (planet.computeSiderealTime(jd, jde, useNutation) + site.longitude).rad
    }

    internal fun j2000ToEquinoxEquatorial(a: Triad, refract: Boolean): Triad {
        return if (refract) {
            refraction.forward(a.transform(matJ2000ToEquinoxEqu))
        } else {
            matJ2000ToEquinoxEqu * a
        }
    }

    internal fun j2000ToAltAz(a: Triad, refract: Boolean): Triad {
        return if (refract) {
            refraction.forward(a.transform(matJ2000ToAltAz))
        } else {
            matJ2000ToAltAz * a
        }
    }

    internal fun j2000ToGalactic(a: Triad) = Consts.MAT_J2000_TO_GALACTIC * a

    internal fun j2000ToSupergalactic(a: Triad) = Consts.MAT_J2000_TO_SUPERGALACTIC * a

    internal fun j2000ToJ1875(a: Triad) = Consts.MAT_J2000_TO_J1875 * a

    internal fun altAzToEquinoxEquatorial(a: Triad, refract: Boolean): Triad {
        return if (refract) {
            refraction.backward(a.transform(matAltAzToEquinoxEqu))
        } else {
            a.transform(matAltAzToEquinoxEqu)
        }
    }

    private fun computeLightTimeSunPosition() {
        planet.computeEclipticPosition(jde, this, false)
        val obsPosJDE = planet.computeHeliocentricEclipticPosition()
        val obsDist = obsPosJDE.length
        planet.computeEclipticPosition(jde - obsDist * (Consts.AU / (Consts.SPEED_OF_LIGHT * 86400.0)), this, false)
        val obsPosJDEbefore = planet.computeHeliocentricEclipticPosition()
        lightTimeSunPosition = obsPosJDE - obsPosJDEbefore
        planet.computeEclipticPosition(jde, this, false)
    }
}