package br.tiagohm.astrum.core.sky

import br.tiagohm.astrum.core.Consts
import br.tiagohm.astrum.core.math.Triad

class Uranus(parent: Sun) : Planet(
    "Uranus",
    25559.0 / Consts.AU,
    0.0229273446,
    0.66,
    null,
    PlanetType.PLANET,
    parent,
) {

    override val siderealDay = -0.71833333334397530864

    override val siderealPeriod = 30685.0

    override val absoluteMagnitude = -7.19

    override fun computePosition(jde: Double): Pair<Triad, Triad> {
        val xyz = computePlanetHeliocentricCoordinates(jde, 6)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }
}