package br.tiagohm.astrum.indi.client

import br.tiagohm.astrum.indi.protocol.*
import br.tiagohm.astrum.indi.protocol.properties.*
import org.redundent.kotlin.xml.xml
import java.io.OutputStream
import java.net.Socket
import java.net.SocketException
import java.util.*
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLStreamReader
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.concurrent.thread

// http://www.clearskyinstitute.com/INDI/INDI.pdf
// https://www.w3schools.com/xml/xml_dtd_intro.asp

open class INDIClient(
    val host: String = "127.0.0.1",
    val port: Int = 7624,
) : INDIConnection {

    private var socket: Socket? = null
    private var inputThread: Thread? = null
    private var xmlInputReader: XMLStreamReader? = null

    private val messageListeners = ArrayList<MessageListener>()
    private val propertyListeners = ArrayList<PropertyListener>()

    private val properties = ArrayList<Property<*>>()

    override fun registerMessageListener(listener: MessageListener) {
        if (!messageListeners.contains(listener)) messageListeners.add(listener)
    }

    override fun unregisterMessageListener(listener: MessageListener) {
        messageListeners.remove(listener)
    }

    override fun registerPropertyListener(listener: PropertyListener) {
        if (!propertyListeners.contains(listener)) propertyListeners.add(listener)
    }

    override fun unregisterPropertyListener(listener: PropertyListener) {
        propertyListeners.remove(listener)
    }

    override fun connect() {
        if (socket == null) {
            socket = Socket(host, port)

            inputThread = thread(true) {
                val input = socket?.getInputStream() ?: return@thread
                xmlInputReader = PARSER.createXMLStreamReader(INDIInputStream(input))
                val reader = xmlInputReader!!

                val mProperties = ArrayList<MutableMap<String, String>>()
                val mVector = HashMap<String, String?>()

                while (reader.hasNext()) {
                    reader.next()

                    // Start.
                    if (reader.isStartElement) {
                        when (reader.localName) {
                            // Ignore it.
                            "fakeRoot" -> continue

                            // Delete the given property, or entire device if no property is specified.
                            "delProperty" -> {
                                val device = reader.getAttributeValue(null, "device")
                                val name = reader.getAttributeValue(null, "name")

                                properties.iterator().also {
                                    while (it.hasNext()) {
                                        val e = it.next()

                                        // Delete by device and name.
                                        if (name.isNotEmpty()) {
                                            if (e.device == device && e.name.startsWith("$name:")) {
                                                it.remove()
                                            }
                                        }
                                        // Delete by device.
                                        else if (device.isNotEmpty()) {
                                            if (e.device == device) {
                                                it.remove()
                                            }
                                        }
                                        // Delete all.
                                        else {
                                            it.remove()
                                        }
                                    }
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
                                mVector["device"] = reader.getAttributeValue(null, "device")!!
                                mVector["name"] = reader.getAttributeValue(null, "name")!!
                            }
                            // One member of a text/switch/light/BLOB vector.
                            // defBLOB does not contain an initial value for the BLOB.
                            "defText",
                            "defNumber",
                            "defSwitch",
                            "defLight",
                            "defBLOB",
                            "oneText",
                            "oneNumber",
                            "oneSwitch",
                            "oneLight",
                            "oneBLOB" -> {
                                val name = reader.getAttributeValue(null, "name")
                                mProperties.add(hashMapOf("name" to name))
                            }
                            // A message associated with a device or entire system.
                            "message" -> {
                                val device = reader.getAttributeValue(null, "device")
                                val message = reader.getAttributeValue(null, "message")

                                Message(device, message).also { m -> messageListeners.forEach { it.onMessage(m) } }
                            }
                        }
                    }
                    // End.
                    else if (reader.isEndElement) {
                        val tag = reader.localName
                        val device = mVector["device"]!!
                        val name = mVector["name"]!!

                        if (tag.startsWith("def") ||
                            tag.startsWith("set")
                        ) {
                            val properties = mProperties.mapNotNull { p ->
                                val pName = "$name:${p["name"]}"
                                val pValue = p["value"]

                                when (tag) {
                                    "defTextVector", "setTextVector" -> TextProperty(device, pName, pValue ?: "")
                                    "defNumberVector", "setNumberVector" -> NumberProperty(device, pName, pValue?.toDouble() ?: 0.0)
                                    "defSwitchVector", "setSwitchVector" -> SwitchProperty(device, pName, pValue == "On")
                                    "defLightVector", "setLightVector" -> LightProperty(device, pName, pValue?.toUpperCase()?.let { State.valueOf(it) } ?: State.IDLE)
                                    // TODO: BLOB
                                    else -> null
                                }
                            }

                            // Add or set properties.
                            if (properties.isNotEmpty()) {
                                if (name == "DRIVER_INFO") {
                                    // TODO
                                } else {
                                    properties.forEach {
                                        val index = findPropertyIndex(device, it.name)

                                        if (index >= 0) this.properties[index] = it
                                        else this.properties.add(it)

                                        propertyListeners.forEach { l -> l.onProperty(it) }
                                    }
                                }

                                mVector.clear()
                                mProperties.clear()
                            }
                        }
                    }
                    // Value.
                    else if (mProperties.isNotEmpty() && reader.hasText()) {
                        // TODO: BLOB
                        val value = reader.text.trim()

                        if (value.isNotEmpty()) {
                            mProperties[mProperties.size - 1]["value"] = value
                        }
                    }
                }
            }
        }
    }

    override fun disconnect() {
        socket?.close()
        socket = null

        inputThread?.interrupt()
        inputThread = null

        xmlInputReader?.close()
        xmlInputReader = null
    }

    override val isClosed: Boolean
        get() = socket != null && socket!!.isClosed

    override val isConnected: Boolean
        get() = socket != null && !socket!!.isClosed && socket!!.isConnected

    protected val outputStream: OutputStream
        get() = socket?.getOutputStream() ?: throw SocketException("Socket is closed")

    protected fun write(
        data: ByteArray,
        range: ClosedRange<Int> = data.indices,
    ) {
        outputStream.write(data, range.start, range.endInclusive - range.start + 1)
    }

    override fun write(command: String) = write(command.toByteArray(Charsets.ISO_8859_1))

    /**
     * Ask [device] to define all Properties, or those for a specific [device] or specific
     * Property [name], for which it is responsible.
     */
    override fun fetchProperties(
        device: String,
        name: String,
    ) {
        val command = xml("getProperties") {
            attribute("version", "1.7")
            if (device.isNotEmpty()) attribute("device", device)
            if (name.isNotEmpty()) attribute("name", name.split(":")[0])
        }.toString(false)

        write(command)
    }

    /**
     * Command to control whether setBLOBs should be sent to this channel from a given [device].
     * They can be turned off completely by setting NEVER (the default),
     * allowed to be intermixed with other INDI commands by setting ALSO or made the
     * only command by setting ONLY.
     */
    override fun enableBLOB(
        device: String,
        name: String,
        state: EnableBLOBState,
    ) {
        val command = xml("enableBLOB") {
            attribute("device", device)
            if (name.isNotEmpty()) attribute("name", name.split(":")[0])
            text(state.label)
        }.toString(true)

        write(command)
    }

    override fun properties(): List<Property<*>> = Collections.unmodifiableList(properties)

    override fun propertiesByDevice(device: String) = properties.filter { it.device == device }

    override fun propertyByDeviceAndName(device: String, name: String) = properties.find { it.device == device && it.name == name }

    protected fun findPropertyIndex(device: String, name: String) = properties.indexOfFirst { it.device == device && it.name == name }

    companion object {

        private val PARSER = XMLInputFactory.newInstance().also {
            it.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false)
            it.setProperty(XMLInputFactory.IS_VALIDATING, false)
        }
    }
}