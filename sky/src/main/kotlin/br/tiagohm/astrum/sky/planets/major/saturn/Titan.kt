package br.tiagohm.astrum.sky.planets.major.saturn

import br.tiagohm.astrum.sky.PlanetType
import br.tiagohm.astrum.sky.core.math.Triad
import br.tiagohm.astrum.sky.core.units.angle.Radians
import br.tiagohm.astrum.sky.core.units.distance.Kilometer
import br.tiagohm.astrum.sky.planets.Planet

class Titan(parent: Saturn) :
    Planet(
        "Titan",
        Kilometer(2575.0),
        0.0,
        0.2,
        null,
        PlanetType.MOON,
        parent
    ) {

    override val siderealDay = 15.94544757648862889384

    override val siderealPeriod = siderealDay

    override val absoluteMagnitude = -1.28

    override fun computeRotObliquity(jde: Double) = Radians(0.48963429556444105)

    override fun computeRotAscendingNode() = Radians(2.952640371569349)

    override fun computePosition(jde: Double): Pair<Triad, Triad> {
        val xyz = computeTass17HeliocentricCoordinates(jde, 5)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }
}