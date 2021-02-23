package br.tiagohm.astrum.sky.planets.major.mars

import br.tiagohm.astrum.sky.AU
import br.tiagohm.astrum.sky.PlanetType
import br.tiagohm.astrum.sky.algorithms.math.Triad
import br.tiagohm.astrum.sky.core.units.Radians
import br.tiagohm.astrum.sky.planets.Planet

class Phobos(parent: Mars) : Planet(
    "Phobos",
    11.08 / AU,
    1.0,
    0.07,
    null,
    PlanetType.MOON,
    parent,
) {

    override val siderealDay = 0.31891010437043201153

    override val siderealPeriod = 0.319

    override val absoluteMagnitude = 11.8

    override fun computeRotObliquity(jde: Double) = Radians(1.4468158007959493)

    override fun computePosition(jde: Double): Pair<Triad, Triad> {
        val xyz = computeMarsSatHeliocentricCoordinates(jde, 0)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }
}