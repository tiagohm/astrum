package br.tiagohm.astrum.indi.client

import br.tiagohm.astrum.indi.client.parser.SimpleXMLParser
import br.tiagohm.astrum.indi.client.parser.XMLTag
import br.tiagohm.astrum.indi.common.Shelf
import br.tiagohm.astrum.indi.drivers.Driver
import br.tiagohm.astrum.indi.drivers.telescope.Telescope
import br.tiagohm.astrum.indi.protocol.*
import org.redundent.kotlin.xml.xml
import java.io.OutputStream
import java.net.Socket
import java.net.SocketException
import java.util.*
import javax.xml.stream.XMLInputFactory
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

    private val messageListeners = ArrayList<MessageListener>()
    private val propertyListeners = ArrayList<PropertyListener>()

    private val properties = Shelf()
    private val drivers = HashMap<String, Driver>()

    open fun registerMessageListener(listener: MessageListener) {
        if (!messageListeners.contains(listener)) messageListeners.add(listener)
    }

    open fun unregisterMessageListener(listener: MessageListener) {
        messageListeners.remove(listener)
    }

    open fun registerPropertyListener(listener: PropertyListener) {
        if (!propertyListeners.contains(listener)) propertyListeners.add(listener)
    }

    open fun unregisterPropertyListener(listener: PropertyListener) {
        propertyListeners.remove(listener)
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
                    properties.clear(device, name)
                }
                // Delete by device.
                else if (device.isNotEmpty()) {
                    properties.clear(device)
                    drivers.remove(device)?.detach()
                }
                // Delete all.
                else {
                    properties.clear()
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
                val properties = ArrayList<Pair<String, Any>>(tag.children.size)

                for (c in tag.children) {
                    val elementName = c.attributes["name"]!!
                    val value = c.text

                    val p = when (c.name) {
                        // One member of a text/switch/light/BLOB vector.
                        // defBLOB does not contain an initial value for the BLOB.
                        "defText", "oneText" -> elementName to (value)
                        "defNumber", "oneNumber" -> elementName to (value.toDoubleOrNull() ?: 0.0)
                        "defSwitch", "oneSwitch" -> elementName to (value == "On")
                        "defLight", "oneLight" -> elementName to (if (value.isEmpty()) State.IDLE else State.valueOf(value.toUpperCase()))
                        // TODO: BLOB
                        else -> continue
                    }

                    properties.add(p)
                }

                // Add or set properties.
                if (properties.isNotEmpty()) {
                    if (propName == "DRIVER_INFO") {
                        var driverName = ""
                        var driverExec = ""

                        properties.forEach {
                            if (it.first == "DRIVER_NAME") driverName = it.second as String
                            else if (it.first == "DRIVER_EXEC") driverExec = it.second as String
                        }

                        if (driverName.isNotEmpty() &&
                            driverExec.isNotEmpty()
                        ) {
                            when (driverExec) {
                                "indi_simulator_telescope" -> Telescope(driverName, driverExec)
                                // TODO: Adicionar os outros drivers.
                                else -> null
                            }?.also {
                                drivers[device]?.detach()
                                it.attach(this)
                                drivers[device] = it
                            }
                        }
                    }

                    properties.forEach {
                        this.properties.set(device, propName, it.first, it.second)
                        propertyListeners.forEach { l -> l.onProperty(device, propName, it.first, it.second) }
                    }
                }
            }
            // A message associated with a device or entire system.
            "message" -> {
                val device = tag.attributes["device"] ?: ""
                val message = tag.attributes["message"] ?: ""

                Message(device, message).also { m -> messageListeners.forEach { it.onMessage(m) } }
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
                        processTag(parser.parse(input.read()) ?: continue)
                    } catch (e: SocketException) {
                        // e.printStackTrace()
                    }
                }
            }

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
            propertyListeners.clear()
            messageListeners.clear()
            properties.clear()
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
        }.toString(true)

        write(command)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> property(device: String, propName: String, elementName: String) = properties.get<T>(device, propName, elementName)

    fun devices(): Set<String> = properties.keys()

    fun propertyNames(device: String) = properties.keys(device)

    fun elementNames(device: String, propName: String) = properties.keys(device, propName)

    fun drivers() = drivers.values.toList()

    companion object {

        private val PARSER = XMLInputFactory.newInstance().also {
            it.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false)
            it.setProperty(XMLInputFactory.IS_VALIDATING, false)
        }
    }
}