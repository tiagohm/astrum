package br.tiagohm.astrum.sky.core.units.distance

import br.tiagohm.astrum.sky.AU

@Suppress("EXPERIMENTAL_FEATURE_WARNING", "RESERVED_MEMBER_INSIDE_INLINE_CLASS", "NOTHING_TO_INLINE")
inline class AU(override val value: Double) : Distance {

    override val meter: Meter
        get() = Meter(value * AU * 1000.0)

    override val kilometer: Kilometer
        get() = Kilometer(value * AU)

    override val au: AU
        get() = this

    override val lightYear: LightYear
        get() = LightYear(value / 63241.07708442430066362006)

    override val parsec: Parsec
        get() = TODO("Not yet implemented")

    override fun compareTo(other: Distance) = value.compareTo(other.meter.value)

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as AU

        if (value != other.value) return false

        return true
    }

    override fun hashCode() = value.hashCode()

    override fun toString() = "$value"

    companion object {

        val ZERO = AU(0.0)
        val ONE = AU(1.0)
    }
}