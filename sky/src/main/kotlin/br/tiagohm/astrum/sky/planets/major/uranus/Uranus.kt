package br.tiagohm.astrum.sky.planets.major.uranus

import br.tiagohm.astrum.sky.Observer
import br.tiagohm.astrum.sky.PlanetType
import br.tiagohm.astrum.sky.core.math.Triad
import br.tiagohm.astrum.sky.core.time.JulianDay
import br.tiagohm.astrum.sky.core.units.angle.Angle
import br.tiagohm.astrum.sky.core.units.angle.Radians
import br.tiagohm.astrum.sky.core.units.distance.Kilometer
import br.tiagohm.astrum.sky.planets.ApparentMagnitudeAlgorithm
import br.tiagohm.astrum.sky.planets.Planet
import br.tiagohm.astrum.sky.planets.Ring
import br.tiagohm.astrum.sky.planets.Sun

class Uranus(parent: Sun) : Planet(
    Kilometer(25559.0),
    0.0229273446,
    0.66,
    null,
    PlanetType.PLANET,
    parent,
    Ring(Kilometer(26840.0), Kilometer(97700.0)),
) {

    override val siderealDay = -0.71833333334397530864

    override val siderealPeriod = 30685.0

    override val absoluteMagnitude = -7.19

    override val meanOppositionMagnitude = 5.52

    override val mass = 1.0 / 22902.981613

    override fun computePosition(jde: JulianDay): Pair<Triad, Triad> {
        val xyz = computePlanetHeliocentricCoordinates(jde, 6)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }

    override fun computeRotObliquity(jde: JulianDay) = Radians(1.4360256624251349)

    override fun computeRotAscendingNode() = Radians(-0.21560564768092383)

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
            ApparentMagnitudeAlgorithm.EXPLANATORY_SUPPLEMENT_2013 -> {
                -7.19 + d + 0.002 * phaseDeg
            }
            ApparentMagnitudeAlgorithm.EXPLANATORY_SUPPLEMENT_1992 -> {
                -7.19 + d + 0.0028 * phaseDeg
            }
            ApparentMagnitudeAlgorithm.MUELLER_1893 -> -6.85 + d
            ApparentMagnitudeAlgorithm.ASTRONOMICAL_ALMANAC_1984 -> -7.19 + d
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