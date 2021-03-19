package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.client.Client
import br.tiagohm.astrum.indi.drivers.BaseDriver
import br.tiagohm.astrum.indi.drivers.guider.Guider

class TitanTCS(client: Client, name: String, executable: String) :
    BaseDriver(client, name, executable),
    Telescope,
    Guider {

    override fun initialize() {
        register(TrackMode.SIDEREAL)
        register(TrackMode.SOLAR)
        register(TrackMode.LUNAR)

        super<Guider>.initialize()
        super<Telescope>.initialize()
        super<BaseDriver>.initialize()
    }
}