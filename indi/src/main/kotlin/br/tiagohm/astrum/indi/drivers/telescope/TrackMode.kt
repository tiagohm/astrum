package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.protocol.SwitchProperty

enum class TrackMode : SwitchProperty {
    SIDEREAL,
    SOLAR,
    LUNAR,
    CUSTOM;

    override val propName = "TELESCOPE_TRACK_MODE"

    override val elementName = "TRACK_$name"
}