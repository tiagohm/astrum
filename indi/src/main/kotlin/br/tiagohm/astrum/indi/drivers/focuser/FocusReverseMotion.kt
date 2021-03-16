package br.tiagohm.astrum.indi.drivers.focuser

import br.tiagohm.astrum.indi.protocol.SwitchElement

enum class FocusReverseMotion : SwitchElement {
    ENABLED,
    DISABLED;

    override val propName = "FOCUS_REVERSE_MOTION"

    override val elementName = "INDI_$name"
}