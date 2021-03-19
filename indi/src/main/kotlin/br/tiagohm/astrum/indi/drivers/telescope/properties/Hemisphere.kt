package br.tiagohm.astrum.indi.drivers.telescope.properties

import br.tiagohm.astrum.indi.protocol.SwitchElement

enum class Hemisphere(override val elementName: String) : SwitchElement {
    NORTH("North"),
    SOUTH("South");

    override val propName = "HEMISPHERE"
}