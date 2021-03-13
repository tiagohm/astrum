package br.tiagohm.astrum.common.units.pressure

@Suppress("EXPERIMENTAL_FEATURE_WARNING", "RESERVED_MEMBER_INSIDE_INLINE_CLASS", "NOTHING_TO_INLINE")
inline class Pascal(override val value: Double) : Pressure {

    override val millibar: Millibar
        get() = Millibar(value / 100)

    override val atmosphere: Atmosphere
        get() = Atmosphere(value / 101325.0)

    override val pascal: Pascal
        get() = this

    override fun compareTo(other: Pressure) = value.compareTo(other.pascal.value)

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as Pascal

        if (value != other.value) return false

        return true
    }

    override fun hashCode() = value.hashCode()

    override fun toString() = "$value"

    companion object {

        val ZERO = Pascal(0.0)
        val ONE = Pascal(1.0)
    }
}