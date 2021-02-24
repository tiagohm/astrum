package br.tiagohm.astrum.sky.core.units.angle

import br.tiagohm.astrum.sky.*

@Suppress("EXPERIMENTAL_FEATURE_WARNING", "RESERVED_MEMBER_INSIDE_INLINE_CLASS", "NOTHING_TO_INLINE")
inline class Radians(override val value: Double) : Angle {

    override val radians: Radians
        get() = this

    override val degrees: Degrees
        get() = if (value == 0.0) Degrees.ZERO else Degrees(value * M_180_PI)

    override val normalized: Radians
        get() = Radians(value.pmod(M_2_PI))

    override operator fun minus(a: Angle) = Radians(value - a.radians.value)

    override operator fun unaryMinus() = Radians(-value)

    override operator fun plus(a: Angle) = Radians(value + a.radians.value)

    override operator fun times(a: Angle) = Radians(value * a.radians.value)

    override operator fun times(a: Number) = Radians(value * a.toDouble())

    override fun compareTo(other: Angle) = value.compareTo(other.radians.value)

    override operator fun div(a: Angle) = Radians(value / a.radians.value)

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as Radians

        if (value != other.value) return false

        return true
    }

    override fun hashCode() = value.hashCode()

    override fun toString() = "$value"

    companion object {

        val ZERO = Radians(0.0)
        val PI = Radians(M_PI)
        val TWO_PI = Radians(M_2_PI)
        val PI_2 = Radians(M_PI_2)
    }
}