package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.protocol.SwitchElement

enum class DstState : SwitchElement {
    ENABLED;

    override val propName = "DST_STATE"

    override val elementName = "DST_$name"
}