package br.tiagohm.astrum.indi.drivers.focuser

import br.tiagohm.astrum.indi.protocol.NumberElement

enum class FocusMin : NumberElement {
    VALUE;

    override val propName = "FOCUS_MIN"

    override val elementName = "FOCUS_MIN_VALUE"
}