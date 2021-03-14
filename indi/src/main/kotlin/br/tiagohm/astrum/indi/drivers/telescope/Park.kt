package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.protocol.SwitchProperty

enum class Park : SwitchProperty {
    PARK,
    UNPARK;

    override val propName = "TELESCOPE_PARK"

    override val elementName = name
}