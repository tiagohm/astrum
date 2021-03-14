package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.protocol.SwitchProperty

enum class PierSide : SwitchProperty {
    WEST,
    EAST;

    override val propName = "TELESCOPE_PIER_SIDE"

    override val elementName = "PIER_$name"
}