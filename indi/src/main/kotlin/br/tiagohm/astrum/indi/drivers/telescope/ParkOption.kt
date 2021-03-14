package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.protocol.SwitchElement

enum class ParkOption : SwitchElement {
    CURRENT,
    DEFAULT,
    WRITE_DATA,
    PURGE_DATA;

    override val propName = "TELESCOPE_PARK_OPTION"

    override val elementName = "PARK_$name"

    companion object {

        fun parse(name: String) = valueOf(name.substring(5))
    }
}