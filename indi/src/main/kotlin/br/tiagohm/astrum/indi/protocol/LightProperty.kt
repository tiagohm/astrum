package br.tiagohm.astrum.indi.protocol

interface LightProperty : Property<State> {

    override val type: String
        get() = "Light"

    override fun valueToText(value: State) = value.text
}