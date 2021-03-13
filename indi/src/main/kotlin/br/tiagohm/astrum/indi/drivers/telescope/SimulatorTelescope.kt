package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.client.INDIConnection
import br.tiagohm.astrum.indi.drivers.DefaultDriver

class SimulatorTelescope(
    connection: INDIConnection,
) : DefaultDriver(connection, "Telescope Simulator", "indi_simulator_telescope"),
    TelescopeDriver