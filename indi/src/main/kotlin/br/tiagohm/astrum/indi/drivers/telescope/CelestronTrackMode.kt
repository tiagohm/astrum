package br.tiagohm.astrum.indi.drivers.telescope

open class CelestronTrackMode(mode: String) : TrackMode(mode) {

    override val propName = "CELESTRON_TRACK_MODE"

    override val elementName = "MODE_$mode"
}