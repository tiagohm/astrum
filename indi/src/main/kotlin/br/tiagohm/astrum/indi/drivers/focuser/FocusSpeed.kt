package br.tiagohm.astrum.indi.drivers.focuser

import br.tiagohm.astrum.indi.protocol.NumberElement

enum class FocusSpeed : NumberElement {
    VALUE;

    override val propName = "FOCUS_SPEED"

    override val elementName = "FOCUS_SPEED_VALUE"
}