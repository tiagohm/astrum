package br.tiagohm.astrum.indi.protocol

interface LightElement : Element<State> {

    override val type: ElementType
        get() = ElementType.LIGHT

    override fun valueToText(value: State) = value.text
}