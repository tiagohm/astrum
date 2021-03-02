package br.tiagohm.astrum.sky.planets.major.saturn

import br.tiagohm.astrum.sky.PlanetType
import br.tiagohm.astrum.sky.core.math.Triad
import br.tiagohm.astrum.sky.core.units.angle.Radians
import br.tiagohm.astrum.sky.core.units.distance.Kilometer
import br.tiagohm.astrum.sky.planets.Planet

class Rhea(parent: Saturn) :
    Planet(
        "Rhea",
        Kilometer(763.5),
        0.0,
        0.6,
        null,
        PlanetType.MOON,
        parent
    ) {

    override val siderealDay = 4.51750262345808255369

    override val siderealPeriod = siderealDay

    override val absoluteMagnitude = 0.1

    override fun computeRotObliquity(jde: Double) = Radians(0.4891649953832684)

    override fun computeRotAscendingNode() = Radians(2.9584560923466263)

    override fun computePosition(jde: Double): Pair<Triad, Triad> {
        val xyz = computeTass17HeliocentricCoordinates(jde, 4)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }
}