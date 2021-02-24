package br.tiagohm.astrum.sky.core.units.temperature

@Suppress("EXPERIMENTAL_FEATURE_WARNING", "RESERVED_MEMBER_INSIDE_INLINE_CLASS", "NOTHING_TO_INLINE")
inline class Kelvin(override val value: Double) : Temperature {

    override val celsius: Celsius
        get() = Celsius(value - 273.15)

    override val fahrenheit: Fahrenheit
        get() = Fahrenheit(value * 1.8 - 459.67)

    override val kelvin: Kelvin
        get() = this

    override fun compareTo(other: Temperature) = value.compareTo(other.kelvin.value)

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as Kelvin

        if (value != other.value) return false

        return true
    }

    override fun hashCode() = value.hashCode()

    override fun toString() = "$value"

    companion object {

        val ZERO = Kelvin(0.0)
    }
}