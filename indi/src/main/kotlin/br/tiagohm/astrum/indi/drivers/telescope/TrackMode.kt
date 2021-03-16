package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.protocol.SwitchElement

class TrackMode(override val elementName: String) : SwitchElement {

    override val propName = "TELESCOPE_TRACK_MODE"

    override fun toString() = elementName.replace("TRACK_", "")

    companion object {

        val NONE = TrackMode("NONE")

        fun parse(name: String) = TrackMode(name)
    }
}