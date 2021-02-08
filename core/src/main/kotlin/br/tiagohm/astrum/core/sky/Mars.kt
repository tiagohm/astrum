package br.tiagohm.astrum.core.sky

import br.tiagohm.astrum.core.Consts
import br.tiagohm.astrum.core.math.Triad

class Mars(parent: Sun) : Planet(
    "Mars",
    3396.19 / Consts.AU,
    0.005886,
    0.150,
    null,
    PlanetType.PLANET,
    parent,
) {

    override val siderealDay = 1.02595675596028993319

    override val siderealPeriod = 686.971

    override val absoluteMagnitude = -1.52

    override fun computePosition(jde: Double): Pair<Triad, Triad> {
        val xyz = computePlanetHeliocentricCoordinates(jde, 3)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }
}