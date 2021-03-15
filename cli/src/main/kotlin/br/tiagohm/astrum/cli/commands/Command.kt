package br.tiagohm.astrum.cli.commands

import br.tiagohm.astrum.cli.sexagesimal
import br.tiagohm.astrum.indi.drivers.Driver
import picocli.CommandLine
import java.time.ZonedDateTime
import java.util.concurrent.Callable

interface Command : Callable<Int> {

    override fun call() = 0

    interface Driverable<D : Driver> : Command {

        val driver: D
    }

    interface Configurable<D : Driver> : Driverable<D> {

        @CommandLine.Command(
            name = "datetime",
            description = ["Sets the UTC Date & Offset. Use NOW for get the current date"],
            subcommands = [CommandLine.HelpCommand::class],
        )
        fun dateTime(
            @CommandLine.Parameters(
                index = "0",
                description = ["The UTC Date & Offset in ISO 8601 format"],
            )
            date: String
        ) {
            if (date == "NOW") driver.dateTime(ZonedDateTime.now())
            else driver.dateTime(ZonedDateTime.parse(date))
        }

        @CommandLine.Command(
            name = "location",
            description = ["Sets the Earth geodetic coordinate"],
            subcommands = [CommandLine.HelpCommand::class],
        )
        fun location(
            @CommandLine.Option(
                names = ["--lat"],
                description = ["Site latitude (-90 to +90), degrees +N"],
            )
            latitude: String? = null,
            @CommandLine.Option(
                names = ["--lon"],
                description = ["Site longitude (0 to 360), degrees +E"],
            )
            longitude: String? = null,
            @CommandLine.Option(
                names = ["-e"],
                description = ["Site elevation, meters"],
            )
            elevation: String? = null,
        ) {
            driver.location(
                longitude?.sexagesimal(),
                latitude?.sexagesimal(),
                elevation?.sexagesimal(),
            )
        }
    }
}