package br.tiagohm.astrum.common.units.temperature

import br.tiagohm.astrum.common.units.Unit

interface Temperature : Unit, Comparable<Temperature> {

    val celsius: Celsius

    val fahrenheit: Fahrenheit

    val kelvin: Kelvin
}