package br.tiagohm.astrum.indi.drivers

import br.tiagohm.astrum.indi.client.INDIConnection
import br.tiagohm.astrum.indi.protocol.properties.SwitchProperty

abstract class DefaultDriver(
    protected val connection: INDIConnection,
    override val name: String,
    override val executable: String,
    override val manufacturer: String,
    override val version: String,
) : Driver {

    @Suppress("UNCHECKED_CAST")
    override fun <T> property(name: String): T? = connection.propertyByDeviceAndName(this.name, name)?.value as? T

    override fun on() {
        SwitchProperty(name, "CONNECTION:CONNECT", true).send(connection)
    }

    override fun off() {
        SwitchProperty(name, "CONNECTION:CONNECT", false).send(connection)
    }
}