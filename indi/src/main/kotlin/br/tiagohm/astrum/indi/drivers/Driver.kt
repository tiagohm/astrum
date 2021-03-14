package br.tiagohm.astrum.indi.drivers

import br.tiagohm.astrum.indi.client.Client
import br.tiagohm.astrum.indi.protocol.*

interface Driver {

    val client: Client?

    /**
     * The driver's name.
     */
    val name: String

    /**
     * The driver's executable.
     */
    val executable: String

    /**
     * Determines if the driver is connected.
     */
    val isOn: Boolean
        get() = switch(Connection.CONNECT)

    /**
     * Attaches the driver to the [client].
     */
    fun attach(client: Client): Driver

    /**
     * Detaches the driver from the [client].
     */
    fun detach(): Driver

    /**
     * Connects the driver.
     */
    fun on() = send(Connection.CONNECT, true)

    /**
     * Disconnects the driver.
     */
    fun off() = send(Connection.DISCONNECT, true)

    /**
     * Sends the [property] and your [value] to the driver.
     */
    fun <T> send(p: Property<T>, value: T) = also { client?.send(name, p, value) }

    /**
     * Sends atomically the [properties] and yours [values] to the driver.
     */
    fun <T> send(properties: Array<Property<T>>, values: Array<T>) = also { client?.send(name, AtomicProperty(*properties), values) }

    /**
     * Gets the [property]'s value.
     */
    fun <T> property(property: Property<T>) = client?.property<T>(name, property.propName, property.elementName)

    /**
     * Gets the [property]'s value as Switch.
     */
    fun switch(property: SwitchProperty) = this.property(property) == true

    /**
     * Gets the [property]'s value as Number or [value] if not exists.
     */
    fun number(property: NumberProperty, value: Double = 0.0) = this.property(property) ?: value

    /**
     * Gets the [property]'s value as Text or [value] if not exists.
     */
    fun text(property: TextProperty, value: String = "") = this.property(property) ?: value

    /**
     * Gets the [property]'s value as Light or [value] if not exists.
     */
    fun light(property: LightProperty, value: State = State.IDLE) = this.property(property) ?: value

    /**
     * Gets the [property]'s value as BLOB.
     */
    fun blob(property: BLOBProperty) = this.property(property)
}