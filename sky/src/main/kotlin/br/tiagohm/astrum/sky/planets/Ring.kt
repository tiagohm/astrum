package br.tiagohm.astrum.sky.planets

import br.tiagohm.astrum.sky.core.units.distance.Distance

data class Ring(
    val min: Distance,
    val max: Distance,
) {

    val size = max.au
}
