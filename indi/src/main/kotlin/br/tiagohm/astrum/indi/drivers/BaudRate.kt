package br.tiagohm.astrum.indi.drivers

import br.tiagohm.astrum.indi.protocol.SwitchElement

enum class BaudRate(val rate: Int) : SwitchElement {
    RATE_9600(9600),
    RATE_19200(19200),
    RATE_38400(38400),
    RATE_57600(57600),
    RATE_115200(115200),
    RATE_230400(230400);

    override val propName = "DEVICE_BAUD_RATE"

    override val elementName = "$rate"

    companion object {

        fun parse(name: String) = valueOf("RATE_$name")
    }
}