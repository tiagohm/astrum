package br.tiagohm.astrum.indi.protocol.properties

import br.tiagohm.astrum.indi.client.INDIConnection
import org.redundent.kotlin.xml.xml

data class TextProperty(
    override val device: String,
    override val name: String,
    override val value: String,
) : Property<String> {

    override fun send(connection: INDIConnection): Boolean {
        val command = xml("newTextVector") {
            val (devName, pName) = name.split(":")

            attribute("device", device)
            attribute("name", devName)

            "oneText" {
                attribute("name", pName)
                text(value)
            }
        }.toString(false)

        connection.write(command)

        return true
    }
}