package br.tiagohm.astrum.core

import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

fun remainder(numer: Double, denom: Double): Double {
    return numer - round(numer / denom) * denom
}

fun bound(a: Double, b: Double, c: Double): Double {
    return max(a, min(b, c))
}

fun fuzzyEquals(a: Double, b: Double, eps: Double = Consts.EPSILON): Boolean {
    if (a == b) return true
    if (((a + eps) < b) || ((a - eps) > b)) return false
    return true
}

fun readDoubleArrayFromResources(name: String): DoubleArray {
    return Thread.currentThread().contextClassLoader.getResourceAsStream(name)?.let {
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
    return Thread.currentThread().contextClassLoader.getResourceAsStream(name)?.let {
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
    return Thread.currentThread().contextClassLoader.getResourceAsStream(name)?.readBytes() ?: ByteArray(0)
}

inline val Double.rad: Double
    get() = this * Consts.M_PI_180

inline val Double.deg: Double
    get() = this * Consts.M_180_PI

inline val Double.arcsecToRad: Double
    get() = this * Consts.M_ARCSEC_RAD

val Double.in360: Double
    get() {
        var d = this % 360.0
        if (d < 0.0) d += 360.0
        return d
    }


