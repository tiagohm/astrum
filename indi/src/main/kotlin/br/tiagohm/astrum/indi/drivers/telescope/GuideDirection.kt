package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.protocol.NumberElement

enum class GuideDirection(override val propName: String) : NumberElement {
    // DEC pulse
    NORTH("TELESCOPE_TIMED_GUIDE_NS"),
    SOUTH("TELESCOPE_TIMED_GUIDE_NS"),

    // RA pulse
    WEST("TELESCOPE_TIMED_GUIDE_WE"),
    EAST("TELESCOPE_TIMED_GUIDE_WE");

    override val elementName = "TIMED_GUIDE_${name[0]}"

    companion object {

        fun parse(name: String) = when (name[name.length - 1]) {
            'N' -> NORTH
            'S' -> SOUTH
            'W' -> WEST
            'E' -> EAST
            else -> error("Invalid value for GuideDirection: $name")
        }
    }
}