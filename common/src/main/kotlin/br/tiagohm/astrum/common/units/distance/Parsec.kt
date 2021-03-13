package br.tiagohm.astrum.common.units.distance

@Suppress("EXPERIMENTAL_FEATURE_WARNING", "RESERVED_MEMBER_INSIDE_INLINE_CLASS", "NOTHING_TO_INLINE")
inline class Parsec(override val value: Double) : Distance {

    override val meter: Meter
        get() = TODO("Not yet implemented")

    override val kilometer: Kilometer
        get() = TODO("Not yet implemented")

    override val au: AU
        get() = TODO("Not yet implemented")

    override val lightYear: LightYear
        get() = TODO("Not yet implemented")

    override val parsec: Parsec
        get() = this

    override fun compareTo(other: Distance) = value.compareTo(other.meter.value)

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as Parsec

        if (value != other.value) return false

        return true
    }

    override fun hashCode() = value.hashCode()

    override fun toString() = "$value"

    companion object {

        val ZERO = Parsec(0.0)
        val ONE = Parsec(1.0)
    }
}