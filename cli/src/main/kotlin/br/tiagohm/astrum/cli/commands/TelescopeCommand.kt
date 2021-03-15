package br.tiagohm.astrum.cli.commands

import br.tiagohm.astrum.cli.*
import br.tiagohm.astrum.indi.client.Client
import br.tiagohm.astrum.indi.drivers.telescope.GuideDirection
import br.tiagohm.astrum.indi.drivers.telescope.SlewRate
import br.tiagohm.astrum.indi.drivers.telescope.Telescope
import br.tiagohm.astrum.indi.drivers.telescope.TrackMode
import picocli.CommandLine
import java.io.IOException

// TODO: Multiple connected telescopes!
@CommandLine.Command(
    name = "telescope",
    description = ["Manages the telescopes and mounts"],
    subcommands = [CommandLine.HelpCommand::class, TelescopeCommand.Config::class],
)
class TelescopeCommand : Command.Driverable<Telescope> {

    @CommandLine.ParentCommand
    lateinit var astrum: Astrum
        private set

    val client: Client?
        get() = astrum.client

    override val driver: Telescope
        get() = client?.telescopes()?.firstOrNull()
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
        else error("[ERROR] Parking is not supported")
    }

    @CommandLine.Command(
        name = "unpark",
        description = ["Unparks the telescope"],
        subcommands = [CommandLine.HelpCommand::class],
    )
    fun unpark() {
        if (driver.canPark()) driver.unpark()
        else error("[ERROR] Parking is not supported")
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
        else error("[ERROR] Slewing is not supported")
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
        else error("[ERROR] Goto is not supported")
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
        else error("[ERROR] Syncing is not supported")
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
        } else {
            error("[ERROR] Guide pulsing is not supported")
        }
    }

    @CommandLine.Command(
        name = "list",
        description = ["Lists the available telescopes"],
        subcommands = [CommandLine.HelpCommand::class],
    )
    fun list() {
        val telescopes = client?.telescopes() ?: emptyList()

        if (telescopes.isEmpty()) {
            showInfo("[INFO] No available telescopes")
        } else {
            telescopes.forEachIndexed { i, t ->
                val title = "[$i] ${t.name}:${t.executable}"
                if (t.isOn) showSuccess(title) else showError(title)
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
            val coordinates = it.position()

            val title = "${it.name}:${it.executable}"
            if (it.isOn) showSuccess(title) else showError(title)

            showInfo(
                """
                SLEWING: $slewing
                PARKING: $parking
                PARKED: $parked
                TRACKING: $tracking
                TRACK MODE: $trackMode
                RA: ${coordinates.first}
                DEC: ${coordinates.second}
                """.trimIndent()
            )
        }
    }

    @CommandLine.Command(
        name = "info",
        description = ["Shows the telescopes' info"],
        subcommands = [CommandLine.HelpCommand::class],
    )
    fun info() {
        client?.telescopes()?.let {
            if (it.isNotEmpty()) {
                for (i in it.indices) {
                    val t = it[i]

                    val title = "[$i] ${t.name}:${t.executable}"
                    if (t.isOn) showSuccess(title) else showError(title)

                    val sr = t.slewRate()
                    val rates = t.slewRates().joinToString { a -> if (a == sr) "[$a]" else "$a" }
                    val (raRate, decRate) = t.trackRate()
                    val canSync = t.canSync()
                    val canGoto = t.canGoto()
                    val canTrackSatellite = t.canTrackSatellite()
                    val canGuide = t.canGuide()
                    val canPark = t.canPark()
                    val (raPark, decPark) = t.parkPosition()
                    val dateTime = t.dateTime()
                    val (lon, lat, elev) = t.location()

                    showInfo(
                        """
                        CAN SYNC: $canSync
                        CAN GOTO: $canGoto
                        CAN TRACK SATELLITE: $canTrackSatellite
                        CAN GUIDE: $canGuide
                        CAN PARK: $canPark    
                        SLEW RATES: $rates
                        TRACK RATE: RA: $raRate DEC: $decRate
                        PARK POSITION: RA: $raPark DEC: $decPark
                        DATE TIME: $dateTime
                        LOCATION: LON: $lon LAT: $lat ELEV: $elev
                        """.trimIndent()
                    )
                }

                return
            }
        }

        showInfo("[INFO] No available telescopes")
    }

    // CONFIG

    @CommandLine.Command(
        name = "config",
        description = ["Configs the telescope"],
        subcommands = [CommandLine.HelpCommand::class],
    )
    class Config : Command.Configurable<Telescope> {

        @CommandLine.ParentCommand
        lateinit var telescope: TelescopeCommand
            private set

        override val driver: Telescope
            get() = telescope.driver

        @CommandLine.Command(
            name = "trackmode",
            description = ["Sets the track mode"],
            subcommands = [CommandLine.HelpCommand::class],
        )
        fun trackmode(
            @CommandLine.Parameters(
                index = "0",
                arity = "1",
                description = ["The track mode: SIDEREAL, SOLAR, LUNAR or CUSTOM"],
            )
            mode: String
        ) {
            driver.trackMode(TrackMode.valueOf(mode))
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
                error("[ERROR] Invalid slew rate: $rate")
            }
        }
    }
}