package br.tiagohm.astrum.sky.planets.major.neptune

import br.tiagohm.astrum.sky.Observer
import br.tiagohm.astrum.sky.PlanetType
import br.tiagohm.astrum.sky.core.math.Triad
import br.tiagohm.astrum.sky.core.units.angle.Angle
import br.tiagohm.astrum.sky.core.units.angle.Radians
import br.tiagohm.astrum.sky.core.units.distance.Kilometer
import br.tiagohm.astrum.sky.planets.ApparentMagnitudeAlgorithm
import br.tiagohm.astrum.sky.planets.Planet
import br.tiagohm.astrum.sky.planets.Ring
import br.tiagohm.astrum.sky.planets.Sun

class Neptune(parent: Sun) : Planet(
    "Neptune",
    Kilometer(24764.0),
    0.01708124697,
    0.62,
    null,
    PlanetType.PLANET,
    parent,
    Ring(Kilometer(40900.0), Kilometer(62932.0)),
) {

    override val siderealDay = 0.671249999952453125

    override val siderealPeriod = 60189.0

    override val absoluteMagnitude = -6.87

    override val meanOppositionMagnitude = 7.84

    override fun computePosition(jde: Double): Pair<Triad, Triad> {
        val xyz = computePlanetHeliocentricCoordinates(jde, 7)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }

    override fun computeRotObliquity(jde: Double) = Radians(0.489152978736078)

    override fun computeRotAscendingNode() = Radians(0.8593144058841349)

    override fun computeVisualMagnitude(
        o: Observer,
        phaseAngle: Angle,
        cosChi: Double,
        observerRq: Double,
        planetRq: Double,
        observerPlanetRq: Double,
        d: Double,
        shadowFactor: Double,
    ): Double {
        return when (o.apparentMagnitudeAlgorithm) {
            ApparentMagnitudeAlgorithm.EXPLANATORY_SUPPLEMENT_2013,
            ApparentMagnitudeAlgorithm.EXPLANATORY_SUPPLEMENT_1992 -> -6.87 + d
            ApparentMagnitudeAlgorithm.MUELLER_1893 -> -7.05 + d
            ApparentMagnitudeAlgorithm.ASTRONOMICAL_ALMANAC_1984 -> -6.87 + d
            // Calculate the visual magnitude from phase angle and albedo of the planet
            else -> super.computeVisualMagnitude(
                o,
                phaseAngle,
                cosChi,
                observerRq,
                planetRq,
                observerPlanetRq,
                d,
                shadowFactor,
            )
        }
    }
}