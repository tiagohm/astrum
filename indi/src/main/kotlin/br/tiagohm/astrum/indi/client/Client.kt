package br.tiagohm.astrum.indi.client

import br.tiagohm.astrum.indi.EMPTY_BYTE_ARRAY
import br.tiagohm.astrum.indi.client.parser.SimpleXMLParser
import br.tiagohm.astrum.indi.client.parser.XMLTag
import br.tiagohm.astrum.indi.drivers.*
import br.tiagohm.astrum.indi.drivers.focuser.Focuser
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
import kotlin.reflect.KClass

// http://www.clearskyinstitute.com/INDI/INDI.pdf
// https://www.w3schools.com/xml/xml_dtd_intro.asp

@Suppress("UNCHECKED_CAST")
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
    private val registeredElements = Shelf<Element<*>>()

    init {
        register(DriverInfo::class)
    }

    fun registerMessageListener(listener: MessageListener) {
        if (!messageListeners.contains(listener)) messageListeners.add(listener)
    }

    fun registerElementListener(listener: ElementListener) {
        if (!elementListeners.contains(listener)) elementListeners.add(listener)
    }

    fun registerDriverListener(listener: DriverListener) {
        if (!driverListeners.contains(listener)) driverListeners.add(listener)
    }

    fun register(element: Element<*>, device: String = "") {
        registeredElements.set(device, element.propName, element.elementName, element)
    }

    fun <T> register(element: KClass<out Enum<T>>, device: String = "") where T : Enum<T>, T : Element<*> {
        element.java.enumConstants.forEach { register(it as Element<*>, device) }
    }

    fun unregisterMessageListener(listener: MessageListener) {
        messageListeners.remove(listener)
    }

    fun unregisterElementListener(listener: ElementListener) {
        elementListeners.remove(listener)
    }

    fun unregisterDriverListener(listener: DriverListener) {
        driverListeners.remove(listener)
    }

    fun unregister(element: Element<*>, device: String = "") {
        registeredElements.remove(device, element.propName, element.elementName)
    }

    fun <T> unregister(element: KClass<out Enum<T>>, device: String = "") where T : Enum<T>, T : Element<*> {
        element.java.enumConstants.forEach { unregister(it as Element<*>, device) }
    }

    private fun registerDriver(device: String, driver: PropertyElement<String>) {
        DRIVERS[driver.value]
            ?.java
            ?.getDeclaredConstructor(Client::class.java, String::class.java, String::class.java)
            ?.newInstance(this, device, driver.value)
            ?.also {
                if (!drivers.contains(device)) {
                    it.initialize()
                    drivers[device] = it
                    driverListeners.forEach { listener -> listener.onDriverAdded(it) }
                }
            }
    }

    private fun processTag(tag: XMLTag) {
        when (tag.name) {
            // Delete the given property, or entire device if no property is specified.
            "delProperty" -> {
                val device = tag.attributes["device"]!!
                val name = tag.attributes["name"]!!

                // Delete by device and name.
                if (name.isNotEmpty()) {
                    elements.remove(device, name)
                }
                // Delete by device.
                else if (device.isNotEmpty()) {
                    elements.remove(device)
                }
                // Delete all.
                else {
                    elements.clear()
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
                val state = tag.attributes["state"]?.toUpperCase()?.let { State.valueOf(it) }

                for (c in tag.children) {
                    val name = c.attributes["name"]!!
                    val value = c.text

                    val size = c.attributes["size"]?.toIntOrNull() ?: 0
                    val format = c.attributes["format"] ?: ""
                    val min = c.attributes["min"]?.toDoubleOrNull() ?: 0.0
                    val max = c.attributes["max"]?.toDoubleOrNull() ?: 0.0
                    val step = c.attributes["step"]?.toDoubleOrNull() ?: 0.0

                    when (c.name) {
                        "defText",
                        "defNumber",
                        "defSwitch",
                        "defLight",
                        "defBLOB" -> {
                            val e = registeredElements.get(device, propName, name)
                                ?: registeredElements.get("", propName, name)
                                ?: continue

                            val pe = when (e) {
                                is TextElement -> PropertyElement(e, value, isReadOnly ?: false, state ?: State.IDLE)
                                is NumberElement -> PropertyElement(e, value.toDoubleOrNull() ?: 0.0, isReadOnly ?: false, state ?: State.IDLE, min, max, step, size, format)
                                is SwitchElement -> PropertyElement(e, value == "On", isReadOnly ?: false, state ?: State.IDLE)
                                is LightElement -> PropertyElement(e, State.valueOf(value), isReadOnly ?: false, state ?: State.IDLE)
                                else -> PropertyElement(e as BLOBElement, EMPTY_BYTE_ARRAY, isReadOnly ?: false, state ?: State.IDLE, min, max, step, size, format)
                            }

                            elements.set(device, propName, name, pe)

                            if (propName == "DRIVER_INFO" && name == "DRIVER_EXEC") {
                                registerDriver(device, pe as PropertyElement<String>)
                            }
                        }
                        "oneText",
                        "oneNumber",
                        "oneSwitch",
                        "oneLight",
                        "oneBLOB" -> {
                            val ope = elements.get(device, propName, name) ?: continue
                            val a = isReadOnly ?: ope.isReadOnly
                            val b = state ?: ope.state

                            val pe = when (ope.type) {
                                ElementType.TEXT -> (ope as PropertyElement<String>).copy(value = value, isReadOnly = a, state = b)
                                ElementType.NUMBER -> (ope as PropertyElement<Double>).copy(value = value.toDoubleOrNull() ?: ope.value, isReadOnly = a, state = b)
                                ElementType.SWITCH -> (ope as PropertyElement<Boolean>).copy(value = value == "On", isReadOnly = a, state = b)
                                ElementType.LIGHT -> (ope as PropertyElement<State>).copy(value = State.valueOf(value), isReadOnly = a, state = b)
                                else -> (ope.element as PropertyElement<ByteArray>).copy(value = EMPTY_BYTE_ARRAY, isReadOnly = a, state = b) // TODO
                            }

                            elements.set(device, propName, name, pe)
                        }
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

            fetchProperties(name = "DRIVER_INFO")
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
    ) = outputStream.write(data, range.start, range.endInclusive - range.start + 1)

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

    fun element(device: String, propName: String, name: String) = elements.get(device, propName, name)

    fun devices(): List<String> = elements.keys()

    fun properties(device: String) = elements.keys(device)

    fun elements(device: String, propName: String) = elements.values(device, propName)

    fun drivers() = drivers.values.toList()

    fun telescopes() = drivers.values.filterIsInstance<Telescope>()

    fun focusers() = drivers.values.filterIsInstance<Focuser>()

    companion object {

        private val DRIVERS = mapOf<String, KClass<out Driver>?>(
            // CCD
            "indi_atik_ccd" to null,
            "indi_asi_ccd" to null,
            "indi_nikon_ccd" to null,
            "indi_inovaplx_ccd" to null,
            "indi_v4l2_ccd" to null,
            "indi_canon_ccd" to null,
            "indi_toupcam_ccd" to null,
            "indi_simulator_ccd" to null,
            "indi_qhy_ccd" to null,
            "indi_sony_ccd" to null,
            "indi_pentax_ccd" to null,
            "indi_mi_ccd_usb" to null,
            "indi_sbig_ccd" to null,
            "indi_gphoto_ccd" to null,
            "indi_mallincam_ccd" to null,
            "indi_nightscape_ccd" to null,
            "indi_pentax" to null,
            "indi_nncam_ccd" to null,
            "indi_starshootg_ccd" to null,
            "indi_ffmv_ccd" to null,
            "indi_simulator_guide" to null,
            "indi_mi_ccd_eth" to null,
            "indi_apogee_ccd" to null,
            "indi_webcam_ccd" to null,
            "indi_qsi_ccd" to null,
            "indi_altair_ccd" to null,
            "indi_sv305_ccd" to null,
            "indi_sx_ccd" to null,
            "indi_dsi_ccd" to null,
            "indi_fuji_ccd" to null,
            "indi_fishcamp_ccd" to null,
            "indi_fli_ccd" to null,
            "indi_rpicam" to null,
            // Weather
            "indi_sqm_weather" to null,
            "indi_aagcloudwatcher_ng" to null,
            "indi_simulator_weather" to null,
            "indi_vantage_weather" to null,
            "indi_weatherradio" to null,
            "indi_astromech_lpm" to null,
            "indi_watcher_weather" to null,
            "indi_weather_safety_proxy" to null,
            "indi_openweathermap_weather" to null,
            "indi_mbox_weather" to null,
            "indi_meta_weather" to null,
            "indi_duino" to null,
            "indi_aagcloudwatcher" to null,
            // Agent
            "indi_imager_agent" to null,
            // Focuser
            "indi_simulator_focus" to null,
            "indi_moonlitedro_focus" to null,
            "indi_steeldrive2_focus" to null,
            "indi_lynx_focus" to null,
            "indi_fcusb_focus" to null,
            "indi_smartfocus_focus" to null,
            "indi_simulator_rotator" to null,
            "indi_rbfocus_focus" to null,
            "indi_lacerta_mfoc_focus" to null,
            "indi_gemini_focus" to null,
            "indi_myfocuserpro2_focus" to null,
            "indi_tcfs_focus" to null,
            "indi_lakeside_focus" to null,
            "indi_robo_focus" to null,
            "indi_celestron_sct_focus" to null,
            "indi_nstep_focus" to null,
            "indi_asi_focuser" to null,
            "indi_pyxis_rotator" to null,
            "indi_falcon_rotator" to null,
            "indi_deepskydad_af2_focus" to null,
            "indi_nightcrawler_focus" to null,
            "indi_steeldrive_focus" to null,
            "indi_hitecastrodc_focus" to null,
            "indi_tcfs3_focus" to null,
            "indi_armadillo_focus" to null,
            "indi_dreamfocuser_focus" to null,
            "indi_dmfc_focus" to null,
            "indi_platypus_focus" to null,
            "indi_activefocuser_focus" to null,
            "indi_astromechfoc" to null,
            "indi_beefocus" to null,
            "indi_onfocus_focus" to null,
            "indi_aaf2_focus" to null,
            "indi_perfectstar_focus" to null,
            "indi_fli_focus" to null,
            "indi_moonlite_focus" to null,
            "indi_deepskydad_af3_focus" to null,
            "indi_sestosenso_focus" to null,
            "indi_rainbowrsf_focus" to null,
            "indi_microtouch_focus" to null,
            "indi_nfocus" to null,
            "indi_siefs_focus" to null,
            "indi_usbfocusv3_focus" to null,
            "indi_deepskydad_af1_focus" to null,
            "indi_sestosenso2_focus" to null,
            "indi_efa_focus" to null,
            // Dome
            "indi_scopedome_dome" to null,
            "indi_talon6" to null,
            "indi_script_dome" to null,
            "indi_baader_dome" to null,
            "indi_domepro2_dome" to null,
            "indi_nexdome" to null,
            "indi_ddw_dome" to null,
            "indi_rigel_dome" to null,
            "indi_rolloff_dome" to null,
            "indi_maxdomeii" to null,
            "indi_simulator_dome" to null,
            "indi_dragonfly_dome" to null,
            // Spectograph
            "indi_limesdr_detector" to null,
            "indi_rtlsdr" to null,
            "indi_spectracyber" to null,
            "indi_simulator_spectrograph" to null,
            "indi_shelyakeshel_spectrograph" to null,
            "indi_shelyakspox_spectrograph" to null,
            // Auxiliary
            "indi_gpusb" to null,
            "indi_skysafari" to null,
            "indi_planewave_deltat" to null,
            "indi_watchdog" to null,
            "indi_gpsnmea" to null,
            "indi_star2000" to null,
            "indi_pegasus_ppb" to null,
            "indi_pegasus_ppba" to null,
            "indi_usbdewpoint" to null,
            "indi_rtklib" to null,
            "indi_astrolink4" to null,
            "indi_joystick" to null,
            "indi_snapcap" to null,
            "indi_mgenautoguider" to null,
            "indi_pegasus_upb" to null,
            "indi_gpsd" to null,
            "indi_flipflat" to null,
            "indi_astrometry" to null,
            "indi_asi_st4" to null,
            "indi_duino" to null,
            "indi_seletek_rotator" to null,
            "indi_asi_power" to null,
            "indi_simulator_gps" to null,
            "indi_arduinost4" to null,
            // Detector
            "indi_ahp_correlator" to null,
            // Telescope
            "indi_lx200zeq25" to null,
            "indi_pmc8_telescope" to null,
            "indi_ioptronv3_telescope" to null,
            "indi_celestron_aux" to null,
            "indi_azgti_telescope" to null,
            "indi_synscan_telescope" to null,
            "indi_skywatcherAltAzMount" to null,
            "indi_starbook_telescope" to null,
            "indi_lx200gemini" to null,
            "indi_simulator_telescope" to TelescopeSimulator::class,
            "indi_ieq_telescope" to null,
            "indi_dsc_telescope" to DSC::class,
            "indi_lx200stargo" to null,
            "indi_nexstarevo_telescope" to null,
            "indi_skycommander_telescope" to null,
            "indi_lx200_10micron" to null,
            "indi_rainbow_telescope" to null,
            "indi_lx200basic" to null,
            "indi_crux_mount" to TitanTCS::class,
            "indi_lx200gps" to null,
            "indi_lx200pulsar2" to null,
            "indi_eq500x_telescope" to null,
            "indi_lx200ap_gtocp2" to null,
            "indi_lx200_TeenAstro" to null,
            "indi_lx200autostar" to null,
            "indi_celestron_gps" to CelestronGPS::class,
            "indi_lx200aok" to null,
            "indi_ioptronHC8406" to null,
            "indi_script_telescope" to null,
            "indi_synscanlegacy_telescope" to null,
            "indi_lx200ss2000pc" to null,
            "indi_lx200ap" to null,
            "indi_lx200fs2" to null,
            "indi_paramount_telescope" to null,
            "indi_ieqlegacy_telescope" to null,
            "indi_lx200classic" to null,
            "indi_lx200_16" to null,
            "indi_skywatcherAltAzSimple" to null,
            "indi_eqmod_telescope" to null,
            "indi_lx200gotonova" to null,
            "indi_lx200_OnStep" to null,
            "indi_lx200ap_experimental" to null,
            "indi_temma_telescope" to null,
            // Filter Wheel
            "indi_manual_wheel" to null,
            "indi_quantum_wheel" to null,
            "indi_fli_wheel" to null,
            "indi_qhycfw3_wheel" to null,
            "indi_asi_wheel" to null,
            "indi_trutech_wheel" to null,
            "indi_sx_wheel" to null,
            "indi_qhycfw1_wheel" to null,
            "indi_apogee_wheel" to null,
            "indi_optec_wheel" to null,
            "indi_xagyl_wheel" to null,
            "indi_qhycfw2_wheel" to null,
            "indi_simulator_wheel" to null,
            "indi_atik_wheel" to null,
            // Adaptive Optic
            "indi_sx_ao" to null,
        )


    }
}