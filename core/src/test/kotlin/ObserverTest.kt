import br.tiagohm.astrum.core.ObservationSite
import br.tiagohm.astrum.core.Observer
import br.tiagohm.astrum.core.deg
import br.tiagohm.astrum.core.math.Duad
import br.tiagohm.astrum.core.sky.*
import br.tiagohm.astrum.core.time.DateTime
import br.tiagohm.astrum.core.time.DeltaTAlgorithmType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ObserverTest {

    @Test
    fun fromEarth() {
        val sun = Sun()
        val earth = Earth(parent = sun)

        // None
        testFrom(
            sun, earth,
            ObservationSite.SAO_JOSE_DAS_PALMEIRAS,
            DateTime(2021, 2, 5, 12, 0, 0), // 2459251.000000
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
            ObservationSite.SAO_JOSE_DAS_PALMEIRAS,
            DateTime(2021, 2, 5, 12, 0, 0), // 2459251.000000
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
            ObservationSite.SAO_JOSE_DAS_PALMEIRAS,
            DateTime(2021, 2, 5, 12, 0, 0), // 2459251.000000
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
            ObservationSite.SAO_JOSE_DAS_PALMEIRAS,
            DateTime(2021, 2, 5, 12, 0, 0), // 2459251.000000
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
            ObservationSite.SAO_JOSE_DAS_PALMEIRAS,
            DateTime(2021, 2, 5, 12, 0, 0), // 2459251.000000
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
    fun fromMercury() {
        val sun = Sun()
        val mercury = Mercury(parent = sun)

        testFrom(
            sun, mercury,
            ObservationSite("?", 0.0, 0.0, 0.0),
            DateTime(2021, 2, 5, 12, 0, 0), // 2459251.000000
            // Coordinates from Stellarium!!!
            listOf(
                Duad(254.64116, 0.0353),
                null,
                Duad(244.47535, 3.0177),
                Duad(94.977, -10.2287),
                Duad(26.7884, -3.6063),
                Duad(261.03025, 5.9501),
                Duad(256.07273, 6.1676),
                Duad(350.31127, 0.637),
                Duad(301.58492, 4.8377),
            ),
            DeltaTAlgorithmType.ESPEANAK_MEEUS,
            useTopocentricCoordinates = true,
            useNutation = true,
            useLightTravelTime = true,
            delta = 0.01,
        )
    }

    @Test
    fun fromVenus() {
        val sun = Sun()
        val venus = Venus(parent = sun)

        testFrom(
            sun, venus,
            ObservationSite("?", 0.0, 0.0, 0.0),
            DateTime(2021, 2, 5, 12, 0, 0), // 2459251.000000
            // Coordinates from Stellarium!!!
            listOf(
                Duad(348.11758, 2.0377),
                Duad(352.57937, 3.4949),
                null,
                Duad(4.44972, 0.6831),
                Duad(331.53259, 1.898),
                Duad(193.00046, -0.1601),
                Duad(185.50387, -0.2444),
                Duad(281.21562, 0.8482),
                Duad(231.20865, -0.1019),
            ),
            DeltaTAlgorithmType.ESPEANAK_MEEUS,
            useTopocentricCoordinates = true,
            useNutation = true,
            useLightTravelTime = true,
            delta = 0.001,
        )
    }

    @Test
    fun fromMars() {
        val sun = Sun()
        val mars = Mars(parent = sun)

        testFrom(
            sun, mars,
            ObservationSite("?", 0.0, 0.0, 0.0),
            DateTime(2021, 2, 5, 12, 0, 0), // 2459251.000000
            // Coordinates from Stellarium!!!
            listOf(
                Duad(181.46025, -0.4373),
                Duad(172.96494, -3.0603),
                Duad(188.43485, 2.7639),
                Duad(145.38434, -17.3767),
                null,
                Duad(213.85139, 14.8264),
                Duad(213.55342, 14.9409),
                Duad(316.2515, 18.6091),
                Duad(263.66439, 25.4280),
            ),
            DeltaTAlgorithmType.ESPEANAK_MEEUS,
            useTopocentricCoordinates = true,
            useNutation = true,
            useLightTravelTime = true,
            delta = 0.01,
        )
    }

    @Test
    fun fromJupiter() {
        val sun = Sun()
        val jupiter = Jupiter(parent = sun)

        testFrom(
            sun, jupiter,
            ObservationSite("?", 0.0, 0.0, 0.0),
            DateTime(2021, 2, 5, 12, 0, 0), // 2459251.000000
            // Coordinates from Stellarium!!!
            listOf(
                Duad(151.8999, -0.4077),
                Duad(151.46675, -0.0566),
                Duad(155.3689, -0.4846),
                Duad(153.01722, -0.4725),
                Duad(141.67622, -0.5988),
                null,
                Duad(321.1847, 1.0994),
                Duad(76.0703, -2.4142),
                Duad(19.48327, -1.8694),
            ),
            DeltaTAlgorithmType.ESPEANAK_MEEUS,
            useTopocentricCoordinates = true,
            useNutation = true,
            useLightTravelTime = true,
            delta = 0.01,
        )
    }

    @Test
    fun fromSaturn() {
        val sun = Sun()
        val saturn = Saturn(parent = sun)

        testFrom(
            sun, saturn,
            ObservationSite("?", 0.0, 0.0, 0.0),
            DateTime(2021, 2, 5, 12, 0, 0), // 2459251.000000
            // Coordinates from Stellarium!!!
            listOf(
                Duad(318.65432, 19.893),
                Duad(318.66756, 20.0986),
                Duad(319.86314, 19.3473),
                Duad(319.71552, 19.4682),
                Duad(313.4937, 21.729),
                Duad(313.11683, 21.5618),
                null,
                Duad(253.86393, 26.9318),
                Duad(196.45235, 7.2672),
            ),
            DeltaTAlgorithmType.ESPEANAK_MEEUS,
            useTopocentricCoordinates = true,
            useNutation = true,
            useLightTravelTime = true,
            delta = 0.001,
        )
    }

    @Test
    fun fromUranus() {
        val sun = Sun()
        val uranus = Uranus(parent = sun)

        testFrom(
            sun, uranus,
            ObservationSite("?", 0.0, 0.0, 0.0),
            DateTime(2021, 2, 5, 12, 0, 0), // 2459251.000000
            // Coordinates from Stellarium!!!
            listOf(
                Duad(189.01196, 51.2506),
                Duad(188.5445, 50.368),
                Duad(189.78901, 53.1305),
                Duad(188.14745, 48.491),
                Duad(187.8229, 47.9974),
                Duad(196.38444, 65.183),
                Duad(210.70511, 75.4602),
                null,
                Duad(352.4839, 38.2398),
            ),
            DeltaTAlgorithmType.ESPEANAK_MEEUS,
            useTopocentricCoordinates = true,
            useNutation = true,
            useLightTravelTime = true,
            delta = 0.01,
        )
    }

    @Test
    fun fromNeptune() {
        val sun = Sun()
        val neptune = Neptune(parent = sun)

        testFrom(
            sun, neptune,
            ObservationSite("?", 0.0, 0.0, 0.0),
            DateTime(2021, 2, 5, 12, 0, 0), // 2459251.000000
            // Coordinates from Stellarium!!!
            listOf(
                Duad(123.83216, -22.7148),
                Duad(123.33756, -22.7713),
                Duad(125.14225, -22.4131),
                Duad(122.77255, -23.0061),
                Duad(120.70892, -23.4092),
                Duad(131.26003, -20.6209),
                Duad(141.26626, -17.1594),
                Duad(78.32872, -26.4601),
                null,
            ),
            DeltaTAlgorithmType.ESPEANAK_MEEUS,
            useTopocentricCoordinates = true,
            useNutation = true,
            useLightTravelTime = true,
            delta = 0.001,
        )
    }

    @Test
    fun timeAdvance() {
        val sun = Sun()
        val earth = Earth(sun)

        val o0 = Observer(
            earth,
            ObservationSite.SAO_JOSE_DAS_PALMEIRAS,
            DateTime(2021, 2, 5, 12, 0, 0), // 2459251.000000
            DeltaTAlgorithmType.NONE,
            useTopocentricCoordinates = true,
            useNutation = true,
            useLightTravelTime = true,
        )

        val mercury = Mercury(sun)

        for (i in 5 until 12) {
            val oi = o0.copy(dateTime = DateTime(2021, 2, i, 12, 0, 0))
            var Duad = mercury.computeRaDec(oi)

            when (i) {
                5 -> assertDuadEquals(Duad(324.69526, -10.6663), Duad, 0.0001)
                6 -> assertDuadEquals(Duad(323.64525, -10.8355), Duad, 0.0001)
                7 -> assertDuadEquals(Duad(322.52248, -11.0620), Duad, 0.0001)
                8 -> assertDuadEquals(Duad(321.35815, -11.3369), Duad, 0.0001)
                9 -> assertDuadEquals(Duad(320.18409, -11.6507), Duad, 0.0001)
                10 -> assertDuadEquals(Duad(319.03114, -11.9929), Duad, 0.0001)
                11 -> assertDuadEquals(Duad(317.92765, -12.3538), Duad, 0.0001)
            }

            Duad = sun.computeRaDec(oi)

            when (i) {
                5 -> assertDuadEquals(Duad(319.35453, -15.7683), Duad, 0.0001)
                6 -> assertDuadEquals(Duad(320.35724, -15.4602), Duad, 0.0001)
                7 -> assertDuadEquals(Duad(321.35671, -15.1476), Duad, 0.0001)
                8 -> assertDuadEquals(Duad(322.35295, -14.8307), Duad, 0.0001)
                9 -> assertDuadEquals(Duad(323.34596, -14.5096), Duad, 0.0001)
                10 -> assertDuadEquals(Duad(324.33576, -14.1845), Duad, 0.0001)
                11 -> assertDuadEquals(Duad(325.32234, -13.8555), Duad, 0.0001)
            }
        }

        for (i in 4 downTo 1) {
            val oi = o0.copy(dateTime = DateTime(2021, 2, i, 12, 0, 0))
            var Duad = mercury.computeRaDec(oi)

            when (i) {
                4 -> assertDuadEquals(Duad(325.64359, -10.5614), Duad, 0.0001)
                3 -> assertDuadEquals(Duad(326.46494, -10.5262), Duad, 0.0001)
                2 -> assertDuadEquals(Duad(327.13875, -10.5637), Duad, 0.0001)
                1 -> assertDuadEquals(Duad(327.64970, -10.6746), Duad, 0.0001)
            }

            Duad = sun.computeRaDec(oi)

            when (i) {
                4 -> assertDuadEquals(Duad(318.34857, -16.0720), Duad, 0.0001)
                3 -> assertDuadEquals(Duad(317.33935, -16.3710), Duad, 0.0001)
                2 -> assertDuadEquals(Duad(316.32684, -16.6652), Duad, 0.0001)
                1 -> assertDuadEquals(Duad(315.31102, -16.9546), Duad, 0.0001)
            }
        }
    }

    @Test
    fun isAboveHorizon() {
        val sun = Sun()
        val earth = Earth(parent = sun)

        val o0 = Observer(
            earth,
            ObservationSite.PICO_DOS_DIAS_OBSERVATORY,
            DateTime(2021, 2, 5, 12, 0, 0), // 2459251.000000
        )

        assertTrue(Neptune(sun).isAboveHorizon(o0))
        assertTrue(!Mars(sun).isAboveHorizon(o0))
        assertTrue(!Uranus(sun).isAboveHorizon(o0))

        val o1 = Observer(
            earth,
            ObservationSite.PICO_DOS_DIAS_OBSERVATORY,
            DateTime(2021, 2, 6, 0, 0, 0), // 2459251.500000
        )

        assertTrue(!Neptune(sun).isAboveHorizon(o1))
        assertTrue(Mars(sun).isAboveHorizon(o1))
        assertTrue(Uranus(sun).isAboveHorizon(o1))
    }

    fun testFrom(
        sun: Sun,
        home: Planet,
        site: ObservationSite,
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

        val mercury = Mercury(sun)
        val venus = Venus(sun)
        val earth = Earth(sun)
        val mars = Mars(sun)
        val jupiter = Jupiter(sun)
        val saturn = Saturn(sun)
        val uranus = Uranus(sun)
        val neptune = Neptune(sun)

        val planets = listOf(sun, mercury, venus, earth, mars, jupiter, saturn, uranus, neptune)

        for (i in 0 until 8) {
            positions[i]?.let {
                val (ra, dec) = planets[i].computeRaDec(o)
                assertEquals(it[0], ra, delta)
                assertEquals(it[1], dec, delta)
            }
        }
    }

    @Test
    fun parallacticAngle() {
        val sun = Sun()
        val earth = Earth(parent = sun)

        // TODO: BUG!!!

        val o = Observer(
            earth,
            ObservationSite.PICO_DOS_DIAS_OBSERVATORY,
            DateTime(2021, 2, 5, 12, 0, 0), // 2459251.000000
        )

        System.err.println(Jupiter(sun).computeParallacticAngle(o).deg)
    }

    @Test
    fun altAz() {
        val sun = Sun()
        val earth = Earth(parent = sun)

        val o0 = Observer(
            earth,
            ObservationSite.PICO_DOS_DIAS_OBSERVATORY,
            DateTime(2021, 2, 5, 12, 0, 0), // 2459251.000000
        )

        assertDuadEquals(Duad(90.7432, 43.3413), sun.computeAltAz(o0), 0.0001)
        assertDuadEquals(Duad(86.5517, 36.8015), Mercury(sun).computeAltAz(o0), 0.0001)
        assertDuadEquals(Duad(92.5681, 55.3030), Venus(sun).computeAltAz(o0), 0.0001)
        assertDuadEquals(Duad(87.1402, -44.6719), Mars(sun).computeAltAz(o0), 0.0001)
        assertDuadEquals(Duad(91.7507, 49.0952), Jupiter(sun).computeAltAz(o0), 0.0001)
        assertDuadEquals(Duad(91.8896, 54.4151), Saturn(sun).computeAltAz(o0), 0.0001)
        assertDuadEquals(Duad(89.7587, -36.6858), Uranus(sun).computeAltAz(o0), 0.0001)
        assertDuadEquals(Duad(91.1530, 10.8117), Neptune(sun).computeAltAz(o0), 0.0001)

        val o1 = Observer(
            earth,
            ObservationSite("Tokyo", 35.689499, 139.691711, 44.0),
            DateTime(2021, 2, 5, 12, 0, 0), // 2459251.000000
        )

        assertDuadEquals(Duad(285.4773, -46.2733), sun.computeAltAz(o1), 0.0001)
        assertDuadEquals(Duad(286.8195, -39.0608), Mercury(sun).computeAltAz(o1), 0.0001)
        assertDuadEquals(Duad(291.0016, -57.8109), Venus(sun).computeAltAz(o1), 0.0001)
        assertDuadEquals(Duad(263.6072, 39.5997), Mars(sun).computeAltAz(o1), 0.0001)
        assertDuadEquals(Duad(287.5396, -51.9072), Jupiter(sun).computeAltAz(o1), 0.0001)
        assertDuadEquals(Duad(290.97, -56.8399), Saturn(sun).computeAltAz(o1), 0.0001)
        assertDuadEquals(Duad(264.0623, 31.3826), Uranus(sun).computeAltAz(o1), 0.0001)
        assertDuadEquals(Duad(274.4963, -15.0647), Neptune(sun).computeAltAz(o1), 0.0001)
    }

    @Test
    fun ahDec() {
        val sun = Sun()
        val earth = Earth(parent = sun)

        val o0 = Observer(
            earth,
            ObservationSite.PICO_DOS_DIAS_OBSERVATORY,
            DateTime(2021, 2, 5, 12, 0, 0), // 2459251.000000
        )

        assertDuadEquals(Duad(20.72784, -15.7682), sun.computeHaDec(o0), 0.0001)
        assertDuadEquals(Duad(20.37190, -10.6666), Mercury(sun).computeHaDec(o0), 0.0001)
        assertDuadEquals(Duad(21.52107, -19.7945), Venus(sun).computeHaDec(o0), 0.0001)
        assertDuadEquals(Duad(15.21116, 17.5901), Mars(sun).computeHaDec(o0), 0.0001)
        assertDuadEquals(Duad(21.10202, -17.9465), Jupiter(sun).computeHaDec(o0), 0.0001)
        assertDuadEquals(Duad(21.46518, -19.232), Saturn(sun).computeHaDec(o0), 0.0001)
        assertDuadEquals(Duad(15.70202, 13.4193), Uranus(sun).computeHaDec(o0), 0.0001)
        assertDuadEquals(Duad(18.63843, -5.172), Neptune(sun).computeHaDec(o0), 0.0001)

        val o1 = Observer(
            earth,
            ObservationSite("Tokyo", 35.689499, 139.691711, 44.0),
            DateTime(2021, 2, 5, 12, 0, 0), // 2459251.000000
        )

        assertDuadEquals(Duad(9.07967, -15.7697), sun.computeHaDec(o1), 0.0001)
        assertDuadEquals(Duad(8.72385, -10.6693), Mercury(sun).computeHaDec(o1), 0.0001)
        assertDuadEquals(Duad(9.87279, -19.7951), Venus(sun).computeHaDec(o1), 0.0001)
        assertDuadEquals(Duad(3.56296, 17.5890), Mars(sun).computeHaDec(o1), 0.0001)
        assertDuadEquals(Duad(9.45366, -17.9467), Jupiter(sun).computeHaDec(o1), 0.0001)
        assertDuadEquals(Duad(9.81681, -19.2321), Saturn(sun).computeHaDec(o1), 0.0001)
        assertDuadEquals(Duad(4.05365, 13.4192), Uranus(sun).computeHaDec(o1), 0.0001)
        assertDuadEquals(Duad(6.99005, -5.172), Neptune(sun).computeHaDec(o1), 0.0001)
    }
}