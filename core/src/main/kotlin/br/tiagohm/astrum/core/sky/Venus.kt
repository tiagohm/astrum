package br.tiagohm.astrum.core.sky

import br.tiagohm.astrum.core.Consts
import br.tiagohm.astrum.core.math.Triad

class Venus(parent: Sun) : Planet(
    "Venus",
    6051.8 / Consts.AU,
    0.0,
    0.77,
    null,
    PlanetType.PLANET,
    parent,
) {

    override val siderealDay = -243.01848398589196694301

    override val siderealPeriod = 224.70

    override val absoluteMagnitude = -5.18

    override fun computePosition(jde: Double): Pair<Triad, Triad> {
        val xyz = computePlanetHeliocentricCoordinates(jde, 1)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }
}