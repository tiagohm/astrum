package br.tiagohm.astrum.indi.protocol

interface NumberElement : Element<Double> {

    override val type: ElementType
        get() = ElementType.NUMBER

    override fun convert(value: Double) = "$value"
}