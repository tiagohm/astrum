package br.tiagohm.astrum.sky.core.units.pressure

import br.tiagohm.astrum.sky.core.units.Unit

interface Pressure : Unit, Comparable<Pressure> {

    val millibar: Millibar

    val atmosphere: Atmosphere

    val pascal: Pascal
}