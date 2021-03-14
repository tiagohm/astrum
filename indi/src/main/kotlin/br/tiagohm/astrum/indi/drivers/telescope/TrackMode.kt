package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.protocol.SwitchElement

enum class TrackMode : SwitchElement {
    SIDEREAL,
    SOLAR,
    LUNAR,
    CUSTOM;

    override val propName = "TELESCOPE_TRACK_MODE"

    override val elementName = "TRACK_$name"

    companion object {

        fun parse(name: String) = valueOf(name.substring(6))
    }
}