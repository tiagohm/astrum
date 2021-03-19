package br.tiagohm.astrum.indi.drivers

import br.tiagohm.astrum.indi.protocol.TextElement

enum class DriverInfo : TextElement {
    NAME,
    EXEC,
    VERSION,
    INTERFACE;

    override val propName = "DRIVER_INFO"

    override val elementName = "DRIVER_$name"
}