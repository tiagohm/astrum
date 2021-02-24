import br.tiagohm.astrum.sky.AU
import br.tiagohm.astrum.sky.Location
import br.tiagohm.astrum.sky.Observer
import br.tiagohm.astrum.sky.constellations.Constellation
import br.tiagohm.astrum.sky.core.math.Duad
import br.tiagohm.astrum.sky.core.math.Triad
import br.tiagohm.astrum.sky.core.time.DateTime
import br.tiagohm.astrum.sky.core.time.TimeCorrectionType
import br.tiagohm.astrum.sky.core.units.angle.Degrees
import br.tiagohm.astrum.sky.core.units.distance.Meter
import br.tiagohm.astrum.sky.planets.ApparentMagnitudeAlgorithm
import br.tiagohm.astrum.sky.planets.Sun
import br.tiagohm.astrum.sky.planets.major.earth.*
import br.tiagohm.astrum.sky.planets.major.jupiter.Jupiter
import br.tiagohm.astrum.sky.planets.major.mars.Deimos
import br.tiagohm.astrum.sky.planets.major.mars.Mars
import br.tiagohm.astrum.sky.planets.major.mars.Phobos
import br.tiagohm.astrum.sky.planets.major.mercury.Mercury
import br.tiagohm.astrum.sky.planets.major.neptune.Neptune
import br.tiagohm.astrum.sky.planets.major.saturn.Saturn
import br.tiagohm.astrum.sky.planets.major.uranus.Uranus
import br.tiagohm.astrum.sky.planets.major.venus.Venus
import br.tiagohm.astrum.sky.planets.minor.pluto.Pluto
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
            DateTime(2021, 2, 5, 9, 0, 0), // 2459251.000000
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
            DateTime(2021, 2, 5, 9, 0, 0), // 2459251.000000
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
            DateTime(2021, 2, 5, 9, 0, 0), // 2459251.000000
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
            DateTime(2021, 2, 5, 9, 0, 0), // 2459251.000000
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
            DateTime(2021, 2, 5, 9, 0, 0), // 2459251.000000
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
            DateTime(2021, 2, 5, 9, 0, 0), // 2459251.000000
            TimeCorrectionType.NONE,
            useTopocentricCoordinates = true,
            useNutation = true,
            useLightTravelTime = true,
        )

        val mercury = Mercury(sun)

        for (i in 5 until 12) {
            val oi = o0.copy(dateTime = DateTime(2021, 2, i, 9, 0, 0))
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
            val oi = o0.copy(dateTime = DateTime(2021, 2, i, 9, 0, 0))
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
            DateTime(2021, 2, 5, 9, 0, 0),
        )

        assertTrue(Neptune(sun).isAboveHorizon(o0))
        assertTrue(!Mars(sun).isAboveHorizon(o0))
        assertTrue(!Uranus(sun).isAboveHorizon(o0))

        val o1 = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            DateTime(2021, 2, 5, 21, 0, 0),
        )

        assertTrue(!Neptune(sun).isAboveHorizon(o1))
        assertTrue(Mars(sun).isAboveHorizon(o1))
        assertTrue(Uranus(sun).isAboveHorizon(o1))
    }

    private fun testFrom(
        sun: Sun,
        home: Earth,
        site: Location,
        dateTime: DateTime,
        positions: List<Duad?>,
        deltaTAlgorithm: TimeCorrectionType = TimeCorrectionType.NONE,
        useTopocentricCoordinates: Boolean = false,
        useNutation: Boolean = false,
        useLightTravelTime: Boolean = false,
        delta: Double = DELTA_4,
    ) {
        val o = Observer(
            home,
            site,
            dateTime,
            deltaTAlgorithm,
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
            DateTime(2021, 2, 5, 9, 0, 0), // 2459251.000000
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
            DateTime(2021, 2, 5, 9, 0, 0), // 2459251.000000
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
            DateTime(2021, 2, 5, 9, 0, 0), // 2459251.000000
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
            DateTime(2021, 2, 5, 9, 0, 0), // 2459251.000000
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
            DateTime(2021, 2, 5, 9, 0, 0), // 2459251.000000
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
            DateTime(2021, 2, 5, 9, 0, 0), // 2459251.000000
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
            DateTime(2021, 2, 5, 9, 0, 0), // 2459251.000000
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
            DateTime(2021, 2, 5, 9, 0, 0), // 2459251.000000
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
            DateTime(2021, 2, 5, 9, 0, 0), // 2459251.000000
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
            DateTime(2021, 2, 5, 9, 0, 0), // 2459251.000000
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
            DateTime(2021, 2, 5, 9, 0, 0), // 2459251.000000
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
            DateTime(2021, 2, 5, 9, 0, 0), // 2459251.000000
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
            DateTime(2021, 2, 5, 9, 0, 0), // 2459251.000000
        )

        // TODO: moons, DSOs, etc

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
            DateTime(2021, 2, 5, 9, 0, 0), // 2459251.000000
        )

        assertEquals(0.986, sun.distance(o), DELTA_3)
        assertEquals(0.679, Mercury(sun).distance(o), DELTA_3)
        assertEquals(1.663, Venus(sun).distance(o), DELTA_3)
        assertEquals(1.236, Mars(sun).distance(o), DELTA_3)
        assertEquals(6.064, Jupiter(sun).distance(o), DELTA_3)
        assertEquals(10.947, Saturn(sun).distance(o), DELTA_3)
        assertEquals(19.914, Uranus(sun).distance(o), DELTA_3)
        assertEquals(30.753, Neptune(sun).distance(o), DELTA_3)
        assertEquals(365486.706 / AU, Moon(earth).distance(o), DELTA_3)
        assertEquals(35.130, Pluto(sun).distance(o), DELTA_3)
    }

    @Test
    fun distanceFromSun() {
        val sun = Sun()
        val earth = Earth(sun)

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            DateTime(2021, 2, 5, 9, 0, 0), // 2459251.000000
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
            DateTime(2021, 2, 5, 9, 0, 0), // 2459251.000000
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
            DateTime(2021, 2, 5, 9, 0, 0), // 2459251.000000
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
            DateTime(2021, 2, 5, 9, 0, 0), // 2459251.000000
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
            DateTime(2021, 2, 5, 9, 0, 0), // 2459251.000000
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
    fun phase() {
        val sun = Sun()
        val earth = Earth(sun)

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            DateTime(2021, 2, 5, 9, 0, 0), // 2459251.000000
        )

        assertEquals(Double.NaN, sun.phase(o), DELTA_3)
        assertEquals(0.039, Mercury(sun).phase(o), DELTA_3)
        assertEquals(0.980, Venus(sun).phase(o), DELTA_3)
        assertEquals(0.887, Mars(sun).phase(o), DELTA_3)
        assertEquals(1.000, Jupiter(sun).phase(o), DELTA_3)
        assertEquals(1.000, Saturn(sun).phase(o), DELTA_3)
        assertEquals(0.999, Uranus(sun).phase(o), DELTA_3)
        assertEquals(1.000, Neptune(sun).phase(o), DELTA_3)
        assertEquals(1.000, Pluto(sun).phase(o), DELTA_3)
    }

    @Test
    fun siderealTime() {
        val sun = Sun()
        val earth = Earth(sun)

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            DateTime(2021, 2, 5, 9, 0, 0), // 2459251.000000
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
            DateTime(2021, 2, 5, 13, 0, 0), // All planets in the sky
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

        val dt = DateTime(2021, 2, 5, 9, 0, 0) // 2459251.000000

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            dt,
        )

        val jupiter = Jupiter(sun)

        assertEquals(Constellation.CAP, jupiter.constellation(o))
        assertEquals(Constellation.AQR, jupiter.constellation(o.copy(dateTime = dt.copy(year = 2022))))
        assertEquals(Constellation.PSC, jupiter.constellation(o.copy(dateTime = dt.copy(year = 2023))))
        assertEquals(Constellation.ARI, jupiter.constellation(o.copy(dateTime = dt.copy(year = 2024))))
        assertEquals(Constellation.TAU, jupiter.constellation(o.copy(dateTime = dt.copy(year = 2025))))
        assertEquals(Constellation.GEM, jupiter.constellation(o.copy(dateTime = dt.copy(year = 2026))))
        assertEquals(Constellation.LEO, jupiter.constellation(o.copy(dateTime = dt.copy(year = 2027))))
        assertEquals(Constellation.VIR, jupiter.constellation(o.copy(dateTime = dt.copy(year = 2028))))
        assertEquals(Constellation.LIB, jupiter.constellation(o.copy(dateTime = dt.copy(year = 2030))))
        assertEquals(Constellation.OPH, jupiter.constellation(o.copy(dateTime = dt.copy(year = 2031))))
        assertEquals(Constellation.SGR, jupiter.constellation(o.copy(dateTime = dt.copy(year = 2032))))
        assertEquals(Constellation.CAP, jupiter.constellation(o.copy(dateTime = dt.copy(year = 2033))))
    }

    @Test
    fun eclipticObliquity() {
        val sun = Sun()
        val earth = Earth(sun)

        val dt = DateTime(2021, 2, 5, 9, 0, 0) // 2459251.000000

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            dt,
        )

        assertEquals(23.4371, o.computeEclipticObliquity(), DELTA_4, true)
        assertEquals(23.4372, o.copy(dateTime = dt.copy(day = 10)).computeEclipticObliquity(), DELTA_4, true)
        assertEquals(23.6893, o.copy(dateTime = dt.copy(year = 46)).computeEclipticObliquity(), DELTA_4, true)
        assertEquals(24.2108, o.copy(dateTime = dt.copy(year = -6046)).computeEclipticObliquity(), DELTA_4, true)
        assertEquals(22.9481, o.copy(dateTime = dt.copy(year = 6046)).computeEclipticObliquity(), DELTA_4, true)
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

        val dt = DateTime(2021, 2, 5, 9, 0, 0) // 2459251.000000

        val o = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            dt,
        )

        val jupiter = Jupiter(sun)

        assertEquals(0.00903, jupiter.angularSize(o) * 2, DELTA_5, true)
        assertEquals(0.01053, jupiter.angularSize(o.copy(dateTime = dt.copy(month = 5))) * 2, DELTA_5, true)

        val saturn = Saturn(sun)

        assertEquals(0.00982, saturn.angularSize(o) * 2, DELTA_5, true)
        assertEquals(0.01088, saturn.angularSize(o.copy(dateTime = dt.copy(month = 5))) * 2, DELTA_5, true)
        assertEquals(0.00422, saturn.spheroidAngularSize(o) * 2, DELTA_5, true)
        assertEquals(0.00467, saturn.spheroidAngularSize(o.copy(dateTime = dt.copy(month = 5))) * 2, DELTA_5, true)

        val uranus = Uranus(sun)

        assertEquals(0.00376, uranus.angularSize(o) * 2, DELTA_5, true)
        assertEquals(0.00360, uranus.angularSize(o.copy(dateTime = dt.copy(month = 5))) * 2, DELTA_5, true)
        assertEquals(0.00098, uranus.spheroidAngularSize(o) * 2, DELTA_5, true)
        assertEquals(0.00094, uranus.spheroidAngularSize(o.copy(dateTime = dt.copy(month = 5))) * 2, DELTA_5, true)

        val neptune = Neptune(sun)

        assertEquals(0.00157, neptune.angularSize(o) * 2, DELTA_5, true)
        assertEquals(0.00158, neptune.angularSize(o.copy(dateTime = dt.copy(month = 5))) * 2, DELTA_5, true)
        assertEquals(0.00062, neptune.spheroidAngularSize(o) * 2, DELTA_5, true)
        assertEquals(0.00062, neptune.spheroidAngularSize(o.copy(dateTime = dt.copy(month = 5))) * 2, DELTA_5, true)
    }

    @Test
    fun solarEclipse() {
        val sun = Sun()
        val earth = Earth(sun)
        val moon = Moon(earth)

        val site = Location("Rangpur", Degrees(25.9896), Degrees(87.0868), Meter(14.0))

        val a = Observer(earth, site, DateTime(2009, 7, 22, 5, 57, 28, utcOffset = 6.0))
        assertEquals(69.5333, 4.5405, sun.horizontal(a), DELTA_4, true)
        assertEquals(69.6924, 5.0706, moon.horizontal(a), DELTA_4, true)

        assertEquals(0.0, a.eclipseObscuration(moon), DELTA_2)
        assertEquals(-25.26, sun.visualMagnitudeWithExtinction(a, moon), DELTA_2)

        val b = Observer(earth, site, DateTime(2009, 7, 22, 6, 37, 28, utcOffset = 6.0))
        assertEquals(61.73, b.eclipseObscuration(moon), DELTA_2)
        assertEquals(-25.09, sun.visualMagnitudeWithExtinction(b, moon), DELTA_2)

        val c = Observer(earth, site, DateTime(2009, 7, 22, 6, 57, 28, utcOffset = 6.0))
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

        val o = Observer(earth, PICO_DOS_DIAS_OBSERVATORY, DateTime(2021, 2, 11, 15, 0, 0))

        o.copy(dateTime = DateTime(2021, 2, 11, 16, 0, 0)).let {
            assertEquals(LunarPhase.NEW_MOON, moon.lunarPhase(it))
            assertEquals(29.5, moon.age(it), DELTA_1)
        }

        o.copy(dateTime = DateTime(2021, 2, 11, 18, 0, 0)).let {
            assertEquals(LunarPhase.WAXING_CRESCENT, moon.lunarPhase(it))
            assertEquals(DELTA_1, moon.age(it), DELTA_1)
        }

        o.copy(dateTime = DateTime(2021, 2, 19, 15, 0, 0)).let {
            assertEquals(LunarPhase.FIRST_QUARTER, moon.lunarPhase(it))
            assertEquals(7.4, moon.age(it), DELTA_1)
        }

        o.copy(dateTime = DateTime(2021, 2, 20, 21, 0, 0)).let {
            assertEquals(LunarPhase.WAXING_GIBBOUS, moon.lunarPhase(it))
            assertEquals(8.5, moon.age(it), DELTA_1)
        }

        o.copy(dateTime = DateTime(2021, 2, 27, 5, 0, 0)).let {
            assertEquals(LunarPhase.FULL_MOON, moon.lunarPhase(it))
            assertEquals(14.8, moon.age(it), DELTA_1)
        }

        o.copy(dateTime = DateTime(2021, 2, 27, 21, 0, 0)).let {
            assertEquals(LunarPhase.WANING_GIBBOUS, moon.lunarPhase(it))
            assertEquals(15.5, moon.age(it), DELTA_1)
        }

        o.copy(dateTime = DateTime(2021, 3, 5, 23, 0, 0)).let {
            assertEquals(LunarPhase.THIRD_QUARTER, moon.lunarPhase(it))
            assertEquals(22.2, moon.age(it), DELTA_1)
        }

        o.copy(dateTime = DateTime(2021, 3, 7, 2, 0, 0)).let {
            assertEquals(LunarPhase.WANING_CRESCENT, moon.lunarPhase(it))
            assertEquals(23.4, moon.age(it), DELTA_1)
        }
    }

    @Test
    fun lunarEclipse() {
        val sun = Sun()
        val earth = Earth(sun)
        val moon = Moon(earth)

        val o = Observer(earth, PICO_DOS_DIAS_OBSERVATORY, DateTime(2022, 5, 15, 22, 31, 0))

        // https://www.timeanddate.com/eclipse/lunar/2022-may-16

        // Not started
        o.copy().let {
            assertEquals(LunarPhase.WAXING_GIBBOUS, moon.lunarPhase(it))
            assertEquals(14.6, moon.age(it), DELTA_1)
            assertEquals(82.5454, 70.3246, moon.horizontal(it), DELTA_3, true)
            assertEquals(1.0, moon.phase(it), DELTA_1)
            assertTrue(!LunarEclipse.compute(it, moon).isEclipsing)
            assertEquals(-12.26, moon.visualMagnitudeWithExtinction(it), DELTA_2)
        }

        // Penumbral Eclipse begins
        o.copy(dateTime = DateTime(2022, 5, 15, 22, 32, 5)).let {
            assertEquals(82.366, 70.5680, moon.horizontal(it), DELTA_3, true)
            val le = LunarEclipse.compute(it, moon)
            assertEquals(0.00003, le.penumbralMagnitude, DELTA_5)
            assertTrue(le.umbralMagnitude < 1E-6)
            assertEquals(-12.26, moon.visualMagnitudeWithExtinction(it), DELTA_2)
        }

        // Full Eclipse begins
        o.copy(dateTime = DateTime(2022, 5, 15, 23, 27, 52)).let {
            assertEquals(61.3293, 82.7327, moon.horizontal(it), DELTA_3, true)
            val le = LunarEclipse.compute(it, moon)
            assertEquals(0.95943, le.penumbralMagnitude, DELTA_5)
            assertEquals(0.00002, le.umbralMagnitude, DELTA_5)
            assertEquals(-12.27, moon.visualMagnitudeWithExtinction(it), DELTA_2)
        }

        // Maximum Eclipse
        o.copy(dateTime = DateTime(2022, 5, 16, 1, 11, 20)).let {
            assertEquals(LunarPhase.FULL_MOON, moon.lunarPhase(it))
            assertEquals(277.2745, 72.3696, moon.horizontal(it), DELTA_3, true)
            val le = LunarEclipse.compute(it, moon)
            assertEquals(2.37272, le.penumbralMagnitude, DELTA_5)
            assertEquals(1.41382, le.umbralMagnitude, DELTA_5)
            assertEquals(-0.85, moon.visualMagnitudeWithExtinction(it), DELTA_2)
        }

        // Full Eclipse ends
        o.copy(dateTime = DateTime(2022, 5, 16, 1, 53, 57)).let {
            assertEquals(271.2277, 62.8147, moon.horizontal(it), DELTA_3, true)
            val le = LunarEclipse.compute(it, moon)
            assertEquals(1.95858, le.penumbralMagnitude, DELTA_5)
            assertEquals(0.99988, le.umbralMagnitude, DELTA_5)
            assertEquals(-5.01, moon.visualMagnitudeWithExtinction(it), DELTA_2)
        }

        // Partial Eclipse ends
        o.copy(dateTime = DateTime(2022, 5, 16, 2, 55, 8)).let {
            assertEquals(LunarPhase.WANING_GIBBOUS, moon.lunarPhase(it))
            assertEquals(265.7446, 49.0918, moon.horizontal(it), DELTA_3, true)
            val le = LunarEclipse.compute(it, moon)
            assertEquals(0.95841, le.penumbralMagnitude, DELTA_5)
            assertTrue(le.umbralMagnitude < 1E-6)
            assertEquals(-12.22, moon.visualMagnitudeWithExtinction(it), DELTA_2)
        }

        // Penumbral Eclipse ends
        o.copy(dateTime = DateTime(2022, 5, 16, 3, 50, 51)).let {
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
            DateTime(2021, 8, 5, 9, 0, 0), // 2459432.000000
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
        assertEquals(98.8, 100 * phobos.phase(o), DELTA_1)
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
        assertEquals(98.8, 100 * deimos.phase(o), DELTA_1)
        assertEquals(2.571, deimos.distance(o), DELTA_3)
        assertEquals(1.663, deimos.distanceFromSun(o), DELTA_3)
        assertEquals(1.350, deimos.orbitalVelocity(o), DELTA_3)
        assertEquals(22.289, deimos.heliocentricVelocity(o), DELTA_3)
        assertEquals(0.000001, deimos.angularSize(o) * 2, DELTA_6, true)
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