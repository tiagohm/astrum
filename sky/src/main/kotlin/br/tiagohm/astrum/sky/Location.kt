package br.tiagohm.astrum.sky

import br.tiagohm.astrum.sky.core.units.Degrees

data class Location(
    val name: String,
    val latitude: Degrees,
    val longitude: Degrees,
    val altitude: Double = 0.0,
)
