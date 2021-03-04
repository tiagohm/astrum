package br.tiagohm.astrum.sky.planets.major.saturn

import br.tiagohm.astrum.sky.PlanetType
import br.tiagohm.astrum.sky.core.math.Triad
import br.tiagohm.astrum.sky.core.time.JulianDay
import br.tiagohm.astrum.sky.core.units.angle.Radians
import br.tiagohm.astrum.sky.core.units.distance.Kilometer
import br.tiagohm.astrum.sky.planets.Planet

class Dione(parent: Saturn) :
    Planet(
        "Dione",
        Kilometer(561.4),
        0.0,
        0.6,
        null,
        PlanetType.MOON,
        parent
    ) {

    override val siderealDay = 2.73691555255273345199

    override val siderealPeriod = siderealDay

    override val absoluteMagnitude = 0.8

    override fun computeRotObliquity(jde: JulianDay) = Radians(0.4899198175355082)

    override fun computeRotAscendingNode() = Radians(2.9586361818973743)

    override fun computePosition(jde: JulianDay): Pair<Triad, Triad> {
        val xyz = computeTass17HeliocentricCoordinates(jde, 3)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }
}