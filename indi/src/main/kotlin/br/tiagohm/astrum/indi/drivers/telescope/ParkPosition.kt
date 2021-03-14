package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.protocol.NumberElement

enum class ParkPosition : NumberElement {
    HA,
    DEC,
    AZ,
    ALT;

    override val propName = "TELESCOPE_PARK_POSITION"

    override val elementName = "PARK_$name"

    companion object {

        fun parse(name: String) = valueOf(name.substring(5))
    }
}