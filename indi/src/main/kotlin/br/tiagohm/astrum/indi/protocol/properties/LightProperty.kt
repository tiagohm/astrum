package br.tiagohm.astrum.indi.protocol.properties

import br.tiagohm.astrum.indi.client.INDIConnection
import org.redundent.kotlin.xml.xml

data class LightProperty(
    override val device: String,
    override val name: String,
    override val value: State,
) : Property<State> {

    override fun send(connection: INDIConnection): Boolean {
        val command = xml("newLightVector") {
            val (devName, pName) = name.split(":")

            attribute("device", device)
            attribute("name", devName)

            "oneLight" {
                attribute("name", pName)
                text(value.text)
            }
        }.toString(false)

        connection.write(command)

        return true
    }
}