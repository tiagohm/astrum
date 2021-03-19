package br.tiagohm.astrum.cli.commands

import br.tiagohm.astrum.indi.drivers.Driver
import java.util.concurrent.Callable

interface Command : Callable<Int> {

    override fun call() = 0

    interface Driverable<D : Driver> : Command {

        fun driver(index: Int): D
    }
}