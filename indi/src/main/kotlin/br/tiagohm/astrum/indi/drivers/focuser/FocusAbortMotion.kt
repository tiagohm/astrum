package br.tiagohm.astrum.indi.drivers.focuser

import br.tiagohm.astrum.indi.protocol.SwitchElement

enum class FocusAbortMotion : SwitchElement {
    ABORT;

    override val propName = "FOCUS_ABORT_MOTION"

    override val elementName = name
}