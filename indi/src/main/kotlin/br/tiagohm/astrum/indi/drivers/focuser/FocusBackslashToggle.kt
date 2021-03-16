package br.tiagohm.astrum.indi.drivers.focuser

import br.tiagohm.astrum.indi.protocol.SwitchElement

enum class FocusBackslashToggle : SwitchElement {
    ENABLED,
    DISABLED;

    override val propName = "FOCUS_BACKLASH_TOGGLE"

    override val elementName = "INDI_$name"
}