package br.tiagohm.astrum.sky

import br.tiagohm.astrum.sky.core.units.angle.Angle
import br.tiagohm.astrum.sky.core.units.distance.Distance
import br.tiagohm.astrum.sky.core.units.distance.Meter

data class Location(
    val name: String,
    val latitude: Angle,
    val longitude: Angle,
    val altitude: Distance = Meter.ZERO,
)
