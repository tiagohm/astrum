package br.tiagohm.astrum.indi.drivers.telescope.properties

import br.tiagohm.astrum.indi.protocol.SwitchElement

enum class OnCoordSet : SwitchElement {
    SLEW,
    TRACK,
    SYNC;

    override val propName = "ON_COORD_SET"

    override val elementName = name
}