package br.tiagohm.astrum.sky.planets

import br.tiagohm.astrum.sky.core.units.distance.Distance

data class Ring(
    val radiusMin: Distance,
    val radiusMax: Distance,
) {

    val size = radiusMax
}
