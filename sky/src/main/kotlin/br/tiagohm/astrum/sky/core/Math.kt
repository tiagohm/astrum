@file:Suppress("NOTHING_TO_INLINE")

package br.tiagohm.astrum.sky.core

import br.tiagohm.astrum.sky.core.units.Degrees
import br.tiagohm.astrum.sky.core.units.Radians

// Radians

inline fun cos(a: Radians) = kotlin.math.cos(a.value)
inline fun sin(a: Radians) = kotlin.math.sin(a.value)
inline fun tan(a: Radians) = kotlin.math.tan(a.value)

// Degrees

inline fun cos(a: Degrees) = kotlin.math.cos(a.radians.value)
inline fun sin(a: Degrees) = kotlin.math.sin(a.radians.value)
inline fun tan(a: Degrees) = kotlin.math.tan(a.radians.value)
