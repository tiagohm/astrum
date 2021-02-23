package br.tiagohm.astrum.sky.core.units

import br.tiagohm.astrum.sky.M_PI_180
import br.tiagohm.astrum.sky.pmod

@Suppress("EXPERIMENTAL_FEATURE_WARNING", "RESERVED_MEMBER_INSIDE_INLINE_CLASS", "NOTHING_TO_INLINE")
inline class Degrees(override val value: Double) : Angle, Comparable<Degrees> {

    override val radians: Radians
        get() = Radians(value * M_PI_180)

    override val degrees: Degrees
        get() = this

    override val normalized: Degrees
        get() = Degrees(value.pmod(360.0))

    inline operator fun minus(a: Degrees) = Degrees(value - a.value)

    inline operator fun minus(a: Double) = Degrees(value - a)

    inline operator fun unaryMinus() = Degrees(-value)

    inline operator fun plus(a: Degrees) = Degrees(value + a.value)

    inline operator fun plus(a: Double) = Degrees(value + a)

    inline operator fun times(a: Degrees) = Degrees(value * a.value)

    inline operator fun times(a: Number) = Degrees(value * a.toDouble())

    inline operator fun div(a: Degrees) = Degrees(value / a.value)

    inline operator fun div(a: Double) = Degrees(value / a)

    override fun compareTo(other: Degrees) = value.compareTo(other.value)

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as Degrees

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    companion object {

        val ZERO = Degrees(0.0)
    }
}