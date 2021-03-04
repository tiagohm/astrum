package br.tiagohm.astrum.sky.planets.major.uranus

import br.tiagohm.astrum.sky.PlanetType
import br.tiagohm.astrum.sky.core.math.Triad
import br.tiagohm.astrum.sky.core.time.JulianDay
import br.tiagohm.astrum.sky.core.units.angle.Radians
import br.tiagohm.astrum.sky.core.units.distance.Kilometer
import br.tiagohm.astrum.sky.planets.Planet

class Ariel(parent: Uranus) :
    Planet(
        "Ariel",
        Kilometer(578.9),
        0.0,
        0.39,
        null,
        PlanetType.MOON,
        parent
    ) {

    override val siderealDay = -2.5203788716692395966

    override val siderealPeriod = siderealDay

    override val absoluteMagnitude = 1.45

    override fun computeRotObliquity(jde: JulianDay) = Radians(1.434545736666446)

    override fun computeRotAscendingNode() = Radians(-0.21370599450540695)

    override fun computePosition(jde: JulianDay): Pair<Triad, Triad> {
        val xyz = computeGust86HeliocentricCoordinates(jde, 1)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }
}