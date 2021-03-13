import br.tiagohm.astrum.indi.client.INDIClient
import br.tiagohm.astrum.indi.client.MessageListener
import br.tiagohm.astrum.indi.client.PropertyListener
import br.tiagohm.astrum.indi.protocol.Message
import br.tiagohm.astrum.indi.protocol.properties.Property
import br.tiagohm.astrum.indi.protocol.properties.SwitchProperty
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class ClientTest {

    /*
    indiserver driver indi_simulator_ccd indi_simulator_spectrograph \
    indi_simulator_dome indi_simulator_sqm indi_simulator_focus indi_simulator_telescope \
    indi_simulator_gps indi_simulator_weather indi_simulator_guide indi_simulator_wheel \
    indi_simulator_rotator
     */

    @Test
    fun getProperties() {
        client.registerMessageListener(object : MessageListener {
            override fun onMessage(message: Message) {
                println(message.message)
            }
        })

        client.registerPropertyListener(object : PropertyListener {
            override fun onProperty(property: Property<*>) {
                println("${property.name}:${property.value}")
            }
        })

        client.fetchProperties()

        Thread.sleep(2000)

        val p = client.propertyByDeviceAndName("Telescope Simulator", "CONNECTION:CONNECT")!! as SwitchProperty
        client.send(!p)

        Thread.sleep(10000)

        System.err.println(client.properties().size)

        client.disconnect()
    }

    companion object {

        private lateinit var client: INDIClient

        @BeforeAll
        @JvmStatic
        fun setupAll() {
            client = INDIClient("192.168.0.121")
            client.connect()
        }
    }
}