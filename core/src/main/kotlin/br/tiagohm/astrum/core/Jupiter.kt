package br.tiagohm.astrum.core

class Jupiter(parent: Sun) : Planet(
    "Jupiter",
    71492.0 / AU,
    0.064874,
    0.51,
    null,
    PlanetType.PLANET,
    parent,
) {

    override val siderealDay = 0.41366472474059774553

    override val siderealPeriod = 4331.87

    override val absoluteMagnitude = -9.40

    override val meanOppositionMagnitude = -2.7

    override fun computePosition(jde: Double): Pair<Triad, Triad> {
        val xyz = computePlanetHeliocentricCoordinates(jde, 4)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }

    override fun computeRotObliquity(jde: Double) = 0.03868532751568998

    override fun computeVisualMagnitude(
        o: Observer,
        phaseAngle: Radians,
        cosChi: Double,
        observerRq: Double,
        planetRq: Double,
        observerPlanetRq: Double,
        d: Double,
        shadowFactor: Double,
    ): Double {
        val phaseDeg = phaseAngle.deg

        return when (o.apparentMagnitudeAlgorithm) {
            ApparentMagnitudeAlgorithm.EXPLANATORY_SUPPLEMENT_2013,
            ApparentMagnitudeAlgorithm.ASTRONOMICAL_ALMANAC_1984 -> {
                -9.40 + d + 0.005 * phaseDeg
            }
            ApparentMagnitudeAlgorithm.EXPLANATORY_SUPPLEMENT_1992 -> {
                -9.25 + d + 0.005 * phaseDeg
            }
            ApparentMagnitudeAlgorithm.MUELLER_1893 -> -8.93 + d
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