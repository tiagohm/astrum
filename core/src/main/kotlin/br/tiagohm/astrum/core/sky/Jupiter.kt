package br.tiagohm.astrum.core.sky

import br.tiagohm.astrum.core.Consts
import br.tiagohm.astrum.core.math.Triad

class Jupiter(parent: Sun) : Planet(
    "Jupiter",
    71492.0 / Consts.AU,
    0.064874,
    0.51,
    null,
    PlanetType.PLANET,
    parent,
) {

    override val siderealDay = 0.41366472474059774553

    override val siderealPeriod = 4331.87

    override val absoluteMagnitude = -9.40

    override fun computePosition(jde: Double): Pair<Triad, Triad> {
        val xyz = computePlanetHeliocentricCoordinates(jde, 4)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }
}