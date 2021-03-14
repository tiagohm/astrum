package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.protocol.NumberElement

enum class TrackRate : NumberElement {
    RA,
    DE;

    override val propName = "TELESCOPE_TRACK_RATE"

    override val elementName = "TRACK_RATE_$name"

    companion object {

        fun parse(name: String) = valueOf(name.substring(11))
    }
}