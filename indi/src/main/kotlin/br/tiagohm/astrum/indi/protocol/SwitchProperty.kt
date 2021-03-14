package br.tiagohm.astrum.indi.protocol

interface SwitchProperty : Property<Boolean> {

    override val type: String
        get() = "Switch"

    override fun valueToText(value: Boolean) = if (value) "On" else "Off"
}