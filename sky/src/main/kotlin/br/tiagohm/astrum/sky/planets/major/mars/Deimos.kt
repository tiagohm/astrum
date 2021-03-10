package br.tiagohm.astrum.sky.planets.major.mars

import br.tiagohm.astrum.sky.PlanetType
import br.tiagohm.astrum.sky.core.math.Triad
import br.tiagohm.astrum.sky.core.time.JulianDay
import br.tiagohm.astrum.sky.core.units.angle.Radians
import br.tiagohm.astrum.sky.core.units.distance.Kilometer
import br.tiagohm.astrum.sky.planets.Planet

class Deimos(parent: Mars) : Planet(
    Kilometer(6.2),
    1.0,
    0.08,
    null,
    PlanetType.MOON,
    parent,
) {

    override val siderealDay = 360 / 285.16188899

    override val siderealPeriod = 1.263

    override val absoluteMagnitude = 12.89

    override fun computeRotObliquity(jde: JulianDay) = Radians(0.4510749586748628)

    override fun computeRotAscendingNode() = Radians(1.4450075283699964)

    override fun computePosition(jde: JulianDay): Pair<Triad, Triad> {
        val xyz = computeMarsSatHeliocentricCoordinates(jde, 1)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }
}