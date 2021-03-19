package br.tiagohm.astrum.indi.drivers.guider

import br.tiagohm.astrum.indi.protocol.NumberElement

enum class GuideDirection(override val propName: String) : NumberElement {
    // DEC pulse
    NORTH("TELESCOPE_TIMED_GUIDE_NS"),
    SOUTH("TELESCOPE_TIMED_GUIDE_NS"),

    // RA pulse
    WEST("TELESCOPE_TIMED_GUIDE_WE"),
    EAST("TELESCOPE_TIMED_GUIDE_WE");

    override val elementName = "TIMED_GUIDE_${name[0]}"
}