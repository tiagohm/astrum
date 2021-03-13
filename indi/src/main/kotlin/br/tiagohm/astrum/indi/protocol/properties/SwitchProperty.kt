package br.tiagohm.astrum.indi.protocol.properties

import br.tiagohm.astrum.indi.client.INDIConnection
import org.redundent.kotlin.xml.xml

data class SwitchProperty(
    override val device: String,
    override val name: String,
    override val value: Boolean,
) : Property<Boolean> {

    override fun send(connection: INDIConnection): Boolean {
        val command = xml("newSwitchVector") {
            val (devName, pName) = name.split(":")

            attribute("device", device)
            attribute("name", devName)

            "oneSwitch" {
                attribute("name", pName)
                text(if (value) "On" else "Off")
            }
        }.toString(false)

        connection.write(command)

        return true
    }

    operator fun not(): SwitchProperty = copy(value = !value)
}