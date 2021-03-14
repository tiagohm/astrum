package br.tiagohm.astrum.indi.protocol

interface TextElement : Element<String> {

    override val type: ElementType
        get() = ElementType.TEXT

    override fun valueToText(value: String) = value
}