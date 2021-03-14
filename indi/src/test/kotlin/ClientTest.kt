import br.tiagohm.astrum.indi.client.Client
import br.tiagohm.astrum.indi.client.ElementListener
import br.tiagohm.astrum.indi.client.MessageListener
import br.tiagohm.astrum.indi.client.PropertyElement
import br.tiagohm.astrum.indi.protocol.Message
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

        client.registerPropertyListener(object : ElementListener {
            override fun onElement(device: String, element: PropertyElement<*>) {
                System.err.println(element)
            }
        })

        Thread.sleep(4000)

        val telescope = client.telescopes().first()

        telescope.on()

        Thread.sleep(5000)

        System.err.println(telescope.coordinate())
        System.err.println(telescope.isTracking)
        System.err.println(telescope.isParked)
        System.err.println(telescope.mountType())
        System.err.println(telescope.parkPosition())
        System.err.println(telescope.pierSide())
        System.err.println(telescope.slewRates())
        System.err.println(telescope.slewRate())
        System.err.println(telescope.trackMode())
        System.err.println(telescope.dateTime())

        Thread.sleep(10000)

        telescope.off()
        client.disconnect()
    }

    companion object {

        private lateinit var client: Client

        @BeforeAll
        @JvmStatic
        fun setupAll() {
            client = Client("192.168.0.121")
            client.connect()
        }
    }
}