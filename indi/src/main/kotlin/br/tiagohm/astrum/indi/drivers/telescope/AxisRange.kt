package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.protocol.SwitchElement

enum class AxisRange : SwitchElement {
    FULL_STEP,
    HALF_STEP;

    override val propName = "AXIS_RANGE"

    override val elementName = "AXIS_$name"
}