package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.protocol.SwitchElement

enum class MotionDirection(override val propName: String) : SwitchElement {
    NORTH("TELESCOPE_MOTION_NS"),
    SOUTH("TELESCOPE_MOTION_NS"),
    WEST("TELESCOPE_MOTION_WE"),
    EAST("TELESCOPE_MOTION_WE");

    override val elementName = "MOTION_$name"
}