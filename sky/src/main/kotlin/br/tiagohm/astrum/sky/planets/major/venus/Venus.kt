package br.tiagohm.astrum.sky.planets.major.venus

import br.tiagohm.astrum.sky.Observer
import br.tiagohm.astrum.sky.PlanetType
import br.tiagohm.astrum.sky.core.math.Triad
import br.tiagohm.astrum.sky.core.units.angle.Angle
import br.tiagohm.astrum.sky.core.units.angle.Radians
import br.tiagohm.astrum.sky.core.units.distance.Kilometer
import br.tiagohm.astrum.sky.planets.ApparentMagnitudeAlgorithm
import br.tiagohm.astrum.sky.planets.Planet
import br.tiagohm.astrum.sky.planets.Sun

class Venus(parent: Sun) : Planet(
    "Venus",
    Kilometer(6051.8).au,
    0.0,
    0.77,
    null,
    PlanetType.PLANET,
    parent,
) {

    override val siderealDay = -243.01848398589196694301

    override val siderealPeriod = 224.70

    override val absoluteMagnitude = -5.18

    override fun computePosition(jde: Double): Pair<Triad, Triad> {
        val xyz = computePlanetHeliocentricCoordinates(jde, 1)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }

    override fun computeRotObliquity(jde: Double) = Radians(0.021624851729521666)

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
                // There are two regions strongly enclosed per phaseDeg (2.2..163.6..170.2). However, we must deliver a solution for every case.
                // GZ: The model seems flawed. See https://sourceforge.net/p/stellarium/discussion/278769/thread/b7cab45f62/?limit=25#907d
                // In this case, it seems better to deviate from the paper and --- only for the inferior conjunction --
                // Use a more modern value from Mallama&Hilton, https://doi.org/10.1016/j.ascom.2018.08.002
                // The reversal and intermediate peak is real and due to forward scattering on sulphur acide droplets.
                return if (phaseDeg < 163.6)
                    -4.47 + d + ((0.13E-6 * phaseDeg + 0.000057) * phaseDeg + 0.0103) * phaseDeg
                else
                    236.05828 + d - 2.81914 * phaseDeg + 8.39034E-3 * phaseDeg * phaseDeg
            }
            ApparentMagnitudeAlgorithm.EXPLANATORY_SUPPLEMENT_1992 -> {
                val f1 = phaseDeg / 100.0
                -4.29 + d + 0.09 * f1 + 2.39 * f1 * f1 - 0.65 * f1 * f1 * f1
            }
            ApparentMagnitudeAlgorithm.MUELLER_1893 -> {
                -4.00 + d + 0.01322 * phaseDeg + 0.0000004247 * phaseDeg * phaseDeg * phaseDeg
            }
            ApparentMagnitudeAlgorithm.ASTRONOMICAL_ALMANAC_1984 -> {
                -4.40 + d + 0.0009 * phaseDeg + 0.000239 * phaseDeg * phaseDeg - 0.00000065 * phaseDeg * phaseDeg * phaseDeg
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