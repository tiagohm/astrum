package br.tiagohm.astrum.common.units.pressure

@Suppress("EXPERIMENTAL_FEATURE_WARNING", "RESERVED_MEMBER_INSIDE_INLINE_CLASS", "NOTHING_TO_INLINE")
inline class Millibar(override val value: Double) : Pressure {

    override val millibar: Millibar
        get() = this

    override val atmosphere: Atmosphere
        get() = Atmosphere(value / 1013.25)

    override val pascal: Pascal
        get() = Pascal(value * 100)

    override fun compareTo(other: Pressure) = value.compareTo(other.millibar.value)

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as Millibar

        if (value != other.value) return false

        return true
    }

    override fun hashCode() = value.hashCode()

    override fun toString() = "$value"

    companion object {

        val ZERO = Millibar(0.0)
        val ONE = Millibar(1.0)
    }
}