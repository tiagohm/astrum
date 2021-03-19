package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.client.Client
import br.tiagohm.astrum.indi.drivers.BaseDriver

class DSC(client: Client, name: String, executable: String) :
    BaseDriver(client, name, executable),
    Telescope {

    override fun initialize() {
        register(MountType.ALTAZ)
        register(MountType.EQUATORIAL)
        register(AxisEncoder::class)
        register(AxisRange::class)
        register(AxisReverse::class)

        super<Telescope>.initialize()
        super<BaseDriver>.initialize()
    }

    fun ticks(axis1: Double, axis2: Double) = send(
        arrayOf(AxisEncoder.AXIS1_TICKS, AxisEncoder.AXIS2_TICKS),
        arrayOf(axis1, axis2),
    )

    fun ticks() = number(AxisEncoder.AXIS1_TICKS) to number(AxisEncoder.AXIS2_TICKS)

    fun reverse(axis1: Boolean, axis2: Boolean) = send(
        arrayOf(AxisReverse.AXIS1, AxisReverse.AXIS2),
        arrayOf(axis1, axis2),
    )

    val isAxis1Reversed: Boolean
        get() = switch(AxisReverse.AXIS1)

    val isAxis2Reversed: Boolean
        get() = switch(AxisReverse.AXIS2)

    fun range(range: AxisRange) = send(range, true)

    val isHalfStep: Boolean
        get() = switch(AxisRange.HALF_STEP)

    val isFullStep: Boolean
        get() = switch(AxisRange.FULL_STEP)
}