package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.protocol.SwitchProperty

enum class OnCoordSet : SwitchProperty {
    SLEW,
    TRACK,
    SYNC;

    override val propName = "ON_COORD_SET"

    override val elementName = name
}