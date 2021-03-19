package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.protocol.SwitchElement

open class TrackMode(val mode: String) : SwitchElement {

    override val propName = "TELESCOPE_TRACK_MODE"

    override val elementName = "TRACK_$mode"

    override fun toString() = mode

    companion object {

        val NONE = TrackMode("NONE")
        val SIDEREAL = TrackMode("SIDEREAL")
        val SOLAR = TrackMode("SOLAR")
        val LUNAR = TrackMode("LUNAR")
        val CUSTOM = TrackMode("CUSTOM")
    }
}