package br.tiagohm.astrum.indi.drivers

import br.tiagohm.astrum.indi.protocol.TextElement

/**
 * Device connection port.
 */
enum class DevicePort : TextElement {
    PORT;

    override val propName = "DEVICE_PORT"

    override val elementName = name

    companion object {

        fun parse(name: String) = PORT
    }
}