package br.tiagohm.astrum.sky.planets.major.uranus

import br.tiagohm.astrum.sky.PlanetType
import br.tiagohm.astrum.sky.core.math.Triad
import br.tiagohm.astrum.sky.core.time.JulianDay
import br.tiagohm.astrum.sky.core.units.angle.Radians
import br.tiagohm.astrum.sky.core.units.distance.Kilometer
import br.tiagohm.astrum.sky.planets.Planet

class Titania(parent: Uranus) :
    Planet(
        "Titania",
        Kilometer(788.9),
        0.0,
        0.27,
        null,
        PlanetType.MOON,
        parent
    ) {

    override val siderealDay = -8.70586545787208005635

    override val siderealPeriod = siderealDay

    override val absoluteMagnitude = 1.02

    override fun computeRotObliquity(jde: JulianDay) = Radians(1.434545736666446)

    override fun computeRotAscendingNode() = Radians(-0.21370599450540695)

    override fun computePosition(jde: JulianDay): Pair<Triad, Triad> {
        val xyz = computeGust86HeliocentricCoordinates(jde, 3)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }
}