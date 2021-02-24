package br.tiagohm.astrum.sky.core.units.temperature

@Suppress("EXPERIMENTAL_FEATURE_WARNING", "RESERVED_MEMBER_INSIDE_INLINE_CLASS", "NOTHING_TO_INLINE")
inline class Celsius(override val value: Double) : Temperature {

    override val celsius: Celsius
        get() = this

    override val fahrenheit: Fahrenheit
        get() = Fahrenheit(value * 1.8 + 32)

    override val kelvin: Kelvin
        get() = Kelvin(value + 273.15)

    override fun compareTo(other: Temperature) = value.compareTo(other.celsius.value)

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as Celsius

        if (value != other.value) return false

        return true
    }

    override fun hashCode() = value.hashCode()

    override fun toString() = "$value"

    companion object {

        val ZERO = Celsius(0.0)
    }
}