package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.protocol.SwitchElement

data class SlewRate(override val elementName: String) : SwitchElement {

    override val propName = "TELESCOPE_SLEW_RATE"

    companion object {

        val NONE = SlewRate("NONE")

        fun parse(name: String) = SlewRate(name)
    }
}