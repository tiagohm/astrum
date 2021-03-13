package br.tiagohm.astrum.common.units.distance

import br.tiagohm.astrum.common.AU_KM
import br.tiagohm.astrum.common.LIGHT_YEAR

@Suppress("EXPERIMENTAL_FEATURE_WARNING", "RESERVED_MEMBER_INSIDE_INLINE_CLASS", "NOTHING_TO_INLINE")
inline class Meter(override val value: Double) : Distance {

    override val meter: Meter
        get() = this

    override val kilometer: Kilometer
        get() = Kilometer(value / 1000.0)

    override val au: AU
        get() = AU(value / (AU_KM * 1000.0))

    override val lightYear: LightYear
        get() = LightYear(value / LIGHT_YEAR)

    override val parsec: Parsec
        get() = TODO("Not yet implemented")

    override fun compareTo(other: Distance) = value.compareTo(other.meter.value)

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as Meter

        if (value != other.value) return false

        return true
    }

    override fun hashCode() = value.hashCode()

    override fun toString() = "$value"

    companion object {

        val ZERO = Meter(0.0)
        val ONE = Meter(1.0)
    }
}