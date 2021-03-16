package br.tiagohm.astrum.indi.drivers.focuser

import br.tiagohm.astrum.indi.protocol.NumberElement

enum class FocusMax : NumberElement {
    VALUE;

    override val propName = "FOCUS_MAX"

    override val elementName = "FOCUS_MAX_VALUE"
}