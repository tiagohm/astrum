package br.tiagohm.astrum.sky.planets.major.uranus

import br.tiagohm.astrum.sky.PlanetType
import br.tiagohm.astrum.sky.core.math.Triad
import br.tiagohm.astrum.sky.core.time.JulianDay
import br.tiagohm.astrum.sky.core.units.angle.Radians
import br.tiagohm.astrum.sky.core.units.distance.Kilometer
import br.tiagohm.astrum.sky.planets.Planet

class Umbriel(parent: Uranus) : Planet(
    Kilometer(584.7),
    0.0,
    0.21,
    null,
    PlanetType.MOON,
    parent
) {

    override val siderealDay = -4.14417624616125098213

    override val siderealPeriod = siderealDay

    override val absoluteMagnitude = 2.10

    override fun computeRotObliquity(jde: JulianDay) = Radians(1.434545736666446)

    override fun computeRotAscendingNode() = Radians(-0.21370599450540695)

    override fun computePosition(jde: JulianDay): Pair<Triad, Triad> {
        val xyz = computeGust86HeliocentricCoordinates(jde, 2)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }
}