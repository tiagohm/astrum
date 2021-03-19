package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.protocol.SwitchElement

enum class TrackState : SwitchElement {
    OFF,
    ON;

    override val propName = "TELESCOPE_TRACK_STATE"

    override val elementName = "TRACK_$name"
}