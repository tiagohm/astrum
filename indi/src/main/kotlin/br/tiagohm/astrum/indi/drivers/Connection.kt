package br.tiagohm.astrum.indi.drivers

import br.tiagohm.astrum.indi.protocol.SwitchProperty

enum class Connection : SwitchProperty {
    CONNECT,
    DISCONNECT;

    override val propName = "CONNECTION"

    override val elementName = name
}