package br.tiagohm.astrum.core.sky

import br.tiagohm.astrum.core.Consts
import br.tiagohm.astrum.core.math.Triad

class Saturn(parent: Sun) : Planet(
    "Saturn",
    60268.0 / Consts.AU,
    0.09796243446,
    0.50,
    null,
    PlanetType.PLANET,
    parent,
) {

    override val siderealDay = 0.44400925923884945092

    override val siderealPeriod = 10760.0

    override val absoluteMagnitude = -8.88

    override fun computePosition(jde: Double): Pair<Triad, Triad> {
        val xyz = computePlanetHeliocentricCoordinates(jde, 5)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }

    override fun computeRotObliquity(jde: Double) = 0.4896026430986047
}