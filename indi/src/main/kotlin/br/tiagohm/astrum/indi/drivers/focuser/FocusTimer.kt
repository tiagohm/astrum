package br.tiagohm.astrum.indi.drivers.focuser

import br.tiagohm.astrum.indi.protocol.NumberElement

enum class FocusTimer : NumberElement {
    VALUE;

    override val propName = "FOCUS_TIMER"

    override val elementName = "FOCUS_TIMER_VALUE"
}