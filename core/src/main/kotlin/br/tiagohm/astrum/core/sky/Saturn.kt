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

    init {
        setRotation(
            0.44400925923884945092, // 360.0 / W1
            358.922, // Offset
            Consts.J2000, // Epoch
            0.4896026430986047, // Obliquity
            2.9588132951645223, // Ascending Node
            38.90, // W0
            810.7939024, // W1
        )

        siderealPeriod = 10760.0
        absoluteMagnitude = -8.88
    }

    override fun computePosition(jde: Double): Pair<Triad, Triad> {
        val xyz = computePlanetHeliocentricCoordinates(jde, 5)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }
}