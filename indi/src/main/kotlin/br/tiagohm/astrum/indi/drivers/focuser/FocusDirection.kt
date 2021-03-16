package br.tiagohm.astrum.indi.drivers.focuser

import br.tiagohm.astrum.indi.protocol.SwitchElement

enum class FocusDirection : SwitchElement {
    INWARD,
    OUTWARD;

    override val propName = "FOCUS_MOTION"

    override val elementName = "FOCUS_$name"

    companion object {

        // TODO: USAR replace em todos!!
        fun parse(name: String) = valueOf(name.replace("FOCUS_", ""))
    }
}