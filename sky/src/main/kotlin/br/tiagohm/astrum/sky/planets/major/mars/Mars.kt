package br.tiagohm.astrum.sky.planets.major.mars

import br.tiagohm.astrum.sky.Observer
import br.tiagohm.astrum.sky.PlanetType
import br.tiagohm.astrum.sky.core.math.Triad
import br.tiagohm.astrum.sky.core.units.angle.Angle
import br.tiagohm.astrum.sky.core.units.angle.Radians
import br.tiagohm.astrum.sky.core.units.distance.Kilometer
import br.tiagohm.astrum.sky.planets.ApparentMagnitudeAlgorithm
import br.tiagohm.astrum.sky.planets.Planet
import br.tiagohm.astrum.sky.planets.Sun

class Mars(parent: Sun) : Planet(
    "Mars",
    Kilometer(3396.19),
    0.005886,
    0.150,
    null,
    PlanetType.PLANET,
    parent,
) {

    override val siderealDay = 1.02595675596028993319

    override val siderealPeriod = 686.971

    override val absoluteMagnitude = -1.52

    override val meanOppositionMagnitude = -2.01

    override fun computePosition(jde: Double): Pair<Triad, Triad> {
        val xyz = computePlanetHeliocentricCoordinates(jde, 3)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }

    override fun computeRotObliquity(jde: Double) = Radians(0.44338065731385523)

    override fun computeRotAscendingNode() = Radians(1.4808002454424123)

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
        val phaseDeg = phaseAngle.degrees.value

        return when (o.apparentMagnitudeAlgorithm) {
            ApparentMagnitudeAlgorithm.EXPLANATORY_SUPPLEMENT_2013,
            ApparentMagnitudeAlgorithm.EXPLANATORY_SUPPLEMENT_1992,
            ApparentMagnitudeAlgorithm.ASTRONOMICAL_ALMANAC_1984 -> {
                -1.52 + d + 0.016 * phaseDeg
            }
            ApparentMagnitudeAlgorithm.MUELLER_1893 -> {
                -1.30 + d + 0.01486 * phaseDeg
            }
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