package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.protocol.SwitchProperty

data class SlewRate(override val elementName: String) : SwitchProperty {

    override val propName = "TELESCOPE_SLEW_RATE"

    companion object {

        val NONE = SlewRate("NONE")
    }
}