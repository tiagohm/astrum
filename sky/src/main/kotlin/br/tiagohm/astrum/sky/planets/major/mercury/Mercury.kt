package br.tiagohm.astrum.sky.planets.major.mercury

import br.tiagohm.astrum.sky.Observer
import br.tiagohm.astrum.sky.PlanetType
import br.tiagohm.astrum.sky.core.math.Triad
import br.tiagohm.astrum.sky.core.time.JulianDay
import br.tiagohm.astrum.sky.core.units.angle.Angle
import br.tiagohm.astrum.sky.core.units.angle.Radians
import br.tiagohm.astrum.sky.core.units.distance.Kilometer
import br.tiagohm.astrum.sky.planets.ApparentMagnitudeAlgorithm
import br.tiagohm.astrum.sky.planets.Planet
import br.tiagohm.astrum.sky.planets.Sun

class Mercury(parent: Sun) : Planet(
    "Mercury",
    Kilometer(2440.53),
    0.0009301258,
    0.06,
    null,
    PlanetType.PLANET,
    parent,
) {

    override val siderealDay = 58.64614590235794649087

    override val siderealPeriod = 87.97

    override val absoluteMagnitude = -0.60

    override val mass = 1.0 / 6023682.155592

    override fun computePosition(jde: JulianDay): Pair<Triad, Triad> {
        val xyz = computePlanetHeliocentricCoordinates(jde, 0)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }

    override fun computeRotObliquity(jde: JulianDay) = Radians(0.1228178112752234)

    override fun computeRotAscendingNode() = Radians(0.8418651386288667)

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
                -0.6 + d + (((3.02E-6 * phaseDeg - 0.000488) * phaseDeg + 0.0498) * phaseDeg)
            }
            ApparentMagnitudeAlgorithm.EXPLANATORY_SUPPLEMENT_1992 -> {
                var f1 = phaseDeg / 100.0
                if (phaseDeg > 150.0) f1 = 1.5
                -0.36 + d + 3.8 * f1 - 2.73 * f1 * f1 + 2 * f1 * f1 * f1
            }
            ApparentMagnitudeAlgorithm.MUELLER_1893 -> {
                val ph50 = phaseDeg - 50.0
                1.16 + d + 0.02838 * ph50 + 0.0001023 * ph50 * ph50
            }
            ApparentMagnitudeAlgorithm.ASTRONOMICAL_ALMANAC_1984 -> {
                -0.42 + d + .038 * phaseDeg - 0.000273 * phaseDeg * phaseDeg + 0.000002 * phaseDeg * phaseDeg * phaseDeg
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