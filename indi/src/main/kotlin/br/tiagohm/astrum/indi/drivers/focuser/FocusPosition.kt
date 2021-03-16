package br.tiagohm.astrum.indi.drivers.focuser

import br.tiagohm.astrum.indi.protocol.NumberElement

enum class FocusPosition : NumberElement {
    ABSOLUTE,
    RELATIVE;

    override val propName = "${name.substring(0, 3)}_FOCUS_POSITION"

    override val elementName = "FOCUS_${name}_POSITION"
}