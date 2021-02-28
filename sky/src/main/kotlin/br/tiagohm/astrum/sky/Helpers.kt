package br.tiagohm.astrum.sky

import br.tiagohm.astrum.sky.core.math.Mat4
import br.tiagohm.astrum.sky.core.math.Triad
import br.tiagohm.astrum.sky.core.precession.Precession
import br.tiagohm.astrum.sky.core.time.DateTime
import br.tiagohm.astrum.sky.core.units.angle.Degrees
import br.tiagohm.astrum.sky.core.units.angle.Radians
import java.io.InputStream
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

// Constants

const val AU = 1.495978706996262E+08
const val PARSEC = 30.857E+12
const val LIGHT_YEAR = 9460730472580800.0 // meters
const val EPSILON = 1E-12
const val M_PI = Math.PI
const val M_2_PI = 2 * M_PI
const val M_3_PI = 3 * M_PI
const val M_PI_2 = M_PI / 2
const val GAUSS_GRAV_K = 0.01720209895
const val GAUSS_GRAV_K_SQ = GAUSS_GRAV_K * GAUSS_GRAV_K
const val J2000 = 2451545.0 // epoch J2000: 12 UT on 1 Jan 2000
const val SPEED_OF_LIGHT = 299792.458 // (km/sec)
val EPS_0 = Degrees(23.4392803055555555555556) // Ecliptic obliquity of J2000.0, degrees
const val M_PI_180 = M_PI / 180.0
const val M_180_PI = 180.0 / M_PI
const val JD_SECOND = 1.0 / 86400.0
const val JD_MINUTE = 1.0 / 1440.0
const val JD_HOUR = 1.0 / 24.0
const val JD_DAY = 1.0
const val SECONDS_PER_DAY = 86400.0
const val ONE_OVER_JD_SECOND = SECONDS_PER_DAY
const val TZ_ERA_BEGINNING = 2395996.5 // December 1, 1847
const val M_ARCSEC_RAD = M_2_PI / (360.0 * 3600.0)

val MAT_J2000_TO_VSOP87 = Mat4.xrotation(-EPS_0) * Mat4.zrotation(Radians(0.0000275 * M_PI_180))
val MAT_VSOP87_TO_J2000 = MAT_J2000_TO_VSOP87.transpose()

val J2000_POLE = MAT_J2000_TO_VSOP87.multiplyWithoutTranslation(Triad.Z)

val MAT_J2000_TO_GALACTIC = Mat4(
    -0.054875539726, 0.494109453312, -0.867666135858, 0.0,
    -0.873437108010, -0.444829589425, -0.198076386122, 0.0,
    -0.483834985808, 0.746982251810, 0.455983795705, 0.0,
    0.0, 0.0, 0.0, 1.0
)

val MAT_GALACTIC_TO_J2000 = MAT_J2000_TO_GALACTIC.transpose()

val MAT_J2000_TO_SUPERGALACTIC = Mat4(
    0.37501548, -0.89832046, 0.22887497, 0.0,
    0.34135896, -0.09572714, -0.93504565, 0.0,
    0.86188018, 0.42878511, 0.27075058, 0.0,
    0.0, 0.0, 0.0, 1.0
)

val MAT_SUPERGALACTIC_TO_J2000 = MAT_J2000_TO_SUPERGALACTIC.transpose()

val MAT_J2000_TO_J1875 by lazy {
    val jdB1875 = DateTime.computeJDFromBesselianEpoch(1875.0)
    val p = Precession.computeVondrak(jdB1875)

    (Mat4.xrotation(Radians(84381.406 * 1.0 / 3600.0 * M_PI / 180.0)) *
            Mat4.zrotation(-p.psi) *
            Mat4.xrotation(-p.omega) *
            Mat4.zrotation(p.chi)).transpose()
}

// Math

fun remainder(numer: Double, denom: Double) = numer - round(numer / denom) * denom

@Suppress("NOTHING_TO_INLINE")
inline fun bound(a: Double, b: Double, c: Double) = max(a, min(b, c))

/**
 * Modulo where the result is always positive.
 */
fun Double.amod(b: Double) = (this % b).let { if (it <= 0.0) it + b else it }

/**
 * Modulo where the result is always nonnegative.
 */
fun Double.pmod(b: Double) = (this % b).let { if (it < 0.0) it + b else it }

// Misc

fun getResourceAsStream(name: String): InputStream? = Thread.currentThread().contextClassLoader.getResourceAsStream(name)

fun readDoubleArrayFromResources(name: String): DoubleArray {
    return getResourceAsStream(name)?.let {
        val bytes = it.readBytes()
        val res = DoubleArray(bytes.size / 8)
        for (i in bytes.indices step 8) {
            val bits = ((bytes[i].toLong() and 0xFF shl 0) or
                    (bytes[i + 1].toLong() and 0xFF shl 8) or
                    (bytes[i + 2].toLong() and 0xFF shl 16) or
                    (bytes[i + 3].toLong() and 0xFF shl 24) or
                    (bytes[i + 4].toLong() and 0xFF shl 32) or
                    (bytes[i + 5].toLong() and 0xFF shl 40) or
                    (bytes[i + 6].toLong() and 0xFF shl 48) or
                    (bytes[i + 7].toLong() and 0xFF shl 56))
            res[i / 8] = Double.fromBits(bits)
        }
        res
    } ?: DoubleArray(0)
}

fun readIntArrayFromResources(name: String): IntArray {
    return getResourceAsStream(name)?.let {
        val bytes = it.readBytes()
        val res = IntArray(bytes.size / 4)
        for (i in bytes.indices step 4) {
            val bits = ((bytes[i].toInt() and 0xFF shl 0) or
                    (bytes[i + 1].toInt() and 0xFF shl 8) or
                    (bytes[i + 2].toInt() and 0xFF shl 16) or
                    (bytes[i + 3].toInt() and 0xFF shl 24))
            res[i / 4] = bits
        }
        res
    } ?: IntArray(0)
}

fun readByteArrayFromResources(name: String): ByteArray {
    return getResourceAsStream(name)?.readBytes() ?: ByteArray(0)
}


