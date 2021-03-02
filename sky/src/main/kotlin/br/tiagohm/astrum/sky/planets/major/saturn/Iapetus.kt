package br.tiagohm.astrum.sky.planets.major.saturn

import br.tiagohm.astrum.sky.PlanetType
import br.tiagohm.astrum.sky.core.math.Triad
import br.tiagohm.astrum.sky.core.units.angle.Radians
import br.tiagohm.astrum.sky.core.units.distance.Kilometer
import br.tiagohm.astrum.sky.planets.Planet

class Iapetus(parent: Saturn) :
    Planet(
        "Iapetus",
        Kilometer(734.3),
        0.0,
        0.2,
        null,
        PlanetType.MOON,
        parent
    ) {

    override val siderealDay = 79.33084957257860431121

    override val siderealPeriod = siderealDay

    override val absoluteMagnitude = 1.5

    override fun computeRotObliquity(jde: Double) = Radians(0.3015265531175175)

    override fun computeRotAscendingNode() = Radians(2.4366181933137536)

    override fun computePosition(jde: Double): Pair<Triad, Triad> {
        val xyz = computeTass17HeliocentricCoordinates(jde, 6)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }
}