package br.tiagohm.astrum.indi.drivers.telescope.properties

import br.tiagohm.astrum.indi.protocol.SwitchElement

data class TrackMode(val mode: String) : SwitchElement {

    override val propName = "TELESCOPE_TRACK_MODE"

    override val elementName = "TRACK_$mode"

    override fun toString() = mode

    companion object {

        val NONE = TrackMode("NONE")
    }
}