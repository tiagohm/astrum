package br.tiagohm.astrum.indi.drivers.focuser

import br.tiagohm.astrum.indi.client.Client
import br.tiagohm.astrum.indi.drivers.BaseDriver

class FocuserSimulator(client: Client, name: String, executable: String) :
    BaseDriver(client, name, executable),
    Focuser {

    override fun initialize() {
        super<Focuser>.initialize()
        super<BaseDriver>.initialize()
    }
}