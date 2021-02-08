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

    init {
        setRotation(
            -0.71833333334397530864, // 360.0 / W1
            331.18, // Offset
            Consts.J2000, // Epoch
            1.4360256624251349, // Obliquity
            -0.21560564768092383, // Ascending Node
            203.81, // W0
            -501.1600928, // W1
        )

        siderealPeriod = 30685.0
        absoluteMagnitude = -7.19
    }

    override val siderealDay: Double
        get() = computeMeanSolarDay(true)

    override fun computePosition(jde: Double): Pair<Triad, Triad> {
        val xyz = computePlanetHeliocentricCoordinates(jde, 6)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }
}