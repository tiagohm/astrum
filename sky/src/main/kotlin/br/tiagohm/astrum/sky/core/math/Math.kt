@file:Suppress("NOTHING_TO_INLINE")

package br.tiagohm.astrum.sky.core.math

import br.tiagohm.astrum.sky.core.units.angle.Angle

inline fun cos(a: Angle) = kotlin.math.cos(a.radians.value)
inline fun sin(a: Angle) = kotlin.math.sin(a.radians.value)
inline fun tan(a: Angle) = kotlin.math.tan(a.radians.value)
