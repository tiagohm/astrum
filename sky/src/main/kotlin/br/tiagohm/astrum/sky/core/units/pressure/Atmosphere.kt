package br.tiagohm.astrum.sky.core.units.pressure

@Suppress("EXPERIMENTAL_FEATURE_WARNING", "RESERVED_MEMBER_INSIDE_INLINE_CLASS", "NOTHING_TO_INLINE")
inline class Atmosphere(override val value: Double) : Pressure {

    override val millibar: Millibar
        get() = Millibar(value * 1013.25)

    override val atmosphere: Atmosphere
        get() = this

    override val pascal: Pascal
        get() = Pascal(value * 101325)

    override fun compareTo(other: Pressure) = value.compareTo(other.atmosphere.value)

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as Atmosphere

        if (value != other.value) return false

        return true
    }

    override fun hashCode() = value.hashCode()

    override fun toString() = "$value"

    companion object {

        val ZERO = Atmosphere(0.0)
        val ONE = Atmosphere(1.0)
    }
}