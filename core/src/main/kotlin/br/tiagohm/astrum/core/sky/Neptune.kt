package br.tiagohm.astrum.core.sky

import br.tiagohm.astrum.core.Consts
import br.tiagohm.astrum.core.math.Triad

class Neptune(parent: Sun) : Planet(
    "Neptune",
    24764.0 / Consts.AU,
    0.01708124697,
    0.62,
    null,
    PlanetType.PLANET,
    parent,
) {

    override val siderealDay = 0.671249999952453125

    override val siderealPeriod = 60189.0

    override val absoluteMagnitude = -6.87

    override fun computePosition(jde: Double): Pair<Triad, Triad> {
        val xyz = computePlanetHeliocentricCoordinates(jde, 7)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }
}