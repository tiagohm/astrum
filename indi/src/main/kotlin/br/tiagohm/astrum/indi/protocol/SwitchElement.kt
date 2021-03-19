package br.tiagohm.astrum.indi.protocol

interface SwitchElement : Element<Boolean> {

    override val type: ElementType
        get() = ElementType.SWITCH

    override fun convert(value: Boolean) = if (value) "On" else "Off"
}