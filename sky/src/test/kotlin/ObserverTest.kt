import br.tiagohm.astrum.common.AU_KM
import br.tiagohm.astrum.common.M_PI
import br.tiagohm.astrum.common.M_PI_180
import br.tiagohm.astrum.common.units.angle.Degrees
import br.tiagohm.astrum.common.units.angle.Radians
import br.tiagohm.astrum.common.units.distance.AU
import br.tiagohm.astrum.common.units.distance.Kilometer
import br.tiagohm.astrum.common.units.distance.LightYear
import br.tiagohm.astrum.common.units.distance.Meter
import br.tiagohm.astrum.sky.Location
import br.tiagohm.astrum.sky.Observer
import br.tiagohm.astrum.sky.constellations.Constellation
import br.tiagohm.astrum.sky.core.coordinates.EquatorialCoord
import br.tiagohm.astrum.sky.core.math.Duad
import br.tiagohm.astrum.sky.core.math.Triad
import br.tiagohm.astrum.sky.core.orbit.KeplerOrbit
import br.tiagohm.astrum.sky.core.time.JulianDay
import br.tiagohm.astrum.sky.core.time.TimeCorrectionType
import br.tiagohm.astrum.sky.dso.DeepSky
import br.tiagohm.astrum.sky.planets.ApparentMagnitudeAlgorithm
import br.tiagohm.astrum.sky.planets.Satellite
import br.tiagohm.astrum.sky.planets.Sun
import br.tiagohm.astrum.sky.planets.major.earth.*
import br.tiagohm.astrum.sky.planets.major.jupiter.*
import br.tiagohm.astrum.sky.planets.major.mars.Deimos
import br.tiagohm.astrum.sky.planets.major.mars.Mars
import br.tiagohm.astrum.sky.planets.major.mars.Phobos
import br.tiagohm.astrum.sky.planets.major.mercury.Mercury
import br.tiagohm.astrum.sky.planets.major.neptune.Neptune
import br.tiagohm.astrum.sky.planets.major.saturn.*
import br.tiagohm.astrum.sky.planets.major.uranus.*
import br.tiagohm.astrum.sky.planets.major.venus.Venus
import br.tiagohm.astrum.sky.planets.minor.MinorPlanet
import br.tiagohm.astrum.sky.planets.minor.comets.Comet
import br.tiagohm.astrum.sky.planets.minor.pluto.Pluto
import br.tiagohm.astrum.sky.stars.Star
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ObserverTest {

    @Test
    fun fromEarth() {
        val sun = Sun()
        val earth = Earth(sun)

        // None
        testFrom(
            sun, earth,
            SAO_JOSE_DAS_PALMEIRAS,
            JulianDay(2021, 2, 5, 9, 0, 0), // 2459251.000000
            // Coordinates from Stellarium!!!
            listOf(
                Duad(319.36244, -15.7656),
                Duad(324.68691, -10.6681),
                Duad(307.46573, -19.7921),
                null,
                Duad(42.11161, 17.5918),
                Duad(313.74888, -17.9443),
                Duad(308.30091, -19.2301),
                Duad(34.74791, 13.4208),
                Duad(350.70116, -5.1698),
            ),
            useTopocentricCoordinates = false,
            useNutation = false,
            useLightTravelTime = false,
        )

        // Nutation
        testFrom(
            sun, earth,
            SAO_JOSE_DAS_PALMEIRAS,
            JulianDay(2021, 2, 5, 9, 0, 0), // 2459251.000000
            // Coordinates from Stellarium!!!
            listOf(
                Duad(319.35828, -15.7673),
                Duad(324.68284, -10.6698),
                Duad(307.46139, -19.7936),
                null,
                Duad(42.10713, 17.5909),
                Duad(313.74464, -17.9460),
                Duad(308.29659, -19.2317),
                Duad(34.74358, 13.4197),
                Duad(350.69722, -5.1716),
            ),
            useTopocentricCoordinates = false,
            useNutation = true,
            useLightTravelTime = false,
        )

        // Topocentric Coordinates
        testFrom(
            sun, earth,
            SAO_JOSE_DAS_PALMEIRAS,
            JulianDay(2021, 2, 5, 9, 0, 0), // 2459251.000000
            // Coordinates from Stellarium!!!
            listOf(
                Duad(319.36441, -15.7649),
                Duad(324.68987, -10.6669),
                Duad(307.46674, -19.7918),
                null,
                Duad(42.11281, 17.5922),
                Duad(313.74919, -17.9442),
                Duad(308.30106, -19.2301),
                Duad(34.74799, 13.4208),
                Duad(350.70124, -5.1698),
            ),
            useTopocentricCoordinates = true,
            useNutation = false,
            useLightTravelTime = false,
        )

        // Light Travel Time
        testFrom(
            sun, earth,
            SAO_JOSE_DAS_PALMEIRAS,
            JulianDay(2021, 2, 5, 9, 0, 0), // 2459251.000000
            // Coordinates from Stellarium!!!
            listOf(
                Duad(319.35672, -15.7673),
                Duad(324.69637, -10.6657),
                Duad(307.45908, -19.7933),
                null,
                Duad(42.10792, 17.5905),
                Duad(313.74629, -17.945),
                Duad(308.29908, -19.2305),
                Duad(34.74669, 13.4203),
                Duad(350.70019, -5.1702),
            ),
            useTopocentricCoordinates = false,
            useNutation = false,
            useLightTravelTime = true,
        )

        // All + Time Correction
        testFrom(
            sun, earth,
            SAO_JOSE_DAS_PALMEIRAS,
            JulianDay(2021, 2, 5, 9, 0, 0), // 2459251.000000
            // Coordinates from Stellarium!!!
            listOf(
                Duad(319.35536, -15.7681),
                Duad(324.69444, -10.6664),
                Duad(307.45683, -19.7944),
                null,
                Duad(42.10508, 17.5902),
                Duad(313.74255, -17.9465),
                Duad(308.29502, -19.2320),
                Duad(34.74246, 13.4193),
                Duad(350.69634, -5.1720),
            ),
            TimeCorrectionType.ESPEANAK_MEEUS,
            useTopocentricCoordinates = true,
            useNutation = true,
            useLightTravelTime = true,
        )
    }

    @Test
    fun timeAdvance() {
        val sun = Sun()
        val earth = Earth(sun)

        val o0 = Observer(
            earth,
            SAO_JOSE_DAS_PALMEIRAS,
            JulianDay(2021, 2, 5, 9, 0, 0), // 2459251.000000
            timeCorrection = TimeCorrectionType.NONE,
            useTopocentricCoordinates = true,
            useNutation = true,
            useLightTravelTime = true,
        )

        val mercury = Mercury(sun)

        for (i in 5 until 12) {
            val oi = o0.copy(jd = JulianDay(2021, 2, i, 9, 0, 0))
            var pos = mercury.equatorial(oi)

            when (i) {
                5 -> assertEquals(324.69526, -10.6663, pos, DELTA_4, true)
                6 -> assertEquals(323.64525, -10.8355, pos, DELTA_4, true)
                7 -> assertEquals(322.52248, -11.0620, pos, DELTA_4, true)
                8 -> assertEquals(321.35815, -11.3369, pos, DELTA_4, true)
                9 -> assertEquals(320.18409, -11.6507, pos, DELTA_4, true)
                10 -> assertEquals(319.03114, -11.9929, pos, DELTA_4, true)
                11 -> assertEquals(317.92765, -12.3538, pos, DELTA_4, true)
            }

            pos = sun.equatorial(oi)

            when (i) {
                5 -> assertEquals(319.35453, -15.7683, pos, DELTA_4, true)
                6 -> assertEquals(320.35724, -15.4602, pos, DELTA_4, true)
                7 -> assertEquals(321.35671, -15.1476, pos, DELTA_4, true)
                8 -> assertEquals(322.35295, -14.8307, pos, DELTA_4, true)
                9 -> assertEquals(323.34596, -14.5096, pos, DELTA_4, true)
                10 -> assertEquals(324.33576, -14.1845, pos, DELTA_4, true)
                11 -> assertEquals(325.32234, -13.8555, pos, DELTA_4, true)
            }
        }

        for (i in 4 downTo 1) {
            val oi = o0.copy(jd = JulianDay(2021, 2, i, 9, 0, 0))
            var pos = mercury.equatorial(oi)

            when (i) {
                4 -> assertEquals(325.64359, -10.5614, pos, DELTA_4, true)
                3 -> assertEquals(326.46494, -10.5262, pos, DELTA_4, true)
                2 -> assertEquals(327.13875, -10.5637, pos, DELTA_4, true)
                1 -> assertEquals(327.64970, -10.6746, pos, DELTA_4, true)
            }

            pos = sun.equatorial(oi)

            when (i) {
                4 -> assertEquals(318.34857, -16.0720, pos, DELTA_4, true)
                3 -> assertEquals(317.33935, -16.3710, pos, DELTA_4, true)
                2 -> assertEquals(316.32684, -16.6652, pos, DELTA_4, true)
                1 -> assertEquals(315.31102, -16.9546, pos, DELTA_4, true)
            }
        }
    }

    @Test
    fun isAboveHorizon() {
        val sun = Sun()
        val earth = Earth(sun)

        val o0 = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 2, 5, 9, 0, 0),
        )

        assertTrue(Neptune(sun).isAboveHorizon(o0))
        assertTrue(!Mars(sun).isAboveHorizon(o0))
        assertTrue(!Uranus(sun).isAboveHorizon(o0))

        val o1 = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 2, 5, 21, 0, 0),
        )

        assertTrue(!Neptune(sun).isAboveHorizon(o1))
        assertTrue(Mars(sun).isAboveHorizon(o1))
        assertTrue(Uranus(sun).isAboveHorizon(o1))
    }

    private fun testFrom(
        sun: Sun,
        home: Earth,
        site: Location,
        jd: JulianDay,
        positions: List<Duad?>,
        timeCorrection: TimeCorrectionType = TimeCorrectionType.NONE,
        useTopocentricCoordinates: Boolean = false,
        useNutation: Boolean = false,
        useLightTravelTime: Boolean = false,
        delta: Double = DELTA_4,
    ) {
        val o = Observer(
            home,
            site,
            jd,
            timeCorrection = timeCorrection,
            useTopocentricCoordinates = useTopocentricCoordinates,
            useNutation = useNutation,
            useLightTravelTime = useLightTravelTime,
        )

        val planets = listOf(
            sun,
            Mercury(sun),
            Venus(sun),
            home,
            Mars(sun),
            Jupiter(sun),
            Saturn(sun),
            Uranus(sun),
            Neptune(sun),
        )

        for (i in 0 until 8) {
            positions[i]?.let {
                val (ra, dec) = planets[i].equatorial(o)
                assertEquals(it[0], ra, delta, true)
                assertEquals(it[1], dec, delta, true)
            }
        }
    }

    @Test
    fun parallacticAngle() {
        val sun = Sun()
        val earth = Earth(sun)

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 2, 5, 9, 0, 0), // 2459251.000000
        )

        assertEquals(-106.3196, sun.parallacticAngle(o), DELTA_4, true)
        assertEquals(-110.2466, Mercury(sun).parallacticAngle(o), DELTA_4, true)
        assertEquals(-101.2810, Venus(sun).parallacticAngle(o), DELTA_4, true)
        assertEquals(-104.5911, Mars(sun).parallacticAngle(o), DELTA_4, true)
        assertEquals(-103.9624, Jupiter(sun).parallacticAngle(o), DELTA_4, true)
        assertEquals(-102.1158, Saturn(sun).parallacticAngle(o), DELTA_4, true)
        assertEquals(-108.2743, Uranus(sun).parallacticAngle(o), DELTA_4, true)
        assertEquals(-111.9848, Neptune(sun).parallacticAngle(o), DELTA_4, true)
        assertEquals(-95.8256, Pluto(sun).parallacticAngle(o), DELTA_4, true)
    }

    @Test
    fun equatorialJ2000() {
        val sun = Sun()
        val earth = Earth(sun)

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 2, 5, 9, 0, 0), // 2459251.000000
        )

        assertEquals(319.06728, -15.8555, sun.equatorialJ2000(o), DELTA_4, true)
        assertEquals(324.41508, -10.7605, Mercury(sun).equatorialJ2000(o), DELTA_4, true)
        assertEquals(307.157, -19.8641, Venus(sun).equatorialJ2000(o), DELTA_4, true)
        assertEquals(41.81461, 17.5037, Mars(sun).equatorialJ2000(o), DELTA_4, true)
        assertEquals(313.4488, -18.0259, Jupiter(sun).equatorialJ2000(o), DELTA_4, true)
        assertEquals(307.99669, -19.303, Saturn(sun).equatorialJ2000(o), DELTA_4, true)
        assertEquals(34.4606, 13.3237, Uranus(sun).equatorialJ2000(o), DELTA_4, true)
        assertEquals(350.42818, -5.286, Neptune(sun).equatorialJ2000(o), DELTA_4, true)
        assertEquals(297.25732, -22.3448, Pluto(sun).equatorialJ2000(o), DELTA_4, true)
    }

    @Test
    fun horizontal() {
        val sun = Sun()
        val earth = Earth(sun)

        val o0 = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 2, 5, 9, 0, 0), // 2459251.000000
        )

        assertEquals(90.7432, 43.3413, sun.horizontal(o0, apparent = false), DELTA_4, true)
        assertEquals(86.5517, 36.8015, Mercury(sun).horizontal(o0, apparent = false), DELTA_4, true)
        assertEquals(92.5681, 55.3030, Venus(sun).horizontal(o0, apparent = false), DELTA_4, true)
        assertEquals(87.1402, -44.6719, Mars(sun).horizontal(o0, apparent = false), DELTA_4, true)
        assertEquals(91.7507, 49.0952, Jupiter(sun).horizontal(o0, apparent = false), DELTA_4, true)
        assertEquals(91.8896, 54.4151, Saturn(sun).horizontal(o0, apparent = false), DELTA_4, true)
        assertEquals(89.7587, -36.6858, Uranus(sun).horizontal(o0, apparent = false), DELTA_4, true)
        assertEquals(91.1530, 10.8117, Neptune(sun).horizontal(o0, apparent = false), DELTA_4, true)
        assertEquals(94.7489, 64.8028, Pluto(sun).horizontal(o0, apparent = false), DELTA_4, true)

        val o1 = Observer(
            earth,
            Location("Tokyo", Degrees(35.689499), Degrees(139.691711), Meter(44.0)),
            JulianDay(2021, 2, 5, 9, 0, 0), // 2459251.000000
        )

        assertEquals(285.4773, -46.2733, sun.horizontal(o1, apparent = false), DELTA_4, true)
        assertEquals(286.8195, -39.0608, Mercury(sun).horizontal(o1, apparent = false), DELTA_4, true)
        assertEquals(291.0016, -57.8109, Venus(sun).horizontal(o1, apparent = false), DELTA_4, true)
        assertEquals(263.6072, 39.5997, Mars(sun).horizontal(o1, apparent = false), DELTA_4, true)
        assertEquals(287.5396, -51.9072, Jupiter(sun).horizontal(o1, apparent = false), DELTA_4, true)
        assertEquals(290.97, -56.8399, Saturn(sun).horizontal(o1, apparent = false), DELTA_4, true)
        assertEquals(264.0623, 31.3826, Uranus(sun).horizontal(o1, apparent = false), DELTA_4, true)
        assertEquals(274.4963, -15.0647, Neptune(sun).horizontal(o1, apparent = false), DELTA_4, true)
    }

    @Test
    fun horizontalApparent() {
        val sun = Sun()
        val earth = Earth(sun)

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 2, 5, 9, 0, 0), // 2459251.000000
        )

        assertEquals(90.7432, 43.3589, sun.horizontal(o), DELTA_4, true)
        assertEquals(86.5517, 36.8238, Mercury(sun).horizontal(o), DELTA_4, true)
        assertEquals(92.5681, 55.3145, Venus(sun).horizontal(o), DELTA_4, true)
        assertEquals(87.1402, -44.6719, Mars(sun).horizontal(o), DELTA_4, true)
        assertEquals(91.7507, 49.1097, Jupiter(sun).horizontal(o), DELTA_4, true)
        assertEquals(91.8896, 54.4271, Saturn(sun).horizontal(o), DELTA_4, true)
        assertEquals(89.7587, -36.6858, Uranus(sun).horizontal(o), DELTA_4, true)
        assertEquals(91.1530, 10.8944, Neptune(sun).horizontal(o), DELTA_4, true)
        assertEquals(94.7489, 64.8106, Pluto(sun).horizontal(o), DELTA_4, true)
    }

    @Test
    fun horizontalApparentSouthAzimuth() {
        val sun = Sun()
        val earth = Earth(sun)

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 2, 5, 9, 0, 0), // 2459251.000000
        )

        assertEquals(270.7432, 43.3589, sun.horizontal(o, true), DELTA_4, true)
        assertEquals(266.5517, 36.8238, Mercury(sun).horizontal(o, true), DELTA_4, true)
        assertEquals(272.5681, 55.3145, Venus(sun).horizontal(o, true), DELTA_4, true)
        assertEquals(267.1402, -44.6719, Mars(sun).horizontal(o, true), DELTA_4, true)
        assertEquals(271.7507, 49.1097, Jupiter(sun).horizontal(o, true), DELTA_4, true)
        assertEquals(271.8896, 54.4271, Saturn(sun).horizontal(o, true), DELTA_4, true)
        assertEquals(269.7587, -36.6858, Uranus(sun).horizontal(o, true), DELTA_4, true)
        assertEquals(271.1530, 10.8944, Neptune(sun).horizontal(o, true), DELTA_4, true)
        assertEquals(274.7489, 64.8106, Pluto(sun).horizontal(o, true), DELTA_4, true)
    }

    @Test
    fun hourAngle() {
        val sun = Sun()
        val earth = Earth(sun)

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 2, 5, 9, 0, 0), // 2459251.000000
        )

        assertEquals(20.72784, -15.7682, sun.hourAngle(o, false), DELTA_4, true)
        assertEquals(20.37190, -10.6666, Mercury(sun).hourAngle(o, false), DELTA_4, true)
        assertEquals(21.52107, -19.7945, Venus(sun).hourAngle(o, false), DELTA_4, true)
        assertEquals(15.21116, 17.5902, Mars(sun).hourAngle(o, false), DELTA_4, true)
        assertEquals(21.10202, -17.9465, Jupiter(sun).hourAngle(o, false), DELTA_4, true)
        assertEquals(21.46518, -19.2320, Saturn(sun).hourAngle(o, false), DELTA_4, true)
        assertEquals(15.70202, 13.4193, Uranus(sun).hourAngle(o, false), DELTA_4, true)
        assertEquals(18.63843, -5.1720, Neptune(sun).hourAngle(o, false), DELTA_4, true)
        assertEquals(22.18045, -22.2921, Pluto(sun).hourAngle(o, false), DELTA_4, true)
    }

    @Test
    fun hourAngleApparent() {
        val sun = Sun()
        val earth = Earth(sun)

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 2, 5, 9, 0, 0), // 2459251.000000
        )

        assertEquals(20.72897, -15.7732, sun.hourAngle(o), DELTA_4, true)
        assertEquals(20.37326, -10.6742, Mercury(sun).hourAngle(o), DELTA_4, true)
        assertEquals(21.52187, -19.7967, Venus(sun).hourAngle(o), DELTA_4, true)
        assertEquals(15.21116, 17.5901, Mars(sun).hourAngle(o), DELTA_4, true)
        assertEquals(21.10296, -17.9499, Jupiter(sun).hourAngle(o), DELTA_4, true)
        assertEquals(21.46598, -19.2344, Saturn(sun).hourAngle(o), DELTA_4, true)
        assertEquals(15.70202, 13.4193, Uranus(sun).hourAngle(o), DELTA_4, true)
        assertEquals(18.64356, -5.2029, Neptune(sun).hourAngle(o), DELTA_4, true)
        assertEquals(22.18102, -22.2929, Pluto(sun).hourAngle(o), DELTA_4, true)
    }

    @Test
    fun galactic() {
        val sun = Sun()
        val earth = Earth(sun)

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 2, 5, 9, 0, 0), // 2459251.000000
        )

        assertEquals(34.1094, -39.0728, sun.galactic(o), DELTA_4, true)
        assertEquals(43.035, -41.6918, Mercury(sun).galactic(o), DELTA_4, true)
        assertEquals(24.5732, -29.9575, Venus(sun).galactic(o), DELTA_4, true)
        assertEquals(158.3943, -37.2674, Mars(sun).galactic(o), DELTA_4, true)
        assertEquals(29.1054, -34.8738, Jupiter(sun).galactic(o), DELTA_4, true)
        assertEquals(25.504, -30.4997, Saturn(sun).galactic(o), DELTA_4, true)
        assertEquals(153.0455, -44.4373, Uranus(sun).galactic(o), DELTA_4, true)
        assertEquals(74.5623, -59.447, Neptune(sun).galactic(o), DELTA_4, true)
        assertEquals(18.324, -22.2122, Pluto(sun).galactic(o), DELTA_4, true)
    }

    @Test
    fun supergalactic() {
        val sun = Sun()
        val earth = Earth(sun)

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 2, 5, 9, 0, 0), // 2459251.000000
        )

        assertEquals(-104.0866, 42.9741, sun.supergalactic(o), DELTA_4, true)
        assertEquals(-94.344, 41.8247, Mercury(sun).supergalactic(o), DELTA_4, true)
        assertEquals(-119.8808, 47.6372, Venus(sun).supergalactic(o), DELTA_4, true)
        assertEquals(-37.5207, -20.5139, Mars(sun).supergalactic(o), DELTA_4, true)
        assertEquals(-111.4607, 45.3493, Jupiter(sun).supergalactic(o), DELTA_4, true)
        assertEquals(-118.4413, 47.639, Saturn(sun).supergalactic(o), DELTA_4, true)
        assertEquals(-44.4606, -15.5939, Uranus(sun).supergalactic(o), DELTA_4, true)
        assertEquals(-75.6146, 20.7693, Neptune(sun).supergalactic(o), DELTA_4, true)
        assertEquals(-134.038, 49.7135, Pluto(sun).supergalactic(o), DELTA_4, true)
    }

    @Test
    fun eclipticJ2000() {
        val sun = Sun()
        val earth = Earth(sun)

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 2, 5, 9, 0, 0), // 2459251.000000
        )

        assertEquals(316.6135, 0.0017, sun.eclipticJ2000(o), DELTA_4, true)
        assertEquals(323.1506, 3.2161, Mercury(sun).eclipticJ2000(o), DELTA_4, true)
        assertEquals(304.6190, -0.7788, Venus(sun).eclipticJ2000(o), DELTA_4, true)
        assertEquals(44.6849, 1.3191, Mars(sun).eclipticJ2000(o), DELTA_4, true)
        assertEquals(310.8423, -0.5331, Jupiter(sun).eclipticJ2000(o), DELTA_4, true)
        assertEquals(305.5226, -0.4264, Saturn(sun).eclipticJ2000(o), DELTA_4, true)
        assertEquals(36.6453, -0.4345, Uranus(sun).eclipticJ2000(o), DELTA_4, true)
        assertEquals(349.1293, -1.0694, Neptune(sun).eclipticJ2000(o), DELTA_4, true)
        assertEquals(295.0683, -1.2464, Pluto(sun).eclipticJ2000(o), DELTA_4, true)
    }

    @Test
    fun eclipticOfDate() {
        val sun = Sun()
        val earth = Earth(sun)

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 2, 5, 9, 0, 0), // 2459251.000000
        )

        assertEquals(316.9039, 0.00000, sun.ecliptic(o), DELTA_4, true)
        assertEquals(323.4408, 3.2146, Mercury(sun).ecliptic(o), DELTA_4, true)
        assertEquals(304.9094, -0.781, Venus(sun).ecliptic(o), DELTA_4, true)
        assertEquals(44.9753, 1.3212, Mars(sun).ecliptic(o), DELTA_4, true)
        assertEquals(311.1328, -0.535, Jupiter(sun).ecliptic(o), DELTA_4, true)
        assertEquals(305.8131, -0.4285, Saturn(sun).ecliptic(o), DELTA_4, true)
        assertEquals(36.9358, -0.4327, Uranus(sun).ecliptic(o), DELTA_4, true)
        assertEquals(349.4198, -1.0697, Neptune(sun).ecliptic(o), DELTA_4, true)
        assertEquals(295.3588, -1.2488, Pluto(sun).ecliptic(o), DELTA_4, true)
    }

    @Test
    fun rts() {
        val sun = Sun()
        val earth = Earth(sun)

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 2, 5, 9, 0, 0), // 2459251.000000
        )

        assertEquals(Triad(5.7500, 12.2833, 18.8167), sun.rts(o), DELTA_2)
        assertEquals(Triad(6.283333, 12.633333, 19.0), Mercury(sun).rts(o), DELTA_2)
        assertEquals(Triad(4.85, 11.483, 18.117), Venus(sun).rts(o), DELTA_2)
        assertEquals(Triad(12.267, 17.817, 23.367), Mars(sun).rts(o), DELTA_2)
        assertEquals(Triad(5.333, 11.9, 18.483), Jupiter(sun).rts(o), DELTA_2)
        assertEquals(Triad(4.933, 11.55, 18.15), Saturn(sun).rts(o), DELTA_2)
        assertEquals(Triad(11.65, 17.317, 23.0), Uranus(sun).rts(o), DELTA_2)
        assertEquals(Triad(8.183, 14.383, 20.583), Neptune(sun).rts(o), DELTA_2)
        assertEquals(Triad(DELTA_1, 6.6167, 13.1167), Moon(earth).rts(o), DELTA_2)
        assertEquals(Triad(4.1167, 10.8167, 17.5333), Pluto(sun).rts(o), DELTA_2)
    }

    @Test
    fun distance() {
        val sun = Sun()
        val earth = Earth(sun)

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 2, 5, 9, 0, 0), // 2459251.000000
        )

        assertEquals(0.986, sun.distance(o), DELTA_3)
        assertEquals(0.679, Mercury(sun).distance(o), DELTA_3)
        assertEquals(1.663, Venus(sun).distance(o), DELTA_3)
        assertEquals(1.236, Mars(sun).distance(o), DELTA_3)
        assertEquals(6.064, Jupiter(sun).distance(o), DELTA_3)
        assertEquals(10.947, Saturn(sun).distance(o), DELTA_3)
        assertEquals(19.914, Uranus(sun).distance(o), DELTA_3)
        assertEquals(30.753, Neptune(sun).distance(o), DELTA_3)
        assertEquals(365486.706 / AU_KM, Moon(earth).distance(o), DELTA_3)
        assertEquals(35.130, Pluto(sun).distance(o), DELTA_3)
    }

    @Test
    fun distanceFromSun() {
        val sun = Sun()
        val earth = Earth(sun)

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 2, 5, 9, 0, 0), // 2459251.000000
        )

        assertEquals(0.0, sun.distanceFromSun(o), DELTA_3)
        assertEquals(0.324, Mercury(sun).distanceFromSun(o), DELTA_3)
        assertEquals(0.728, Venus(sun).distanceFromSun(o), DELTA_3)
        assertEquals(0.986, earth.distanceFromSun(o), DELTA_3)
        assertEquals(1.555, Mars(sun).distanceFromSun(o), DELTA_3)
        assertEquals(5.084, Jupiter(sun).distanceFromSun(o), DELTA_3)
        assertEquals(9.981, Saturn(sun).distanceFromSun(o), DELTA_3)
        assertEquals(19.768, Uranus(sun).distanceFromSun(o), DELTA_3)
        assertEquals(29.926, Neptune(sun).distanceFromSun(o), DELTA_3)
        assertEquals(0.986, Moon(earth).distanceFromSun(o), DELTA_3)
        assertEquals(34.215, Pluto(sun).distanceFromSun(o), DELTA_3)
    }

    @Test
    fun elongation() {
        val sun = Sun()
        val earth = Earth(sun)

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 2, 5, 9, 0, 0), // 2459251.000000
        )

        assertEquals(0.0, sun.elongation(o), DELTA_3)
        assertEquals(7.2763, Mercury(sun).elongation(o), DELTA_3, true)
        assertEquals(12.0253, Venus(sun).elongation(o), DELTA_3, true)
        assertEquals(88.0661, Mars(sun).elongation(o), DELTA_3, true)
        assertEquals(5.8016, Jupiter(sun).elongation(o), DELTA_3, true)
        assertEquals(11.1048, Saturn(sun).elongation(o), DELTA_3, true)
        assertEquals(80.0264, Uranus(sun).elongation(o), DELTA_3, true)
        assertEquals(32.5258, Neptune(sun).elongation(o), DELTA_3, true)
        assertEquals(80.5519, Moon(earth).elongation(o), DELTA_3, true)
        assertEquals(21.5853, Pluto(sun).elongation(o), DELTA_3, true)
    }

    @Test
    fun phaseAngle() {
        val sun = Sun()
        val earth = Earth(sun)

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 2, 5, 9, 0, 0), // 2459251.000000
        )

        assertEquals(Double.NaN, sun.phaseAngle(o), DELTA_3)
        assertEquals(157.3247, Mercury(sun).phaseAngle(o), DELTA_3, true)
        assertEquals(16.3937, Venus(sun).phaseAngle(o), DELTA_3, true)
        assertEquals(39.3243, Mars(sun).phaseAngle(o), DELTA_3, true)
        assertEquals(1.1233, Jupiter(sun).phaseAngle(o), DELTA_3, true)
        assertEquals(1.0902, Saturn(sun).phaseAngle(o), DELTA_3, true)
        assertEquals(2.8157, Uranus(sun).phaseAngle(o), DELTA_3, true)
        assertEquals(1.015, Neptune(sun).phaseAngle(o), DELTA_3, true)
        assertEquals(99.308, Moon(earth).phaseAngle(o), DELTA_3, true)
        assertEquals(0.6074, Pluto(sun).phaseAngle(o), DELTA_3, true)
    }

    @Test
    fun synodicPeriod() {
        val sun = Sun()
        val earth = Earth(sun)

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 2, 5, 9, 0, 0), // 2459251.000000
        )

        assertEquals(0.0, sun.synodicPeriod(o), DELTA_2)
        assertEquals(115.88, Mercury(sun).synodicPeriod(o), DELTA_2)
        assertEquals(583.92, Venus(sun).synodicPeriod(o), DELTA_2)
        assertEquals(779.95, Mars(sun).synodicPeriod(o), DELTA_2)
        assertEquals(398.89, Jupiter(sun).synodicPeriod(o), DELTA_2)
        assertEquals(378.09, Saturn(sun).synodicPeriod(o), DELTA_2)
        assertEquals(369.66, Uranus(sun).synodicPeriod(o), DELTA_2)
        assertEquals(367.49, Neptune(sun).synodicPeriod(o), DELTA_2)
        assertEquals(29.53, Moon(earth).synodicPeriod(o), DELTA_2)
    }

    @Test
    fun orbitalVelocity() {
        val sun = Sun()
        val earth = Earth(sun)

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 2, 5, 9, 0, 0), // 2459251.000000
        )

        assertEquals(0.0, sun.orbitalVelocity(o), DELTA_3)
        assertEquals(56.441, Mercury(sun).orbitalVelocity(o), DELTA_3)
        assertEquals(34.804, Venus(sun).orbitalVelocity(o), DELTA_3)
        assertEquals(23.639, Mars(sun).orbitalVelocity(o), DELTA_3)
        assertEquals(13.367, Jupiter(sun).orbitalVelocity(o), DELTA_3)
        assertEquals(9.230, Saturn(sun).orbitalVelocity(o), DELTA_3)
        assertEquals(6.602, Uranus(sun).orbitalVelocity(o), DELTA_3)
        assertEquals(5.475, Neptune(sun).orbitalVelocity(o), DELTA_3)

        // TODO: Testar velocidade heliocentrica com as luas de júpiter
    }

    @Test
    fun illumination() {
        val sun = Sun()
        val earth = Earth(sun)

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 2, 5, 9, 0, 0), // 2459251.000000
        )

        assertEquals(Double.NaN, sun.illumination(o), DELTA_3)
        assertEquals(0.039, Mercury(sun).illumination(o), DELTA_3)
        assertEquals(0.980, Venus(sun).illumination(o), DELTA_3)
        assertEquals(0.887, Mars(sun).illumination(o), DELTA_3)
        assertEquals(1.000, Jupiter(sun).illumination(o), DELTA_3)
        assertEquals(1.000, Saturn(sun).illumination(o), DELTA_3)
        assertEquals(0.999, Uranus(sun).illumination(o), DELTA_3)
        assertEquals(1.000, Neptune(sun).illumination(o), DELTA_3)
        assertEquals(1.000, Pluto(sun).illumination(o), DELTA_3)
    }

    @Test
    fun siderealTime() {
        val sun = Sun()
        val earth = Earth(sun)

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 2, 5, 9, 0, 0), // 2459251.000000
        )

        assertEquals(18.0184, o.computeMeanSiderealTime(), DELTA_4)
        assertEquals(18.0181, o.computeApparentSiderealTime(), DELTA_4)
    }

    @Test
    fun visualMagnitude() {
        val sun = Sun()
        val earth = Earth(sun)

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 2, 5, 13, 0, 0), // All planets in the sky
        )

        assertEquals(1.02, sun.airmass(o), DELTA_2)
        assertEquals(1.03, Mercury(sun).airmass(o), DELTA_2)
        assertEquals(1.07, Venus(sun).airmass(o), DELTA_2)
        assertEquals(6.05, Mars(sun).airmass(o), DELTA_2)
        assertEquals(1.04, Jupiter(sun).airmass(o), DELTA_2)
        assertEquals(1.07, Saturn(sun).airmass(o), DELTA_2)
        assertEquals(3.31, Uranus(sun).airmass(o), DELTA_2)
        assertEquals(1.11, Neptune(sun).airmass(o), DELTA_2)

        assertEquals(-26.77, sun.visualMagnitude(o), DELTA_2)
        assertEquals(-26.64, sun.visualMagnitudeWithExtinction(o), DELTA_2)

        // Default Apparent Magnitude Algorithm
        assertEquals(3.74, Mercury(sun).visualMagnitude(o), DELTA_2)
        assertEquals(-3.87, Venus(sun).visualMagnitude(o), DELTA_2)
        assertEquals(0.53, Mars(sun).visualMagnitude(o), DELTA_2)
        assertEquals(-1.95, Jupiter(sun).visualMagnitude(o), DELTA_2)
        assertEquals(0.63, Saturn(sun).visualMagnitude(o), DELTA_2)
        assertEquals(5.79, Uranus(sun).visualMagnitude(o), DELTA_2)
        assertEquals(7.95, Neptune(sun).visualMagnitude(o), DELTA_2)
        assertEquals(-10.77, Moon(earth).visualMagnitude(o), DELTA_2)
        assertEquals(14.39, Pluto(sun).visualMagnitude(o), DELTA_2)

        assertEquals(3.88, Mercury(sun).visualMagnitudeWithExtinction(o), DELTA_2)
        assertEquals(-3.73, Venus(sun).visualMagnitudeWithExtinction(o), DELTA_2)
        assertEquals(1.31, Mars(sun).visualMagnitudeWithExtinction(o), DELTA_2)
        assertEquals(-1.81, Jupiter(sun).visualMagnitudeWithExtinction(o), DELTA_2)
        assertEquals(0.77, Saturn(sun).visualMagnitudeWithExtinction(o), DELTA_2)
        assertEquals(6.22, Uranus(sun).visualMagnitudeWithExtinction(o), DELTA_2)
        assertEquals(8.09, Neptune(sun).visualMagnitudeWithExtinction(o), DELTA_2)
        assertEquals(-9.01, Moon(earth).visualMagnitudeWithExtinction(o), DELTA_2)
        assertEquals(14.54, Pluto(sun).visualMagnitudeWithExtinction(o), DELTA_2)

        o.copy(apparentMagnitudeAlgorithm = ApparentMagnitudeAlgorithm.EXPLANATORY_SUPPLEMENT_1992).let {
            assertEquals(2.66, Mercury(sun).visualMagnitude(it), DELTA_2)
            assertEquals(-3.80, Venus(sun).visualMagnitude(it), DELTA_2)
            assertEquals(0.53, Mars(sun).visualMagnitude(it), DELTA_2)
            assertEquals(-1.80, Jupiter(sun).visualMagnitude(it), DELTA_2)
            assertEquals(0.63, Saturn(sun).visualMagnitude(it), DELTA_2)
            assertEquals(5.79, Uranus(sun).visualMagnitude(it), DELTA_2)
            assertEquals(7.95, Neptune(sun).visualMagnitude(it), DELTA_2)
            assertEquals(14.41, Pluto(sun).visualMagnitude(it), DELTA_2)

            assertEquals(2.79, Mercury(sun).visualMagnitudeWithExtinction(it), DELTA_2)
            assertEquals(-3.66, Venus(sun).visualMagnitudeWithExtinction(it), DELTA_2)
            assertEquals(1.31, Mars(sun).visualMagnitudeWithExtinction(it), DELTA_2)
            assertEquals(-1.66, Jupiter(sun).visualMagnitudeWithExtinction(it), DELTA_2)
            assertEquals(0.77, Saturn(sun).visualMagnitudeWithExtinction(it), DELTA_2)
            assertEquals(6.22, Uranus(sun).visualMagnitudeWithExtinction(it), DELTA_2)
            assertEquals(8.09, Neptune(sun).visualMagnitudeWithExtinction(it), DELTA_2)
            assertEquals(14.57, Pluto(sun).visualMagnitudeWithExtinction(it), DELTA_2)
        }

        o.copy(apparentMagnitudeAlgorithm = ApparentMagnitudeAlgorithm.ASTRONOMICAL_ALMANAC_1984).let {
            assertEquals(3.40, Mercury(sun).visualMagnitude(it), DELTA_2)
            assertEquals(-3.91, Venus(sun).visualMagnitude(it), DELTA_2)
            assertEquals(0.53, Mars(sun).visualMagnitude(it), DELTA_2)
            assertEquals(-1.95, Jupiter(sun).visualMagnitude(it), DELTA_2)
            assertEquals(0.63, Saturn(sun).visualMagnitude(it), DELTA_2)
            assertEquals(5.79, Uranus(sun).visualMagnitude(it), DELTA_2)
            assertEquals(7.95, Neptune(sun).visualMagnitude(it), DELTA_2)
            assertEquals(14.40, Pluto(sun).visualMagnitude(it), DELTA_2)

            assertEquals(3.53, Mercury(sun).visualMagnitudeWithExtinction(it), DELTA_2)
            assertEquals(-3.77, Venus(sun).visualMagnitudeWithExtinction(it), DELTA_2)
            assertEquals(1.31, Mars(sun).visualMagnitudeWithExtinction(it), DELTA_2)
            assertEquals(-1.81, Jupiter(sun).visualMagnitudeWithExtinction(it), DELTA_2)
            assertEquals(0.77, Saturn(sun).visualMagnitudeWithExtinction(it), DELTA_2)
            assertEquals(6.22, Uranus(sun).visualMagnitudeWithExtinction(it), DELTA_2)
            assertEquals(8.09, Neptune(sun).visualMagnitudeWithExtinction(it), DELTA_2)
            assertEquals(14.55, Pluto(sun).visualMagnitudeWithExtinction(it), DELTA_2)
        }

        o.copy(apparentMagnitudeAlgorithm = ApparentMagnitudeAlgorithm.MUELLER_1893).let {
            assertEquals(2.14, Mercury(sun).visualMagnitude(it), DELTA_2)
            assertEquals(-3.37, Venus(sun).visualMagnitude(it), DELTA_2)
            assertEquals(0.71, Mars(sun).visualMagnitude(it), DELTA_2)
            assertEquals(-1.49, Jupiter(sun).visualMagnitude(it), DELTA_2)
            assertEquals(0.83, Saturn(sun).visualMagnitude(it), DELTA_2)
            assertEquals(6.13, Uranus(sun).visualMagnitude(it), DELTA_2)
            assertEquals(7.77, Neptune(sun).visualMagnitude(it), DELTA_2)
            assertEquals(14.38, Pluto(sun).visualMagnitude(it), DELTA_2)

            assertEquals(2.28, Mercury(sun).visualMagnitudeWithExtinction(it), DELTA_2)
            assertEquals(-3.23, Venus(sun).visualMagnitudeWithExtinction(it), DELTA_2)
            assertEquals(1.48, Mars(sun).visualMagnitudeWithExtinction(it), DELTA_2)
            assertEquals(-1.35, Jupiter(sun).visualMagnitudeWithExtinction(it), DELTA_2)
            assertEquals(0.97, Saturn(sun).visualMagnitudeWithExtinction(it), DELTA_2)
            assertEquals(6.55, Uranus(sun).visualMagnitudeWithExtinction(it), DELTA_2)
            assertEquals(7.91, Neptune(sun).visualMagnitudeWithExtinction(it), DELTA_2)
            assertEquals(14.54, Pluto(sun).visualMagnitudeWithExtinction(it), DELTA_2)
        }

        o.copy(apparentMagnitudeAlgorithm = ApparentMagnitudeAlgorithm.GENERIC).let {
            assertEquals(3.03, Mercury(sun).visualMagnitude(it), DELTA_2)
            assertEquals(-3.58, Venus(sun).visualMagnitude(it), DELTA_2)
            assertEquals(0.65, Mars(sun).visualMagnitude(it), DELTA_2)
            assertEquals(-1.51, Jupiter(sun).visualMagnitude(it), DELTA_2)
            assertEquals(1.63, Saturn(sun).visualMagnitude(it), DELTA_2)
            assertEquals(5.98, Uranus(sun).visualMagnitude(it), DELTA_2)
            assertEquals(7.95, Neptune(sun).visualMagnitude(it), DELTA_2)
            assertEquals(15.25, Pluto(sun).visualMagnitude(it), DELTA_2)

            assertEquals(3.16, Mercury(sun).visualMagnitudeWithExtinction(it), DELTA_2)
            assertEquals(-3.44, Venus(sun).visualMagnitudeWithExtinction(it), DELTA_2)
            assertEquals(1.42, Mars(sun).visualMagnitudeWithExtinction(it), DELTA_2)
            assertEquals(-1.38, Jupiter(sun).visualMagnitudeWithExtinction(it), DELTA_2)
            assertEquals(1.77, Saturn(sun).visualMagnitudeWithExtinction(it), DELTA_2)
            assertEquals(6.40, Uranus(sun).visualMagnitudeWithExtinction(it), DELTA_2)
            assertEquals(8.10, Neptune(sun).visualMagnitudeWithExtinction(it), DELTA_2)
            assertEquals(15.40, Pluto(sun).visualMagnitudeWithExtinction(it), DELTA_2)
        }
    }

    @Test
    fun constellations() {
        val sun = Sun()
        val earth = Earth(sun)

        val dt = JulianDay(2021, 2, 5, 9, 0, 0) // 2459251.000000

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            dt,
        )

        val jupiter = Jupiter(sun)

        assertEquals(Constellation.CAP, jupiter.constellation(o))
        assertEquals(Constellation.AQR, jupiter.constellation(o.copy(jd = JulianDay(2022, 2, 5, 9, 0, 0))))
        assertEquals(Constellation.PSC, jupiter.constellation(o.copy(jd = JulianDay(2023, 2, 5, 9, 0, 0))))
        assertEquals(Constellation.ARI, jupiter.constellation(o.copy(jd = JulianDay(2024, 2, 5, 9, 0, 0))))
        assertEquals(Constellation.TAU, jupiter.constellation(o.copy(jd = JulianDay(2025, 2, 5, 9, 0, 0))))
        assertEquals(Constellation.GEM, jupiter.constellation(o.copy(jd = JulianDay(2026, 2, 5, 9, 0, 0))))
        assertEquals(Constellation.LEO, jupiter.constellation(o.copy(jd = JulianDay(2027, 2, 5, 9, 0, 0))))
        assertEquals(Constellation.VIR, jupiter.constellation(o.copy(jd = JulianDay(2028, 2, 5, 9, 0, 0))))
        assertEquals(Constellation.LIB, jupiter.constellation(o.copy(jd = JulianDay(2030, 2, 5, 9, 0, 0))))
        assertEquals(Constellation.OPH, jupiter.constellation(o.copy(jd = JulianDay(2031, 2, 5, 9, 0, 0))))
        assertEquals(Constellation.SGR, jupiter.constellation(o.copy(jd = JulianDay(2032, 2, 5, 9, 0, 0))))
        assertEquals(Constellation.CAP, jupiter.constellation(o.copy(jd = JulianDay(2033, 2, 5, 9, 0, 0))))
    }

    @Test
    fun eclipticObliquity() {
        val sun = Sun()
        val earth = Earth(sun)

        val dt = JulianDay(2021, 2, 5, 9, 0, 0) // 2459251.000000

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            dt,
        )

        assertEquals(23.4371, o.computeEclipticObliquity(), DELTA_4, true)
        assertEquals(23.4372, o.copy(jd = JulianDay(2021, 2, 10, 9, 0, 0)).computeEclipticObliquity(), DELTA_4, true)
        assertEquals(23.6893, o.copy(jd = JulianDay(46, 2, 5, 9, 0, 0)).computeEclipticObliquity(), DELTA_4, true)
        assertEquals(24.2108, o.copy(jd = JulianDay(-6046, 2, 5, 9, 0, 0)).computeEclipticObliquity(), DELTA_4, true)
        assertEquals(22.9481, o.copy(jd = JulianDay(6046, 2, 5, 9, 0, 0)).computeEclipticObliquity(), DELTA_4, true)
    }

    @Test
    fun meanSolarDay() {
        val sun = Sun()
        assertEquals(0.4137, Jupiter(sun).meanSolarDay, DELTA_4)
        assertEquals(116.7502, Venus(sun).meanSolarDay, DELTA_4)
    }

    @Test
    fun angularSize() {
        val sun = Sun()
        val earth = Earth(sun)

        val dt = JulianDay(2021, 2, 5, 9, 0, 0) // 2459251.000000

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            dt,
        )

        val jupiter = Jupiter(sun)

        assertEquals(0.00903, jupiter.angularSize(o) * 2, DELTA_5, true)
        assertEquals(0.01053, jupiter.angularSize(o.copy(jd = JulianDay(2021, 5, 5, 9, 0, 0))) * 2, DELTA_5, true)

        val saturn = Saturn(sun)

        assertEquals(0.00982, saturn.angularSize(o) * 2, DELTA_5, true)
        assertEquals(0.01088, saturn.angularSize(o.copy(jd = JulianDay(2021, 5, 5, 9, 0, 0))) * 2, DELTA_5, true)
        assertEquals(0.00422, saturn.spheroidAngularSize(o) * 2, DELTA_5, true)
        assertEquals(0.00467, saturn.spheroidAngularSize(o.copy(jd = JulianDay(2021, 5, 5, 9, 0, 0))) * 2, DELTA_5, true)

        val uranus = Uranus(sun)

        assertEquals(0.00376, uranus.angularSize(o) * 2, DELTA_5, true)
        assertEquals(0.00360, uranus.angularSize(o.copy(jd = JulianDay(2021, 5, 5, 9, 0, 0))) * 2, DELTA_5, true)
        assertEquals(0.00098, uranus.spheroidAngularSize(o) * 2, DELTA_5, true)
        assertEquals(0.00094, uranus.spheroidAngularSize(o.copy(jd = JulianDay(2021, 5, 5, 9, 0, 0))) * 2, DELTA_5, true)

        val neptune = Neptune(sun)

        assertEquals(0.00157, neptune.angularSize(o) * 2, DELTA_5, true)
        assertEquals(0.00158, neptune.angularSize(o.copy(jd = JulianDay(2021, 5, 5, 9, 0, 0))) * 2, DELTA_5, true)
        assertEquals(0.00062, neptune.spheroidAngularSize(o) * 2, DELTA_5, true)
        assertEquals(0.00062, neptune.spheroidAngularSize(o.copy(jd = JulianDay(2021, 5, 5, 9, 0, 0))) * 2, DELTA_5, true)
    }

    @Test
    fun solarEclipse() {
        val sun = Sun()
        val earth = Earth(sun)
        val moon = Moon(earth)

        val site = Location("Rangpur", Degrees(25.9896), Degrees(87.0868), Meter(14.0))

        val a = Observer(earth, site, JulianDay(2009, 7, 22, 5, 57, 28, utcOffset = 6.0))
        assertEquals(69.5333, 4.5405, sun.horizontal(a), DELTA_4, true)
        assertEquals(69.6924, 5.0706, moon.horizontal(a), DELTA_4, true)

        assertEquals(0.0, a.eclipseObscuration(moon), DELTA_2)
        assertEquals(-25.26, sun.visualMagnitudeWithExtinction(a, moon), DELTA_2)

        val b = Observer(earth, site, JulianDay(2009, 7, 22, 6, 37, 28, utcOffset = 6.0))
        assertEquals(61.73, b.eclipseObscuration(moon), DELTA_2)
        assertEquals(-25.09, sun.visualMagnitudeWithExtinction(b, moon), DELTA_2)

        val c = Observer(earth, site, JulianDay(2009, 7, 22, 6, 57, 28, utcOffset = 6.0))
        assertEquals(100.0, c.eclipseObscuration(moon), DELTA_2)
        assertEquals(-16.54, sun.visualMagnitudeWithExtinction(c, moon), DELTA_2)
        assertEquals(75.3662, 17.2680, sun.horizontal(c), DELTA_4, true)
        assertEquals(75.3662, 17.2680, moon.horizontal(c), DELTA_4, true)

        val se = SolarEclipse.compute(c, moon)!!

        assertEquals(87.079, 25.9876, se.position, DELTA_3, true)
        assertEquals(1.033, se.magnitude, DELTA_3)
        assertEquals(252.769, se.azimuth, DELTA_3, true)
    }

    @Test
    fun lunarPhase() {
        val sun = Sun()
        val earth = Earth(sun)
        val moon = Moon(earth)

        val o = Observer(earth, PICO_DOS_DIAS_OBSERVATORY, JulianDay(2021, 2, 11, 15, 0, 0))

        o.copy(jd = JulianDay(2021, 2, 11, 16, 0, 0)).let {
            assertEquals(LunarPhase.NEW_MOON, moon.phase(it))
            assertEquals(29.5, moon.age(it), DELTA_1)
        }

        o.copy(jd = JulianDay(2021, 2, 11, 18, 0, 0)).let {
            assertEquals(LunarPhase.WAXING_CRESCENT, moon.phase(it))
            assertEquals(DELTA_1, moon.age(it), DELTA_1)
        }

        o.copy(jd = JulianDay(2021, 2, 19, 15, 0, 0)).let {
            assertEquals(LunarPhase.FIRST_QUARTER, moon.phase(it))
            assertEquals(7.4, moon.age(it), DELTA_1)
        }

        o.copy(jd = JulianDay(2021, 2, 20, 21, 0, 0)).let {
            assertEquals(LunarPhase.WAXING_GIBBOUS, moon.phase(it))
            assertEquals(8.5, moon.age(it), DELTA_1)
        }

        o.copy(jd = JulianDay(2021, 2, 27, 5, 0, 0)).let {
            assertEquals(LunarPhase.FULL_MOON, moon.phase(it))
            assertEquals(14.8, moon.age(it), DELTA_1)
        }

        o.copy(jd = JulianDay(2021, 2, 27, 21, 0, 0)).let {
            assertEquals(LunarPhase.WANING_GIBBOUS, moon.phase(it))
            assertEquals(15.5, moon.age(it), DELTA_1)
        }

        o.copy(jd = JulianDay(2021, 3, 5, 23, 0, 0)).let {
            assertEquals(LunarPhase.THIRD_QUARTER, moon.phase(it))
            assertEquals(22.2, moon.age(it), DELTA_1)
        }

        o.copy(jd = JulianDay(2021, 3, 7, 2, 0, 0)).let {
            assertEquals(LunarPhase.WANING_CRESCENT, moon.phase(it))
            assertEquals(23.4, moon.age(it), DELTA_1)
        }
    }

    @Test
    fun lunarEclipse() {
        val sun = Sun()
        val earth = Earth(sun)
        val moon = Moon(earth)

        val o = Observer(earth, PICO_DOS_DIAS_OBSERVATORY, JulianDay(2022, 5, 15, 22, 31, 0))

        // https://www.timeanddate.com/eclipse/lunar/2022-may-16

        // Not started
        o.copy().let {
            assertEquals(LunarPhase.WAXING_GIBBOUS, moon.phase(it))
            assertEquals(14.6, moon.age(it), DELTA_1)
            assertEquals(82.5454, 70.3246, moon.horizontal(it), DELTA_3, true)
            assertEquals(1.0, moon.illumination(it), DELTA_1)
            assertTrue(!LunarEclipse.compute(it, moon).isEclipsing)
            assertEquals(-12.26, moon.visualMagnitudeWithExtinction(it), DELTA_2)
        }

        // Penumbral Eclipse begins
        o.copy(jd = JulianDay(2022, 5, 15, 22, 32, 5)).let {
            assertEquals(82.366, 70.5680, moon.horizontal(it), DELTA_3, true)
            val le = LunarEclipse.compute(it, moon)
            assertEquals(0.00003, le.penumbralMagnitude, DELTA_5)
            assertTrue(le.umbralMagnitude < 1E-6)
            assertEquals(-12.26, moon.visualMagnitudeWithExtinction(it), DELTA_2)
        }

        // Full Eclipse begins
        o.copy(jd = JulianDay(2022, 5, 15, 23, 27, 52)).let {
            assertEquals(61.3293, 82.7327, moon.horizontal(it), DELTA_3, true)
            val le = LunarEclipse.compute(it, moon)
            assertEquals(0.95943, le.penumbralMagnitude, DELTA_5)
            assertEquals(0.00002, le.umbralMagnitude, DELTA_5)
            assertEquals(-12.27, moon.visualMagnitudeWithExtinction(it), DELTA_2)
        }

        // Maximum Eclipse
        o.copy(jd = JulianDay(2022, 5, 16, 1, 11, 20)).let {
            assertEquals(LunarPhase.FULL_MOON, moon.phase(it))
            assertEquals(277.2745, 72.3696, moon.horizontal(it), DELTA_3, true)
            val le = LunarEclipse.compute(it, moon)
            assertEquals(2.37272, le.penumbralMagnitude, DELTA_5)
            assertEquals(1.41382, le.umbralMagnitude, DELTA_5)
            assertEquals(-0.85, moon.visualMagnitudeWithExtinction(it), DELTA_2)
        }

        // Full Eclipse ends
        o.copy(jd = JulianDay(2022, 5, 16, 1, 53, 57)).let {
            assertEquals(271.2277, 62.8147, moon.horizontal(it), DELTA_3, true)
            val le = LunarEclipse.compute(it, moon)
            assertEquals(1.95858, le.penumbralMagnitude, DELTA_5)
            assertEquals(0.99988, le.umbralMagnitude, DELTA_5)
            assertEquals(-5.01, moon.visualMagnitudeWithExtinction(it), DELTA_2)
        }

        // Partial Eclipse ends
        o.copy(jd = JulianDay(2022, 5, 16, 2, 55, 8)).let {
            assertEquals(LunarPhase.WANING_GIBBOUS, moon.phase(it))
            assertEquals(265.7446, 49.0918, moon.horizontal(it), DELTA_3, true)
            val le = LunarEclipse.compute(it, moon)
            assertEquals(0.95841, le.penumbralMagnitude, DELTA_5)
            assertTrue(le.umbralMagnitude < 1E-6)
            assertEquals(-12.22, moon.visualMagnitudeWithExtinction(it), DELTA_2)
        }

        // Penumbral Eclipse ends
        o.copy(jd = JulianDay(2022, 5, 16, 3, 50, 51)).let {
            assertEquals(261.6425, 36.7049, moon.horizontal(it), DELTA_3, true)
            assertTrue(!LunarEclipse.compute(it, moon).isEclipsing)
            assertEquals(-12.17, moon.visualMagnitudeWithExtinction(it), DELTA_2)
        }
    }

    @Test
    fun phobosAndDeimos() {
        val sun = Sun()
        val earth = Earth(sun)
        val mars = Mars(sun)
        val phobos = Phobos(mars)
        val deimos = Deimos(mars)

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 8, 5, 9, 0, 0), // 2459432.000000
        )

        // Phobos
        assertEquals(14.96, phobos.visualMagnitude(o), DELTA_2)
        assertEquals(15.43, phobos.visualMagnitudeWithExtinction(o), DELTA_2)
        assertEquals(11.31, phobos.meanOppositionMagnitude, DELTA_2)
        assertEquals(156.17778, 11.0762, phobos.equatorialJ2000(o), DELTA_4, true)
        assertEquals(156.46029, 10.9679, phobos.equatorial(o), DELTA_4, true)
        assertEquals(19.48448, 10.9414, phobos.hourAngle(o), DELTA_4, true)
        assertEquals(70.7166, 15.7174, phobos.horizontal(o), DELTA_4, true)
        assertEquals(-129.2972, 52.0034, phobos.galactic(o), DELTA_4, true)
        assertEquals(92.4084, -31.6069, phobos.supergalactic(o), DELTA_4, true)
        assertEquals(153.8858, 1.0653, phobos.eclipticJ2000(o), DELTA_4, true)
        assertEquals(154.1835, 1.0664, phobos.ecliptic(o), DELTA_4, true)
        assertEquals(-117.3806, phobos.parallacticAngle(o), DELTA_4, true)
        assertEquals(Constellation.LEO, phobos.constellation(o))
        assertEquals(20.9156, phobos.elongation(o), DELTA_4, true)
        assertEquals(12.5759, phobos.phaseAngle(o), DELTA_4, true)
        assertEquals(98.8, 100 * phobos.illumination(o), DELTA_1)
        assertEquals(2.571, phobos.distance(o), DELTA_3)
        assertEquals(1.663, phobos.distanceFromSun(o), DELTA_3)
        assertEquals(2.136, phobos.orbitalVelocity(o), DELTA_3)
        assertEquals(21.272, phobos.heliocentricVelocity(o), DELTA_3)
        assertEquals(0.000003, phobos.angularSize(o) * 2, DELTA_6, true)

        // Phobos
        assertEquals(16.05, deimos.visualMagnitude(o), DELTA_2)
        assertEquals(16.52, deimos.visualMagnitudeWithExtinction(o), DELTA_2)
        assertEquals(12.40, deimos.meanOppositionMagnitude, DELTA_2)
        assertEquals(156.17394, 11.0781, deimos.equatorialJ2000(o), DELTA_4, true)
        assertEquals(156.45645, 10.9699, deimos.equatorial(o), DELTA_4, true)
        assertEquals(19.48474, 10.9433, deimos.hourAngle(o), DELTA_4, true)
        assertEquals(70.7130, 15.7199, deimos.horizontal(o), DELTA_4, true)
        assertEquals(-129.3031, 52.0010, deimos.galactic(o), DELTA_4, true)
        assertEquals(92.4044, -31.6095, deimos.supergalactic(o), DELTA_4, true)
        assertEquals(153.8815, 1.0657, deimos.eclipticJ2000(o), DELTA_4, true)
        assertEquals(154.1792, 1.0668, deimos.ecliptic(o), DELTA_4, true)
        assertEquals((-117.3823), deimos.parallacticAngle(o), DELTA_4, true)
        assertEquals(Constellation.LEO, deimos.constellation(o))
        assertEquals(20.9113, deimos.elongation(o), DELTA_4, true)
        assertEquals(12.5729, deimos.phaseAngle(o), DELTA_4, true)
        assertEquals(98.8, 100 * deimos.illumination(o), DELTA_1)
        assertEquals(2.571, deimos.distance(o), DELTA_3)
        assertEquals(1.663, deimos.distanceFromSun(o), DELTA_3)
        assertEquals(1.350, deimos.orbitalVelocity(o), DELTA_3)
        assertEquals(22.289, deimos.heliocentricVelocity(o), DELTA_3)
        assertEquals(0.000001, deimos.angularSize(o) * 2, DELTA_6, true)
    }

    @Test
    fun jupiterMoons() {
        val sun = Sun()
        val earth = Earth(sun)
        val jupiter = Jupiter(sun)
        val io = Io(jupiter)
        val europa = Europa(jupiter)
        val ganymede = Ganymede(jupiter)
        val callisto = Callisto(jupiter)

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 8, 5, 9, 0, 0), // 2459432.000000
        )

        // Io
        assertEquals(4.99, io.visualMagnitude(o), DELTA_2)
        assertEquals(4.99, io.visualMagnitudeWithExtinction(o), DELTA_2)
        assertEquals(5.02, io.meanOppositionMagnitude, DELTA_2)
        assertEquals(331.37064, -12.9591, io.equatorialJ2000(o), DELTA_4, true)
        assertEquals(331.65676, -12.8553, io.equatorial(o), DELTA_4, true)
        assertEquals(7.80123, -12.8553, io.hourAngle(o), DELTA_4, true)
        assertEquals(246.6340, -18.8934, io.horizontal(o), DELTA_4, true)
        assertEquals(44.5969, -48.7599, io.galactic(o), DELTA_4, true)
        assertEquals(-92.2277, 34.8665, io.supergalactic(o), DELTA_4, true)
        assertEquals(328.8209, -1.1468, io.eclipticJ2000(o), DELTA_4, true)
        assertEquals(329.1186, -1.1480, io.ecliptic(o), DELTA_4, true)
        assertEquals(119.5762, io.parallacticAngle(o), DELTA_4, true)
        assertEquals(Constellation.AQR, io.constellation(o))
        assertEquals(164.1348, io.elongation(o), DELTA_4, true)
        assertEquals(3.161, io.phaseAngle(o), DELTA_4, true)
        assertEquals(99.9, 100 * io.illumination(o), DELTA_1)
        assertEquals(4.046, io.distance(o), DELTA_3)
        assertEquals(5.029, io.distanceFromSun(o), DELTA_3)
        assertEquals(17.354, io.orbitalVelocity(o), DELTA_3)
        assertEquals(23.153, io.heliocentricVelocity(o), DELTA_3)
        assertEquals(0.000344, io.angularSize(o) * 2, DELTA_6, true)
        assertEquals(Triad(18.755, 1.1774, 7.6), io.rts(o), DELTA_3)
        assertEquals(42.4592 / 24.0, io.siderealDay, DELTA_3)
        assertEquals(42.4767 / 24.0, io.meanSolarDay, DELTA_3)

        // Europa
        assertEquals(5.21, europa.visualMagnitude(o), DELTA_2)
        assertEquals(5.21, europa.visualMagnitudeWithExtinction(o), DELTA_2)
        assertEquals(5.29, europa.meanOppositionMagnitude, DELTA_2)
        assertEquals(331.29151, -12.9926, europa.equatorialJ2000(o), DELTA_4, true)
        assertEquals(331.57769, -12.8889, europa.equatorial(o), DELTA_4, true)
        assertEquals(7.80650, -12.8889, europa.hourAngle(o), DELTA_4, true)
        assertEquals(246.5629, -18.9439, europa.horizontal(o), DELTA_4, true)
        assertEquals(44.4993, -48.7058, europa.galactic(o), DELTA_4, true)
        assertEquals(-92.3100, 34.9166, europa.supergalactic(o), DELTA_4, true)
        assertEquals(328.7369, -1.1512, europa.eclipticJ2000(o), DELTA_4, true)
        assertEquals(329.0346, -1.1524, europa.ecliptic(o), DELTA_4, true)
        assertEquals(119.6169, europa.parallacticAngle(o), DELTA_4, true)
        assertEquals(Constellation.AQR, europa.constellation(o))
        assertEquals(164.2182, europa.elongation(o), DELTA_4, true)
        assertEquals(3.1468, europa.phaseAngle(o), DELTA_4, true)
        assertEquals(99.9, 100 * europa.illumination(o), DELTA_1)
        assertEquals(4.042, europa.distance(o), DELTA_3)
        assertEquals(5.026, europa.distanceFromSun(o), DELTA_3)
        assertEquals(13.839, europa.orbitalVelocity(o), DELTA_3)
        assertEquals(10.613, europa.heliocentricVelocity(o), DELTA_3)
        assertEquals(0.000295, europa.angularSize(o) * 2, DELTA_6, true)
        assertEquals(Triad(18.75, 1.172, 7.594), europa.rts(o), DELTA_3)
        assertEquals(85.2283 / 24.0, europa.siderealDay, DELTA_3)
        assertEquals(85.2981 / 24.0, europa.meanSolarDay, DELTA_3)

        // Ganymede
        assertEquals(4.55, ganymede.visualMagnitude(o), DELTA_2)
        assertEquals(4.55, ganymede.visualMagnitudeWithExtinction(o), DELTA_2)
        assertEquals(4.61, ganymede.meanOppositionMagnitude, DELTA_2)
        assertEquals(331.37354, -12.9568, ganymede.equatorialJ2000(o), DELTA_4, true)
        assertEquals(331.65966, -12.8530, ganymede.equatorial(o), DELTA_4, true)
        assertEquals(7.80103, -12.8530, ganymede.hourAngle(o), DELTA_4, true)
        assertEquals(246.6376, -18.8921, ganymede.horizontal(o), DELTA_4, true)
        assertEquals(44.6019, -48.7614, ganymede.galactic(o), DELTA_4, true)
        assertEquals(-92.2236, 34.8652, ganymede.supergalactic(o), DELTA_4, true)
        assertEquals(328.8244, -1.1456, ganymede.eclipticJ2000(o), DELTA_4, true)
        assertEquals(329.1220, -1.1468, ganymede.ecliptic(o), DELTA_4, true)
        assertEquals(119.5744, ganymede.parallacticAngle(o), DELTA_4, true)
        assertEquals(Constellation.AQR, ganymede.constellation(o))
        assertEquals(164.1314, ganymede.elongation(o), DELTA_4, true)
        assertEquals(3.1578, ganymede.phaseAngle(o), DELTA_4, true)
        assertEquals(99.9, 100 * ganymede.illumination(o), DELTA_1)
        assertEquals(4.052, ganymede.distance(o), DELTA_3)
        assertEquals(5.035, ganymede.distanceFromSun(o), DELTA_3)
        assertEquals(10.905, ganymede.orbitalVelocity(o), DELTA_3)
        assertEquals(23.773, ganymede.heliocentricVelocity(o), DELTA_3)
        assertEquals(0.000497, ganymede.angularSize(o) * 2, DELTA_6, true)
        assertEquals(Triad(18.755, 1.1776, 7.6), ganymede.rts(o), DELTA_3)
        assertEquals(171.7092 / 24.0, ganymede.siderealDay, DELTA_3)
        assertEquals(171.9933 / 24.0, ganymede.meanSolarDay, DELTA_3)

        // Callisto
        assertEquals(5.72, callisto.visualMagnitude(o), DELTA_2)
        assertEquals(5.72, callisto.visualMagnitudeWithExtinction(o), DELTA_2)
        assertEquals(5.65, callisto.meanOppositionMagnitude, DELTA_2)
        assertEquals(331.22461, -13.0158, callisto.equatorialJ2000(o), DELTA_4, true)
        assertEquals(331.51085, -12.9121, callisto.equatorial(o), DELTA_4, true)
        assertEquals(7.81096, -12.9121, callisto.hourAngle(o), DELTA_4, true)
        assertEquals(246.5075, -18.9890, callisto.horizontal(o), DELTA_4, true)
        assertEquals(44.4238, -48.6578, callisto.galactic(o), DELTA_4, true)
        assertEquals(-92.3743, 34.9615, callisto.supergalactic(o), DELTA_4, true)
        assertEquals(328.6678, -1.1502, callisto.eclipticJ2000(o), DELTA_4, true)
        assertEquals(328.9654, -1.1514, callisto.ecliptic(o), DELTA_4, true)
        assertEquals(119.6498, callisto.parallacticAngle(o), DELTA_4, true)
        assertEquals(Constellation.AQR, callisto.constellation(o))
        assertEquals(164.2873, callisto.elongation(o), DELTA_4, true)
        assertEquals(3.1252, callisto.phaseAngle(o), DELTA_4, true)
        assertEquals(99.9, 100 * callisto.illumination(o), DELTA_1)
        assertEquals(4.055, callisto.distance(o), DELTA_3)
        assertEquals(5.039, callisto.distanceFromSun(o), DELTA_3)
        assertEquals(8.217, callisto.orbitalVelocity(o), DELTA_3)
        assertEquals(20.595, callisto.heliocentricVelocity(o), DELTA_3)
        assertEquals(0.000455, callisto.angularSize(o) * 2, DELTA_6, true)
        assertEquals(Triad(18.744, 1.1667, 7.591), callisto.rts(o), DELTA_3)
        assertEquals(400.5364 / 24.0, callisto.siderealDay, DELTA_3)
        assertEquals(402.0853 / 24.0, callisto.meanSolarDay, DELTA_3)
    }

    @Test
    fun leda() {
        val sun = Sun()
        val earth = Earth(sun)
        val jupiter = Jupiter(sun)

        // From Stellarium!
        val e = 0.1849972068029340
        val q = Kilometer(11202541.76637748 * (1 - e))
        // val n = KeplerOrbit.computeMeanMotion(e, q)
        val n = 2.0 * M_PI / 242.2566769525344 // period
        val omega = 205.5729208452909
        val longitudeOfPericenter = 133.1912542293990000
        val argOfPericenter = (longitudeOfPericenter - omega) * M_PI_180
        val meanLongitude = 276.4502233853120000
        val MA = (meanLongitude - longitudeOfPericenter) * M_PI_180

        val leda = Satellite(
            jupiter,
            Kilometer(5.0),
            q,
            e,
            Degrees(28.26096889279179),
            Degrees(omega),
            Radians(argOfPericenter),
            JulianDay(2454619.5 - MA / n),
            0.04,
            Radians(n),
            13.5,
        )

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 8, 5, 9, 0, 0), // 2459432.000000
        )

        assertEquals(20.05, leda.visualMagnitude(o), DELTA_2)
        assertEquals(20.05, leda.visualMagnitudeWithExtinction(o), DELTA_2)
        assertEquals(20.20, leda.meanOppositionMagnitude, DELTA_2)
        assertEquals(330.19879, -13.0457, leda.equatorialJ2000(o), DELTA_4, true)
        assertEquals(330.48549, -12.9431, leda.equatorial(o), DELTA_4, true)
        assertEquals(7.87931, -12.9431, leda.hourAngle(o), DELTA_4, true)
        assertEquals(245.9515, -19.8403, leda.horizontal(o), DELTA_4, true)
        assertEquals(43.7184, -47.7753, leda.galactic(o), DELTA_4, true)
        assertEquals(-93.0254, 35.8088, leda.supergalactic(o), DELTA_4, true)
        assertEquals(327.7199, -0.8315, leda.eclipticJ2000(o), DELTA_4, true)
        assertEquals(328.0176, -0.8328, leda.ecliptic(o), DELTA_4, true)
        assertEquals(120.0640, leda.parallacticAngle(o), DELTA_4, true)
        assertEquals(Constellation.AQR, leda.constellation(o))
        assertEquals(165.2533, leda.elongation(o), DELTA_4, true)
        assertEquals(2.9375, leda.phaseAngle(o), DELTA_4, true)
        assertEquals(99.9, 100 * leda.illumination(o), DELTA_1)
        assertEquals(4.051, leda.distance(o), DELTA_3)
        assertEquals(5.039, leda.distanceFromSun(o), DELTA_3)
        assertEquals(3.206, leda.orbitalVelocity(o), DELTA_3)
        assertEquals(13.911, leda.heliocentricVelocity(o), DELTA_3)
        assertEquals(0.000000945, leda.angularSize(o) * 2, DELTA_6, true)
    }

    @Test
    fun asteroids() {
        val sun = Sun()
        val earth = Earth(sun)

        val eccentricity = 0.0757544
        val semiMajorAxis = 2.7681117
        val pericenterDistance = AU(semiMajorAxis * (1.0 - eccentricity))
        val meanMotion = Degrees(0.21400733999999999)
        val ascendingNode = Degrees(80.321799999999996)
        val argOfPericenter = Degrees(72.733239999999995)
        val inclination = Degrees(10.591659999999999)
        val epoch = 2457400.5
        val meanAnomaly = Degrees(181.38132999999999)
        val timeAtPericenter = epoch - (meanAnomaly / meanMotion).value

        val ceres = MinorPlanet(
            sun,
            pericenterDistance,
            eccentricity,
            inclination,
            ascendingNode,
            argOfPericenter,
            JulianDay(timeAtPericenter),
            meanMotion,
            0.09,
            3.4,
            0.12,
            Kilometer(469.7),
        )

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 8, 5, 9, 0, 0), // 2459432.000000
        )

        assertEquals(9.07, ceres.visualMagnitude(o), DELTA_2)
        assertEquals(9.26, ceres.visualMagnitudeWithExtinction(o), DELTA_2)
        assertEquals(6.85, ceres.meanOppositionMagnitude, DELTA_2)
        assertEquals(60.66426, 14.2267, ceres.equatorialJ2000(o), DELTA_4, true)
        assertEquals(60.96341, 14.2854, ceres.equatorial(o), DELTA_4, true)
        assertEquals(1.84675, 14.2717, ceres.hourAngle(o), DELTA_4, true)
        assertEquals(321.0102, 44.2723, ceres.horizontal(o), DELTA_4, true)
        assertEquals(177.2551, -27.8639, ceres.galactic(o), DELTA_4, true)
        assertEquals(-30.6589, -37.9452, ceres.supergalactic(o), DELTA_4, true)
        assertEquals(61.4559, -6.3529, ceres.eclipticJ2000(o), DELTA_4, true)
        assertEquals(61.7536, -6.3503, ceres.ecliptic(o), DELTA_4, true)
        assertEquals(143.1551, ceres.parallacticAngle(o), DELTA_4, true)
        assertEquals(Constellation.TAU, ceres.constellation(o))
        assertEquals(71.6577, ceres.elongation(o), DELTA_4, true)
        assertEquals(19.8699, ceres.phaseAngle(o), DELTA_4, true)
        assertEquals(97.0, 100 * ceres.illumination(o), DELTA_1)
        assertEquals(2.983, ceres.distance(o), DELTA_3)
        assertEquals(2.833, ceres.distanceFromSun(o), DELTA_3)
        assertEquals(17.488, ceres.orbitalVelocity(o), DELTA_3)
        assertEquals(17.488, ceres.heliocentricVelocity(o), DELTA_3)
        assertEquals(0.00012, ceres.angularSize(o) * 2, DELTA_6, true)
        assertEquals(Triad(1.494, 7.147, 12.8), ceres.rts(o), DELTA_3)
        assertEquals(1682.185, ceres.siderealPeriod, DELTA_3)
    }

    @Test
    fun comets() {
        val sun = Sun()
        val earth = Earth(sun)

        val eccentricity = 0.999176
        val pericenterDistance = AU(0.294707)
        val ascendingNode = Degrees(61.0131)
        val argOfPericenter = Degrees(37.267)
        val inclination = Degrees(128.9309)
        val timeAtPericenter = 2459034.175694444

        // "C/2020 F3 (NEOWISE)"
        val neowise = Comet(
            sun,
            pericenterDistance,
            eccentricity,
            inclination,
            ascendingNode,
            argOfPericenter,
            JulianDay(timeAtPericenter),
            albedo = 0.1,
            absoluteMagnitude = 12.5,
            slope = 3.2,
            radius = Kilometer(5.0),
        )

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2020, 7, 5, 9, 0, 0), // 2459036.000000
        )

        assertEquals(8.51, neowise.visualMagnitude(o), DELTA_2)
        assertEquals(8.80, neowise.visualMagnitudeWithExtinction(o), DELTA_2)
        assertEquals(91.28328, 33.2793, neowise.equatorialJ2000(o), DELTA_4, true)
        assertEquals(91.61557, 33.2764, neowise.equatorial(o), DELTA_4, true)
        assertEquals(21.78438, 33.2482, neowise.hourAngle(o), DELTA_4, true)
        assertEquals(30.6174, 25.8483, neowise.horizontal(o), DELTA_4, true)
        assertEquals(178.3385, 5.7911, neowise.galactic(o), DELTA_4, true)
        assertEquals(12.9031, -39.5853, neowise.supergalactic(o), DELTA_4, true)
        assertEquals(91.0888, 9.8449, neowise.eclipticJ2000(o), DELTA_4, true)
        assertEquals(91.3708, 9.8476, neowise.ecliptic(o), DELTA_4, true)
        assertEquals(-145.7715, neowise.parallacticAngle(o), DELTA_4, true)
        assertEquals(Constellation.AUR, neowise.constellation(o))
        assertEquals(15.9060, neowise.elongation(o), DELTA_4, true)
        assertEquals(68.1151, neowise.phaseAngle(o), DELTA_4, true)
        assertEquals(68.6, 100 * neowise.illumination(o), DELTA_1)
        assertEquals(1.090, neowise.distance(o), DELTA_3)
        assertEquals(0.3, neowise.distanceFromSun(o), DELTA_3)
        assertEquals(76.853, neowise.orbitalVelocity(o), DELTA_3)
        assertEquals(Triad(6.212, 11.223, 16.2333), neowise.rts(o), DELTA_3)
        assertEquals(2470547.945, neowise.siderealPeriod, DELTA_3)
        val (diam, length) = neowise.computeComaDiameterAndTailLength(o)
        assertEquals(47630.3, diam.kilometer.value, DELTA_1)
        assertEquals(102246.2, length.kilometer.value, DELTA_1)
    }

    @Test
    fun mimas() {
        val sun = Sun()
        val earth = Earth(sun)
        val saturn = Saturn(sun)
        val mimas = Mimas(saturn)

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 8, 5, 9, 0, 0), // 2459432.000000
        )

        assertEquals(13.04, mimas.visualMagnitude(o), DELTA_2)
        assertEquals(13.04, mimas.visualMagnitudeWithExtinction(o), DELTA_2)
        assertEquals(12.85, mimas.meanOppositionMagnitude, DELTA_2)
        assertEquals(312.29472, -18.5914, mimas.equatorialJ2000(o), DELTA_4, true)
        assertEquals(312.59727, -18.5121, mimas.equatorial(o), DELTA_4, true)
        assertEquals(9.07186, -18.5121, mimas.hourAngle(o), DELTA_4, true)
        assertEquals(229.8433, -30.6096, mimas.horizontal(o), DELTA_4, true)
        assertEquals(27.9918, -34.0507, mimas.galactic(o), DELTA_4, true)
        assertEquals(-113.1629, 45.661, mimas.supergalactic(o), DELTA_4, true)
        assertEquals(309.6344, -0.781, mimas.eclipticJ2000(o), DELTA_4, true)
        assertEquals(309.9321, -0.783, mimas.ecliptic(o), DELTA_4, true)
        assertEquals(131.8881, mimas.parallacticAngle(o), DELTA_4, true)
        assertEquals(Constellation.CAP, mimas.constellation(o))
        assertEquals(176.5488, mimas.elongation(o), DELTA_4, true)
        assertEquals(0.3517, mimas.phaseAngle(o), DELTA_4, true)
        assertEquals(100.0, 100 * mimas.illumination(o), DELTA_1)
        assertEquals(8.936, mimas.distance(o), DELTA_3)
        assertEquals(9.949, mimas.distanceFromSun(o), DELTA_3)
        assertEquals(14.385, mimas.orbitalVelocity(o), DELTA_3)
        assertEquals(11.323, mimas.heliocentricVelocity(o), DELTA_3)
        assertEquals(0.0000169, mimas.angularSize(o) * 2, DELTA_6, true)
    }

    @Test
    fun enceladus() {
        val sun = Sun()
        val earth = Earth(sun)
        val saturn = Saturn(sun)
        val enceladus = Enceladus(saturn)

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 8, 5, 9, 0, 0), // 2459432.000000
        )

        assertEquals(11.85, enceladus.visualMagnitude(o), DELTA_2)
        assertEquals(11.85, enceladus.visualMagnitudeWithExtinction(o), DELTA_2)
        assertEquals(11.65, enceladus.meanOppositionMagnitude, DELTA_2)
        assertEquals(312.29955, -18.5869, enceladus.equatorialJ2000(o), DELTA_4, true)
        assertEquals(312.60209, -18.5076, enceladus.equatorial(o), DELTA_4, true)
        assertEquals(9.07154, -18.5076, enceladus.hourAngle(o), DELTA_4, true)
        assertEquals(229.8508, -30.6092, enceladus.horizontal(o), DELTA_4, true)
        assertEquals(27.9989, -34.0534, enceladus.galactic(o), DELTA_4, true)
        assertEquals(-113.1538, 45.6614, enceladus.supergalactic(o), DELTA_4, true)
        assertEquals(309.6400, -0.7779, enceladus.eclipticJ2000(o), DELTA_4, true)
        assertEquals(309.9377, -0.7799, enceladus.ecliptic(o), DELTA_4, true)
        assertEquals(131.8827, enceladus.parallacticAngle(o), DELTA_4, true)
        assertEquals(Constellation.CAP, enceladus.constellation(o))
        assertEquals(176.5550, enceladus.elongation(o), DELTA_4, true)
        assertEquals(0.351, enceladus.phaseAngle(o), DELTA_4, true)
        assertEquals(100.0, 100 * enceladus.illumination(o), DELTA_1)
        assertEquals(8.938, enceladus.distance(o), DELTA_3)
        assertEquals(9.951, enceladus.distanceFromSun(o), DELTA_3)
        assertEquals(12.582, enceladus.orbitalVelocity(o), DELTA_3)
        assertEquals(21.516, enceladus.heliocentricVelocity(o), DELTA_3)
        assertEquals(0.0000216, enceladus.angularSize(o) * 2, DELTA_6, true)
    }

    @Test
    fun tethys() {
        val sun = Sun()
        val earth = Earth(sun)
        val saturn = Saturn(sun)
        val tethys = Tethys(saturn)

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 8, 5, 9, 0, 0), // 2459432.000000
        )

        assertEquals(10.35, tethys.visualMagnitude(o), DELTA_2)
        assertEquals(10.35, tethys.visualMagnitudeWithExtinction(o), DELTA_2)
        assertEquals(10.15, tethys.meanOppositionMagnitude, DELTA_2)
        assertEquals(312.29112, -18.5865, tethys.equatorialJ2000(o), DELTA_4, true)
        assertEquals(312.59366, -18.5072, tethys.equatorial(o), DELTA_4, true)
        assertEquals(9.07210, -18.5072, tethys.hourAngle(o), DELTA_4, true)
        assertEquals(229.8449, -30.6154, tethys.horizontal(o), DELTA_4, true)
        assertEquals(27.9959, -34.0458, tethys.galactic(o), DELTA_4, true)
        assertEquals(-113.1620, 45.6669, tethys.supergalactic(o), DELTA_4, true)
        assertEquals(309.6324, -0.7754, tethys.eclipticJ2000(o), DELTA_4, true)
        assertEquals(309.9301, -0.7774, tethys.ecliptic(o), DELTA_4, true)
        assertEquals(131.8884, tethys.parallacticAngle(o), DELTA_4, true)
        assertEquals(Constellation.CAP, tethys.constellation(o))
        assertEquals(176.5482, tethys.elongation(o), DELTA_4, true)
        assertEquals(0.3517, tethys.phaseAngle(o), DELTA_4, true)
        assertEquals(100.0, 100 * tethys.illumination(o), DELTA_1)
        assertEquals(8.938, tethys.distance(o), DELTA_3)
        assertEquals(9.951, tethys.distanceFromSun(o), DELTA_3)
        assertEquals(11.350, tethys.orbitalVelocity(o), DELTA_3)
        assertEquals(18.887, tethys.heliocentricVelocity(o), DELTA_3)
        assertEquals(0.0000455, tethys.angularSize(o) * 2, DELTA_6, true)
    }

    @Test
    fun dione() {
        val sun = Sun()
        val earth = Earth(sun)
        val saturn = Saturn(sun)
        val dione = Dione(saturn)

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 8, 5, 9, 0, 0), // 2459432.000000
        )

        assertEquals(10.54, dione.visualMagnitude(o), DELTA_2)
        assertEquals(10.54, dione.visualMagnitudeWithExtinction(o), DELTA_2)
        assertEquals(10.35, dione.meanOppositionMagnitude, DELTA_2)
        assertEquals(312.28547, -18.5909, dione.equatorialJ2000(o), DELTA_4, true)
        assertEquals(312.58802, -18.5116, dione.equatorial(o), DELTA_4, true)
        assertEquals(9.07248, -18.5116, dione.hourAngle(o), DELTA_4, true)
        assertEquals(229.8370, -30.6164, dione.horizontal(o), DELTA_4, true)
        assertEquals(27.9887, -34.0424, dione.galactic(o), DELTA_4, true)
        assertEquals(-113.1719, 45.6671, dione.supergalactic(o), DELTA_4, true)
        assertEquals(309.6261, -0.7781, dione.eclipticJ2000(o), DELTA_4, true)
        assertEquals(309.9238, -0.7802, dione.ecliptic(o), DELTA_4, true)
        assertEquals(131.8942, dione.parallacticAngle(o), DELTA_4, true)
        assertEquals(Constellation.CAP, dione.constellation(o))
        assertEquals(176.5414, dione.elongation(o), DELTA_4, true)
        assertEquals(0.3525, dione.phaseAngle(o), DELTA_4, true)
        assertEquals(100.0, 100 * dione.illumination(o), DELTA_1)
        assertEquals(8.936, dione.distance(o), DELTA_3)
        assertEquals(9.948, dione.distanceFromSun(o), DELTA_3)
        assertEquals(10.025, dione.orbitalVelocity(o), DELTA_3)
        assertEquals(10.778, dione.heliocentricVelocity(o), DELTA_3)
        assertEquals(0.0000481, dione.angularSize(o) * 2, DELTA_6, true)
    }

    @Test
    fun rhea() {
        val sun = Sun()
        val earth = Earth(sun)
        val saturn = Saturn(sun)
        val rhea = Rhea(saturn)

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 8, 5, 9, 0, 0), // 2459432.000000
        )

        assertEquals(9.84, rhea.visualMagnitude(o), DELTA_2)
        assertEquals(9.84, rhea.visualMagnitudeWithExtinction(o), DELTA_2)
        assertEquals(9.65, rhea.meanOppositionMagnitude, DELTA_2)
        assertEquals(312.29998, -18.5974, rhea.equatorialJ2000(o), DELTA_4, true)
        assertEquals(312.60254, -18.5180, rhea.equatorial(o), DELTA_4, true)
        assertEquals(9.07151, -18.5180, rhea.hourAngle(o), DELTA_4, true)
        assertEquals(229.8421, -30.6019, rhea.horizontal(o), DELTA_4, true)
        assertEquals(27.9872, -34.0575, rhea.galactic(o), DELTA_4, true)
        assertEquals(-113.1632, 45.6532, rhea.supergalactic(o), DELTA_4, true)
        assertEquals(309.6376, -0.7881, rhea.eclipticJ2000(o), DELTA_4, true)
        assertEquals(309.9353, -0.7901, rhea.ecliptic(o), DELTA_4, true)
        assertEquals(131.8870, rhea.parallacticAngle(o), DELTA_4, true)
        assertEquals(Constellation.CAP, rhea.constellation(o))
        assertEquals(176.5503, rhea.elongation(o), DELTA_4, true)
        assertEquals(0.3516, rhea.phaseAngle(o), DELTA_4, true)
        assertEquals(100.0, 100 * rhea.illumination(o), DELTA_1)
        assertEquals(8.933, rhea.distance(o), DELTA_3)
        assertEquals(9.946, rhea.distanceFromSun(o), DELTA_3)
        assertEquals(8.477, rhea.orbitalVelocity(o), DELTA_3)
        assertEquals(3.145, rhea.heliocentricVelocity(o), DELTA_3)
        assertEquals(0.0000654, rhea.angularSize(o) * 2, DELTA_6, true)
    }

    @Test
    fun titan() {
        val sun = Sun()
        val earth = Earth(sun)
        val saturn = Saturn(sun)
        val titan = Titan(saturn)

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 8, 5, 9, 0, 0), // 2459432.000000
        )

        assertEquals(8.47, titan.visualMagnitude(o), DELTA_2)
        assertEquals(8.47, titan.visualMagnitudeWithExtinction(o), DELTA_2)
        assertEquals(8.27, titan.meanOppositionMagnitude, DELTA_2)
        assertEquals(312.34177, -18.5843, titan.equatorialJ2000(o), DELTA_4, true)
        assertEquals(312.64428, -18.5049, titan.equatorial(o), DELTA_4, true)
        assertEquals(9.06873, -18.5049, titan.hourAngle(o), DELTA_4, true)
        assertEquals(229.8842, -30.5812, titan.horizontal(o), DELTA_4, true)
        assertEquals(28.0189, -34.0899, titan.galactic(o), DELTA_4, true)
        assertEquals(-113.1082, 45.6370, titan.supergalactic(o), DELTA_4, true)
        assertEquals(309.6793, -0.7860, titan.eclipticJ2000(o), DELTA_4, true)
        assertEquals(309.9769, -0.7880, titan.ecliptic(o), DELTA_4, true)
        assertEquals(131.8524, titan.parallacticAngle(o), DELTA_4, true)
        assertEquals(Constellation.CAP, titan.constellation(o))
        assertEquals(176.5914, titan.elongation(o), DELTA_4, true)
        assertEquals(0.3471, titan.phaseAngle(o), DELTA_4, true)
        assertEquals(100.0, 100 * titan.illumination(o), DELTA_1)
        assertEquals(8.942, titan.distance(o), DELTA_3)
        assertEquals(9.955, titan.distanceFromSun(o), DELTA_3)
        assertEquals(5.729, titan.orbitalVelocity(o), DELTA_3)
        assertEquals(13.440, titan.heliocentricVelocity(o), DELTA_3)
        assertEquals(0.00022, titan.angularSize(o) * 2, DELTA_6, true)
    }

    @Test
    fun iapetus() {
        val sun = Sun()
        val earth = Earth(sun)
        val saturn = Saturn(sun)
        val iapetus = Iapetus(saturn)

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 8, 5, 9, 0, 0), // 2459432.000000
        )

        assertEquals(11.25, iapetus.visualMagnitude(o), DELTA_2)
        assertEquals(11.25, iapetus.visualMagnitudeWithExtinction(o), DELTA_2)
        assertEquals(11.05, iapetus.meanOppositionMagnitude, DELTA_2)
        assertEquals(312.43277, -18.5884, iapetus.equatorialJ2000(o), DELTA_4, true)
        assertEquals(312.73524, -18.5088, iapetus.equatorial(o), DELTA_4, true)
        assertEquals(9.06266, -18.5088, iapetus.hourAngle(o), DELTA_4, true)
        assertEquals(229.9475, -30.5143, iapetus.horizontal(o), DELTA_4, true)
        assertEquals(28.0511, -34.1721, iapetus.galactic(o), DELTA_4, true)
        assertEquals(-113.0194, 45.5770, iapetus.supergalactic(o), DELTA_4, true)
        assertEquals(309.7613, -0.8131, iapetus.eclipticJ2000(o), DELTA_4, true)
        assertEquals(310.0589, -0.8151, iapetus.ecliptic(o), DELTA_4, true)
        assertEquals(131.7913, iapetus.parallacticAngle(o), DELTA_4, true)
        assertEquals(Constellation.CAP, iapetus.constellation(o))
        assertEquals(176.6646, iapetus.elongation(o), DELTA_4, true)
        assertEquals(0.3394, iapetus.phaseAngle(o), DELTA_4, true)
        assertEquals(100.0, 100 * iapetus.illumination(o), DELTA_1)
        assertEquals(8.949, iapetus.distance(o), DELTA_3)
        assertEquals(9.962, iapetus.distanceFromSun(o), DELTA_3)
        assertEquals(3.359, iapetus.orbitalVelocity(o), DELTA_3)
        assertEquals(11.429, iapetus.heliocentricVelocity(o), DELTA_3)
        assertEquals(0.0000628, iapetus.angularSize(o) * 2, DELTA_6, true)
    }

    @Test
    fun hyperion() {
        val sun = Sun()
        val earth = Earth(sun)
        val saturn = Saturn(sun)
        val hyperion = Hyperion(saturn)

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 8, 5, 9, 0, 0), // 2459432.000000
        )

        assertEquals(14.38, hyperion.visualMagnitude(o), DELTA_2)
        assertEquals(14.38, hyperion.visualMagnitudeWithExtinction(o), DELTA_2)
        assertEquals(14.18, hyperion.meanOppositionMagnitude, DELTA_2)
        assertEquals(312.28259, -18.5723, hyperion.equatorialJ2000(o), DELTA_4, true)
        assertEquals(312.58511, -18.4930, hyperion.equatorial(o), DELTA_4, true)
        assertEquals(9.07267, -18.4930, hyperion.hourAngle(o), DELTA_4, true)
        assertEquals(229.8509, -30.6309, hyperion.horizontal(o), DELTA_4, true)
        assertEquals(28.0085, -34.0332, hyperion.galactic(o), DELTA_4, true)
        assertEquals(-113.1573, 45.6829, hyperion.supergalactic(o), DELTA_4, true)
        assertEquals(309.6285, -0.7595, hyperion.eclipticJ2000(o), DELTA_4, true)
        assertEquals(309.9261, -0.7615, hyperion.ecliptic(o), DELTA_4, true)
        assertEquals(131.8880, hyperion.parallacticAngle(o), DELTA_4, true)
        assertEquals(Constellation.CAP, hyperion.constellation(o))
        assertEquals(176.5478, hyperion.elongation(o), DELTA_4, true)
        assertEquals(0.3515, hyperion.phaseAngle(o), DELTA_4, true)
        assertEquals(100.0, 100 * hyperion.illumination(o), DELTA_1)
        assertEquals(8.945, hyperion.distance(o), DELTA_3)
        assertEquals(9.957, hyperion.distanceFromSun(o), DELTA_3)
        assertEquals(5.669, hyperion.orbitalVelocity(o), DELTA_3)
        assertEquals(14.657, hyperion.heliocentricVelocity(o), DELTA_3)
        assertEquals(0.0000115, hyperion.angularSize(o) * 2, DELTA_6, true)
    }

    @Test
    fun janus() {
        val sun = Sun()
        val earth = Earth(sun)
        val saturn = Saturn(sun)

        // From Stellarium!
        val e = 0.01039350532665206
        val q = Kilometer(152044.1833237598760096396 * (1 - e))
        val n = KeplerOrbit.Companion.computeMeanMotion(0.7000394386551769)
        val omega = Degrees(169.4950252408618)
        val longitudeOfPericenter = Degrees(481.1046065786531)
        val argOfPericenter = KeplerOrbit.computeArgOfPericenter(longitudeOfPericenter, omega)
        val meanLongitude = Degrees(500.48456523833078)
        val MA = KeplerOrbit.computeMeanAnomaly(meanLongitude, longitudeOfPericenter)
        val t0 = KeplerOrbit.computeTimeAtPericenter(JulianDay(2457939.5), MA, n)

        val janus = Satellite(
            saturn,
            Kilometer(89.2),
            q,
            e,
            Degrees(0.4924530371249293),
            omega,
            argOfPericenter,
            t0,
            0.5,
            n,
            4.9,
        )

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 8, 5, 9, 0, 0), // 2459432.000000
        )

        assertEquals(14.64, janus.visualMagnitude(o), DELTA_2)
        assertEquals(14.64, janus.visualMagnitudeWithExtinction(o), DELTA_2)
        assertEquals(14.45, janus.meanOppositionMagnitude, DELTA_2)
        assertEquals(312.29495, -18.5886, janus.equatorialJ2000(o), DELTA_4, true)
        assertEquals(312.59749, -18.5093, janus.equatorial(o), DELTA_4, true)
        assertEquals(9.07185, -18.5093, janus.hourAngle(o), DELTA_4, true)
        assertEquals(229.8459, -30.6113, janus.horizontal(o), DELTA_4, true)
        assertEquals(27.9951, -34.0500, janus.galactic(o), DELTA_4, true)
        assertEquals(-113.1601, 45.6629, janus.supergalactic(o), DELTA_4, true)
        assertEquals(309.6354, -0.7784, janus.eclipticJ2000(o), DELTA_4, true)
        assertEquals(309.9330, -0.7804, janus.ecliptic(o), DELTA_4, true)
        assertEquals(131.8867, janus.parallacticAngle(o), DELTA_4, true)
        assertEquals(Constellation.CAP, janus.constellation(o))
        assertEquals(176.5503, janus.elongation(o), DELTA_4, true)
        assertEquals(0.3515, janus.phaseAngle(o), DELTA_4, true)
        assertEquals(100.0, 100 * janus.illumination(o), DELTA_1)
        assertEquals(8.937, janus.distance(o), DELTA_3)
        assertEquals(9.950, janus.distanceFromSun(o), DELTA_3)
        assertEquals(15.962, janus.orbitalVelocity(o), DELTA_3)
        assertEquals(22.182, janus.heliocentricVelocity(o), DELTA_3)
        assertEquals(0.0000076, janus.angularSize(o) * 2, DELTA_6, true)
    }

    @Test
    fun miranda() {
        val sun = Sun()
        val earth = Earth(sun)
        val uranus = Uranus(sun)
        val miranda = Miranda(uranus)

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 8, 5, 9, 0, 0), // 2459432.000000
        )

        assertEquals(16.55, miranda.visualMagnitude(o), DELTA_2)
        assertEquals(16.81, miranda.visualMagnitudeWithExtinction(o), DELTA_2)
        assertEquals(16.31, miranda.meanOppositionMagnitude, DELTA_2)
        assertEquals(42.07319, 15.7623, miranda.equatorialJ2000(o), DELTA_4, true)
        assertEquals(42.36851, 15.8508, miranda.equatorial(o), DELTA_4, true)
        assertEquals(3.08560, 15.8331, miranda.hourAngle(o), DELTA_4, true)
        assertEquals(306.0864, 30.6332, miranda.horizontal(o), DELTA_4, true)
        assertEquals(159.8680, -38.5876, miranda.galactic(o), DELTA_4, true)
        assertEquals(-39.1050, -21.4656, miranda.supergalactic(o), DELTA_4, true)
        assertEquals(44.4063, -0.4176, miranda.eclipticJ2000(o), DELTA_4, true)
        assertEquals(44.7039, -0.4154, miranda.ecliptic(o), DELTA_4, true)
        assertEquals(129.1165, miranda.parallacticAngle(o), DELTA_4, true)
        assertEquals(Constellation.ARI, miranda.constellation(o))
        assertEquals(88.5899, miranda.elongation(o), DELTA_4, true)
        assertEquals(2.9442, miranda.phaseAngle(o), DELTA_4, true)
        assertEquals(99.9, 100 * miranda.illumination(o), DELTA_1)
        assertEquals(19.742, miranda.distance(o), DELTA_3)
        assertEquals(19.743, miranda.distanceFromSun(o), DELTA_3)
        assertEquals(6.680, miranda.orbitalVelocity(o), DELTA_3)
        assertEquals(12.021, miranda.heliocentricVelocity(o), DELTA_3)
        assertEquals(0.00000914, miranda.angularSize(o) * 2, DELTA_6, true)
    }

    @Test
    fun ariel() {
        val sun = Sun()
        val earth = Earth(sun)
        val uranus = Uranus(sun)
        val ariel = Ariel(uranus)

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 8, 5, 9, 0, 0), // 2459432.000000
        )

        assertEquals(14.40, ariel.visualMagnitude(o), DELTA_2)
        assertEquals(14.66, ariel.visualMagnitudeWithExtinction(o), DELTA_2)
        assertEquals(14.16, ariel.meanOppositionMagnitude, DELTA_2)
        assertEquals(42.07555, 15.7616, ariel.equatorialJ2000(o), DELTA_4, true)
        assertEquals(42.37087, 15.8500, ariel.equatorial(o), DELTA_4, true)
        assertEquals(3.08545, 15.8323, ariel.hourAngle(o), DELTA_4, true)
        assertEquals(306.0873, 30.6354, ariel.horizontal(o), DELTA_4, true)
        assertEquals(159.8710, -38.5869, ariel.galactic(o), DELTA_4, true)
        assertEquals(-39.1048, -21.4680, ariel.supergalactic(o), DELTA_4, true)
        assertEquals(44.4082, -0.4190, ariel.eclipticJ2000(o), DELTA_4, true)
        assertEquals(44.7058, -0.4168, ariel.ecliptic(o), DELTA_4, true)
        assertEquals(129.1176, ariel.parallacticAngle(o), DELTA_4, true)
        assertEquals(Constellation.ARI, ariel.constellation(o))
        assertEquals(88.5880, ariel.elongation(o), DELTA_4, true)
        assertEquals(2.9443, ariel.phaseAngle(o), DELTA_4, true)
        assertEquals(99.9, 100 * ariel.illumination(o), DELTA_1)
        assertEquals(19.742, ariel.distance(o), DELTA_3)
        assertEquals(19.743, ariel.distanceFromSun(o), DELTA_3)
        assertEquals(5.508, ariel.orbitalVelocity(o), DELTA_3)
        assertEquals(11.561, ariel.heliocentricVelocity(o), DELTA_3)
        assertEquals(0.0000224, ariel.angularSize(o) * 2, DELTA_6, true)
    }

    @Test
    fun umbriel() {
        val sun = Sun()
        val earth = Earth(sun)
        val uranus = Uranus(sun)
        val umbriel = Umbriel(uranus)

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 8, 5, 9, 0, 0), // 2459432.000000
        )

        assertEquals(15.05, umbriel.visualMagnitude(o), DELTA_2)
        assertEquals(15.31, umbriel.visualMagnitudeWithExtinction(o), DELTA_2)
        assertEquals(14.81, umbriel.meanOppositionMagnitude, DELTA_2)
        assertEquals(42.07649, 15.7605, umbriel.equatorialJ2000(o), DELTA_4, true)
        assertEquals(42.37181, 15.8490, umbriel.equatorial(o), DELTA_4, true)
        assertEquals(3.08538, 15.8313, umbriel.hourAngle(o), DELTA_4, true)
        assertEquals(306.0870, 30.6368, umbriel.horizontal(o), DELTA_4, true)
        assertEquals(159.8727, -38.5873, umbriel.galactic(o), DELTA_4, true)
        assertEquals(-39.1054, -21.4693, umbriel.supergalactic(o), DELTA_4, true)
        assertEquals(44.4088, -0.4202, umbriel.eclipticJ2000(o), DELTA_4, true)
        assertEquals(44.7064, -0.4181, umbriel.ecliptic(o), DELTA_4, true)
        assertEquals(129.1177, umbriel.parallacticAngle(o), DELTA_4, true)
        assertEquals(Constellation.ARI, umbriel.constellation(o))
        assertEquals(88.5874, umbriel.elongation(o), DELTA_4, true)
        assertEquals(2.9443, umbriel.phaseAngle(o), DELTA_4, true)
        assertEquals(99.9, 100 * umbriel.illumination(o), DELTA_1)
        assertEquals(19.742, umbriel.distance(o), DELTA_3)
        assertEquals(19.743, umbriel.distanceFromSun(o), DELTA_3)
        assertEquals(4.659, umbriel.orbitalVelocity(o), DELTA_3)
        assertEquals(10.719, umbriel.heliocentricVelocity(o), DELTA_3)
        assertEquals(0.0000226, umbriel.angularSize(o) * 2, DELTA_6, true)
    }

    @Test
    fun titania() {
        val sun = Sun()
        val earth = Earth(sun)
        val uranus = Uranus(sun)
        val titania = Titania(uranus)

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 8, 5, 9, 0, 0), // 2459432.000000
        )

        assertEquals(13.97, titania.visualMagnitude(o), DELTA_2)
        assertEquals(14.23, titania.visualMagnitudeWithExtinction(o), DELTA_2)
        assertEquals(13.73, titania.meanOppositionMagnitude, DELTA_2)
        assertEquals(42.07334, 15.7563, titania.equatorialJ2000(o), DELTA_4, true)
        assertEquals(42.36865, 15.8448, titania.equatorial(o), DELTA_4, true)
        assertEquals(3.08559, 15.8271, titania.hourAngle(o), DELTA_4, true)
        assertEquals(306.0810, 30.6371, titania.horizontal(o), DELTA_4, true)
        assertEquals(159.8724, -38.5925, titania.galactic(o), DELTA_4, true)
        assertEquals(-39.1108, -21.4682, titania.supergalactic(o), DELTA_4, true)
        assertEquals(44.4046, -0.4233, titania.eclipticJ2000(o), DELTA_4, true)
        assertEquals(44.7023, -0.4212, titania.ecliptic(o), DELTA_4, true)
        assertEquals(129.1138, titania.parallacticAngle(o), DELTA_4, true)
        assertEquals(Constellation.ARI, titania.constellation(o))
        assertEquals(88.5916, titania.elongation(o), DELTA_4, true)
        assertEquals(2.9442, titania.phaseAngle(o), DELTA_4, true)
        assertEquals(99.9, 100 * titania.illumination(o), DELTA_1)
        assertEquals(19.742, titania.distance(o), DELTA_3)
        assertEquals(19.744, titania.distanceFromSun(o), DELTA_3)
        assertEquals(3.650, titania.orbitalVelocity(o), DELTA_3)
        assertEquals(9.662, titania.heliocentricVelocity(o), DELTA_3)
        assertEquals(0.0000306, titania.angularSize(o) * 2, DELTA_6, true)
    }

    @Test
    fun oberon() {
        val sun = Sun()
        val earth = Earth(sun)
        val uranus = Uranus(sun)
        val oberon = Oberon(uranus)

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 8, 5, 9, 0, 0), // 2459432.000000
        )

        assertEquals(14.18, oberon.visualMagnitude(o), DELTA_2)
        assertEquals(14.44, oberon.visualMagnitudeWithExtinction(o), DELTA_2)
        assertEquals(13.94, oberon.meanOppositionMagnitude, DELTA_2)
        assertEquals(42.07790, 15.7543, oberon.equatorialJ2000(o), DELTA_4, true)
        assertEquals(42.37321, 15.8428, oberon.equatorial(o), DELTA_4, true)
        assertEquals(3.08529, 15.8251, oberon.hourAngle(o), DELTA_4, true)
        assertEquals(306.0825, 30.6417, oberon.horizontal(o), DELTA_4, true)
        assertEquals(159.8785, -38.5917, oberon.galactic(o), DELTA_4, true)
        assertEquals(-39.1108, -21.4730, oberon.supergalactic(o), DELTA_4, true)
        assertEquals(44.4082, -0.4265, oberon.eclipticJ2000(o), DELTA_4, true)
        assertEquals(44.7059, -0.4244, oberon.ecliptic(o), DELTA_4, true)
        assertEquals(129.1158, oberon.parallacticAngle(o), DELTA_4, true)
        assertEquals(Constellation.ARI, oberon.constellation(o))
        assertEquals(88.5880, oberon.elongation(o), DELTA_4, true)
        assertEquals(2.9443, oberon.phaseAngle(o), DELTA_4, true)
        assertEquals(99.9, 100 * oberon.illumination(o), DELTA_1)
        assertEquals(19.741, oberon.distance(o), DELTA_3)
        assertEquals(19.743, oberon.distanceFromSun(o), DELTA_3)
        assertEquals(3.150, oberon.orbitalVelocity(o), DELTA_3)
        assertEquals(9.396, oberon.heliocentricVelocity(o), DELTA_3)
        assertEquals(0.0000295, oberon.angularSize(o) * 2, DELTA_6, true)
    }

    @Test
    fun ophelia() {
        val sun = Sun()
        val earth = Earth(sun)
        val uranus = Uranus(sun)

        // From Stellarium!
        val e = 0.01076472821818208
        val q = Kilometer(53842.62468965198265381549 * (1 - e))
        // val n = KeplerOrbit.computeMeanMotion(e, q)
        val n = 2.0 * M_PI / -0.3774574820706313 // period
        val omega = 167.6374567371435
        val longitudeOfPericenter = 181.2480236190866
        val argOfPericenter = (longitudeOfPericenter - omega) * M_PI_180
        val meanLongitude = 232.27393980920791
        val MA = (meanLongitude - longitudeOfPericenter) * M_PI_180

        val ophelia = Satellite(
            uranus,
            Kilometer(15.0),
            q,
            e,
            Degrees(1.706664669034183),
            Degrees(omega),
            Radians(argOfPericenter),
            JulianDay(2457939.5 - MA / n),
            0.07,
            Radians(n),
            11.1,
        )

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 8, 5, 9, 0, 0), // 2459432.000000
        )

        assertEquals(24.05, ophelia.visualMagnitude(o), DELTA_2)
        assertEquals(24.31, ophelia.visualMagnitudeWithExtinction(o), DELTA_2)
        assertEquals(23.81, ophelia.meanOppositionMagnitude, DELTA_2)
        assertEquals(42.07339, 15.7638, ophelia.equatorialJ2000(o), DELTA_4, true)
        assertEquals(42.36871, 15.8523, ophelia.equatorial(o), DELTA_4, true)
        assertEquals(3.08559, 15.8346, ophelia.hourAngle(o), DELTA_4, true)
        assertEquals(306.0879, 30.6324, ophelia.horizontal(o), DELTA_4, true)
        assertEquals(159.8671, -38.5862, ophelia.galactic(o), DELTA_4, true)
        assertEquals(-39.1035, -21.4652, ophelia.supergalactic(o), DELTA_4, true)
        assertEquals(44.4069, -0.4162, ophelia.eclipticJ2000(o), DELTA_4, true)
        assertEquals(44.7045, -0.4140, ophelia.ecliptic(o), DELTA_4, true)
        assertEquals(129.1173, ophelia.parallacticAngle(o), DELTA_4, true)
        assertEquals(Constellation.ARI, ophelia.constellation(o))
        assertEquals(88.5893, ophelia.elongation(o), DELTA_4, true)
        assertEquals(2.9442, ophelia.phaseAngle(o), DELTA_4, true)
        assertEquals(99.9, 100 * ophelia.illumination(o), DELTA_1)
        assertEquals(19.742, ophelia.distance(o), DELTA_3)
        assertEquals(19.743, ophelia.distanceFromSun(o), DELTA_3)
        assertEquals(10.306, ophelia.orbitalVelocity(o), DELTA_3)
        assertEquals(8.892, ophelia.heliocentricVelocity(o), DELTA_3)
    }

    @Test
    fun deepSky() {
        val ngc4565 = DeepSky(
            mB = 13.609999656677246,
            mV = 12.430000305175781,
            majorAxisSize = Degrees(0.26499998569488525),
            minorAxisSize = Degrees(0.030833333730697632),
            distance = LightYear(5.679008041430424E7),
            posEquJ2000 = EquatorialCoord(Radians(3.300185203552246), Radians(0.4535655677318573)),
        )

        val sun = Sun()
        val earth = Earth(sun)

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 8, 5, 15, 0, 0), // 2459432.250000
        )

        assertEquals(0.265, ngc4565.majorAxisSize, DELTA_3, isDegrees = true)
        assertEquals(0.03083, ngc4565.minorAxisSize, DELTA_3, isDegrees = true)
        assertEquals(1.18, ngc4565.mB - ngc4565.mV, DELTA_2)

        assertEquals(12.43, ngc4565.visualMagnitude(o), DELTA_2)
        assertEquals(12.63, ngc4565.visualMagnitudeWithExtinction(o), DELTA_2)
        assertEquals(189.08668, 25.9874, ngc4565.equatorialJ2000(o), DELTA_4, true)
        assertEquals(189.35088, 25.8702, ngc4565.equatorial(o), DELTA_4, true)
        assertEquals(23.30503, 25.8512, ngc4565.hourAngle(o), DELTA_4, true)
        assertEquals(12.3785, 40.5713, ngc4565.horizontal(o), DELTA_4, true)
        assertEquals(-129.2399, 86.4379, ngc4565.galactic(o), DELTA_4, true)
        assertEquals(90.2108, 2.7641, ngc4565.supergalactic(o), DELTA_4, true)
        assertEquals(177.1589, 27.2894, ngc4565.eclipticJ2000(o), DELTA_4, true)
        assertEquals(177.4580, 27.2893, ngc4565.ecliptic(o), DELTA_4, true)
        assertEquals(-167.2899, ngc4565.parallacticAngle(o), DELTA_4, true)
        assertEquals(Constellation.COM, ngc4565.constellation(o))
        assertEquals(56790080.4143, ngc4565.distance(o).lightYear.value, DELTA_3)
        assertEquals(Triad(10.4104, 15.697, 20.983), ngc4565.rts(o), DELTA_3)
    }

    @Test
    fun star() {
        // http://simbad.u-strasbg.fr/simbad/sim-id?Ident=Barnard%27s%20star
        val barnard = Star(
            EquatorialCoord(Degrees(269.4520824975141), Degrees(4.69336426506333)),
            547.4506,
            -802.803, 10362.542,
            -110.353, 11.08, 9.5, 6.741,
        )

        val sun = Sun()
        val earth = Earth(sun)

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            JulianDay(2021, 8, 5, 9, 0, 0), // 2459432.000000
        )

        assertEquals(9.50, barnard.visualMagnitude(o), DELTA_2)
        assertEquals(9.50, barnard.visualMagnitudeWithExtinction(o), DELTA_2)
        assertEquals(13.19, barnard.absoluteMagnitude(o), DELTA_2)
        assertEquals(Constellation.OPH, barnard.constellation(o))
        assertEquals(5.957, barnard.distance(o).lightYear.value, DELTA_3)

        for (year in 2021..2035) {
            when (year) {
                // Coordinates are very close to real (from Stellarium).
                2021 -> assertEquals(269.7103, 4.7537, barnard.equatorial(o.copy(jd = JulianDay(year, 8, 5, 9, 0, 0))), DELTA_4, true)
                2022 -> assertEquals(269.7231, 4.7558, barnard.equatorial(o.copy(jd = JulianDay(year, 8, 5, 9, 0, 0))), DELTA_4, true)
                2023 -> assertEquals(269.7364, 4.7581, barnard.equatorial(o.copy(jd = JulianDay(year, 8, 5, 9, 0, 0))), DELTA_4, true)
                2024 -> assertEquals(269.7499, 4.7608, barnard.equatorial(o.copy(jd = JulianDay(year, 8, 5, 9, 0, 0))), DELTA_4, true)
                2025 -> assertEquals(269.7633, 4.7637, barnard.equatorial(o.copy(jd = JulianDay(year, 8, 5, 9, 0, 0))), DELTA_4, true)
                2026 -> assertEquals(269.7767, 4.7668, barnard.equatorial(o.copy(jd = JulianDay(year, 8, 5, 9, 0, 0))), DELTA_4, true)
                2027 -> assertEquals(269.7900, 4.7701, barnard.equatorial(o.copy(jd = JulianDay(year, 8, 5, 9, 0, 0))), DELTA_4, true)
                2028 -> assertEquals(269.8029, 4.7737, barnard.equatorial(o.copy(jd = JulianDay(year, 8, 5, 9, 0, 0))), DELTA_4, true)
                2029 -> assertEquals(269.8153, 4.7775, barnard.equatorial(o.copy(jd = JulianDay(year, 8, 5, 9, 0, 0))), DELTA_4, true)
                2030 -> assertEquals(269.8272, 4.7812, barnard.equatorial(o.copy(jd = JulianDay(year, 8, 5, 9, 0, 0))), DELTA_4, true)
                2031 -> assertEquals(269.8388, 4.7848, barnard.equatorial(o.copy(jd = JulianDay(year, 8, 5, 9, 0, 0))), DELTA_4, true)
                2032 -> assertEquals(269.8499, 4.7883, barnard.equatorial(o.copy(jd = JulianDay(year, 8, 5, 9, 0, 0))), DELTA_4, true)
                2033 -> assertEquals(269.8606, 4.7916, barnard.equatorial(o.copy(jd = JulianDay(year, 8, 5, 9, 0, 0))), DELTA_4, true)
                2034 -> assertEquals(269.8713, 4.7945, barnard.equatorial(o.copy(jd = JulianDay(year, 8, 5, 9, 0, 0))), DELTA_4, true)
                2035 -> assertEquals(269.8821, 4.7972, barnard.equatorial(o.copy(jd = JulianDay(year, 8, 5, 9, 0, 0))), DELTA_4, true)
            }
        }

        o.copy(jd = JulianDay(5353, 8, 5, 9, 0, 0)).also {
            assertEquals(306.1128, 22.0816, barnard.equatorial(it), DELTA_4, true) // Not very close!!!
            assertEquals(Constellation.HER, barnard.constellation(it))
        }
    }

    companion object {

        private const val DELTA_1 = 0.1
        private const val DELTA_2 = 0.01
        private const val DELTA_3 = 0.001
        private const val DELTA_4 = 0.0001
        private const val DELTA_5 = 0.00001
        private const val DELTA_6 = 0.000001

        private val SAO_JOSE_DAS_PALMEIRAS = Location("São José das Palmeiras - BR", Degrees(-24.837778), Degrees(-54.063889).radians, Meter(563.0))
        private val PICO_DOS_DIAS_OBSERVATORY = Location("Pico dos Dias Observatory - BR", Degrees(-22.534444).radians, Degrees(-45.5825), Meter(1864.0))
    }
}