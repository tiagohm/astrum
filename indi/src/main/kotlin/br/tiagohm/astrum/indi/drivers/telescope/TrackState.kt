package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.protocol.SwitchProperty

enum class TrackState : SwitchProperty {
    OFF,
    ON;

    override val propName = "TELESCOPE_TRACK_STATE"

    override val elementName = "TRACK_$name"
}