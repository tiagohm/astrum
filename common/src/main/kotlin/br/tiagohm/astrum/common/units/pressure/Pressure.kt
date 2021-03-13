package br.tiagohm.astrum.common.units.pressure

import br.tiagohm.astrum.common.units.Unit

interface Pressure : Unit, Comparable<Pressure> {

    val millibar: Millibar

    val atmosphere: Atmosphere

    val pascal: Pascal
}