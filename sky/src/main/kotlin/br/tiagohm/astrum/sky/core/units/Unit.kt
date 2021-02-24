package br.tiagohm.astrum.sky.core.units

interface Unit {

    val value: Double

    val isPositive: Boolean
        get() = value > 0.0

    val isNonNegative: Boolean
        get() = value >= 0.0

    val isNegative: Boolean
        get() = value < 0.0
}