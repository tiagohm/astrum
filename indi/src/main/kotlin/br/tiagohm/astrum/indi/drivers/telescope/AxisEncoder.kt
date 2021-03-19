package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.protocol.NumberElement

enum class AxisEncoder : NumberElement {
    AXIS1_TICKS,
    AXIS2_TICKS,
    AXIS1_DEGREE_OFFSET,
    AXIS2_DEGREE_OFFSET;

    override val propName = "AXIS_SETTINGS"

    override val elementName = name
}
