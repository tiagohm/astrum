@file:Suppress("NOTHING_TO_INLINE")

package br.tiagohm.astrum.sky.core.units

// Radians

inline fun cos(a: Radians) = kotlin.math.cos(a.value)
inline fun sin(a: Radians) = kotlin.math.sin(a.value)
inline fun tan(a: Radians) = kotlin.math.tan(a.value)
inline operator fun Double.times(a: Radians) = Radians(this * a.value)
inline operator fun Double.plus(a: Radians) = Radians(this + a.value)
inline operator fun Double.minus(a: Radians) = Radians(this - a.value)

// Degrees

inline fun cos(a: Degrees) = kotlin.math.cos(a.radians.value)
inline fun sin(a: Degrees) = kotlin.math.sin(a.radians.value)
inline fun tan(a: Degrees) = kotlin.math.tan(a.radians.value)
inline operator fun Double.times(a: Degrees) = Degrees(this * a.value)
inline operator fun Double.plus(a: Degrees) = Degrees(this + a.value)
inline operator fun Double.minus(a: Degrees) = Degrees(this - a.value)
