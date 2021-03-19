package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.protocol.SwitchElement

enum class Park : SwitchElement {
    PARK,
    UNPARK;

    override val propName = "TELESCOPE_PARK"

    override val elementName = name
}