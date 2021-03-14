import br.tiagohm.astrum.indi.client.Client
import br.tiagohm.astrum.indi.client.MessageListener
import br.tiagohm.astrum.indi.client.PropertyListener
import br.tiagohm.astrum.indi.drivers.telescope.Telescope
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

        client.registerPropertyListener(object : PropertyListener {
            override fun onProperty(device: String, propName: String, elementName: String, value: Any) {
                if (device == "Telescope Simulator") {
                    // System.err.println("$propName:$memberName:$value")
                }
            }
        })

        Thread.sleep(1000)

        val telescope = client.drivers().first() as Telescope

        System.err.println(telescope.coordinate())
        System.err.println(telescope.isTracking)
        System.err.println(telescope.isParked)
        System.err.println(telescope.mountType())
        System.err.println(telescope.parkPosition())
        System.err.println(telescope.pierSide())
        System.err.println(telescope.slewRates())
        System.err.println(telescope.slewRate())
        System.err.println(telescope.trackMode())

        Thread.sleep(1000)

        telescope.goTo(8.7677713, -16.7458)

        Thread.sleep(10000)

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