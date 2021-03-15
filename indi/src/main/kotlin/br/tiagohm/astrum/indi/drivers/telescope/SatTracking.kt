package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.protocol.SwitchElement

enum class SatTracking : SwitchElement {
    TRACK,
    HALT;

    override val propName = "SAT_TRACKING_STAT"

    override val elementName = "SAT_$name"

    companion object {

        fun parse(name: String) = valueOf(name.substring(4))
    }
}