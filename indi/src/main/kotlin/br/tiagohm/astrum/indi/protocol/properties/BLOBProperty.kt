package br.tiagohm.astrum.indi.protocol.properties

import br.tiagohm.astrum.indi.client.INDIConnection
import org.redundent.kotlin.xml.xml

data class BLOBProperty(
    override val device: String,
    override val name: String,
    override val value: ByteArray,
) : Property<ByteArray> {

    override fun send(connection: INDIConnection): Boolean {
        val command = xml("newBLOBVector") {
            val (devName, pName) = name.split(":")

            attribute("device", device)
            attribute("name", devName)

            "oneBLOB" {
                attribute("name", pName)
                // TODO: text()
            }
        }.toString(false)

        connection.write(command)

        return true
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BLOBProperty

        if (device != other.device) return false
        if (name != other.name) return false
        if (!value.contentEquals(other.value)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = device.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + value.contentHashCode()
        return result
    }
}