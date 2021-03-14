package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.protocol.SwitchProperty

enum class ParkOption : SwitchProperty {
    CURRENT,
    DEFAULT;

    override val propName = "TELESCOPE_PARK_OPTION"

    override val elementName = "PARK_$name"
}