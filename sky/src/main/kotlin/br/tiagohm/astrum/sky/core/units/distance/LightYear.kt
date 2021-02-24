package br.tiagohm.astrum.sky.core.units.distance

import br.tiagohm.astrum.sky.LIGHT_YEAR

@Suppress("EXPERIMENTAL_FEATURE_WARNING", "RESERVED_MEMBER_INSIDE_INLINE_CLASS", "NOTHING_TO_INLINE")
inline class LightYear(override val value: Double) : Distance {

    override val meter: Meter
        get() = Meter(value * LIGHT_YEAR)

    override val kilometer: Kilometer
        get() = Kilometer(value * (LIGHT_YEAR / 1000))

    override val au: AU
        get() = AU(value * 63241.07708442430066362006)

    override val lightYear: LightYear
        get() = this

    override val parsec: Parsec
        get() = TODO("Not yet implemented")

    override fun compareTo(other: Distance) = value.compareTo(other.meter.value)

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as LightYear

        if (value != other.value) return false

        return true
    }

    override fun hashCode() = value.hashCode()

    override fun toString() = "$value"

    companion object {

        val ZERO = LightYear(0.0)
        val ONE = LightYear(1.0)
    }
}