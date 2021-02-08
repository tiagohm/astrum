package br.tiagohm.astrum.core.sky

import br.tiagohm.astrum.core.Consts
import br.tiagohm.astrum.core.math.Triad

class Mercury(parent: Sun) : Planet(
    "Mercury",
    2440.53 / Consts.AU,
    0.0009301258,
    0.06,
    null,
    PlanetType.PLANET,
    parent,
) {

    override val siderealDay = 58.64614590235794649087

    override val siderealPeriod = 87.97

    override val absoluteMagnitude = -0.60

    override fun computePosition(jde: Double): Pair<Triad, Triad> {
        val xyz = computePlanetHeliocentricCoordinates(jde, 0)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }
}