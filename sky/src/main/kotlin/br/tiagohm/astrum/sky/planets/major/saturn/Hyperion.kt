package br.tiagohm.astrum.sky.planets.major.saturn

import br.tiagohm.astrum.sky.PlanetType
import br.tiagohm.astrum.sky.core.math.Triad
import br.tiagohm.astrum.sky.core.units.angle.Radians
import br.tiagohm.astrum.sky.core.units.distance.Kilometer
import br.tiagohm.astrum.sky.planets.Planet

class Hyperion(parent: Saturn) :
    Planet(
        "Hyperion",
        Kilometer(135.0),
        0.0,
        0.3,
        null,
        PlanetType.MOON,
        parent
    ) {

    override val siderealDay = 21.2766088

    override val siderealPeriod = siderealDay

    override val absoluteMagnitude = 4.63

    override fun computeRotObliquity(jde: Double) = Radians(1.06465084371654104192)

    override fun computeRotAscendingNode() = Radians(2.53072741539177788654)

    override fun computePosition(jde: Double): Pair<Triad, Triad> {
        val xyz = computeTass17HeliocentricCoordinates(jde, 7)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }
}