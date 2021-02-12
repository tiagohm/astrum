package br.tiagohm.astrum.core

import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

fun remainder(numer: Double, denom: Double): Double {
    return numer - round(numer / denom) * denom
}

@Suppress("NOTHING_TO_INLINE")
inline fun bound(a: Double, b: Double, c: Double): Double {
    return max(a, min(b, c))
}

/**
 * Modulo where the result is always nonnegative.
 */
fun Double.pmod(b: Double): Double {
    val res = this % b
    return if (res < 0.0) res + b else res
}

fun Double.ranged(value: Double): Double {
    var d = this % value
    if (d < 0.0) d += value
    return d
}
