package br.tiagohm.astrum.common.units.distance

import br.tiagohm.astrum.common.AU_KM
import br.tiagohm.astrum.common.LIGHT_YEAR

@Suppress("EXPERIMENTAL_FEATURE_WARNING", "RESERVED_MEMBER_INSIDE_INLINE_CLASS", "NOTHING_TO_INLINE")
inline class Kilometer(override val value: Double) : Distance {

    override val meter: Meter
        get() = Meter(value * 1000.0)

    override val kilometer: Kilometer
        get() = this

    override val au: AU
        get() = AU(value / AU_KM)

    override val lightYear: LightYear
        get() = LightYear(value / (LIGHT_YEAR / 1000.0))

    override val parsec: Parsec
        get() = TODO("Not yet implemented")

    override fun compareTo(other: Distance) = value.compareTo(other.meter.value)

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as Kilometer

        if (value != other.value) return false

        return true
    }

    override fun hashCode() = value.hashCode()

    override fun toString() = "$value"

    companion object {

        val ZERO = Kilometer(0.0)
        val ONE = Kilometer(1.0)
    }
}