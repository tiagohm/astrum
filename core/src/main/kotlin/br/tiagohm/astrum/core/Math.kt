package br.tiagohm.astrum.core

import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

fun remainder(numer: Double, denom: Double): Double {
    return numer - round(numer / denom) * denom
}

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

