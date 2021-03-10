package br.tiagohm.astrum.sky.planets.major.uranus

import br.tiagohm.astrum.sky.PlanetType
import br.tiagohm.astrum.sky.core.math.Triad
import br.tiagohm.astrum.sky.core.time.JulianDay
import br.tiagohm.astrum.sky.core.units.angle.Radians
import br.tiagohm.astrum.sky.core.units.distance.Kilometer
import br.tiagohm.astrum.sky.planets.Planet

class Miranda(parent: Uranus) : Planet(
    Kilometer(235.8),
    0.0,
    0.32,
    null,
    PlanetType.MOON,
    parent
) {

    override val siderealDay = 360 / -254.6906892

    override val siderealPeriod = siderealDay

    override val absoluteMagnitude = 3.6

    override fun computeRotObliquity(jde: JulianDay) = Radians(1.4341980059987902)

    override fun computeRotAscendingNode() = Radians(-0.21373678221681214)

    override fun computePosition(jde: JulianDay): Pair<Triad, Triad> {
        val xyz = computeGust86HeliocentricCoordinates(jde, 0)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }
}