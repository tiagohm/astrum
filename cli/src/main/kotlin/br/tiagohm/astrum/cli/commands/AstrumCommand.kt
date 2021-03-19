package br.tiagohm.astrum.cli.commands

import br.tiagohm.astrum.cli.blue
import br.tiagohm.astrum.cli.green
import br.tiagohm.astrum.cli.orange
import br.tiagohm.astrum.cli.red
import br.tiagohm.astrum.indi.client.Client
import br.tiagohm.astrum.indi.client.DriverListener
import br.tiagohm.astrum.indi.client.MessageListener
import br.tiagohm.astrum.indi.drivers.Driver
import br.tiagohm.astrum.indi.protocol.Message
import picocli.CommandLine

@CommandLine.Command(
    name = "astrum",
    subcommands = [CommandLine.HelpCommand::class, TelescopeCommand::class],
)
class AstrumCommand : Command, MessageListener, DriverListener {

    var client: Client? = null
        private set

    @CommandLine.Command(
        name = "connect",
        description = ["Connects to the server running on [host] using the port number [port]"],
        subcommands = [CommandLine.HelpCommand::class],
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
                it.registerMessageListener(this)

                it.connect()

                blue("[INFO] Connected.")
            }
        } else {
            orange("[WARNING] Already connected.")
        }
    }

    @CommandLine.Command(
        name = "disconnect",
        description = ["Disconnects from the server"],
        subcommands = [CommandLine.HelpCommand::class],
    )
    fun disconnect() {
        if (client != null) {
            client!!.unregisterMessageListener(this)
            client!!.unregisterMessageListener(this)
            client!!.disconnect()
            client = null

            blue("[INFO] Disconnected.")
        } else {
            orange("[WARNING] Already disconnected.")
        }
    }

    override fun onMessage(message: Message) {
        val m = message.message

        if (message.fatal || m.startsWith("[ERROR]")) red(m)
        else if (m.startsWith("[WARNING]")) orange(m)
        else blue(m)

        if (message.fatal) {
            client = null
        }
    }

    override fun onDriverAdded(driver: Driver) {
        green("[OK] Driver ${driver.name} found.")
    }

    override fun onDriverRemoved(driver: Driver) {
        orange("[WARNING] Driver ${driver.name} was removed.")
    }
}