package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.protocol.SwitchElement

enum class OnCoordSet : SwitchElement {
    SLEW,
    TRACK,
    SYNC;

    override val propName = "ON_COORD_SET"

    override val elementName = name

    companion object {

        fun parse(name: String) = valueOf(name)
    }
}