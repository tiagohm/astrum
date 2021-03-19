package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.protocol.SwitchElement

enum class TelescopeAbortMotion : SwitchElement {
    ABORT;

    override val propName = "TELESCOPE_ABORT_MOTION"

    override val elementName = name
}