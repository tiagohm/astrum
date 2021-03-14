package br.tiagohm.astrum.indi.protocol

interface TextProperty : Property<String> {

    override val type: String
        get() = "Text"

    override fun valueToText(value: String) = value
}