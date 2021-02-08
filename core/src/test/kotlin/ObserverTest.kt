import br.tiagohm.astrum.core.ObservationSite
import br.tiagohm.astrum.core.Observer
import br.tiagohm.astrum.core.deg
import br.tiagohm.astrum.core.math.Duad
import br.tiagohm.astrum.core.math.Triad
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
        val earth = Earth(sun)

        // None
        testFrom(
            sun, earth,
            ObservationSite.SAO_JOSE_DAS_PALMEIRAS,
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
            ObservationSite.SAO_JOSE_DAS_PALMEIRAS,
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
            ObservationSite.SAO_JOSE_DAS_PALMEIRAS,
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
            ObservationSite.SAO_JOSE_DAS_PALMEIRAS,
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
            ObservationSite.SAO_JOSE_DAS_PALMEIRAS,
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
            ObservationSite.SAO_JOSE_DAS_PALMEIRAS,
            DateTime(2021, 2, 5, 9, 0, 0), // 2459251.000000
            DeltaTAlgorithmType.NONE,
            useTopocentricCoordinates = true,
            useNutation = true,
            useLightTravelTime = true,
        )

        val mercury = Mercury(sun)

        for (i in 5 until 12) {
            val oi = o0.copy(dateTime = DateTime(2021, 2, i, 9, 0, 0))
            var pos = mercury.raDecOfDate(oi)

            when (i) {
                5 -> assertDuadEquals(Duad(324.69526, -10.6663), pos, 0.0001)
                6 -> assertDuadEquals(Duad(323.64525, -10.8355), pos, 0.0001)
                7 -> assertDuadEquals(Duad(322.52248, -11.0620), pos, 0.0001)
                8 -> assertDuadEquals(Duad(321.35815, -11.3369), pos, 0.0001)
                9 -> assertDuadEquals(Duad(320.18409, -11.6507), pos, 0.0001)
                10 -> assertDuadEquals(Duad(319.03114, -11.9929), pos, 0.0001)
                11 -> assertDuadEquals(Duad(317.92765, -12.3538), pos, 0.0001)
            }

            pos = sun.raDecOfDate(oi)

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
            var pos = mercury.raDecOfDate(oi)

            when (i) {
                4 -> assertDuadEquals(Duad(325.64359, -10.5614), pos, 0.0001)
                3 -> assertDuadEquals(Duad(326.46494, -10.5262), pos, 0.0001)
                2 -> assertDuadEquals(Duad(327.13875, -10.5637), pos, 0.0001)
                1 -> assertDuadEquals(Duad(327.64970, -10.6746), pos, 0.0001)
            }

            pos = sun.raDecOfDate(oi)

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
            ObservationSite.PICO_DOS_DIAS_OBSERVATORY,
            DateTime(2021, 2, 5, 9, 0, 0),
        )

        assertTrue(Neptune(sun).isAboveHorizon(o0))
        assertTrue(!Mars(sun).isAboveHorizon(o0))
        assertTrue(!Uranus(sun).isAboveHorizon(o0))

        val o1 = Observer(
            earth,
            ObservationSite.PICO_DOS_DIAS_OBSERVATORY,
            DateTime(2021, 2, 5, 21, 0, 0),
        )

        assertTrue(!Neptune(sun).isAboveHorizon(o1))
        assertTrue(Mars(sun).isAboveHorizon(o1))
        assertTrue(Uranus(sun).isAboveHorizon(o1))
    }

    fun testFrom(
        sun: Sun,
        home: Earth,
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
                val (ra, dec) = planets[i].raDecOfDate(o)
                assertEquals(it[0], ra, delta)
                assertEquals(it[1], dec, delta)
            }
        }
    }

    @Test
    fun parallacticAngle() {
        val sun = Sun()
        val earth = Earth(sun)

        // TODO: BUG!!!

        val o = Observer(
            earth,
            ObservationSite.PICO_DOS_DIAS_OBSERVATORY,
            DateTime(2021, 2, 5, 9, 0, 0), // 2459251.000000
        )

        System.err.println(Jupiter(sun).parallacticAngle(o).deg)
    }

    @Test
    fun raDecJ2000() {
        val sun = Sun()
        val earth = Earth(sun)

        val o = Observer(
            earth,
            ObservationSite.PICO_DOS_DIAS_OBSERVATORY,
            DateTime(2021, 2, 5, 9, 0, 0), // 2459251.000000
        )

        assertDuadEquals(Duad(319.06728, -15.8555), sun.raDecJ2000(o), 0.0001)
        assertDuadEquals(Duad(324.41508, -10.7605), Mercury(sun).raDecJ2000(o), 0.0001)
        assertDuadEquals(Duad(307.157, -19.8641), Venus(sun).raDecJ2000(o), 0.0001)
        assertDuadEquals(Duad(41.81461, 17.5037), Mars(sun).raDecJ2000(o), 0.0001)
        assertDuadEquals(Duad(313.4488, -18.0259), Jupiter(sun).raDecJ2000(o), 0.0001)
        assertDuadEquals(Duad(307.99669, -19.303), Saturn(sun).raDecJ2000(o), 0.0001)
        assertDuadEquals(Duad(34.4606, 13.3237), Uranus(sun).raDecJ2000(o), 0.0001)
        assertDuadEquals(Duad(350.42818, -5.286), Neptune(sun).raDecJ2000(o), 0.0001)
    }

    @Test
    fun altAz() {
        val sun = Sun()
        val earth = Earth(sun)

        val o0 = Observer(
            earth,
            ObservationSite.PICO_DOS_DIAS_OBSERVATORY,
            DateTime(2021, 2, 5, 9, 0, 0), // 2459251.000000
        )

        assertDuadEquals(Duad(90.7432, 43.3413), sun.altAz(o0), 0.0001)
        assertDuadEquals(Duad(86.5517, 36.8015), Mercury(sun).altAz(o0), 0.0001)
        assertDuadEquals(Duad(92.5681, 55.3030), Venus(sun).altAz(o0), 0.0001)
        assertDuadEquals(Duad(87.1402, -44.6719), Mars(sun).altAz(o0), 0.0001)
        assertDuadEquals(Duad(91.7507, 49.0952), Jupiter(sun).altAz(o0), 0.0001)
        assertDuadEquals(Duad(91.8896, 54.4151), Saturn(sun).altAz(o0), 0.0001)
        assertDuadEquals(Duad(89.7587, -36.6858), Uranus(sun).altAz(o0), 0.0001)
        assertDuadEquals(Duad(91.1530, 10.8117), Neptune(sun).altAz(o0), 0.0001)

        val o1 = Observer(
            earth,
            ObservationSite("Tokyo", 35.689499, 139.691711, 44.0),
            DateTime(2021, 2, 5, 9, 0, 0), // 2459251.000000
        )

        assertDuadEquals(Duad(285.4773, -46.2733), sun.altAz(o1), 0.0001)
        assertDuadEquals(Duad(286.8195, -39.0608), Mercury(sun).altAz(o1), 0.0001)
        assertDuadEquals(Duad(291.0016, -57.8109), Venus(sun).altAz(o1), 0.0001)
        assertDuadEquals(Duad(263.6072, 39.5997), Mars(sun).altAz(o1), 0.0001)
        assertDuadEquals(Duad(287.5396, -51.9072), Jupiter(sun).altAz(o1), 0.0001)
        assertDuadEquals(Duad(290.97, -56.8399), Saturn(sun).altAz(o1), 0.0001)
        assertDuadEquals(Duad(264.0623, 31.3826), Uranus(sun).altAz(o1), 0.0001)
        assertDuadEquals(Duad(274.4963, -15.0647), Neptune(sun).altAz(o1), 0.0001)
    }

    @Test
    fun altAzApparent() {
        val sun = Sun()
        val earth = Earth(sun)

        val o = Observer(
            earth,
            ObservationSite.PICO_DOS_DIAS_OBSERVATORY,
            DateTime(2021, 2, 5, 9, 0, 0), // 2459251.000000
        )

        assertDuadEquals(Duad(90.7432, 43.3584), sun.altAz(o, apparent = true), 0.0001)
        assertDuadEquals(Duad(86.5517, 36.8230), Mercury(sun).altAz(o, apparent = true), 0.0001)
        assertDuadEquals(Duad(92.5681, 55.3141), Venus(sun).altAz(o, apparent = true), 0.0001)
        assertDuadEquals(Duad(87.1402, -44.6719), Mars(sun).altAz(o, apparent = true), 0.0001)
        assertDuadEquals(Duad(91.7507, 49.1092), Jupiter(sun).altAz(o, apparent = true), 0.0001)
        assertDuadEquals(Duad(91.8896, 54.4267), Saturn(sun).altAz(o, apparent = true), 0.0001)
        assertDuadEquals(Duad(89.7587, -36.6858), Uranus(sun).altAz(o, apparent = true), 0.0001)
        assertDuadEquals(Duad(91.1530, 10.8916), Neptune(sun).altAz(o, apparent = true), 0.0001)
    }

    @Test
    fun altAzApparentSouthAzimuth() {
        val sun = Sun()
        val earth = Earth(sun)

        val o = Observer(
            earth,
            ObservationSite.PICO_DOS_DIAS_OBSERVATORY,
            DateTime(2021, 2, 5, 9, 0, 0), // 2459251.000000
        )

        assertDuadEquals(Duad(270.7432, 43.3584), sun.altAz(o, true, apparent = true), 0.0001)
        assertDuadEquals(Duad(266.5517, 36.8230), Mercury(sun).altAz(o, true, apparent = true), 0.0001)
        assertDuadEquals(Duad(272.5681, 55.3141), Venus(sun).altAz(o, true, apparent = true), 0.0001)
        assertDuadEquals(Duad(267.1402, -44.6719), Mars(sun).altAz(o, true, apparent = true), 0.0001)
        assertDuadEquals(Duad(271.7507, 49.1092), Jupiter(sun).altAz(o, true, apparent = true), 0.0001)
        assertDuadEquals(Duad(271.8896, 54.4267), Saturn(sun).altAz(o, true, apparent = true), 0.0001)
        assertDuadEquals(Duad(269.7587, -36.6858), Uranus(sun).altAz(o, true, apparent = true), 0.0001)
        assertDuadEquals(Duad(271.1530, 10.8916), Neptune(sun).altAz(o, true, apparent = true), 0.0001)
    }

    @Test
    fun hourAngle() {
        val sun = Sun()
        val earth = Earth(sun)

        val o0 = Observer(
            earth,
            ObservationSite.PICO_DOS_DIAS_OBSERVATORY,
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
            ObservationSite("Tokyo", 35.689499, 139.691711, 44.0),
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
            ObservationSite.PICO_DOS_DIAS_OBSERVATORY,
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
            ObservationSite.PICO_DOS_DIAS_OBSERVATORY,
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
            ObservationSite.PICO_DOS_DIAS_OBSERVATORY,
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
            ObservationSite.PICO_DOS_DIAS_OBSERVATORY,
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
            ObservationSite.PICO_DOS_DIAS_OBSERVATORY,
            DateTime(2021, 2, 5, 9, 0, 0), // 2459251.000000
        )

        assertDuadEquals(Duad(316.9039, 0.00000), sun.eclipticOfDate(o), 0.0001)
        assertDuadEquals(Duad(323.4408, 3.2146), Mercury(sun).eclipticOfDate(o), 0.0001)
        assertDuadEquals(Duad(304.9094, -0.781), Venus(sun).eclipticOfDate(o), 0.0001)
        assertDuadEquals(Duad(44.9753, 1.3212), Mars(sun).eclipticOfDate(o), 0.0001)
        assertDuadEquals(Duad(311.1328, -0.535), Jupiter(sun).eclipticOfDate(o), 0.0001)
        assertDuadEquals(Duad(305.8131, -0.4285), Saturn(sun).eclipticOfDate(o), 0.0001)
        assertDuadEquals(Duad(36.9358, -0.4327), Uranus(sun).eclipticOfDate(o), 0.0001)
        assertDuadEquals(Duad(349.4198, -1.0697), Neptune(sun).eclipticOfDate(o), 0.0001)
    }

    @Test
    fun rts() {
        val sun = Sun()
        val earth = Earth(sun)

        val o = Observer(
            earth,
            ObservationSite.PICO_DOS_DIAS_OBSERVATORY,
            DateTime(2021, 2, 5, 9, 0, 0), // 2459251.000000
        )
        
        // TODO: Moon, DSOs, Pluto, etc

        assertTriadEquals(Triad(5.7500, 12.2833, 18.8167), sun.rts(o), 0.01)
        assertTriadEquals(Triad(6.283333, 12.633333, 19.0), Mercury(sun).rts(o), 0.01)
        assertTriadEquals(Triad(4.85, 11.483, 18.117), Venus(sun).rts(o), 0.01)
        assertTriadEquals(Triad(12.267, 17.817, 23.367), Mars(sun).rts(o), 0.01)
        assertTriadEquals(Triad(5.333, 11.9, 18.483), Jupiter(sun).rts(o), 0.01)
        assertTriadEquals(Triad(4.933, 11.55, 18.15), Saturn(sun).rts(o), 0.01)
        assertTriadEquals(Triad(11.65, 17.317,23.0), Uranus(sun).rts(o), 0.01)
        assertTriadEquals(Triad(8.183, 14.383, 20.583), Neptune(sun).rts(o), 0.01)
    }
}