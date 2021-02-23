package br.tiagohm.astrum.sky.core.units

import br.tiagohm.astrum.sky.M_180_PI
import br.tiagohm.astrum.sky.M_2_PI
import br.tiagohm.astrum.sky.M_PI
import br.tiagohm.astrum.sky.pmod

@Suppress("EXPERIMENTAL_FEATURE_WARNING", "RESERVED_MEMBER_INSIDE_INLINE_CLASS", "NOTHING_TO_INLINE")
inline class Radians(override val value: Double) : Angle, Comparable<Any> {

    override val radians: Radians
        get() = this

    override val degrees: Degrees
        get() = Degrees(value * M_180_PI)

    override val normalized: Radians
        get() = Radians(value.pmod(M_2_PI))

    inline operator fun minus(a: Radians) = Radians(value - a.value)

    inline operator fun minus(a: Double) = Radians(value - a)

    inline operator fun unaryMinus() = Radians(-value)

    inline operator fun plus(a: Radians) = Radians(value + a.value)

    inline operator fun plus(a: Number) = Radians(value + a.toDouble())

    inline operator fun times(a: Radians) = Radians(value * a.value)

    inline operator fun times(a: Number) = Radians(value * a.toDouble())

    override fun compareTo(other: Any): Int {
        return if (other is Radians) value.compareTo(other.value)
        else if (other is Number) value.compareTo(other.toDouble())
        else if (other is Degrees) value.compareTo(other.radians.value)
        else throw IllegalArgumentException("Invalid compare type")
    }

    inline operator fun div(a: Radians) = Radians(value / a.value)

    inline operator fun div(a: Double) = Radians(value / a)

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as Radians

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    companion object {

        val ZERO = Radians(0.0)
        val PI = Radians(M_PI)
    }
}