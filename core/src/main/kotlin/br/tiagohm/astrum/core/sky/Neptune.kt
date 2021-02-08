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

    init {
        setRotation(
            0.671249999952453125, // 360.0 / W1
            153.65, // Offset
            Consts.J2000, // Epoch
            0.489152978736078, // Obliquity
            0.8593144058841349, // Ascending Node
            253.18, // W0
            536.3128492, // W1
        )

        siderealPeriod = 60189.0
        absoluteMagnitude = -6.87
    }

    override fun computePosition(jde: Double): Pair<Triad, Triad> {
        val xyz = computePlanetHeliocentricCoordinates(jde, 7)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }
}