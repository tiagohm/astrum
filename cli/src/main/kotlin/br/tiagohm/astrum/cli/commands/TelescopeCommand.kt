package br.tiagohm.astrum.cli.commands

import br.tiagohm.astrum.cli.blue
import br.tiagohm.astrum.cli.green
import br.tiagohm.astrum.cli.red
import br.tiagohm.astrum.cli.sexagesimal
import br.tiagohm.astrum.indi.client.Client
import br.tiagohm.astrum.indi.drivers.guider.GuideDirection
import br.tiagohm.astrum.indi.drivers.guider.Guider
import br.tiagohm.astrum.indi.drivers.telescope.Telescope
import picocli.CommandLine
import java.io.IOException
import java.time.ZonedDateTime

@CommandLine.Command(
    name = "telescope",
    description = ["Manages the telescopes and mounts"],
    subcommands = [CommandLine.HelpCommand::class],
)
class TelescopeCommand : Command.Driverable<Telescope> {

    @CommandLine.ParentCommand
    lateinit var astrum: AstrumCommand
        private set

    val client: Client?
        get() = astrum.client

    override fun driver(index: Int): Telescope {
        return client?.telescopes()?.takeIf { index >= 0 && index < it.size }?.get(index)
            ?: throw IOException("No driver available")
    }

    @CommandLine.Command(
        name = "on",
        description = ["Connects the telescope"],
        subcommands = [CommandLine.HelpCommand::class],
    )
    fun on(
        @CommandLine.Option(
            names = ["-n"],
            description = ["Driver index"],
            defaultValue = "0",
            paramLabel = "<index>",
        )
        index: Int,
    ) = driver(index).on()

    @CommandLine.Command(
        name = "off",
        description = ["Disconnects the telescope"],
        subcommands = [CommandLine.HelpCommand::class],
    )
    fun off(
        @CommandLine.Option(
            names = ["-n"],
            description = ["Driver index"],
            defaultValue = "0",
            paramLabel = "<index>",
        )
        index: Int,
    ) = driver(index).off()

    @CommandLine.Command(
        name = "abort",
        description = ["Aborts the telescope"],
        subcommands = [CommandLine.HelpCommand::class],
    )
    fun abort(
        @CommandLine.Option(
            names = ["-n"],
            description = ["Driver index"],
            defaultValue = "0",
            paramLabel = "<index>",
        )
        index: Int,
    ) = driver(index).abortMotion()

    @CommandLine.Command(
        name = "park",
        description = ["Moves the telescope to its park position"],
        subcommands = [CommandLine.HelpCommand::class],
    )
    fun park(
        @CommandLine.Option(
            names = ["-n"],
            description = ["Driver index"],
            defaultValue = "0",
            paramLabel = "<index>",
        )
        index: Int,
    ) {
        val driver = driver(index)
        if (driver.canPark()) driver.park()
        else error("Parking is not supported.")
    }

    @CommandLine.Command(
        name = "unpark",
        description = ["Unparks the telescope"],
        subcommands = [CommandLine.HelpCommand::class],
    )
    fun unpark(
        @CommandLine.Option(
            names = ["-n"],
            description = ["Driver index"],
            defaultValue = "0",
            paramLabel = "<index>",
        )
        index: Int,
    ) {
        val driver = driver(index)
        if (driver.canPark()) driver.unpark()
        else error("Parking is not supported.")
    }

    @CommandLine.Command(
        name = "start",
        description = ["Stars the tracking"],
        subcommands = [CommandLine.HelpCommand::class],
    )
    fun start(
        @CommandLine.Option(
            names = ["-n"],
            description = ["Driver index"],
            defaultValue = "0",
            paramLabel = "<index>",
        )
        index: Int,
    ) = driver(index).tracking(true)

    @CommandLine.Command(
        name = "stop",
        description = ["Stops the tracking"],
        subcommands = [CommandLine.HelpCommand::class],
    )
    fun stop(
        @CommandLine.Option(
            names = ["-n"],
            description = ["Driver index"],
            defaultValue = "0",
            paramLabel = "<index>",
        )
        index: Int,
    ) = driver(index).tracking(false)

    @CommandLine.Command(
        name = "slew",
        description = ["Slews the telescope to the coordinates and stop"],
        subcommands = [CommandLine.HelpCommand::class],
    )
    fun slew(
        @CommandLine.Option(
            names = ["-n"],
            description = ["Driver index"],
            defaultValue = "0",
            paramLabel = "<index>",
        )
        index: Int,
        @CommandLine.Option(
            names = ["--ra", "--az"],
            description = ["RA in hours or azimuth in degrees"],
            required = true,
            paramLabel = "<ra>",
        )
        ra: String,
        @CommandLine.Option(
            names = ["--dec", "--alt"],
            description = ["DEC or altitude in degrees"],
            required = true,
            paramLabel = "<dec>",
        )
        dec: String,
    ) {
        val driver = driver(index)
        if (driver.canSlew()) driver.slew(ra.sexagesimal(), dec.sexagesimal())
        else error("Slewing is not supported.")
    }

    @CommandLine.Command(
        name = "goto",
        description = ["Moves the telescope to the coordinates and keep tracking"],
        subcommands = [CommandLine.HelpCommand::class],
    )
    fun goto(
        @CommandLine.Option(
            names = ["-n"],
            description = ["Driver index"],
            defaultValue = "0",
            paramLabel = "<index>",
        )
        index: Int,
        @CommandLine.Option(
            names = ["--ra", "--az"],
            description = ["RA in hours or azimuth in degrees"],
            required = true,
            paramLabel = "<ra>",
        )
        ra: String,
        @CommandLine.Option(
            names = ["--dec", "--alt"],
            description = ["DEC or altitude in degrees"],
            required = true,
            paramLabel = "<dec>",
        )
        dec: String,
    ) {
        val driver = driver(index)
        if (driver.canGoto()) driver.goto(ra.sexagesimal(), dec.sexagesimal())
        else error("Goto is not supported.")
    }

    @CommandLine.Command(
        name = "sync",
        description = ["Syncs the coordinates to the telescope"],
        subcommands = [CommandLine.HelpCommand::class],
    )
    fun sync(
        @CommandLine.Option(
            names = ["-n"],
            description = ["Driver index"],
            defaultValue = "0",
            paramLabel = "<index>",
        )
        index: Int,
        @CommandLine.Option(
            names = ["--ra", "--az"],
            description = ["RA in hours or azimuth in degrees"],
            required = true,
            paramLabel = "<ra>",
        )
        ra: String,
        @CommandLine.Option(
            names = ["--dec", "--alt"],
            description = ["DEC or altitude in degrees"],
            required = true,
            paramLabel = "<dec>",
        )
        dec: String,
    ) {
        val driver = driver(index)
        if (driver.canSync()) driver.sync(ra.sexagesimal(), dec.sexagesimal())
        else error("Syncing is not supported.")
    }

    @CommandLine.Command(
        name = "pulse",
        description = ["Pulses guiding for RA and DEC axes"],
        subcommands = [CommandLine.HelpCommand::class],
    )
    fun pulse(
        @CommandLine.Option(
            names = ["-n"],
            description = ["Driver index"],
            defaultValue = "0",
            paramLabel = "<index>",
        )
        index: Int,
        @CommandLine.Option(
            names = ["--ra"],
            description = ["RA guiding pulse in milliseconds following by direction (E or W)"],
            required = true,
            paramLabel = "<ra>",
        )
        ra: String,
        @CommandLine.Option(
            names = ["--dec"],
            description = ["DEC guiding pulse in milliseconds following by direction (N or S)"],
            required = true,
            paramLabel = "<dec>",
        )
        dec: String,
    ) {
        val driver = driver(index)

        if (driver is Guider && driver.canGuide()) {
            val raWE = ra.last().let { if (it == 'E') "EAST" else if (it == 'W') "WEST" else error("Invalid RA guide direction: $it.") }
            val decNS = dec.last().let { if (it == 'N') "NORTH" else if (it == 'S') "SOUTH" else error("Invalid DEC guide direction: $it.") }
            val raMs = ra.substring(0, ra.length - 1).let { it.toDoubleOrNull() ?: error("Invalid RA time: $it.") }
            val decMs = dec.substring(0, dec.length - 1).let { it.toDoubleOrNull() ?: error("Invalid DEC time: $it.") }
            driver.pulse(GuideDirection.valueOf(raWE), raMs, GuideDirection.valueOf(decNS), decMs)
        } else error("Guide pulsing is not supported.")
    }

    @CommandLine.Command(
        name = "list",
        description = ["Lists the available telescopes"],
        subcommands = [CommandLine.HelpCommand::class],
    )
    fun list() {
        val telescopes = client?.telescopes() ?: emptyList()

        if (telescopes.isEmpty()) {
            blue("[INFO] No available telescopes.")
        } else {
            telescopes.forEachIndexed { i, t ->
                val title = "[$i] ${t.name}:${t.executable}"
                if (t.isOn) green(title) else red(title)
            }
        }
    }

    @CommandLine.Command(
        name = "status",
        description = ["Shows the telescope's status"],
        subcommands = [CommandLine.HelpCommand::class],
    )
    fun status(
        @CommandLine.Option(
            names = ["-n"],
            description = ["Driver index"],
            defaultValue = "0",
            paramLabel = "<index>",
        )
        index: Int,
    ) {
        driver(index).also {
            val title = "${it.name}:${it.executable}"
            if (it.isOn) green(title) else red(title)

            for (e in it.status()) {
                blue("${e.key}: ${e.value}")
            }
        }
    }

    @CommandLine.Command(
        name = "info",
        description = ["Shows the current telescope info"],
        subcommands = [CommandLine.HelpCommand::class],
    )
    fun info(
        @CommandLine.Option(
            names = ["-n"],
            description = ["Driver index"],
            defaultValue = "0",
            paramLabel = "<index>",
        )
        index: Int,
    ) {
        driver(index).also {
            val title = "${it.name}:${it.executable}"

            if (it.isOn) green(title) else {
                red(title)
                return
            }

            for (e in it.info()) {
                blue("${e.key}: ${e.value}")
            }
        }
    }

    @CommandLine.Command(
        name = "time",
        description = ["Sets the UTC Date & Offset. Use NOW for get the current date"],
        subcommands = [CommandLine.HelpCommand::class],
    )
    fun time(
        @CommandLine.Option(
            names = ["-n"],
            description = ["Driver index"],
            defaultValue = "0",
            paramLabel = "<index>",
        )
        index: Int,
        @CommandLine.Parameters(
            index = "0",
            description = ["The UTC Date & Offset in ISO 8601 format"],
            paramLabel = "<date>",
        )
        date: String,
    ) {
        val driver = driver(index)

        if (driver.hasTime()) {
            if (date == "NOW") driver.time(ZonedDateTime.now())
            else driver.time(ZonedDateTime.parse(date))
        } else red("Date and time is not supported")
    }

    @CommandLine.Command(
        name = "location",
        description = ["Sets the Earth geodetic coordinate"],
        subcommands = [CommandLine.HelpCommand::class],
    )
    fun location(
        @CommandLine.Option(
            names = ["-n"],
            description = ["Driver index"],
            defaultValue = "0",
            paramLabel = "<index>",
        )
        index: Int,
        @CommandLine.Option(
            names = ["--lat"],
            description = ["Site latitude (-90 to +90), degrees +N"],
            required = true,
            paramLabel = "<latitude>",
        )
        latitude: String,
        @CommandLine.Option(
            names = ["--lon"],
            description = ["Site longitude (0 to 360), degrees +E"],
            required = true,
            paramLabel = "<longitude>",
        )
        longitude: String,
        @CommandLine.Option(
            names = ["-e"],
            description = ["Site elevation, meters"],
            defaultValue = "0",
            paramLabel = "<elevation>",
        )
        elevation: Double = 0.0,
    ) {
        val driver = driver(index)

        if (driver.hasLocation()) {
            driver.location(longitude.sexagesimal(), latitude.sexagesimal(), elevation)
        } else {
            red("Location is not supported")
        }
    }

    @CommandLine.Command(
        name = "trackmode",
        description = ["Sets the track mode"],
        subcommands = [CommandLine.HelpCommand::class],
    )
    fun trackmode(
        @CommandLine.Option(
            names = ["-n"],
            description = ["Driver index"],
            defaultValue = "0",
            paramLabel = "<index>",
        )
        index: Int,
        @CommandLine.Parameters(
            index = "0",
            arity = "1",
            description = ["Sets the track mode. Use info command to see the available track modes"],
            paramLabel = "<mode>",
        )
        mode: String,
    ) {
        val driver = driver(index)

        driver.trackModes().firstOrNull { it.elementName == mode }?.also {
            driver.trackMode(it)
        } ?: error("Invalid track mode: $mode.")
    }

    @CommandLine.Command(
        name = "slewrate",
        description = ["Sets the slew rate. Use info command to see the available slew rates"],
        subcommands = [CommandLine.HelpCommand::class],
    )
    fun slewrate(
        @CommandLine.Option(
            names = ["-n"],
            description = ["Driver index"],
            defaultValue = "0",
            paramLabel = "<index>",
        )
        index: Int,
        @CommandLine.Parameters(
            index = "0",
            arity = "1",
            description = ["The slew rate"],
            paramLabel = "<rate>",
        )
        rate: String,
    ) {
        val driver = driver(index)

        driver.slewRates()
            .firstOrNull { it.elementName == rate }
            ?.also { driver.slewRate(it) } ?: error("Invalid slew rate: $rate.")
    }
}