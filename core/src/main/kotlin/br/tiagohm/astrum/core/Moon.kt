package br.tiagohm.astrum.core

// TODO: Lunar phase names, libration, or axis orientation/rotation data
class Moon(parent: Earth) : Planet(
    "Moon",
    1737.4 / AU,
    0.0,
    0.12,
    null,
    PlanetType.MOON,
    parent,
) {

    override val siderealDay = 27.32166171424233789516

    override val siderealPeriod = siderealDay

    override val absoluteMagnitude = 0.21

    override val meanOppositionMagnitude = -12.74

    override fun computePosition(jde: Double): Pair<Triad, Triad> {
        val xyz = computeMoonHeliocentricCoordinates(jde)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }

    override fun computeRotObliquity(jde: Double) = 3.7723828609181886E-4
}