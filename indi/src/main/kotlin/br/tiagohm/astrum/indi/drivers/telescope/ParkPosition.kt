package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.protocol.NumberProperty

enum class ParkPosition : NumberProperty {
    HA,
    DEC,
    AZ,
    ALT;

    override val propName = "TELESCOPE_PARK_POSITION"

    override val elementName = "PARK_$name"
}