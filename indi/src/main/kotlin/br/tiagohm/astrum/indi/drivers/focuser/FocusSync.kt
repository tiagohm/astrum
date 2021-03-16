package br.tiagohm.astrum.indi.drivers.focuser

import br.tiagohm.astrum.indi.protocol.NumberElement

enum class FocusSync : NumberElement {
    VALUE;

    override val propName = "FOCUS_SYNC"

    override val elementName = "FOCUS_SYNC_VALUE"
}