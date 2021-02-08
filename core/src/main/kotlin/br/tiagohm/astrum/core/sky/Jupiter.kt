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

    init {
        setRotation(
            0.41366472474059774553, // 360.0 / W1
            -1.0, // Offset
            Consts.J2000, // Epoch
            0.03868532751568998, // Obliquity
            -0.3871470026094814, // Ascending Node
            43.3, // W0
            870.270, // W1
        )

        siderealPeriod = 4331.87
        absoluteMagnitude = -9.40
    }

    override fun computePosition(jde: Double): Pair<Triad, Triad> {
        val xyz = computePlanetHeliocentricCoordinates(jde, 4)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }
}