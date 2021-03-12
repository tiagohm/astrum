package br.tiagohm.astrum.indi.client

import br.tiagohm.astrum.indi.protocol.*
import br.tiagohm.astrum.indi.protocol.commands.Command
import br.tiagohm.astrum.indi.protocol.properties.*
import br.tiagohm.astrum.indi.protocol.vectors.*
import java.io.OutputStream
import java.net.Socket
import java.net.SocketException
import java.util.*
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLStreamReader
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet
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

    val vectors = ArrayList<PropertyVector<*>>()
    val properties = HashMap<String, MutableList<Property<*>>>()

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
                val mVector = HashMap<String, String?>(5)

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

                                properties[device]?.iterator()?.also {
                                    while (it.hasNext()) {
                                        val e = it.next()

                                        // Delete by device and name.
                                        if (name.isNotEmpty()) {
                                            if (e.vector.device == device && e.name == name) {
                                                it.remove()
                                            }
                                        }
                                        // Delete by device.
                                        else if (device.isNotEmpty()) {
                                            if (e.vector.device == device) {
                                                it.remove()
                                            }
                                        }
                                        // Delete all.
                                        else {
                                            it.remove()
                                        }
                                    }
                                }

                                // Clear unreferenced vectors.

                                if (properties.isEmpty()) {
                                    vectors.clear()
                                } else {
                                    val referencedVectors = HashSet<PropertyVector<*>>(vectors.size)

                                    properties[device]?.forEach { referencedVectors.add(it.vector) }

                                    vectors.iterator().also {
                                        while (it.hasNext()) {
                                            val v = it.next()

                                            if (v.device == device &&
                                                !referencedVectors.contains(v)
                                            ) {
                                                it.remove()
                                            }
                                        }
                                    }
                                }
                            }
                            // Define a property that holds one or more text elements.
                            "defTextVector" -> {
                                val device = reader.getAttributeValue(null, "device")
                                val name = reader.getAttributeValue(null, "name")
                                val title = reader.getAttributeValue(null, "label") ?: name
                                val group = reader.getAttributeValue(null, "group")
                                val state = reader.getAttributeValue(null, "state")!!.toUpperCase()
                                val perm = reader.getAttributeValue(null, "perm")!!
                                val timeout = reader.getAttributeValue(null, "timeout")?.toInt() ?: 0

                                findPropertyVectorIndex(device, name).also { if (it >= 0) vectors.removeAt(it) }

                                vectors.add(
                                    TextVector(
                                        device,
                                        name,
                                        title,
                                        group,
                                        State.valueOf(state),
                                        PERMISSIONS[perm]!!,
                                        timeout,
                                    )
                                )
                            }
                            // Define a property that holds one or more numeric values.
                            "defNumberVector" -> {
                                val device = reader.getAttributeValue(null, "device")
                                val name = reader.getAttributeValue(null, "name")
                                val label = reader.getAttributeValue(null, "label") ?: name
                                val group = reader.getAttributeValue(null, "group") ?: ""
                                val state = reader.getAttributeValue(null, "state")!!.toUpperCase()
                                val perm = reader.getAttributeValue(null, "perm")!!
                                val timeout = reader.getAttributeValue(null, "timeout")?.toInt() ?: 0

                                findPropertyVectorIndex(device, name).also { if (it >= 0) vectors.removeAt(it) }

                                vectors.add(
                                    NumberVector(
                                        device,
                                        name,
                                        label,
                                        group,
                                        State.valueOf(state),
                                        PERMISSIONS[perm]!!,
                                        timeout,
                                    )
                                )
                            }
                            // Define a collection of switches.
                            "defSwitchVector" -> {
                                val device = reader.getAttributeValue(null, "device")
                                val name = reader.getAttributeValue(null, "name")
                                val label = reader.getAttributeValue(null, "label") ?: name
                                val group = reader.getAttributeValue(null, "group")
                                val state = reader.getAttributeValue(null, "state")?.toUpperCase() ?: "IDLE"
                                val perm = reader.getAttributeValue(null, "perm") ?: "rw"
                                val rule = reader.getAttributeValue(null, "rule") ?: "AnyOfMany"
                                val timeout = reader.getAttributeValue(null, "timeout") ?: "0"

                                findPropertyVectorIndex(device, name).also { if (it >= 0) vectors.removeAt(it) }

                                vectors.add(
                                    SwitchVector(
                                        device,
                                        name,
                                        label,
                                        group,
                                        State.valueOf(state),
                                        PERMISSIONS[perm]!!,
                                        RULES[rule]!!,
                                        timeout.toInt(),
                                    )
                                )
                            }
                            // Define a collection of passive indicator lights.
                            "defLightVector" -> {
                                val device = reader.getAttributeValue(null, "device")!!
                                val name = reader.getAttributeValue(null, "name")!!
                                val label = reader.getAttributeValue(null, "label") ?: name
                                val group = reader.getAttributeValue(null, "group") ?: ""
                                val state = reader.getAttributeValue(null, "state")?.toUpperCase() ?: "IDLE"

                                findPropertyVectorIndex(device, name).also { if (it >= 0) vectors.removeAt(it) }

                                vectors.add(
                                    LightVector(
                                        device,
                                        name,
                                        label,
                                        group,
                                        State.valueOf(state),
                                    )
                                )
                            }
                            // Define a property that holds one or more Binary Large Objects, BLOBs.
                            "defBLOBVector" -> {
                                val device = reader.getAttributeValue(null, "device")!!
                                val name = reader.getAttributeValue(null, "name")!!
                                val label = reader.getAttributeValue(null, "label") ?: name
                                val group = reader.getAttributeValue(null, "group") ?: ""
                                val state = reader.getAttributeValue(null, "state")?.toUpperCase() ?: "IDLE"
                                val perm = reader.getAttributeValue(null, "perm") ?: "rw"
                                val timeout = reader.getAttributeValue(null, "timeout")?.toInt() ?: 0

                                findPropertyVectorIndex(device, name).also { if (it >= 0) vectors.removeAt(it) }

                                vectors.add(
                                    BLOBVector(
                                        device,
                                        name,
                                        label,
                                        group,
                                        State.valueOf(state),
                                        PERMISSIONS[perm]!!,
                                        timeout,
                                    )
                                )
                            }
                            // Define one member of a text/switch/light/BLOB vector.
                            // defBLOB does not contain an initial value for the BLOB.
                            "defText",
                            "defSwitch",
                            "defLight",
                            "defBLOB" -> {
                                val name = reader.getAttributeValue(null, "name")
                                val label = reader.getAttributeValue(null, "label")
                                mProperties.add(hashMapOf("name" to name, "label" to label))
                            }
                            // Define one member of a number vector.
                            "defNumber" -> {
                                val name = reader.getAttributeValue(null, "name")!!
                                val label = reader.getAttributeValue(null, "label") ?: name
                                val format = reader.getAttributeValue(null, "format")!!
                                val min = reader.getAttributeValue(null, "min")
                                val max = reader.getAttributeValue(null, "max")
                                val step = reader.getAttributeValue(null, "step")

                                mProperties.add(
                                    hashMapOf(
                                        "name" to name,
                                        "label" to label,
                                        "format" to format,
                                        "min" to min,
                                        "max" to max,
                                        "step" to step,
                                    )
                                )
                            }
                            // Send a new set of values for a vector, with optional new timeout, state and message.
                            "setTextVector",
                            "setNumberVector",
                            "setSwitchVector",
                            "setLightVector",
                            "setBLOBVector" -> {
                                mVector["device"] = reader.getAttributeValue(null, "device")!!
                                mVector["name"] = reader.getAttributeValue(null, "name")!!
                                mVector["state"] = reader.getAttributeValue(null, "state")?.toUpperCase()
                                mVector["timeout"] = reader.getAttributeValue(null, "timeout")
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

                        if (tag.startsWith("def")) {
                            val vector = vectors.last()

                            // Only "defXXXVector".
                            val p = when (tag) {
                                "defTextVector" -> mProperties.map { TextProperty(vector as TextVector, it) }
                                "defNumberVector" -> mProperties.map { NumberProperty(vector as NumberVector, it) }
                                "defSwitchVector" -> mProperties.map { SwitchProperty(vector as SwitchVector, it) }
                                "defLightVector" -> mProperties.map { LightProperty(vector as LightVector, it) }
                                // TODO: BLOB
                                else -> continue
                            }

                            // Add or set properties.
                            p.forEach {
                                val device = it.vector.device
                                val index = findPropertyIndex(device, it.vector.name, it.name)

                                if (index >= 0) this.properties[device]!![index] = it
                                else if (this.properties.containsKey(device)) this.properties[device]!!.add(it)
                                else this.properties[device] = arrayListOf(it)

                                propertyListeners.forEach { l -> l.onProperty(it) }
                            }

                            mProperties.clear()
                        } else if (tag.startsWith("set")) {
                            val device = mVector["device"]!!
                            val name = mVector["name"]!!

                            val index = findPropertyVectorIndex(device, name)

                            if (index == -1) {
                                System.err.println("WARNING: Undefinied property $name from device $device")
                                continue
                            }

                            // Update vector.
                            val vector = vectors[index].let { v ->
                                val state = mVector["state"]?.let { State.valueOf(it) } ?: v.state
                                val timeout = mVector["timeout"]?.toInt() ?: v.timeout

                                if (v.state != state || v.timeout != timeout)
                                // TODO: BLOB
                                    if (v is TextVector) v.copy(state = state, timeout = timeout)
                                    else if (v is NumberVector) v.copy(state = state, timeout = timeout)
                                    else if (v is SwitchVector) v.copy(state = state, timeout = timeout)
                                    else (v as LightVector).copy(state = state)
                                else v
                            }

                            vectors[index] = vector

                            // Update properties.
                            properties[device]?.also {
                                // For each new property...
                                mProperties.forEach { m ->
                                    val mName = m["name"]!!
                                    val mValue = m["value"]!!

                                    // For each current property...
                                    for (i in it.indices) {
                                        val p = it[i]

                                        // Find the property will be updated.
                                        if (p.vector.name == name &&
                                            p.name == mName
                                        ) {
                                            it[i] = when (p) {
                                                is TextProperty -> p.copy(value = mValue, vector = vector as TextVector)
                                                is NumberProperty -> p.copy(value = mValue.toDouble(), vector = vector as NumberVector)
                                                is SwitchProperty -> p.copy(value = mValue == "On", vector = vector as SwitchVector)
                                                is LightProperty -> p.copy(value = State.valueOf(mValue), vector = vector as LightVector)
                                                // TODO: BLOB
                                                else -> continue
                                            }

                                            propertyListeners.forEach { l -> l.onProperty(it[i]) }
                                        }
                                    }
                                }
                            }

                            mVector.clear()
                            mProperties.clear()
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

    protected fun write(data: ByteArray, range: ClosedRange<Int> = data.indices) {
        outputStream.write(data, range.start, range.endInclusive - range.start + 1)
    }

    protected fun write(data: String) = write(data.toByteArray(Charsets.ISO_8859_1))

    override fun sendCommand(command: Command) = write(command.toXml())

    protected fun findPropertyVector(device: String, name: String): PropertyVector<*>? {
        return vectors.find { it.device == device && it.name == name }
    }

    protected fun findPropertyVectorIndex(device: String, name: String): Int {
        return vectors.indexOfFirst { it.device == device && it.name == name }
    }

    fun findProperty(device: String, vectorName: String, propertyName: String): Property<*>? {
        return properties[device]?.find { it.vector.name == vectorName && it.name == propertyName }
    }

    protected fun findPropertyIndex(device: String, vectorName: String, propertyName: String): Int {
        return properties[device]?.indexOfFirst { it.vector.name == vectorName && it.name == propertyName } ?: -1
    }

    companion object {

        private val PARSER = XMLInputFactory.newInstance().also {
            it.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false)
            it.setProperty(XMLInputFactory.IS_VALIDATING, false)
        }

        private val PERMISSIONS = mapOf(
            "ro" to Permission.READ,
            "wo" to Permission.WRITE,
            "rw" to Permission.READ_WRITE,
        )

        private val RULES = mapOf(
            "OneOfMany" to SwitchRule.ONE_OF_MANY,
            "AtMostOne" to SwitchRule.AT_MOST_ONE,
            "AnyOfMany" to SwitchRule.ANY_OF_MANY,
        )
    }
}