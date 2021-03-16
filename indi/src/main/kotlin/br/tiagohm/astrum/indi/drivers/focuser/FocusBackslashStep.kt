package br.tiagohm.astrum.indi.drivers.focuser

import br.tiagohm.astrum.indi.protocol.NumberElement

enum class FocusBackslashStep : NumberElement {
    VALUE;

    override val propName = "FOCUS_BACKLASH_STEPS"

    override val elementName = "FOCUS_BACKLASH_VALUE"
}