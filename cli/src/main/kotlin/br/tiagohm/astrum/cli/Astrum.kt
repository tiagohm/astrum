package br.tiagohm.astrum.cli

import br.tiagohm.astrum.indi.client.Client
import br.tiagohm.astrum.indi.client.MessageListener
import br.tiagohm.astrum.indi.protocol.Message
import picocli.CommandLine
import java.util.concurrent.Callable

@CommandLine.Command(name = "astrum")
class Astrum : Callable<Int>, MessageListener {

    var client: Client? = null
        private set

    @CommandLine.Command(
        name = "connect",
        description = ["Connects to the server running on [host] using the port number [port]"],
    )
    fun connect(
        @CommandLine.Option(
            names = ["-h", "--host"],
            description = ["INDI server host (default: localhost)"],
            defaultValue = "localhost",
        )
        host: String = "localhost",
        @CommandLine.Option(
            names = ["-p", "--port"],
            description = ["INDI server port (default: 7624)"],
            defaultValue = "7624",
        )
        port: Int = 7624,
    ) {
        if (client == null) {
            client = Client(host, port).also {
                it.registerMessageListener(this)

                it.connect()
                it.fetchProperties()
                it.enableBLOB()

                info("[INFO] Connected")
            }
        } else {
            warning("[WARNING] Already connected")
        }
    }

    @CommandLine.Command(
        name = "disconnect",
        description = ["Disconnects from the server"],
    )
    fun disconnect() {
        if (client != null) {
            client!!.unregisterMessageListener(this)
            client!!.disconnect()
            client = null

            info("[INFO] Disconnected")
        } else {
            warning("[WARNING] Already disconnected")
        }
    }

    override fun onMessage(message: Message) {
        info(message.message)
    }

    override fun call() = 0
}