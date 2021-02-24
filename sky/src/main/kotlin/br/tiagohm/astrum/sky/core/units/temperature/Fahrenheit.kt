package br.tiagohm.astrum.sky.core.units.temperature

@Suppress("EXPERIMENTAL_FEATURE_WARNING", "RESERVED_MEMBER_INSIDE_INLINE_CLASS", "NOTHING_TO_INLINE")
inline class Fahrenheit(override val value: Double) : Temperature {

    override val celsius: Celsius
        get() = Celsius((value - 32.0) / 1.8)

    override val fahrenheit: Fahrenheit
        get() = this

    override val kelvin: Kelvin
        get() = Kelvin((value + 459.67) / 1.8)

    override fun compareTo(other: Temperature) = value.compareTo(other.fahrenheit.value)

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as Fahrenheit

        if (value != other.value) return false

        return true
    }

    override fun hashCode() = value.hashCode()

    override fun toString() = "$value"

    companion object {

        val ZERO = Fahrenheit(0.0)
    }
}