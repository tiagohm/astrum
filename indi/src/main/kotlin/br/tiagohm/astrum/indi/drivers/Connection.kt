package br.tiagohm.astrum.indi.drivers

import br.tiagohm.astrum.indi.protocol.SwitchElement

enum class Connection : SwitchElement {
    CONNECT,
    DISCONNECT;

    override val propName = "CONNECTION"

    override val elementName = name
}