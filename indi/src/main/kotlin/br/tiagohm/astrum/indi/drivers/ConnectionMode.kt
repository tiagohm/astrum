package br.tiagohm.astrum.indi.drivers

import br.tiagohm.astrum.indi.protocol.SwitchElement

enum class ConnectionMode : SwitchElement {
    SERIAL,
    TCP;

    override val propName = "CONNECTION_MODE"

    override val elementName = "CONNECTION_$name"

    companion object {

        fun parse(name: String) = valueOf(name.substring(11))
    }
}