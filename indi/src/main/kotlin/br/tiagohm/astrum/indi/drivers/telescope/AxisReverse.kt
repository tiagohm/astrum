package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.protocol.SwitchElement

enum class AxisReverse : SwitchElement {
    AXIS1,
    AXIS2;

    override val propName = "AXIS_REVERSE"

    override val elementName = "${name}_REVERSE"
}