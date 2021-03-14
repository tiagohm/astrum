package br.tiagohm.astrum.indi.drivers

import br.tiagohm.astrum.indi.client.Client
import br.tiagohm.astrum.indi.client.PropertyListener
import br.tiagohm.astrum.indi.protocol.*
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

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

    fun registerPropertyListener(listener: PropertyListener)

    fun unregisterPropertyListener(listener: PropertyListener)

    /**
     * Attaches the driver to the [client].
     */
    fun attach(client: Client): Driver

    /**
     * Detaches the driver from the [client].
     */
    fun detach(): Driver

    /**
     * Sends the [property] and your [value] to the driver.
     */
    fun <T> send(p: Property<T>, value: T) = also { client?.send(name, p, value) }

    /**
     * Sends atomically the [properties] and yours [values] to the driver.
     */
    fun <T> send(properties: Array<Property<T>>, values: Array<T>) = also { client?.send(name, AtomicProperty(*properties), values) }

    /**
     * Connects the driver.
     */
    fun on() = send(Connection.CONNECT, true)

    /**
     * Disconnects the driver.
     */
    fun off() = send(Connection.DISCONNECT, true)

    fun dateTime(): ZonedDateTime {
        if (has(Time.UTC)) {
            val utc = ISO_8601.parse(text(Time.UTC)).toInstant()
            val offset = ZoneId.of(text(Time.OFFSET, "Z"))
            return ZonedDateTime.ofInstant(utc, offset)
        } else {
            throw UnsupportedOperationException("TIME_UTC is not supported")
        }
    }

    /**
     * Sets the UTC Time and Time Zone [offset] in hours.
     */
    fun dateTime(utc: Date, offset: Double) {
        send(arrayOf(Time.UTC, Time.OFFSET), arrayOf(ISO_8601.format(utc), "$offset"))
    }

    /**
     * Sets the UTC Time and Time Zone.
     */
    fun dateTime(dateTime: ZonedDateTime) {
        val utc = Date.from(dateTime.toInstant())
        val offset = dateTime.offset.totalSeconds / 3600.0
        dateTime(utc, offset)
    }

    /**
     * Gets the [property]'s value.
     */
    fun <T> property(property: Property<T>) = client?.property<T>(name, property.propName, property.elementName)

    fun <T> has(property: Property<T>) = this.property(property) != null

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

    companion object {

        val ISO_8601 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    }
}