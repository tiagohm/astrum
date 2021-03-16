package br.tiagohm.astrum.indi.drivers

import br.tiagohm.astrum.indi.protocol.SwitchElement

enum class Debug : SwitchElement {
    ENABLE,
    DISABLE;

    override val propName = "DEBUG"

    override val elementName = name

    companion object {

        fun parse(name: String) = valueOf(name)
    }
}