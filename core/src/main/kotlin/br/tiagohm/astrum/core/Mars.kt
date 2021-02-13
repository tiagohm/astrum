package br.tiagohm.astrum.core

class Mars(parent: Sun) : Planet(
    "Mars",
    3396.19 / AU,
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

    override fun computeRotObliquity(jde: Double) = 0.44338065731385523

    override fun computeVisualMagnitude(
        o: Observer,
        phaseAngle: Double,
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