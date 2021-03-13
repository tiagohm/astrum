package br.tiagohm.astrum.sky.planets

import br.tiagohm.astrum.common.units.distance.Distance

data class Ring(
    val min: Distance,
    val max: Distance,
) {

    val size = max.au
}
