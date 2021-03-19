package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.protocol.SwitchElement

enum class Hibernate(override val elementName: String) : SwitchElement {
    ENABLE("Enable"),
    DISABLE("Disable");

    override val propName = "Hibernate"
}