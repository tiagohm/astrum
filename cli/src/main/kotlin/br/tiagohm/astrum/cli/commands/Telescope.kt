package br.tiagohm.astrum.cli.commands

import br.tiagohm.astrum.cli.Astrum
import br.tiagohm.astrum.cli.info
import br.tiagohm.astrum.cli.success
import br.tiagohm.astrum.indi.drivers.telescope.TelescopeDriver
import br.tiagohm.astrum.indi.drivers.telescope.TrackMode
import picocli.CommandLine
import java.io.IOException
import java.time.ZonedDateTime
import java.util.concurrent.Callable

// TODO: Quando tem mais de um telescópio conectado!
// TODO: Pulse Guide
@CommandLine.Command(name = "telescope")
class Telescope(val astrum: Astrum) : Callable<Int> {

    private var driver: TelescopeDriver? = null

    private fun chooseDriver(): TelescopeDriver {
        return (driver ?: astrum.client?.telescopes()?.firstOrNull()).also { driver = it }
            ?: throw IOException("No driver available")
    }

    @CommandLine.Command(
        name = "on",
        description = ["Connectes the telescope"],
    )
    fun on() {
        chooseDriver().also { it.on() }
    }

    @CommandLine.Command(
        name = "off",
        description = ["Disconnects the telescope"],
    )
    fun off() {
        chooseDriver().also { it.off() }
    }

    @CommandLine.Command(
        name = "abort",
        description = ["Aborts the telescope"],
    )
    fun abort() {
        chooseDriver().also { it.abort() }
    }

    @CommandLine.Command(
        name = "park",
        description = ["Moves the telescope to its park position"],
    )
    fun park() {
        chooseDriver().also { it.park() }
    }

    @CommandLine.Command(
        name = "unpark",
        description = ["Unparks the telescope"],
    )
    fun unpark() {
        chooseDriver().also { it.unpark() }
    }

    @CommandLine.Command(
        name = "start",
        description = ["Stars the tracking"],
    )
    fun start() {
        chooseDriver().also { it.tracking(true) }
    }

    @CommandLine.Command(
        name = "stop",
        description = ["Stops the tracking"],
    )
    fun stop() {
        chooseDriver().also { it.tracking(false) }
    }

    @CommandLine.Command(
        name = "slew",
        description = ["Slews the telescope to the coordinates and stop"],
    )
    fun slew(
        // TODO: Sexagesimal
        @CommandLine.Option(
            names = ["--ra", "--az"],
            description = ["RA in hours or azimuth in degrees"],
            required = true,
        )
        raAz: Double,
        @CommandLine.Option(
            names = ["--dec", "--alt"],
            description = ["DEC or altitude in degrees"],
            required = true,
        )
        decAlt: Double,
    ) {
        chooseDriver().also { it.slewTo(raAz, decAlt) }
    }

    @CommandLine.Command(
        name = "goto",
        description = ["Moves the telescope to the coordinates and keep tracking"],
    )
    fun goto(
        // TODO: Sexagesimal
        @CommandLine.Option(
            names = ["--ra", "--az"],
            description = ["RA in hours or azimuth in degrees"],
            required = true,
        )
        raAz: Double,
        @CommandLine.Option(
            names = ["--dec", "--alt"],
            description = ["DEC or altitude in degrees"],
            required = true,
        )
        decAlt: Double,
    ) {
        chooseDriver().also { it.goTo(raAz, decAlt) }
    }

    @CommandLine.Command(
        name = "sync",
        description = ["Syncs the coordinates to the telescope"],
    )
    fun sync(
        // TODO: Sexagesimal
        @CommandLine.Option(
            names = ["--ra", "--az"],
            description = ["RA in hours or azimuth in degrees"],
            required = true,
        )
        raAz: Double,
        @CommandLine.Option(
            names = ["--dec", "--alt"],
            description = ["DEC or altitude in degrees"],
            required = true,
        )
        decAlt: Double,
    ) {
        chooseDriver().also { it.sync(raAz, decAlt) }
    }

    @CommandLine.Command(
        name = "list",
        description = ["Lists the available telescopes"],
    )
    fun list() {
        val telescopes = astrum.client?.telescopes() ?: emptyList()

        if (telescopes.isEmpty()) {
            info("[INFO] No available telescopes")
        } else {
            telescopes.forEachIndexed { i, t ->
                val text = "[$i] ${t.name}:${t.executable}"
                if (t.isOn) success(text) else error(text)
            }
        }
    }

    @CommandLine.Command(
        name = "status",
        description = ["Shows the telescope's status"],
    )
    fun status() {
        chooseDriver().also {
            val online = it.isOn
            val slewing = it.isSlewing
            val parked = it.isParked
            val tracking = it.isTracking
            val trackMode = it.trackMode()
            val coordinates = it.coordinates()

            info(
                """
                ONLINE: $online
                SLEWING: $slewing
                PARKED: $parked
                TRACKING: $tracking
                TRACK MODE: $trackMode
                RA/AZ: ${coordinates.first}
                DEC/ALT: ${coordinates.second}
                """.trimIndent()
            )
        }
    }

    @CommandLine.Command(
        name = "info",
        description = ["Shows the telescopes' info"],
    )
    fun info() {
        astrum.client?.telescopes()?.let {
            if (it.isNotEmpty()) {
                for (i in it.indices) {
                    val t = it[i]

                    val text = "[$i] ${t.name}:${t.executable}"
                    if (t.isOn) success(text) else error(text)

                    val sr = t.slewRate()
                    val rates = t.slewRates().joinToString { a -> if (a == sr) "[$a]" else "$a" }
                    val (raRate, decRate) = t.trackRate()
                    val canGuide = t.canGuide()
                    val canPark = t.canPark()
                    val (raPark, decPark) = t.parkPosition()
                    val dateTime = t.dateTime()

                    info(
                        """
                        CAN GUIDE: $canGuide
                        CAN PARK: $canPark    
                        SLEW RATES: $rates
                        TRACK RATE (RA/DEC): $raRate/$decRate
                        PARK POSITION (RA/DEC): $raPark/$decPark
                        DATE TIME: $dateTime
                        """.trimIndent()
                    )
                }

                return
            }
        }

        info("[INFO] No available telescopes")
    }

    @CommandLine.Command(
        name = "config",
        description = ["Configs the telescope"],
    )
    fun config(
        @CommandLine.Option(
            names = ["--trackmode"],
            description = ["Sets the track mode (SIDEREAL, SOLAR, LUNAR or CUSTOM)"],
        )
        trackMode: String? = null,
        @CommandLine.Option(
            names = ["--datetime"],
            description = ["Sets the UTC Date & Offset. Use NOW for get the current date"],
        )
        dateTime: String? = null,
    ) {
        chooseDriver().also {
            if (trackMode != null) it.trackMode(TrackMode.valueOf(trackMode))
            if (!dateTime.isNullOrEmpty()) {
                if (dateTime == "NOW") it.dateTime(ZonedDateTime.now())
                else it.dateTime(ZonedDateTime.parse(dateTime))
            }
        }
    }

    override fun call() = 0
}