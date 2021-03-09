package br.tiagohm.astrum.sky.planets.minor.pluto

import br.tiagohm.astrum.sky.M_PI_180
import br.tiagohm.astrum.sky.Observer
import br.tiagohm.astrum.sky.core.Algorithms
import br.tiagohm.astrum.sky.core.math.Triad
import br.tiagohm.astrum.sky.core.time.JulianDay
import br.tiagohm.astrum.sky.core.units.angle.Angle
import br.tiagohm.astrum.sky.core.units.angle.Radians
import br.tiagohm.astrum.sky.core.units.distance.AU
import br.tiagohm.astrum.sky.core.units.distance.Kilometer
import br.tiagohm.astrum.sky.planets.ApparentMagnitudeAlgorithm
import br.tiagohm.astrum.sky.planets.Sun
import br.tiagohm.astrum.sky.planets.minor.MinorPlanet
import kotlin.math.cos
import kotlin.math.sin

class Pluto(parent: Sun) : MinorPlanet(
    parent,
    AU(29.5739917738007),
    0.250248713478499,
    Radians(0.29825933192269555762),
    Radians(1.92644133465723380742),
    Radians(1.96519085060668286585),
    JulianDay(2447654.529313563835),
    Radians(0.00006943722388998144),
    0.55,
    -0.4,
    radius = Kilometer(1188.3),
) {

    override val siderealDay = 6.38722299911257520456

    override val siderealPeriod = 90797.0

    override val meanOppositionMagnitude = 15.12

    override val mass = 1.0 / 135836683.768617

    /**
     * Calculate Pluto's heliocentric ecliptical coordinates for given julian day.
     */
    override fun computePosition(jde: JulianDay): Pair<Triad, Triad> {
        // Meeus, Astron. Algorithms 2nd ed (1998). Chap 37. Equ 37.1
        // This function is accurate to within 0.07" in longitude, 0.02" in latitude and 0.000006 AU in radius.
        // Note: This function is not valid outside the period of 1885-2099.
        // Get julian centuries since J2000
        val t = (jde.value - 2451545) / 36525
        // Calculate mean longitudes for jupiter, saturn and pluto
        val J = 34.35 + 3034.9057 * t
        val S = 50.08 + 1222.1138 * t
        val P = 238.96 + 144.9600 * t

        var longitude = 0.0
        var latitude = 0.0
        var radius = 0.0

        // Calc periodic terms in table 37.A
        for (i in 0..42) {
            val a = ARGUMENT[i][0] * J + ARGUMENT[i][1] * S + ARGUMENT[i][2] * P
            val sinA = sin(a * M_PI_180)
            val cosA = cos(a * M_PI_180)

            longitude += LONGITUDE[i][0] * sinA + LONGITUDE[i][1] * cosA
            latitude += LATITUDE[i][0] * sinA + LATITUDE[i][1] * cosA
            radius += RADIUS[i][0] * sinA + RADIUS[i][1] * cosA
        }

        val L = (238.958116 + 144.96 * t + longitude * 0.000001) * M_PI_180
        val B = (-3.908239 + latitude * 0.000001) * M_PI_180
        val R = 40.7241346 + radius * 0.0000001

        val pos = Algorithms.sphericalToRectangularCoordinates(Radians(L), Radians(B))

        return Triad(R * pos[0], R * pos[1], R * pos[2]) to Triad.ZERO
    }

    override fun computeRotObliquity(jde: JulianDay) = Radians(1.9690025972455527)

    override fun computeRotAscendingNode() = Radians(3.96802141178535)

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
            ApparentMagnitudeAlgorithm.MUELLER_1893 -> -1.01 + d
            ApparentMagnitudeAlgorithm.EXPLANATORY_SUPPLEMENT_1992 -> -1.01 + d + 0.041 * phaseDeg
            ApparentMagnitudeAlgorithm.ASTRONOMICAL_ALMANAC_1984 -> -1.00 + d
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

    companion object {

        private val ARGUMENT = arrayOf(
            doubleArrayOf(0.0, 0.0, 1.0),
            doubleArrayOf(0.0, 0.0, 2.0),
            doubleArrayOf(0.0, 0.0, 3.0),
            doubleArrayOf(0.0, 0.0, 4.0),
            doubleArrayOf(0.0, 0.0, 5.0),
            doubleArrayOf(0.0, 0.0, 6.0),
            doubleArrayOf(0.0, 1.0, -1.0),
            doubleArrayOf(0.0, 1.0, 0.0),
            doubleArrayOf(0.0, 1.0, 1.0),
            doubleArrayOf(0.0, 1.0, 2.0),
            doubleArrayOf(0.0, 1.0, 3.0),
            doubleArrayOf(0.0, 2.0, -2.0),
            doubleArrayOf(0.0, 2.0, -1.0),
            doubleArrayOf(0.0, 2.0, 0.0),
            doubleArrayOf(1.0, -1.0, 0.0),
            doubleArrayOf(1.0, -1.0, 1.0),
            doubleArrayOf(1.0, 0.0, -3.0),
            doubleArrayOf(1.0, 0.0, -2.0),
            doubleArrayOf(1.0, 0.0, -1.0),
            doubleArrayOf(1.0, 0.0, 0.0),
            doubleArrayOf(1.0, 0.0, 1.0),
            doubleArrayOf(1.0, 0.0, 2.0),
            doubleArrayOf(1.0, 0.0, 3.0),
            doubleArrayOf(1.0, 0.0, 4.0),
            doubleArrayOf(1.0, 1.0, -3.0),
            doubleArrayOf(1.0, 1.0, -2.0),
            doubleArrayOf(1.0, 1.0, -1.0),
            doubleArrayOf(1.0, 1.0, 0.0),
            doubleArrayOf(1.0, 1.0, 1.0),
            doubleArrayOf(1.0, 1.0, 3.0),
            doubleArrayOf(2.0, 0.0, -6.0),
            doubleArrayOf(2.0, 0.0, -5.0),
            doubleArrayOf(2.0, 0.0, -4.0),
            doubleArrayOf(2.0, 0.0, -3.0),
            doubleArrayOf(2.0, 0.0, -2.0),
            doubleArrayOf(2.0, 0.0, -1.0),
            doubleArrayOf(2.0, 0.0, 0.0),
            doubleArrayOf(2.0, 0.0, 1.0),
            doubleArrayOf(2.0, 0.0, 2.0),
            doubleArrayOf(2.0, 0.0, 3.0),
            doubleArrayOf(3.0, 0.0, -2.0),
            doubleArrayOf(3.0, 0.0, -1.0),
            doubleArrayOf(3.0, 0.0, 0.0),
        )

        private val LONGITUDE = arrayOf(
            doubleArrayOf(-19799805.0, 19850055.0),
            doubleArrayOf(897144.0, -4954829.0),
            doubleArrayOf(611149.0, 1211027.0),
            doubleArrayOf(-341243.0, -189585.0),
            doubleArrayOf(129287.0, -34992.0),
            doubleArrayOf(-38164.0, 30893.0),
            doubleArrayOf(20442.0, -9987.0),
            doubleArrayOf(-4063.0, -5071.0),
            doubleArrayOf(-6016.0, -3336.0),
            doubleArrayOf(-3956.0, 3039.0),
            doubleArrayOf(-667.0, 3572.0),
            doubleArrayOf(1276.0, 501.0),
            doubleArrayOf(1152.0, -917.0),
            doubleArrayOf(630.0, -1277.0),
            doubleArrayOf(2571.0, -459.0),
            doubleArrayOf(899.0, -1449.0),
            doubleArrayOf(-1016.0, 1043.0),
            doubleArrayOf(-2343.0, -1012.0),
            doubleArrayOf(7042.0, 788.0),
            doubleArrayOf(1199.0, -338.0),
            doubleArrayOf(418.0, -67.0),
            doubleArrayOf(120.0, -274.0),
            doubleArrayOf(-60.0, -159.0),
            doubleArrayOf(-82.0, -29.0),
            doubleArrayOf(-36.0, -20.0),
            doubleArrayOf(-40.0, 7.0),
            doubleArrayOf(-14.0, 22.0),
            doubleArrayOf(4.0, 13.0),
            doubleArrayOf(5.0, 2.0),
            doubleArrayOf(-1.0, 0.0),
            doubleArrayOf(2.0, 0.0),
            doubleArrayOf(-4.0, 5.0),
            doubleArrayOf(4.0, -7.0),
            doubleArrayOf(14.0, 24.0),
            doubleArrayOf(-49.0, -34.0),
            doubleArrayOf(163.0, -48.0),
            doubleArrayOf(9.0, 24.0),
            doubleArrayOf(-4.0, 1.0),
            doubleArrayOf(-3.0, 1.0),
            doubleArrayOf(1.0, 3.0),
            doubleArrayOf(-3.0, -1.0),
            doubleArrayOf(5.0, -3.0),
            doubleArrayOf(0.0, 0.0),
        )

        private val LATITUDE = arrayOf(
            doubleArrayOf(-5452852.0, -14974862.0),
            doubleArrayOf(3527812.0, 1672790.0),
            doubleArrayOf(-1050748.0, 327647.0),
            doubleArrayOf(178690.0, -292153.0),
            doubleArrayOf(18650.0, 100340.0),
            doubleArrayOf(-30697.0, -25823.0),
            doubleArrayOf(4878.0, 11248.0),
            doubleArrayOf(226.0, -64.0),
            doubleArrayOf(2030.0, -836.0),
            doubleArrayOf(69.0, -604.0),
            doubleArrayOf(-247.0, -567.0),
            doubleArrayOf(-57.0, 1.0),
            doubleArrayOf(-122.0, 175.0),
            doubleArrayOf(-49.0, -164.0),
            doubleArrayOf(-197.0, 199.0),
            doubleArrayOf(-25.0, 217.0),
            doubleArrayOf(589.0, -248.0),
            doubleArrayOf(-269.0, 711.0),
            doubleArrayOf(185.0, 193.0),
            doubleArrayOf(315.0, 807.0),
            doubleArrayOf(-130.0, -43.0),
            doubleArrayOf(5.0, 3.0),
            doubleArrayOf(2.0, 17.0),
            doubleArrayOf(2.0, 5.0),
            doubleArrayOf(2.0, 3.0),
            doubleArrayOf(3.0, 1.0),
            doubleArrayOf(2.0, -1.0),
            doubleArrayOf(1.0, -1.0),
            doubleArrayOf(0.0, -1.0),
            doubleArrayOf(0.0, 0.0),
            doubleArrayOf(0.0, -2.0),
            doubleArrayOf(2.0, 2.0),
            doubleArrayOf(-7.0, 0.0),
            doubleArrayOf(10.0, -8.0),
            doubleArrayOf(-3.0, 20.0),
            doubleArrayOf(6.0, 5.0),
            doubleArrayOf(14.0, 17.0),
            doubleArrayOf(-2.0, 0.0),
            doubleArrayOf(0.0, 0.0),
            doubleArrayOf(0.0, 0.0),
            doubleArrayOf(0.0, 1.0),
            doubleArrayOf(0.0, 0.0),
            doubleArrayOf(1.0, 0.0),
        )

        private val RADIUS = arrayOf(
            doubleArrayOf(66865439.0, 68951812.0),
            doubleArrayOf(-11827535.0, -332538.0),
            doubleArrayOf(1593179.0, -1438890.0),
            doubleArrayOf(-18444.0, 483220.0),
            doubleArrayOf(-65977.0, -85431.0),
            doubleArrayOf(31174.0, -6032.0),
            doubleArrayOf(-5794.0, 22161.0),
            doubleArrayOf(4601.0, 4032.0),
            doubleArrayOf(-1729.0, 234.0),
            doubleArrayOf(-415.0, 702.0),
            doubleArrayOf(239.0, 723.0),
            doubleArrayOf(67.0, -67.0),
            doubleArrayOf(1034.0, -451.0),
            doubleArrayOf(-129.0, 504.0),
            doubleArrayOf(480.0, -231.0),
            doubleArrayOf(2.0, -441.0),
            doubleArrayOf(-3359.0, 265.0),
            doubleArrayOf(7856.0, -7832.0),
            doubleArrayOf(36.0, 45763.0),
            doubleArrayOf(8663.0, 8547.0),
            doubleArrayOf(-809.0, -769.0),
            doubleArrayOf(263.0, -144.0),
            doubleArrayOf(-126.0, 32.0),
            doubleArrayOf(-35.0, -16.0),
            doubleArrayOf(-19.0, -4.0),
            doubleArrayOf(-15.0, 8.0),
            doubleArrayOf(-4.0, 12.0),
            doubleArrayOf(5.0, 6.0),
            doubleArrayOf(3.0, 1.0),
            doubleArrayOf(6.0, -2.0),
            doubleArrayOf(2.0, 2.0),
            doubleArrayOf(-2.0, -2.0),
            doubleArrayOf(14.0, 13.0),
            doubleArrayOf(-63.0, 13.0),
            doubleArrayOf(136.0, -236.0),
            doubleArrayOf(273.0, 1065.0),
            doubleArrayOf(251.0, 149.0),
            doubleArrayOf(-25.0, -9.0),
            doubleArrayOf(9.0, -2.0),
            doubleArrayOf(-8.0, 7.0),
            doubleArrayOf(2.0, -10.0),
            doubleArrayOf(19.0, 35.0),
            doubleArrayOf(10.0, 2.0),
        )
    }
}