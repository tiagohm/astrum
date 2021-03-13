package br.tiagohm.astrum.indi.client

import br.tiagohm.astrum.indi.protocol.EnableBLOBState
import br.tiagohm.astrum.indi.protocol.properties.Property

interface INDIConnection {

    fun connect()

    fun disconnect()

    fun write(command: String)

    fun send(property: Property<*>) = property.send(this)

    fun fetchProperties(device: String = "", name: String = "")

    fun enableBLOB(device: String, name: String = "", state: EnableBLOBState = EnableBLOBState.NEVER)

    fun properties(): List<Property<*>>

    fun propertiesByDevice(device: String): List<Property<*>>

    fun propertyByDeviceAndName(device: String, name: String): Property<*>?

    fun registerMessageListener(listener: MessageListener)

    fun unregisterMessageListener(listener: MessageListener)

    fun registerPropertyListener(listener: PropertyListener)

    fun unregisterPropertyListener(listener: PropertyListener)

    val isClosed: Boolean

    val isConnected: Boolean
}