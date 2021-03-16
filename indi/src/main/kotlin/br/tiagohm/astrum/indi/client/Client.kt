package br.tiagohm.astrum.indi.client

import br.tiagohm.astrum.indi.client.parser.SimpleXMLParser
import br.tiagohm.astrum.indi.client.parser.XMLTag
import br.tiagohm.astrum.indi.common.Shelf
import br.tiagohm.astrum.indi.drivers.*
import br.tiagohm.astrum.indi.drivers.telescope.*
import br.tiagohm.astrum.indi.protocol.*
import org.redundent.kotlin.xml.xml
import java.io.OutputStream
import java.net.Socket
import java.net.SocketException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.concurrent.thread

// http://www.clearskyinstitute.com/INDI/INDI.pdf
// https://www.w3schools.com/xml/xml_dtd_intro.asp

open class Client(
    val host: String = "127.0.0.1",
    val port: Int = 7624,
) {

    private var socket: Socket? = null
    private var inputThread: Thread? = null

    private val messageListeners = ArrayList<MessageListener>(1)
    private val elementListeners = ArrayList<ElementListener>(1)
    private val driverListeners = ArrayList<DriverListener>(1)

    private val elements = Shelf<PropertyElement<*>>()
    private val drivers = HashMap<String, Driver>()

    open fun registerMessageListener(listener: MessageListener) {
        if (!messageListeners.contains(listener)) messageListeners.add(listener)
    }

    open fun unregisterMessageListener(listener: MessageListener) {
        messageListeners.remove(listener)
    }

    open fun registerElementListener(listener: ElementListener) {
        if (!elementListeners.contains(listener)) elementListeners.add(listener)
    }

    open fun unregisterElementListener(listener: ElementListener) {
        elementListeners.remove(listener)
    }

    open fun registerDriverListener(listener: DriverListener) {
        if (!driverListeners.contains(listener)) driverListeners.add(listener)
    }

    open fun unregisterDriverListener(listener: DriverListener) {
        driverListeners.remove(listener)
    }

    private fun registerDriver(device: String, driver: PropertyElement<String>) {
        when (val exec = driver.value) {
            "indi_simulator_telescope",
            "indi_lx200basic",
            "indi_lx200gps",
            "indi_lx200autostar",
            "indi_lx200classic",
            "indi_lx200_OnStep",
            "indi_lx200_TeenAstro",
            "indi_lx200_16",
            "indi_eq500x_telescope",
            "indi_lx200ap_experimental",
            "indi_lx200ap_gtocp2",
            "indi_lx200ap",
            "indi_celestron_gps",
            "indi_lx200fs2",
            "indi_paramount_telescope",
            "indi_rainbow_telescope",
            "indi_crux_mount",
            "indi_synscan_telescope",
            "indi_lx200gemini",
            "indi_temma_telescope",
            "indi_synscanlegacy_telescope",
            "indi_lx200ss2000pc",
            "indi_ieqlegacy_telescope",
            "indi_ieq_telescope",
            "indi_ioptronv3_telescope",
            "indi_lx200zeq25",
            "indi_lx200gotonova",
            "indi_ioptronHC8406",
            "indi_lx200pulsar2",
            "indi_skycommander_telescope",
            "indi_lx200_10micron",
            "indi_skywatcherAltAzMount",
            "indi_skywatcherAltAzSimple",
            "indi_script_telescope",
            "indi_dsc_telescope",
            "indi_pmc8_telescope" -> Telescope(device, exec)
            // TODO: Adicionar os outros drivers.
            else -> null
        }?.also {
            it.attach(this)

            if (drivers.contains(device)) {
                drivers.remove(device)?.detach()
                drivers[device] = it
            } else {
                drivers[device] = it
                driverListeners.forEach { listener -> listener.onDriverAdded(it) }
            }
        }
    }

    private fun processTag(tag: XMLTag) {
        when (tag.name) {
            // Ignore it.
            "indi" -> return
            // Delete the given property, or entire device if no property is specified.
            "delProperty" -> {
                val device = tag.attributes["device"]!!
                val name = tag.attributes["name"]!!

                // Delete by device and name.
                if (name.isNotEmpty()) {
                    elements.clear(device, name)
                }
                // Delete by device.
                else if (device.isNotEmpty()) {
                    elements.clear(device)

                    drivers.remove(device)?.also {
                        it.detach()
                        driverListeners.forEach { listener -> listener.onDriverRemoved(it) }
                    }
                }
                // Delete all.
                else {
                    elements.clear()

                    drivers.forEach { it.value.detach() }
                    drivers.clear()
                }
            }
            // Define a property that holds one or more elements or
            // Send a new set of values for a vector, with optional new timeout, state and message.
            "defTextVector",
            "defNumberVector",
            "defSwitchVector",
            "defLightVector",
            "defBLOBVector",
            "setTextVector",
            "setNumberVector",
            "setSwitchVector",
            "setLightVector",
            "setBLOBVector" -> {
                val device = tag.attributes["device"]!!
                val propName = tag.attributes["name"]!!
                val isReadOnly = tag.attributes["perm"]?.let { it == "ro" }
                val state = tag.attributes["state"]?.let { State.valueOf(it.toUpperCase()) }
                val elements = ArrayList<Array<Any?>>(tag.children.size)

                for (c in tag.children) {
                    val elementName = c.attributes["name"]!!
                    val size = c.attributes["size"]?.toIntOrNull()
                    val min = c.attributes["min"]?.toDoubleOrNull()
                    val max = c.attributes["max"]?.toDoubleOrNull()
                    val step = c.attributes["step"]?.toDoubleOrNull()
                    val format = c.attributes["format"]
                    val value = c.text

                    val p: Array<Any?> = when (c.name) {
                        // One member of a text/number/switch/light/BLOB vector.
                        // defBLOB does not contain an initial value for the BLOB.
                        "defText", "oneText" -> arrayOf(elementName, value, size, format, min, max, step)
                        "defNumber", "oneNumber" -> arrayOf(elementName, value.toDoubleOrNull() ?: 0.0, size, format, min, max, step)
                        "defSwitch", "oneSwitch" -> arrayOf(elementName, value == "On", size, format, min, max, step)
                        "defLight", "oneLight" -> arrayOf(elementName, if (value.isEmpty()) State.IDLE else State.valueOf(value.toUpperCase()), size, format, min, max, step)
                        "defBLOB" -> arrayOf(elementName, EMPTY_BYTE_ARRAY, size, format, min, max, step)
                        // TODO: oneBLOB
                        else -> continue
                    }

                    elements.add(p)
                }

                // Add or set properties.
                if (elements.isNotEmpty()) {
                    for (element in elements) {
                        val name = element[0] as String

                        val ne = PROPERTIES[propName]?.invoke(name) ?: continue
                        val oe = element(device, propName, name)

                        val value = element[1] ?: oe?.value
                        val size = element[2] as? Int ?: oe?.size ?: 0
                        val format = element[3] as? String ?: oe?.format ?: ""
                        val min = element[4] as? Double ?: oe?.min ?: 0.0
                        val max = element[5] as? Double ?: oe?.max ?: 0.0
                        val step = element[6] as? Double ?: oe?.step ?: 0.0

                        val perm = isReadOnly ?: oe?.isReadOnly ?: false
                        val s = state ?: oe?.state ?: State.IDLE

                        val pe = when (ne.type) {
                            ElementType.TEXT -> PropertyElement(ne as TextElement, value as String, perm, s, min, max, step, size, format).also {
                                if (propName == "DRIVER_INFO" && ne.elementName == "DRIVER_EXEC") registerDriver(device, it)
                            }
                            ElementType.NUMBER -> PropertyElement(ne as NumberElement, value as Double, perm, s, min, max, step, size, format)
                            ElementType.SWITCH -> PropertyElement(ne as SwitchElement, value as Boolean, perm, s, min, max, step, size, format)
                            ElementType.LIGHT -> PropertyElement(ne as LightElement, value as State, perm, s, min, max, step, size, format)
                            ElementType.BLOB -> PropertyElement(ne as BLOBElement, value as ByteArray, perm, s, min, max, step, size, format)
                        }

                        this.elements.set(device, propName, name, pe)

                        elementListeners.forEach { listener -> listener.onElement(device, pe) }
                    }
                }
            }
            // A message associated with a device or entire system.
            "message" -> {
                val device = tag.attributes["device"] ?: ""
                val message = tag.attributes["message"] ?: ""

                Message(device, message).also { messageListeners.forEach { listener -> listener.onMessage(it) } }
            }
        }
    }

    fun connect() {
        if (socket == null) {
            socket = Socket(host, port).also {
                it.receiveBufferSize = 512
                it.sendBufferSize = 512
            }

            inputThread = thread(true) {
                val input = socket?.getInputStream() ?: return@thread
                val parser = SimpleXMLParser()

                while (true) {
                    try {
                        val b = input.read()
                        if (b == -1) break
                        processTag(parser.parse(b) ?: continue)
                    } catch (e: Exception) {
                        val message = Message("", "[ERROR] ${e.message}", true)
                        messageListeners.forEach { listener -> listener.onMessage(message) }
                    }
                }

                val message = Message("", "[ERROR] Server is offline", true)
                messageListeners.forEach { listener -> listener.onMessage(message) }

                disconnect()
            }

            // TODO: Emitir "ready" quando nao há mais elementos de definição
            fetchProperties()
            enableBLOB()
        }
    }

    fun disconnect(clear: Boolean = true) {
        inputThread?.interrupt()
        inputThread = null

        socket?.close()
        socket = null

        if (clear) {
            drivers().forEach { it.detach() }
            elementListeners.clear()
            messageListeners.clear()

            elements.clear()

            for (key in drivers.keys.toList()) {
                val driver = drivers.remove(key)
                driverListeners.forEach { listener -> listener.onDriverRemoved(driver!!) }
            }
        }
    }

    val isClosed: Boolean
        get() = socket != null && socket!!.isClosed

    val isConnected: Boolean
        get() = socket != null && !socket!!.isClosed && socket!!.isConnected

    protected val outputStream: OutputStream
        get() = socket?.getOutputStream() ?: throw SocketException("Socket is closed")

    protected fun write(
        data: ByteArray,
        range: ClosedRange<Int> = data.indices,
    ) {
        outputStream.write(data, range.start, range.endInclusive - range.start + 1)
    }

    fun write(command: String) = write(command.toByteArray(Charsets.ISO_8859_1))

    fun <T> send(device: String, command: Command<T>, value: T) = write(command.toXML(device, value))

    fun fetchProperties(
        device: String = "",
        name: String = "",
    ) {
        val command = xml("getProperties") {
            attribute("version", "1.7")
            if (device.isNotEmpty()) attribute("device", device)
            if (name.isNotEmpty()) attribute("name", name)
        }.toString(false)

        write(command)
    }

    fun enableBLOB(
        device: String = "",
        name: String = "",
        state: EnableBLOBState = EnableBLOBState.NEVER,
    ) {
        val command = xml("enableBLOB") {
            attribute("device", device)
            if (name.isNotEmpty()) attribute("name", name)
            text(state.text)
        }.toString(false)

        write(command)
    }

    /**
     * Gets the specified element.
     */
    fun element(device: String, propName: String, elementName: String) = synchronized(elements) { elements.get(device, propName, elementName) }

    /**
     * Gets the device names.
     */
    fun devices(): Set<String> = synchronized(elements) { elements.keys() }

    /**
     * Gets the available property names from the given [device].
     */
    fun propertyNames(device: String) = synchronized(elements) { elements.keys(device) }

    /**
     * Gets the available elemens names from the given [device] and property name.
     */
    fun elementNames(device: String, propName: String) = synchronized(elements) { elements.keys(device, propName) }

    /**
     * Gets the available drivers.
     */
    fun drivers() = drivers.values.toList()

    /**
     * Gets the available telescope drivers.
     */
    fun telescopes() = drivers.values.filterIsInstance<Telescope>()

    companion object {

        private val EMPTY_BYTE_ARRAY = byteArrayOf()

        // TODO: Add more properties.
        private val PROPERTIES = mapOf<String, (String) -> Element<*>>(
            "CONNECTION" to Connection::parse,
            "CONNECTION_MODE" to ConnectionMode::parse,
            "TIME_UTC" to Time::parse,
            "DEVICE_BAUD_RATE" to BaudRate::parse,
            "DEVICE_PORT" to DevicePort::parse,
            "DRIVER_INFO" to DriverInfo::parse,
            "DEBUG" to Debug::parse,
            "TELESCOPE_ABORT_MOTION" to AbortMotion::parse,
            "EQUATORIAL_EOD_COORD" to Coordinate::parse,
            "EQUATORIAL_COORD" to CoordinateJ2000::parse,
            "HORIZONTAL_COORD" to Coordinate::parse,
            "GEOGRAPHIC_COORD" to Coordinate::parse,
            "TELESCOPE_MOTION_NS" to MotionDirection::parse,
            "TELESCOPE_MOTION_WE" to MotionDirection::parse,
            "MOUNT_TYPE" to MountType::parse,
            "MOUNT_AXES" to MountAxis::parse,
            "ON_COORD_SET" to OnCoordSet::parse,
            "TELESCOPE_PARK" to Park::parse,
            "TELESCOPE_PARK_OPTION" to ParkOption::parse,
            "TELESCOPE_PARK_POSITION" to ParkPosition::parse,
            "TELESCOPE_PIER_SIDE" to PierSide::parse,
            "TELESCOPE_SLEW_RATE" to SlewRate::parse,
            "TELESCOPE_TRACK_MODE" to TrackMode::parse,
            "TELESCOPE_TRACK_STATE" to TrackState::parse,
            "TELESCOPE_TRACK_RATE" to TrackRate::parse,
            "TELESCOPE_TIMED_GUIDE_NS" to GuideDirection::parse,
            "TELESCOPE_TIMED_GUIDE_WE" to GuideDirection::parse,
            "SAT_TRACKING_STAT" to SatTracking::parse,
            "HEMISPHERE" to Hemisphere::parse,
        )
    }
}