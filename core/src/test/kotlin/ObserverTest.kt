import br.tiagohm.astrum.core.*
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
            DeltaTAlgorithmType.ESPEANAK_MEEUS,
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
            DeltaTAlgorithmType.NONE,
            useTopocentricCoordinates = true,
            useNutation = true,
            useLightTravelTime = true,
        )

        val mercury = Mercury(sun)

        for (i in 5 until 12) {
            val oi = o0.copy(dateTime = DateTime(2021, 2, i, 9, 0, 0))
            var pos = mercury.equatorial(oi)

            when (i) {
                5 -> assertDuadEquals(Duad(324.69526, -10.6663), pos, 0.0001)
                6 -> assertDuadEquals(Duad(323.64525, -10.8355), pos, 0.0001)
                7 -> assertDuadEquals(Duad(322.52248, -11.0620), pos, 0.0001)
                8 -> assertDuadEquals(Duad(321.35815, -11.3369), pos, 0.0001)
                9 -> assertDuadEquals(Duad(320.18409, -11.6507), pos, 0.0001)
                10 -> assertDuadEquals(Duad(319.03114, -11.9929), pos, 0.0001)
                11 -> assertDuadEquals(Duad(317.92765, -12.3538), pos, 0.0001)
            }

            pos = sun.equatorial(oi)

            when (i) {
                5 -> assertDuadEquals(Duad(319.35453, -15.7683), pos, 0.0001)
                6 -> assertDuadEquals(Duad(320.35724, -15.4602), pos, 0.0001)
                7 -> assertDuadEquals(Duad(321.35671, -15.1476), pos, 0.0001)
                8 -> assertDuadEquals(Duad(322.35295, -14.8307), pos, 0.0001)
                9 -> assertDuadEquals(Duad(323.34596, -14.5096), pos, 0.0001)
                10 -> assertDuadEquals(Duad(324.33576, -14.1845), pos, 0.0001)
                11 -> assertDuadEquals(Duad(325.32234, -13.8555), pos, 0.0001)
            }
        }

        for (i in 4 downTo 1) {
            val oi = o0.copy(dateTime = DateTime(2021, 2, i, 9, 0, 0))
            var pos = mercury.equatorial(oi)

            when (i) {
                4 -> assertDuadEquals(Duad(325.64359, -10.5614), pos, 0.0001)
                3 -> assertDuadEquals(Duad(326.46494, -10.5262), pos, 0.0001)
                2 -> assertDuadEquals(Duad(327.13875, -10.5637), pos, 0.0001)
                1 -> assertDuadEquals(Duad(327.64970, -10.6746), pos, 0.0001)
            }

            pos = sun.equatorial(oi)

            when (i) {
                4 -> assertDuadEquals(Duad(318.34857, -16.0720), pos, 0.0001)
                3 -> assertDuadEquals(Duad(317.33935, -16.3710), pos, 0.0001)
                2 -> assertDuadEquals(Duad(316.32684, -16.6652), pos, 0.0001)
                1 -> assertDuadEquals(Duad(315.31102, -16.9546), pos, 0.0001)
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

    fun testFrom(
        sun: Sun,
        home: Earth,
        site: Location,
        dateTime: DateTime,
        positions: List<Duad?>,
        deltaTAlgorithm: DeltaTAlgorithmType = DeltaTAlgorithmType.NONE,
        useTopocentricCoordinates: Boolean = false,
        useNutation: Boolean = false,
        useLightTravelTime: Boolean = false,
        delta: Double = 0.0001,
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
                assertEquals(it[0], ra, delta)
                assertEquals(it[1], dec, delta)
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

        assertEquals(-106.3198, sun.parallacticAngle(o).deg, 0.0001)
        assertEquals(-110.2467, Mercury(sun).parallacticAngle(o).deg, 0.0001)
        assertEquals(-101.2811, Venus(sun).parallacticAngle(o).deg, 0.0001)
        assertEquals(-104.5911, Mars(sun).parallacticAngle(o).deg, 0.0001)
        assertEquals(-103.9626, Jupiter(sun).parallacticAngle(o).deg, 0.0001)
        assertEquals(-102.1160, Saturn(sun).parallacticAngle(o).deg, 0.0001)
        assertEquals(-108.2743, Uranus(sun).parallacticAngle(o).deg, 0.0001)
        assertEquals(-111.9851, Neptune(sun).parallacticAngle(o).deg, 0.0001)
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

        assertDuadEquals(Duad(319.06728, -15.8555), sun.equatorialJ2000(o), 0.0001)
        assertDuadEquals(Duad(324.41508, -10.7605), Mercury(sun).equatorialJ2000(o), 0.0001)
        assertDuadEquals(Duad(307.157, -19.8641), Venus(sun).equatorialJ2000(o), 0.0001)
        assertDuadEquals(Duad(41.81461, 17.5037), Mars(sun).equatorialJ2000(o), 0.0001)
        assertDuadEquals(Duad(313.4488, -18.0259), Jupiter(sun).equatorialJ2000(o), 0.0001)
        assertDuadEquals(Duad(307.99669, -19.303), Saturn(sun).equatorialJ2000(o), 0.0001)
        assertDuadEquals(Duad(34.4606, 13.3237), Uranus(sun).equatorialJ2000(o), 0.0001)
        assertDuadEquals(Duad(350.42818, -5.286), Neptune(sun).equatorialJ2000(o), 0.0001)
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

        assertDuadEquals(Duad(90.7432, 43.3413), sun.horizontal(o0), 0.0001)
        assertDuadEquals(Duad(86.5517, 36.8015), Mercury(sun).horizontal(o0), 0.0001)
        assertDuadEquals(Duad(92.5681, 55.3030), Venus(sun).horizontal(o0), 0.0001)
        assertDuadEquals(Duad(87.1402, -44.6719), Mars(sun).horizontal(o0), 0.0001)
        assertDuadEquals(Duad(91.7507, 49.0952), Jupiter(sun).horizontal(o0), 0.0001)
        assertDuadEquals(Duad(91.8896, 54.4151), Saturn(sun).horizontal(o0), 0.0001)
        assertDuadEquals(Duad(89.7587, -36.6858), Uranus(sun).horizontal(o0), 0.0001)
        assertDuadEquals(Duad(91.1530, 10.8117), Neptune(sun).horizontal(o0), 0.0001)

        val o1 = Observer(
            earth,
            Location("Tokyo", 35.689499, 139.691711, 44.0),
            DateTime(2021, 2, 5, 9, 0, 0), // 2459251.000000
        )

        assertDuadEquals(Duad(285.4773, -46.2733), sun.horizontal(o1), 0.0001)
        assertDuadEquals(Duad(286.8195, -39.0608), Mercury(sun).horizontal(o1), 0.0001)
        assertDuadEquals(Duad(291.0016, -57.8109), Venus(sun).horizontal(o1), 0.0001)
        assertDuadEquals(Duad(263.6072, 39.5997), Mars(sun).horizontal(o1), 0.0001)
        assertDuadEquals(Duad(287.5396, -51.9072), Jupiter(sun).horizontal(o1), 0.0001)
        assertDuadEquals(Duad(290.97, -56.8399), Saturn(sun).horizontal(o1), 0.0001)
        assertDuadEquals(Duad(264.0623, 31.3826), Uranus(sun).horizontal(o1), 0.0001)
        assertDuadEquals(Duad(274.4963, -15.0647), Neptune(sun).horizontal(o1), 0.0001)
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

        assertDuadEquals(Duad(90.7432, 43.3584), sun.horizontal(o, apparent = true), 0.0001)
        assertDuadEquals(Duad(86.5517, 36.8230), Mercury(sun).horizontal(o, apparent = true), 0.0001)
        assertDuadEquals(Duad(92.5681, 55.3141), Venus(sun).horizontal(o, apparent = true), 0.0001)
        assertDuadEquals(Duad(87.1402, -44.6719), Mars(sun).horizontal(o, apparent = true), 0.0001)
        assertDuadEquals(Duad(91.7507, 49.1092), Jupiter(sun).horizontal(o, apparent = true), 0.0001)
        assertDuadEquals(Duad(91.8896, 54.4267), Saturn(sun).horizontal(o, apparent = true), 0.0001)
        assertDuadEquals(Duad(89.7587, -36.6858), Uranus(sun).horizontal(o, apparent = true), 0.0001)
        assertDuadEquals(Duad(91.1530, 10.8916), Neptune(sun).horizontal(o, apparent = true), 0.0001)
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

        assertDuadEquals(Duad(270.7432, 43.3584), sun.horizontal(o, true, apparent = true), 0.0001)
        assertDuadEquals(Duad(266.5517, 36.8230), Mercury(sun).horizontal(o, true, apparent = true), 0.0001)
        assertDuadEquals(Duad(272.5681, 55.3141), Venus(sun).horizontal(o, true, apparent = true), 0.0001)
        assertDuadEquals(Duad(267.1402, -44.6719), Mars(sun).horizontal(o, true, apparent = true), 0.0001)
        assertDuadEquals(Duad(271.7507, 49.1092), Jupiter(sun).horizontal(o, true, apparent = true), 0.0001)
        assertDuadEquals(Duad(271.8896, 54.4267), Saturn(sun).horizontal(o, true, apparent = true), 0.0001)
        assertDuadEquals(Duad(269.7587, -36.6858), Uranus(sun).horizontal(o, true, apparent = true), 0.0001)
        assertDuadEquals(Duad(271.1530, 10.8916), Neptune(sun).horizontal(o, true, apparent = true), 0.0001)
    }

    @Test
    fun hourAngle() {
        val sun = Sun()
        val earth = Earth(sun)

        val o0 = Observer(
            earth,
            PICO_DOS_DIAS_OBSERVATORY,
            DateTime(2021, 2, 5, 9, 0, 0), // 2459251.000000
        )

        assertDuadEquals(Duad(20.72784, -15.7682), sun.hourAngle(o0), 0.0001)
        assertDuadEquals(Duad(20.37190, -10.6666), Mercury(sun).hourAngle(o0), 0.0001)
        assertDuadEquals(Duad(21.52107, -19.7945), Venus(sun).hourAngle(o0), 0.0001)
        assertDuadEquals(Duad(15.21116, 17.5901), Mars(sun).hourAngle(o0), 0.0001)
        assertDuadEquals(Duad(21.10202, -17.9465), Jupiter(sun).hourAngle(o0), 0.0001)
        assertDuadEquals(Duad(21.46518, -19.232), Saturn(sun).hourAngle(o0), 0.0001)
        assertDuadEquals(Duad(15.70202, 13.4193), Uranus(sun).hourAngle(o0), 0.0001)
        assertDuadEquals(Duad(18.63843, -5.172), Neptune(sun).hourAngle(o0), 0.0001)

        val o1 = Observer(
            earth,
            Location("Tokyo", 35.689499, 139.691711, 44.0),
            DateTime(2021, 2, 5, 9, 0, 0), // 2459251.000000
        )

        assertDuadEquals(Duad(9.07967, -15.7697), sun.hourAngle(o1), 0.0001)
        assertDuadEquals(Duad(8.72385, -10.6693), Mercury(sun).hourAngle(o1), 0.0001)
        assertDuadEquals(Duad(9.87279, -19.7951), Venus(sun).hourAngle(o1), 0.0001)
        assertDuadEquals(Duad(3.56296, 17.5890), Mars(sun).hourAngle(o1), 0.0001)
        assertDuadEquals(Duad(9.45366, -17.9467), Jupiter(sun).hourAngle(o1), 0.0001)
        assertDuadEquals(Duad(9.81681, -19.2321), Saturn(sun).hourAngle(o1), 0.0001)
        assertDuadEquals(Duad(4.05365, 13.4192), Uranus(sun).hourAngle(o1), 0.0001)
        assertDuadEquals(Duad(6.99005, -5.172), Neptune(sun).hourAngle(o1), 0.0001)
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

        assertDuadEquals(Duad(20.72897, -15.7730), sun.hourAngle(o, true), 0.0001)
        assertDuadEquals(Duad(20.37327, -10.6740), Mercury(sun).hourAngle(o, true), 0.0001)
        assertDuadEquals(Duad(21.52185, -19.7966), Venus(sun).hourAngle(o, true), 0.0001)
        assertDuadEquals(Duad(15.21116, 17.5901), Mars(sun).hourAngle(o, true), 0.0001)
        assertDuadEquals(Duad(21.10297, -17.9499), Jupiter(sun).hourAngle(o, true), 0.0001)
        assertDuadEquals(Duad(21.46598, -19.2344), Saturn(sun).hourAngle(o, true), 0.0001)
        assertDuadEquals(Duad(15.70202, 13.4193), Uranus(sun).hourAngle(o, true), 0.0001)
        assertDuadEquals(Duad(18.64339, -5.2019), Neptune(sun).hourAngle(o, true), 0.0001)
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

        assertDuadEquals(Duad(34.1094, -39.0728), sun.galactic(o), 0.0001)
        assertDuadEquals(Duad(43.035, -41.6918), Mercury(sun).galactic(o), 0.0001)
        assertDuadEquals(Duad(24.5732, -29.9575), Venus(sun).galactic(o), 0.0001)
        assertDuadEquals(Duad(158.3943, -37.2674), Mars(sun).galactic(o), 0.0001)
        assertDuadEquals(Duad(29.1054, -34.8738), Jupiter(sun).galactic(o), 0.0001)
        assertDuadEquals(Duad(25.504, -30.4997), Saturn(sun).galactic(o), 0.0001)
        assertDuadEquals(Duad(153.0455, -44.4373), Uranus(sun).galactic(o), 0.0001)
        assertDuadEquals(Duad(74.5623, -59.447), Neptune(sun).galactic(o), 0.0001)
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

        assertDuadEquals(Duad(-104.0866, 42.9741), sun.supergalactic(o), 0.0001)
        assertDuadEquals(Duad(-94.344, 41.8247), Mercury(sun).supergalactic(o), 0.0001)
        assertDuadEquals(Duad(-119.8808, 47.6372), Venus(sun).supergalactic(o), 0.0001)
        assertDuadEquals(Duad(-37.5207, -20.5139), Mars(sun).supergalactic(o), 0.0001)
        assertDuadEquals(Duad(-111.4607, 45.3493), Jupiter(sun).supergalactic(o), 0.0001)
        assertDuadEquals(Duad(-118.4413, 47.639), Saturn(sun).supergalactic(o), 0.0001)
        assertDuadEquals(Duad(-44.4606, -15.5939), Uranus(sun).supergalactic(o), 0.0001)
        assertDuadEquals(Duad(-75.6146, 20.7693), Neptune(sun).supergalactic(o), 0.0001)
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

        assertDuadEquals(Duad(316.6135, 0.0017), sun.eclipticJ2000(o), 0.0001)
        assertDuadEquals(Duad(323.1506, 3.2161), Mercury(sun).eclipticJ2000(o), 0.0001)
        assertDuadEquals(Duad(304.6190, -0.7788), Venus(sun).eclipticJ2000(o), 0.0001)
        assertDuadEquals(Duad(44.6849, 1.3191), Mars(sun).eclipticJ2000(o), 0.0001)
        assertDuadEquals(Duad(310.8423, -0.5331), Jupiter(sun).eclipticJ2000(o), 0.0001)
        assertDuadEquals(Duad(305.5226, -0.4264), Saturn(sun).eclipticJ2000(o), 0.0001)
        assertDuadEquals(Duad(36.6453, -0.4345), Uranus(sun).eclipticJ2000(o), 0.0001)
        assertDuadEquals(Duad(349.1293, -1.0694), Neptune(sun).eclipticJ2000(o), 0.0001)
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

        assertDuadEquals(Duad(316.9039, 0.00000), sun.ecliptic(o), 0.0001)
        assertDuadEquals(Duad(323.4408, 3.2146), Mercury(sun).ecliptic(o), 0.0001)
        assertDuadEquals(Duad(304.9094, -0.781), Venus(sun).ecliptic(o), 0.0001)
        assertDuadEquals(Duad(44.9753, 1.3212), Mars(sun).ecliptic(o), 0.0001)
        assertDuadEquals(Duad(311.1328, -0.535), Jupiter(sun).ecliptic(o), 0.0001)
        assertDuadEquals(Duad(305.8131, -0.4285), Saturn(sun).ecliptic(o), 0.0001)
        assertDuadEquals(Duad(36.9358, -0.4327), Uranus(sun).ecliptic(o), 0.0001)
        assertDuadEquals(Duad(349.4198, -1.0697), Neptune(sun).ecliptic(o), 0.0001)
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

        // TODO: Moon, DSOs, Pluto, etc

        assertTriadEquals(Triad(5.7500, 12.2833, 18.8167), sun.rts(o), 0.01)
        assertTriadEquals(Triad(6.283333, 12.633333, 19.0), Mercury(sun).rts(o), 0.01)
        assertTriadEquals(Triad(4.85, 11.483, 18.117), Venus(sun).rts(o), 0.01)
        assertTriadEquals(Triad(12.267, 17.817, 23.367), Mars(sun).rts(o), 0.01)
        assertTriadEquals(Triad(5.333, 11.9, 18.483), Jupiter(sun).rts(o), 0.01)
        assertTriadEquals(Triad(4.933, 11.55, 18.15), Saturn(sun).rts(o), 0.01)
        assertTriadEquals(Triad(11.65, 17.317, 23.0), Uranus(sun).rts(o), 0.01)
        assertTriadEquals(Triad(8.183, 14.383, 20.583), Neptune(sun).rts(o), 0.01)
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

        assertEquals(0.986, sun.distance(o), 0.001)
        assertEquals(0.679, Mercury(sun).distance(o), 0.001)
        assertEquals(1.663, Venus(sun).distance(o), 0.001)
        assertEquals(1.236, Mars(sun).distance(o), 0.001)
        assertEquals(6.064, Jupiter(sun).distance(o), 0.001)
        assertEquals(10.947, Saturn(sun).distance(o), 0.001)
        assertEquals(19.914, Uranus(sun).distance(o), 0.001)
        assertEquals(30.753, Neptune(sun).distance(o), 0.001)
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

        assertEquals(0.0, sun.distanceFromSun(o), 0.001)
        assertEquals(0.324, Mercury(sun).distanceFromSun(o), 0.001)
        assertEquals(0.728, Venus(sun).distanceFromSun(o), 0.001)
        assertEquals(0.986, earth.distanceFromSun(o), 0.001)
        assertEquals(1.555, Mars(sun).distanceFromSun(o), 0.001)
        assertEquals(5.084, Jupiter(sun).distanceFromSun(o), 0.001)
        assertEquals(9.981, Saturn(sun).distanceFromSun(o), 0.001)
        assertEquals(19.768, Uranus(sun).distanceFromSun(o), 0.001)
        assertEquals(29.926, Neptune(sun).distanceFromSun(o), 0.001)
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

        assertEquals(0.0, sun.elongation(o), 0.001)
        assertEquals(7.2763.rad, Mercury(sun).elongation(o), 0.001)
        assertEquals(12.0253.rad, Venus(sun).elongation(o), 0.001)
        assertEquals(88.0661.rad, Mars(sun).elongation(o), 0.001)
        assertEquals(5.8016.rad, Jupiter(sun).elongation(o), 0.001)
        assertEquals(11.1048.rad, Saturn(sun).elongation(o), 0.001)
        assertEquals(80.0264.rad, Uranus(sun).elongation(o), 0.001)
        assertEquals(32.5258.rad, Neptune(sun).elongation(o), 0.001)
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

        assertEquals(Double.NaN, sun.phaseAngle(o), 0.001)
        assertEquals(157.3247, Mercury(sun).phaseAngle(o).deg, 0.001)
        assertEquals(16.3937, Venus(sun).phaseAngle(o).deg, 0.001)
        assertEquals(39.3243, Mars(sun).phaseAngle(o).deg, 0.001)
        assertEquals(1.1233, Jupiter(sun).phaseAngle(o).deg, 0.001)
        assertEquals(1.0902, Saturn(sun).phaseAngle(o).deg, 0.001)
        assertEquals(2.8157, Uranus(sun).phaseAngle(o).deg, 0.001)
        assertEquals(1.015, Neptune(sun).phaseAngle(o).deg, 0.001)
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

        assertEquals(0.0, sun.synodicPeriod(o), 0.01)
        assertEquals(115.88, Mercury(sun).synodicPeriod(o), 0.01)
        assertEquals(583.92, Venus(sun).synodicPeriod(o), 0.01)
        assertEquals(779.95, Mars(sun).synodicPeriod(o), 0.01)
        assertEquals(398.89, Jupiter(sun).synodicPeriod(o), 0.01)
        assertEquals(378.09, Saturn(sun).synodicPeriod(o), 0.01)
        assertEquals(369.66, Uranus(sun).synodicPeriod(o), 0.01)
        assertEquals(367.49, Neptune(sun).synodicPeriod(o), 0.01)
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

        assertEquals(0.0, sun.orbitalVelocity(o), 0.001)
        assertEquals(56.441, Mercury(sun).orbitalVelocity(o), 0.001)
        assertEquals(34.804, Venus(sun).orbitalVelocity(o), 0.001)
        assertEquals(23.639, Mars(sun).orbitalVelocity(o), 0.001)
        assertEquals(13.367, Jupiter(sun).orbitalVelocity(o), 0.001)
        assertEquals(9.230, Saturn(sun).orbitalVelocity(o), 0.001)
        assertEquals(6.602, Uranus(sun).orbitalVelocity(o), 0.001)
        assertEquals(5.475, Neptune(sun).orbitalVelocity(o), 0.001)

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

        assertEquals(Double.NaN, sun.phase(o), 0.001)
        assertEquals(0.039, Mercury(sun).phase(o), 0.001)
        assertEquals(0.980, Venus(sun).phase(o), 0.001)
        assertEquals(0.887, Mars(sun).phase(o), 0.001)
        assertEquals(1.000, Jupiter(sun).phase(o), 0.001)
        assertEquals(1.000, Saturn(sun).phase(o), 0.001)
        assertEquals(0.999, Uranus(sun).phase(o), 0.001)
        assertEquals(1.000, Neptune(sun).phase(o), 0.001)
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

        assertEquals(18.0184, o.computeMeanSiderealTime(), 0.0001)
        assertEquals(18.0181, o.computeApparentSiderealTime(), 0.0001)
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

        assertEquals(1.02, sun.airmass(o), 0.01)
        assertEquals(1.03, Mercury(sun).airmass(o), 0.01)
        assertEquals(1.07, Venus(sun).airmass(o), 0.01)
        assertEquals(6.05, Mars(sun).airmass(o), 0.01)
        assertEquals(1.04, Jupiter(sun).airmass(o), 0.01)
        assertEquals(1.07, Saturn(sun).airmass(o), 0.01)
        assertEquals(3.31, Uranus(sun).airmass(o), 0.01)
        assertEquals(1.11, Neptune(sun).airmass(o), 0.01)

        assertEquals(-26.77, sun.visualMagnitude(o), 0.01)
        assertEquals(-26.64, sun.visualMagnitudeWithExtinction(o), 0.01)

        // Default Apparent Magnitude Algorithm
        assertEquals(3.74, Mercury(sun).visualMagnitude(o), 0.01)
        assertEquals(-3.87, Venus(sun).visualMagnitude(o), 0.01)
        assertEquals(0.53, Mars(sun).visualMagnitude(o), 0.01)
        assertEquals(-1.95, Jupiter(sun).visualMagnitude(o), 0.01)
        assertEquals(0.63, Saturn(sun).visualMagnitude(o), 0.01)
        assertEquals(5.79, Uranus(sun).visualMagnitude(o), 0.01)
        assertEquals(7.95, Neptune(sun).visualMagnitude(o), 0.01)

        assertEquals(3.88, Mercury(sun).visualMagnitudeWithExtinction(o), 0.01)
        assertEquals(-3.73, Venus(sun).visualMagnitudeWithExtinction(o), 0.01)
        assertEquals(1.31, Mars(sun).visualMagnitudeWithExtinction(o), 0.01)
        assertEquals(-1.81, Jupiter(sun).visualMagnitudeWithExtinction(o), 0.01)
        assertEquals(0.77, Saturn(sun).visualMagnitudeWithExtinction(o), 0.01)
        assertEquals(6.22, Uranus(sun).visualMagnitudeWithExtinction(o), 0.01)
        assertEquals(8.09, Neptune(sun).visualMagnitudeWithExtinction(o), 0.01)

        o.copy(apparentMagnitudeAlgorithm = ApparentMagnitudeAlgorithm.EXPLANATORY_SUPPLEMENT_1992).let {
            assertEquals(2.66, Mercury(sun).visualMagnitude(it), 0.01)
            assertEquals(-3.80, Venus(sun).visualMagnitude(it), 0.01)
            assertEquals(0.53, Mars(sun).visualMagnitude(it), 0.01)
            assertEquals(-1.80, Jupiter(sun).visualMagnitude(it), 0.01)
            assertEquals(0.63, Saturn(sun).visualMagnitude(it), 0.01)
            assertEquals(5.79, Uranus(sun).visualMagnitude(it), 0.01)
            assertEquals(7.95, Neptune(sun).visualMagnitude(it), 0.01)

            assertEquals(2.79, Mercury(sun).visualMagnitudeWithExtinction(it), 0.01)
            assertEquals(-3.66, Venus(sun).visualMagnitudeWithExtinction(it), 0.01)
            assertEquals(1.31, Mars(sun).visualMagnitudeWithExtinction(it), 0.01)
            assertEquals(-1.66, Jupiter(sun).visualMagnitudeWithExtinction(it), 0.01)
            assertEquals(0.77, Saturn(sun).visualMagnitudeWithExtinction(it), 0.01)
            assertEquals(6.22, Uranus(sun).visualMagnitudeWithExtinction(it), 0.01)
            assertEquals(8.09, Neptune(sun).visualMagnitudeWithExtinction(it), 0.01)
        }

        o.copy(apparentMagnitudeAlgorithm = ApparentMagnitudeAlgorithm.ASTRONOMICAL_ALMANAC_1984).let {
            assertEquals(3.40, Mercury(sun).visualMagnitude(it), 0.01)
            assertEquals(-3.91, Venus(sun).visualMagnitude(it), 0.01)
            assertEquals(0.53, Mars(sun).visualMagnitude(it), 0.01)
            assertEquals(-1.95, Jupiter(sun).visualMagnitude(it), 0.01)
            assertEquals(0.63, Saturn(sun).visualMagnitude(it), 0.01)
            assertEquals(5.79, Uranus(sun).visualMagnitude(it), 0.01)
            assertEquals(7.95, Neptune(sun).visualMagnitude(it), 0.01)

            assertEquals(3.53, Mercury(sun).visualMagnitudeWithExtinction(it), 0.01)
            assertEquals(-3.77, Venus(sun).visualMagnitudeWithExtinction(it), 0.01)
            assertEquals(1.31, Mars(sun).visualMagnitudeWithExtinction(it), 0.01)
            assertEquals(-1.81, Jupiter(sun).visualMagnitudeWithExtinction(it), 0.01)
            assertEquals(0.77, Saturn(sun).visualMagnitudeWithExtinction(it), 0.01)
            assertEquals(6.22, Uranus(sun).visualMagnitudeWithExtinction(it), 0.01)
            assertEquals(8.09, Neptune(sun).visualMagnitudeWithExtinction(it), 0.01)
        }

        o.copy(apparentMagnitudeAlgorithm = ApparentMagnitudeAlgorithm.MUELLER_1893).let {
            assertEquals(2.14, Mercury(sun).visualMagnitude(it), 0.01)
            assertEquals(-3.37, Venus(sun).visualMagnitude(it), 0.01)
            assertEquals(0.71, Mars(sun).visualMagnitude(it), 0.01)
            assertEquals(-1.49, Jupiter(sun).visualMagnitude(it), 0.01)
            assertEquals(0.83, Saturn(sun).visualMagnitude(it), 0.01)
            assertEquals(6.13, Uranus(sun).visualMagnitude(it), 0.01)
            assertEquals(7.77, Neptune(sun).visualMagnitude(it), 0.01)

            assertEquals(2.28, Mercury(sun).visualMagnitudeWithExtinction(it), 0.01)
            assertEquals(-3.23, Venus(sun).visualMagnitudeWithExtinction(it), 0.01)
            assertEquals(1.48, Mars(sun).visualMagnitudeWithExtinction(it), 0.01)
            assertEquals(-1.35, Jupiter(sun).visualMagnitudeWithExtinction(it), 0.01)
            assertEquals(0.97, Saturn(sun).visualMagnitudeWithExtinction(it), 0.01)
            assertEquals(6.55, Uranus(sun).visualMagnitudeWithExtinction(it), 0.01)
            assertEquals(7.91, Neptune(sun).visualMagnitudeWithExtinction(it), 0.01)
        }

        o.copy(apparentMagnitudeAlgorithm = ApparentMagnitudeAlgorithm.GENERIC).let {
            assertEquals(3.03, Mercury(sun).visualMagnitude(it), 0.01)
            assertEquals(-3.58, Venus(sun).visualMagnitude(it), 0.01)
            assertEquals(0.65, Mars(sun).visualMagnitude(it), 0.01)
            assertEquals(-1.51, Jupiter(sun).visualMagnitude(it), 0.01)
            assertEquals(1.63, Saturn(sun).visualMagnitude(it), 0.01)
            assertEquals(5.98, Uranus(sun).visualMagnitude(it), 0.01)
            assertEquals(7.95, Neptune(sun).visualMagnitude(it), 0.01)

            assertEquals(3.16, Mercury(sun).visualMagnitudeWithExtinction(it), 0.01)
            assertEquals(-3.44, Venus(sun).visualMagnitudeWithExtinction(it), 0.01)
            assertEquals(1.42, Mars(sun).visualMagnitudeWithExtinction(it), 0.01)
            assertEquals(-1.38, Jupiter(sun).visualMagnitudeWithExtinction(it), 0.01)
            assertEquals(1.77, Saturn(sun).visualMagnitudeWithExtinction(it), 0.01)
            assertEquals(6.40, Uranus(sun).visualMagnitudeWithExtinction(it), 0.01)
            assertEquals(8.10, Neptune(sun).visualMagnitudeWithExtinction(it), 0.01)
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

        assertEquals(23.4371, o.computeEclipticObliquity().deg, 0.0001)
        assertEquals(23.4372, o.copy(dateTime = dt.copy(day = 10)).computeEclipticObliquity().deg, 0.0001)
        assertEquals(23.6893, o.copy(dateTime = dt.copy(year = 46)).computeEclipticObliquity().deg, 0.0001)
        assertEquals(24.2108, o.copy(dateTime = dt.copy(year = -6046)).computeEclipticObliquity().deg, 0.0001)
        assertEquals(22.9481, o.copy(dateTime = dt.copy(year = 6046)).computeEclipticObliquity().deg, 0.0001)
    }

    @Test
    fun meanSolarDay() {
        val sun = Sun()
        assertEquals(0.4137, Jupiter(sun).meanSolarDay, 0.0001)
        assertEquals(116.7502, Venus(sun).meanSolarDay, 0.0001)
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

        assertEquals(0.00903, jupiter.angularSize(o) * 2, 0.00001)
        assertEquals(0.00904, jupiter.angularSize(o.copy(dateTime = dt.copy(day = 8))) * 2, 0.00001)
        assertEquals(0.01053, jupiter.angularSize(o.copy(dateTime = dt.copy(month = 5))) * 2, 0.00001)
    }

    companion object {

        val SAO_JOSE_DAS_PALMEIRAS = Location("São José das Palmeiras - BR", -24.837778, -54.063889, 563.0)
        val PICO_DOS_DIAS_OBSERVATORY = Location("Pico dos Dias Observatory - BR", -22.534444, -45.5825, 1864.0)
    }
}