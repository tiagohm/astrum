package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.protocol.SwitchElement

enum class Hemisphere(override val elementName: String) : SwitchElement {
    NORTH("North"),
    SOUTH("South");

    override val propName = "HEMISPHERE"
}