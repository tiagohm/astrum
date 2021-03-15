package br.tiagohm.astrum.indi.drivers

import br.tiagohm.astrum.indi.client.Client
import br.tiagohm.astrum.indi.client.ElementListener
import br.tiagohm.astrum.indi.drivers.telescope.Coordinate
import br.tiagohm.astrum.indi.protocol.*
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import kotlin.math.abs

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

    fun registerElementListener(listener: ElementListener)

    fun unregisterElementListener(listener: ElementListener)

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
    fun <E : Element<T>, T> send(elements: Array<E>, values: Array<T>) = also { client?.send(name, Property(*elements), values) }

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
            // Fix invalid format error!
            val offset = text(Time.OFFSET, "Z")
                .replace(".", ":")
                .split(":")
                .let {
                    val h = if (it[0] == "Z") 0 else it[0].toInt()
                    val m = if (it.size == 2) it[1].toInt() else 0

                    if (h == 0 && m == 0) "Z"
                    else {
                        val sign = if (h >= 0) "+" else "-"
                        String.format("$sign%02d:%02d", abs(h), m)
                    }
                }
            return ZonedDateTime.ofInstant(utc, ZoneId.of(offset))
        } else {
            error("TIME_UTC is not supported")
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
     * Sets the telescope's location.
     *
     * @param longitude Site latitude (-90 to +90), degrees +N
     * @param latitude Site longitude (0 to 360), degrees +E
     * @param elevation Site elevation, meters
     */
    fun location(
        longitude: Double? = null,
        latitude: Double? = null,
        elevation: Double? = null,
    ) {
        val a = ArrayList<NumberElement>(3)
        val b = ArrayList<Double>(3)

        if (latitude != null) {
            a.add(Coordinate.LAT)
            b.add(latitude)
        }

        if (longitude != null) {
            a.add(Coordinate.LONG)
            b.add(longitude)
        }

        if (elevation != null) {
            a.add(Coordinate.ELEV)
            b.add(elevation)
        }

        if (a.isNotEmpty()) {
            send(a.toTypedArray(), b.toTypedArray())
        }
    }

    /**
     * Gets the telescope's location.
     *
     * @return The telescope location (longitude, latitude and elevation).
     */
    fun location() = Triple(number(Coordinate.LONG), number(Coordinate.LAT), number(Coordinate.ELEV))

    /**
     * Gets the [element].
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> element(element: Element<T>) = client?.element(name, element.propName, element.elementName)

    /**
     * Gets the [element]'s value.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> value(element: Element<T>) = element(element)?.value as? T

    /**
     * Determines if [element] exists.
     */
    fun <T> has(element: Element<T>) = element(element) != null

    /**
     * Gets the [element]'s value as Switch.
     */
    fun switch(element: SwitchElement) = value(element) == true

    /**
     * Gets the [element]'s value as Number or [value] if not exists.
     */
    fun number(element: NumberElement, value: Double = 0.0) = value(element) ?: value

    /**
     * Gets the [element]'s value as Text or [value] if not exists.
     */
    fun text(element: TextElement, value: String = "") = value(element) ?: value

    /**
     * Gets the [element]'s value as Light or [value] if not exists.
     */
    fun light(element: LightElement, value: State = State.IDLE) = value(element) ?: value

    /**
     * Gets the [element]'s value as BLOB.
     */
    fun blob(element: BLOBElement) = value(element)

    companion object {

        val ISO_8601 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    }
}