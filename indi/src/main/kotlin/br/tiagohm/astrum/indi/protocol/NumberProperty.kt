package br.tiagohm.astrum.indi.protocol

interface NumberProperty : Property<Double> {

    override val type: String
        get() = "Number"

    override fun valueToText(value: Double) = "$value"
}