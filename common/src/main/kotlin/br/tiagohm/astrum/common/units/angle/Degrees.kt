package br.tiagohm.astrum.common.units.angle

import br.tiagohm.astrum.common.M_PI_180
import br.tiagohm.astrum.common.pmod

@Suppress("EXPERIMENTAL_FEATURE_WARNING", "RESERVED_MEMBER_INSIDE_INLINE_CLASS", "NOTHING_TO_INLINE")
inline class Degrees(override val value: Double) : Angle {

    override val radians: Radians
        get() = if (value == 0.0) Radians.ZERO else Radians(value * M_PI_180)

    override val degrees: Degrees
        get() = this

    override val normalized: Degrees
        get() = Degrees(value.pmod(360.0))

    override operator fun minus(a: Angle) = Degrees(value - a.degrees.value)

    override operator fun unaryMinus() = Degrees(-value)

    override operator fun plus(a: Angle) = Degrees(value + a.degrees.value)

    override operator fun times(a: Angle) = Degrees(value * a.degrees.value)

    override operator fun times(n: Number) = Degrees(value * n.toDouble())

    override fun compareTo(other: Angle) = value.compareTo(other.degrees.value)

    override operator fun div(a: Angle) = Degrees(value / a.degrees.value)

    override operator fun div(n: Number) = Degrees(value / n.toDouble())

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as Degrees

        if (value != other.value) return false

        return true
    }

    override fun hashCode() = value.hashCode()

    override fun toString() = "$value"

    companion object {

        val ZERO = Degrees(0.0)
        val PLUS_45 = Degrees(45.0)
        val PLUS_90 = Degrees(90.0)
    }
}