package br.tiagohm.astrum.sky.planets.major.mars

import br.tiagohm.astrum.sky.PlanetType
import br.tiagohm.astrum.sky.core.math.Triad
import br.tiagohm.astrum.sky.core.time.JulianDay
import br.tiagohm.astrum.sky.core.units.angle.Radians
import br.tiagohm.astrum.sky.core.units.distance.Kilometer
import br.tiagohm.astrum.sky.planets.Planet

class Phobos(parent: Mars) : Planet(
    Kilometer(11.08),
    1.0,
    0.07,
    null,
    PlanetType.MOON,
    parent,
) {

    override val siderealDay = 360 / 1128.84475928

    override val siderealPeriod = 0.319

    override val absoluteMagnitude = 11.8

    override fun computeRotObliquity(jde: JulianDay) = Radians(0.4662456205433312)

    override fun computeRotAscendingNode() = Radians(1.4468158007959493)

    override fun computePosition(jde: JulianDay): Pair<Triad, Triad> {
        val xyz = computeMarsSatHeliocentricCoordinates(jde, 0)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }
}