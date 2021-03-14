package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.protocol.SwitchProperty

enum class AbortMotion : SwitchProperty {
    ABORT;

    override val propName = "TELESCOPE_ABORT_MOTION"

    override val elementName = name
}