package br.tiagohm.astrum.sky.planets.major.uranus

import br.tiagohm.astrum.sky.PlanetType
import br.tiagohm.astrum.sky.core.math.Triad
import br.tiagohm.astrum.sky.core.units.angle.Radians
import br.tiagohm.astrum.sky.core.units.distance.Kilometer
import br.tiagohm.astrum.sky.planets.Planet

class Oberon(parent: Uranus) :
    Planet(
        "Oberon",
        Kilometer(761.4),
        0.0,
        0.23,
        null,
        PlanetType.MOON,
        parent
    ) {

    override val siderealDay = -13.46323198077665884857

    override val siderealPeriod = siderealDay

    override val absoluteMagnitude = 1.23

    override fun computeRotObliquity(jde: Double) = Radians(1.434545736666446)

    override fun computeRotAscendingNode() = Radians(-0.21370599450540695)

    override fun computePosition(jde: Double): Pair<Triad, Triad> {
        val xyz = computeGust86HeliocentricCoordinates(jde, 4)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }
}