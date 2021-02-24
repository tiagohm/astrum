package br.tiagohm.astrum.sky.core.units.temperature

import br.tiagohm.astrum.sky.core.units.Unit

interface Temperature : Unit, Comparable<Temperature> {

    val celsius: Celsius

    val fahrenheit: Fahrenheit

    val kelvin: Kelvin
}