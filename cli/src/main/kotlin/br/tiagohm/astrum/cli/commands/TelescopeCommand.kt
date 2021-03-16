package br.tiagohm.astrum.cli.commands

import br.tiagohm.astrum.cli.blue
import br.tiagohm.astrum.cli.green
import br.tiagohm.astrum.cli.red
import br.tiagohm.astrum.cli.sexagesimal
import br.tiagohm.astrum.indi.client.Client
import br.tiagohm.astrum.indi.drivers.telescope.GuideDirection
import br.tiagohm.astrum.indi.drivers.telescope.SlewRate
import br.tiagohm.astrum.indi.drivers.telescope.Telescope
import br.tiagohm.astrum.indi.drivers.telescope.TrackMode
import picocli.CommandLine
import java.io.IOException

@CommandLine.Command(
    name = "telescope",
    description = ["Manages the telescopes and mounts"],
    subcommands = [CommandLine.HelpCommand::class],
)
class TelescopeCommand : Command.General<Telescope> {

    @CommandLine.ParentCommand
    lateinit var astrum: AstrumCommand
        private set

    val client: Client?
        get() = astrum.client

    private var index = 0

    override val driver: Telescope
        get() = client?.telescopes()?.takeIf { index >= 0 && index < it.size }?.get(index)
            ?: throw IOException("No driver available")

    // TELESCOPE

    @CommandLine.Command(
        name = "on",
        description = ["Connects the telescope"],
        subcommands = [CommandLine.HelpCommand::class],
    )
    fun on() = driver.on()

    @CommandLine.Command(
        name = "off",
        description = ["Disconnects the telescope"],
        subcommands = [CommandLine.HelpCommand::class],
    )
    fun off() = driver.off()

    @CommandLine.Command(
        name = "abort",
        description = ["Aborts the telescope"],
        subcommands = [CommandLine.HelpCommand::class],
    )
    fun abort() = driver.abort()

    @CommandLine.Command(
        name = "park",
        description = ["Moves the telescope to its park position"],
        subcommands = [CommandLine.HelpCommand::class],
    )
    fun park() {
        if (driver.canPark()) driver.park()
        else error("Parking is not supported")
    }

    @CommandLine.Command(
        name = "unpark",
        description = ["Unparks the telescope"],
        subcommands = [CommandLine.HelpCommand::class],
    )
    fun unpark() {
        if (driver.canPark()) driver.unpark()
        else error("Parking is not supported")
    }

    @CommandLine.Command(
        name = "start",
        description = ["Stars the tracking"],
        subcommands = [CommandLine.HelpCommand::class],
    )
    fun start() = driver.tracking(true)

    @CommandLine.Command(
        name = "stop",
        description = ["Stops the tracking"],
        subcommands = [CommandLine.HelpCommand::class],
    )
    fun stop() = driver.tracking(false)

    @CommandLine.Command(
        name = "slew",
        description = ["Slews the telescope to the coordinates and stop"],
        subcommands = [CommandLine.HelpCommand::class],
    )
    fun slew(
        @CommandLine.Option(
            names = ["--ra", "--az"],
            description = ["RA in hours or azimuth in degrees"],
            required = true,
        )
        ra: String,
        @CommandLine.Option(
            names = ["--dec", "--alt"],
            description = ["DEC or altitude in degrees"],
            required = true,
        )
        dec: String,
    ) {
        if (driver.canSlew()) driver.slew(ra.sexagesimal(), dec.sexagesimal())
        else error("Slewing is not supported")
    }

    @CommandLine.Command(
        name = "goto",
        description = ["Moves the telescope to the coordinates and keep tracking"],
        subcommands = [CommandLine.HelpCommand::class],
    )
    fun goto(
        @CommandLine.Option(
            names = ["--ra", "--az"],
            description = ["RA in hours or azimuth in degrees"],
            required = true,
        )
        ra: String,
        @CommandLine.Option(
            names = ["--dec", "--alt"],
            description = ["DEC or altitude in degrees"],
            required = true,
        )
        dec: String,
    ) {
        if (driver.canGoto()) driver.goto(ra.sexagesimal(), dec.sexagesimal())
        else error("Goto is not supported")
    }

    @CommandLine.Command(
        name = "sync",
        description = ["Syncs the coordinates to the telescope"],
        subcommands = [CommandLine.HelpCommand::class],
    )
    fun sync(
        @CommandLine.Option(
            names = ["--ra", "--az"],
            description = ["RA in hours or azimuth in degrees"],
            required = true,
        )
        ra: String,
        @CommandLine.Option(
            names = ["--dec", "--alt"],
            description = ["DEC or altitude in degrees"],
            required = true,
        )
        dec: String,
    ) {
        if (driver.canSync()) driver.sync(ra.sexagesimal(), dec.sexagesimal())
        else error("Syncing is not supported")
    }

    @CommandLine.Command(
        name = "pulse",
        description = ["Pulses guiding for RA and DEC axes"],
        subcommands = [CommandLine.HelpCommand::class],
    )
    fun pulse(
        @CommandLine.Option(
            names = ["--ra"],
            description = ["RA guiding pulse in milliseconds following by direction (E or W)"],
            required = true,
        )
        ra: String,
        @CommandLine.Option(
            names = ["--dec"],
            description = ["DEC guiding pulse in milliseconds following by direction (N or S)"],
            required = true,
        )
        dec: String,
    ) {
        if (driver.canGuide()) {
            val raWE = ra.last().let { if (it == 'E') "EAST" else if (it == 'W') "WEST" else error("Invalid RA guide direction: $it") }
            val decNS = dec.last().let { if (it == 'N') "NORTH" else if (it == 'S') "SOUTH" else error("Invalid DEC guide direction: $it") }
            val raMs = ra.substring(0, ra.length - 1).let { it.toDoubleOrNull() ?: error("Invalid RA time: $it") }
            val decMs = dec.substring(0, dec.length - 1).let { it.toDoubleOrNull() ?: error("Invalid DEC time: $it") }
            driver.pulse(GuideDirection.valueOf(raWE), raMs, GuideDirection.valueOf(decNS), decMs)
        } else error("Guide pulsing is not supported")
    }

    @CommandLine.Command(
        name = "list",
        description = ["Lists the available telescopes"],
        subcommands = [CommandLine.HelpCommand::class],
    )
    fun list() {
        val telescopes = client?.telescopes() ?: emptyList()

        if (telescopes.isEmpty()) {
            blue("[INFO] No available telescopes")
        } else {
            telescopes.forEachIndexed { i, t ->
                val selected = if (index == i) "*" else " "
                val title = " $selected [$i] ${t.name}:${t.executable}"
                if (t.isOn) green(title) else red(title)
            }
        }
    }

    @CommandLine.Command(
        name = "status",
        description = ["Shows the telescope's status"],
        subcommands = [CommandLine.HelpCommand::class],
    )
    fun status() {
        driver.also {
            val slewing = it.isSlewing
            val parking = it.isParking
            val parked = it.isParked
            val tracking = it.isTracking
            val trackMode = it.trackMode()
            val slewRate = it.slewRate()
            val coordinates = it.position()

            val title = "${it.name}:${it.executable}"
            if (it.isOn) green(title) else red(title)

            blue(
                """
                SLEWING: $slewing
                PARKING: $parking
                PARKED: $parked
                TRACKING: $tracking
                TRACK MODE: $trackMode
                SLEW RATE: $slewRate
                RA: ${coordinates.first}
                DEC: ${coordinates.second}
                """.trimIndent()
            )
        }
    }

    @CommandLine.Command(
        name = "info",
        description = ["Shows the current telescope info"],
        subcommands = [CommandLine.HelpCommand::class],
    )
    fun info() {
        driver.also {
            val title = "${it.name}:${it.executable}"

            if (it.isOn) green(title) else {
                red(title)
                return
            }

            val slewRates = it.slewRates().joinToString()
            val trackModes = it.trackModes().joinToString()
            val (raRate, decRate) = it.trackRate()
            val canSync = it.canSync()
            val canGoto = it.canGoto()
            val canTrackSatellite = it.canTrackSatellite()
            val canGuide = it.canGuide()
            val canPark = it.canPark()
            val (raPark, decPark) = it.parkPosition()
            val dateTime = it.dateTime()
            val (lon, lat, elev) = it.location()

            blue(
                """
                CAN SYNC: $canSync
                CAN GOTO: $canGoto
                CAN TRACK SATELLITE: $canTrackSatellite
                CAN GUIDE: $canGuide
                CAN PARK: $canPark    
                TRACK MODES: $trackModes
                SLEW RATES: $slewRates
                TRACK RATE: RA: $raRate DEC: $decRate
                PARK POSITION: RA: $raPark DEC: $decPark
                DATE TIME: $dateTime
                LOCATION: LON: $lon LAT: $lat ELEV: $elev
                """.trimIndent()
            )
        }

        blue("[INFO] No available telescopes")
    }

    @CommandLine.Command(
        name = "choose",
        description = ["Chooses the current telescope"],
        subcommands = [CommandLine.HelpCommand::class],
    )
    fun choose(
        @CommandLine.Parameters(
            index = "0",
            arity = "1",
            description = ["The telescope index from info command"],
        )
        index: Int
    ) {
        client?.telescopes()?.also {
            if (it.isEmpty()) error("No available telescopes")
            if (index >= 0 && index < it.size) {
                this.index = index
                blue("[INFO] Choosed ${driver.name}")
            } else error("Index out of range")
        } ?: error("No driver available")
    }

    @CommandLine.Command(
        name = "trackmode",
        description = ["Sets the track mode"],
        subcommands = [CommandLine.HelpCommand::class],
    )
    fun trackmode(
        @CommandLine.Parameters(
            index = "0",
            arity = "1",
            description = ["Sets the track mode. Use info command to see the available track modes"],
        )
        mode: String
    ) {
        if (driver.trackModes().any { it.elementName == mode }) {
            driver.trackMode(TrackMode(mode))
        } else {
            error("Invalid track mode: $mode")
        }
    }

    @CommandLine.Command(
        name = "slewrate",
        description = ["Sets the slew rate. Use info command to see the available slew rates"],
        subcommands = [CommandLine.HelpCommand::class],
    )
    fun slewrate(
        @CommandLine.Parameters(
            index = "0",
            arity = "1",
            description = ["The slew rate"],
        )
        rate: String
    ) {
        if (driver.slewRates().any { it.elementName == rate }) {
            driver.slewRate(SlewRate(rate))
        } else {
            error("Invalid slew rate: $rate")
        }
    }
}