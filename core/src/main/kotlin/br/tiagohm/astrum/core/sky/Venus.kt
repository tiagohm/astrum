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

    init {
        setRotation(
            -243.01848398589196694301, // 360.0 / W1
            137.45, // Offset
            Consts.J2000, // Epoch
            0.021624851729521666, // Obliquity
            2.097642769084066, // Ascending Node
            160.20, // W0
        )

        siderealPeriod = 224.70
        absoluteMagnitude = -5.18
    }

    override val siderealDay: Double
        get() = computeMeanSolarDay(true)

    override fun computePosition(jde: Double): Pair<Triad, Triad> {
        val xyz = computePlanetHeliocentricCoordinates(jde, 1)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }
}