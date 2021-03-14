package br.tiagohm.astrum.indi.drivers

import br.tiagohm.astrum.indi.client.Client
import br.tiagohm.astrum.indi.client.ElementListener
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

    fun registerPropertyListener(listener: ElementListener)

    fun unregisterPropertyListener(listener: ElementListener)

    /**
     * Attaches the driver to the [client].
     */
    fun attach(client: Client): Driver

    /**
     * Detaches the driver from the [client].
     */
    fun detach(): Driver

    /**
     * Sends the [element] and your [value] to the driver.
     */
    fun <T> send(element: Element<T>, value: T) = also { client?.send(name, element, value) }

    /**
     * Sends atomically the [elements] and yours [values] to the driver.
     */
    fun <T> send(elements: Array<Element<T>>, values: Array<T>) = also { client?.send(name, Property(*elements), values) }

    /**
     * Connects the driver.
     */
    fun on() = send(Connection.CONNECT, true)

    /**
     * Disconnects the driver.
     */
    fun off() = send(Connection.DISCONNECT, true)

    @Throws(UnsupportedOperationException::class)
    fun dateTime(): ZonedDateTime {
        if (has(Time.UTC)) {
            val utc = ISO_8601.parse(text(Time.UTC)).toInstant()
            val offset = ZoneId.of(text(Time.OFFSET, "Z").replace(".", ":"))
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
     * Gets the [element]'s value.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> element(element: Element<T>) = client?.element(name, element.propName, element.elementName)?.value as? T

    /**
     * Determines if [element] exists.
     */
    fun <T> has(element: Element<T>) = this.element(element) != null

    /**
     * Gets the [element]'s value as Switch.
     */
    fun switch(element: SwitchElement) = this.element(element) == true

    /**
     * Gets the [element]'s value as Number or [value] if not exists.
     */
    fun number(element: NumberElement, value: Double = 0.0) = this.element(element) ?: value

    /**
     * Gets the [element]'s value as Text or [value] if not exists.
     */
    fun text(element: TextElement, value: String = "") = this.element(element) ?: value

    /**
     * Gets the [element]'s value as Light or [value] if not exists.
     */
    fun light(element: LightElement, value: State = State.IDLE) = this.element(element) ?: value

    /**
     * Gets the [element]'s value as BLOB.
     */
    fun blob(element: BLOBElement) = this.element(element)

    companion object {

        val ISO_8601 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    }
}