package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.client.Client
import br.tiagohm.astrum.indi.drivers.BaseDriver
import br.tiagohm.astrum.indi.drivers.focuser.Focuser
import br.tiagohm.astrum.indi.drivers.guider.Guider

class CelestronGPS(client: Client, name: String, executable: String) :
    BaseDriver(client, name, executable),
    Telescope,
    Guider,
    Focuser {

    override fun initialize() {
        register(CelestronTrackMode("ALTAZ"))
        register(CelestronTrackMode("EQ_N"))
        register(CelestronTrackMode("EQ_S"))
        register(CelestronTrackMode("RA_DEC"))
        register(TrackMode("SIDEREAL"))
        register(TrackMode("SOLAR"))
        register(TrackMode("LUNAR"))
        register(DstState::class)
        register(Hibernate::class)
        // TODO: PEC & Alignment

        super<Guider>.initialize()
        super<Telescope>.initialize()
        super<BaseDriver>.initialize()
    }

    override fun trackModes(): List<TrackMode> {
        val modes = ArrayList<TrackMode>(8)
        modes.addAll(super.trackModes())
        modes.addAll(client.elements(name, "CELESTRON_TRACK_MODE").map { it.element as TrackMode })
        return modes
    }

    val isHibernating: Boolean
        get() = switch(Hibernate.ENABLE)

    fun hibernate(enabled: Boolean) = send(if (enabled) Hibernate.ENABLE else Hibernate.DISABLE, true)
}