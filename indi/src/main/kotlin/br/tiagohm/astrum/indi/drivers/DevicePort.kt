package br.tiagohm.astrum.indi.drivers

import br.tiagohm.astrum.indi.protocol.TextElement

enum class DevicePort : TextElement {
    PORT;

    override val propName = "DEVICE_PORT"

    override val elementName = name
}