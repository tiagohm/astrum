package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.protocol.NumberElement
import java.io.IOException

enum class GuideDirection(override val propName: String) : NumberElement {
    // RA pulse
    NORTH("TELESCOPE_TIMED_GUIDE_NS"),
    SOUTH("TELESCOPE_TIMED_GUIDE_NS"),

    // DEC pulse
    WEST("TELESCOPE_TIMED_GUIDE_WE"),
    EAST("TELESCOPE_TIMED_GUIDE_WE");

    override val elementName = "TIMED_GUIDE_${name[0]}"

    companion object {

        fun parse(name: String) = when (name[name.length - 1]) {
            'N' -> NORTH
            'S' -> SOUTH
            'W' -> WEST
            'E' -> EAST
            else -> throw IOException("Invalid value for GuideDirection: $name")
        }
    }
}