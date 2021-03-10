package br.tiagohm.astrum.sky.stars

import br.tiagohm.astrum.sky.*
import br.tiagohm.astrum.sky.core.coordinates.EquatorialCoord
import br.tiagohm.astrum.sky.core.math.Triad
import br.tiagohm.astrum.sky.core.math.cos
import br.tiagohm.astrum.sky.core.math.sin
import br.tiagohm.astrum.sky.core.units.angle.Angle
import br.tiagohm.astrum.sky.core.units.angle.Radians
import br.tiagohm.astrum.sky.dso.DeepSky
import kotlin.math.log10

/**
 * Star.
 *
 * @param posEquJ2000 Equatorial J2000 position.
 * @param properMotionRA Proper Motion in RA axis [mas/y].
 * @param properMotionDEC Proper Motion in DEC axis [mas/y].
 * @param radialVelocity Radial Velocity in km/s.
 * @param mB B Magnitude.
 * @param mV Visual Magnitude.
 * @param mI I Magnitude.
 */
open class Star(
    posEquJ2000: EquatorialCoord = EquatorialCoord.ZERO,
    val parallax: Double = 0.0,
    val properMotionRA: Double = 0.0,
    val properMotionDEC: Double = 0.0,
    val radialVelocity: Double = 0.0,
    mB: Double = 99.0,
    mV: Double = 99.0,
    val mI: Double = 99.0,
) : DeepSky(
    posEquJ2000,
    mB,
    mV,
    distance = computeDistanceFromParallax(parallax),
) {

    private val pv by lazy {
        computePositionAndVelocity(
            posEquJ2000.ra, posEquJ2000.dec,
            Radians(((properMotionRA / 1000.0) * M_ARCSEC_RAD) / cos(posEquJ2000.dec)),
            Radians((properMotionDEC / 1000.0) * M_ARCSEC_RAD),
            parallax,
            radialVelocity,
        ).let { Triple(it.first, it.first.normalized, it.second) }
    }

    override val type = PlanetType.STAR

    fun absoluteMagnitude(o: Observer) = if (parallax != 0.0) visualMagnitude(o) + 5.0 * (1.0 + log10(parallax / 1000)) else 99.0

    override fun computeJ2000EquatorialPosition(o: Observer) = pv.second

    private fun computeEquinoxEquatorialPositionFromPV(o: Observer): Triad {
        // TODO: How to improve it?
        val T = (o.jde - J2000).value
        val distance = pv.first
        val velocity = (pv.third * T)
        return (distance + velocity).normalized
    }

    override fun computeEquinoxEquatorialPosition(o: Observer): Triad {
        return o.j2000ToEquinoxEquatorial(computeEquinoxEquatorialPositionFromPV(o), false)
    }

    override fun computeEquinoxEquatorialPositionApparent(o: Observer): Triad {
        return o.j2000ToEquinoxEquatorial(computeEquinoxEquatorialPositionFromPV(o), true)
    }

    override fun info(o: Observer): Map<String, Any> {
        return HashMap<String, Any>().also {
            it.putAll(super.info(o))

            it["parallax"] = parallax
            it["absoluteMagnitude"] = absoluteMagnitude(o)
            it["bMag"] = mB
            if (mB < 50 && mV < 50) it["bV"] = mB - mV
        }
    }

    override fun toString(): String {
        return "Star(parallax=$parallax, properMotionRA=$properMotionRA," +
                " properMotionDEC=$properMotionDEC, radialVelocity=$radialVelocity," +
                " mB=$mB, mV=$mV, mI=$mI, distance=$distance, type=$type)"
    }

    companion object {

        fun computePositionAndVelocity(
            ra: Angle,
            dec: Angle,
            pmRA: Angle, // rad/y
            pmDEC: Angle, // rad/y
            parallax: Double, // mas
            radialVelocity: Double, // km/s
        ): Pair<Triad, Triad> {
            val r = 206264.8062470963551564734 / (parallax / 1000.0)
            val td = pmRA.radians.value / 365.25 // rad/day
            val pd = pmDEC.radians.value / 365.25 // rad/day
            val rd = (radialVelocity / AU_KM) * SECONDS_PER_DAY // AU/day
            val st = sin(ra)
            val ct = cos(ra)
            val sp = sin(dec)
            val cp = cos(dec)
            val rcp = r * cp
            val x = rcp * ct
            val y = rcp * st
            val rpd = r * pd
            val w = rpd * sp - cp * rd

            val pv = DoubleArray(6)

            pv[0] = x
            pv[1] = y
            pv[2] = r * sp
            pv[3] = -y * td - w * ct
            pv[4] = x * td - w * st
            pv[5] = rpd * cp + sp * rd

            return Triad(pv[0], pv[1], pv[2]) to Triad(pv[3], pv[4], pv[5])
        }
    }
}