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
        val earth = Earth(sun)

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
            var pos = mercury.computeRaDecOfDate(oi)

            when (i) {
                5 -> assertDuadEquals(Duad(324.69526, -10.6663), pos, 0.0001)
                6 -> assertDuadEquals(Duad(323.64525, -10.8355), pos, 0.0001)
                7 -> assertDuadEquals(Duad(322.52248, -11.0620), pos, 0.0001)
                8 -> assertDuadEquals(Duad(321.35815, -11.3369), pos, 0.0001)
                9 -> assertDuadEquals(Duad(320.18409, -11.6507), pos, 0.0001)
                10 -> assertDuadEquals(Duad(319.03114, -11.9929), pos, 0.0001)
                11 -> assertDuadEquals(Duad(317.92765, -12.3538), pos, 0.0001)
            }

            pos = sun.computeRaDecOfDate(oi)

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
            val oi = o0.copy(dateTime = DateTime(2021, 2, i, 12, 0, 0))
            var pos = mercury.computeRaDecOfDate(oi)

            when (i) {
                4 -> assertDuadEquals(Duad(325.64359, -10.5614), pos, 0.0001)
                3 -> assertDuadEquals(Duad(326.46494, -10.5262), pos, 0.0001)
                2 -> assertDuadEquals(Duad(327.13875, -10.5637), pos, 0.0001)
                1 -> assertDuadEquals(Duad(327.64970, -10.6746), pos, 0.0001)
            }

            pos = sun.computeRaDecOfDate(oi)

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
                val (ra, dec) = planets[i].computeRaDecOfDate(o)
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
            DateTime(2021, 2, 5, 12, 0, 0), // 2459251.000000
        )

        System.err.println(Jupiter(sun).computeParallacticAngle(o).deg)
    }

    @Test
    fun raDecJ2000() {
        val sun = Sun()
        val earth = Earth(sun)

        val o = Observer(
            earth,
            ObservationSite.PICO_DOS_DIAS_OBSERVATORY,
            DateTime(2021, 2, 5, 12, 0, 0), // 2459251.000000
        )

        assertDuadEquals(Duad(319.06728, -15.8555), sun.computeRaDecJ2000(o), 0.0001)
        assertDuadEquals(Duad(324.41508, -10.7605), Mercury(sun).computeRaDecJ2000(o), 0.0001)
        assertDuadEquals(Duad(307.157, -19.8641), Venus(sun).computeRaDecJ2000(o), 0.0001)
        assertDuadEquals(Duad(41.81461, 17.5037), Mars(sun).computeRaDecJ2000(o), 0.0001)
        assertDuadEquals(Duad(313.4488, -18.0259), Jupiter(sun).computeRaDecJ2000(o), 0.0001)
        assertDuadEquals(Duad(307.99669, -19.303), Saturn(sun).computeRaDecJ2000(o), 0.0001)
        assertDuadEquals(Duad(34.4606, 13.3237), Uranus(sun).computeRaDecJ2000(o), 0.0001)
        assertDuadEquals(Duad(350.42818, -5.286), Neptune(sun).computeRaDecJ2000(o), 0.0001)
    }

    @Test
    fun altAz() {
        val sun = Sun()
        val earth = Earth(sun)

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
    fun altAzApparent() {
        val sun = Sun()
        val earth = Earth(sun)

        val o = Observer(
            earth,
            ObservationSite.PICO_DOS_DIAS_OBSERVATORY,
            DateTime(2021, 2, 5, 12, 0, 0), // 2459251.000000
        )

        assertDuadEquals(Duad(90.7432, 43.3584), sun.computeAltAz(o, apparent = true), 0.0001)
        assertDuadEquals(Duad(86.5517, 36.8230), Mercury(sun).computeAltAz(o, apparent = true), 0.0001)
        assertDuadEquals(Duad(92.5681, 55.3141), Venus(sun).computeAltAz(o, apparent = true), 0.0001)
        assertDuadEquals(Duad(87.1402, -44.6719), Mars(sun).computeAltAz(o, apparent = true), 0.0001)
        assertDuadEquals(Duad(91.7507, 49.1092), Jupiter(sun).computeAltAz(o, apparent = true), 0.0001)
        assertDuadEquals(Duad(91.8896, 54.4267), Saturn(sun).computeAltAz(o, apparent = true), 0.0001)
        assertDuadEquals(Duad(89.7587, -36.6858), Uranus(sun).computeAltAz(o, apparent = true), 0.0001)
        assertDuadEquals(Duad(91.1530, 10.8916), Neptune(sun).computeAltAz(o, apparent = true), 0.0001)
    }

    @Test
    fun altAzApparentSouthAzimuth() {
        val sun = Sun()
        val earth = Earth(sun)

        val o = Observer(
            earth,
            ObservationSite.PICO_DOS_DIAS_OBSERVATORY,
            DateTime(2021, 2, 5, 12, 0, 0), // 2459251.000000
        )

        assertDuadEquals(Duad(270.7432, 43.3584), sun.computeAltAz(o, true, apparent = true), 0.0001)
        assertDuadEquals(Duad(266.5517, 36.8230), Mercury(sun).computeAltAz(o, true, apparent = true), 0.0001)
        assertDuadEquals(Duad(272.5681, 55.3141), Venus(sun).computeAltAz(o, true, apparent = true), 0.0001)
        assertDuadEquals(Duad(267.1402, -44.6719), Mars(sun).computeAltAz(o, true, apparent = true), 0.0001)
        assertDuadEquals(Duad(271.7507, 49.1092), Jupiter(sun).computeAltAz(o, true, apparent = true), 0.0001)
        assertDuadEquals(Duad(271.8896, 54.4267), Saturn(sun).computeAltAz(o, true, apparent = true), 0.0001)
        assertDuadEquals(Duad(269.7587, -36.6858), Uranus(sun).computeAltAz(o, true, apparent = true), 0.0001)
        assertDuadEquals(Duad(271.1530, 10.8916), Neptune(sun).computeAltAz(o, true, apparent = true), 0.0001)
    }

    @Test
    fun hourAngle() {
        val sun = Sun()
        val earth = Earth(sun)

        val o0 = Observer(
            earth,
            ObservationSite.PICO_DOS_DIAS_OBSERVATORY,
            DateTime(2021, 2, 5, 12, 0, 0), // 2459251.000000
        )

        assertDuadEquals(Duad(20.72784, -15.7682), sun.computeHourAngle(o0), 0.0001)
        assertDuadEquals(Duad(20.37190, -10.6666), Mercury(sun).computeHourAngle(o0), 0.0001)
        assertDuadEquals(Duad(21.52107, -19.7945), Venus(sun).computeHourAngle(o0), 0.0001)
        assertDuadEquals(Duad(15.21116, 17.5901), Mars(sun).computeHourAngle(o0), 0.0001)
        assertDuadEquals(Duad(21.10202, -17.9465), Jupiter(sun).computeHourAngle(o0), 0.0001)
        assertDuadEquals(Duad(21.46518, -19.232), Saturn(sun).computeHourAngle(o0), 0.0001)
        assertDuadEquals(Duad(15.70202, 13.4193), Uranus(sun).computeHourAngle(o0), 0.0001)
        assertDuadEquals(Duad(18.63843, -5.172), Neptune(sun).computeHourAngle(o0), 0.0001)

        val o1 = Observer(
            earth,
            ObservationSite("Tokyo", 35.689499, 139.691711, 44.0),
            DateTime(2021, 2, 5, 12, 0, 0), // 2459251.000000
        )

        assertDuadEquals(Duad(9.07967, -15.7697), sun.computeHourAngle(o1), 0.0001)
        assertDuadEquals(Duad(8.72385, -10.6693), Mercury(sun).computeHourAngle(o1), 0.0001)
        assertDuadEquals(Duad(9.87279, -19.7951), Venus(sun).computeHourAngle(o1), 0.0001)
        assertDuadEquals(Duad(3.56296, 17.5890), Mars(sun).computeHourAngle(o1), 0.0001)
        assertDuadEquals(Duad(9.45366, -17.9467), Jupiter(sun).computeHourAngle(o1), 0.0001)
        assertDuadEquals(Duad(9.81681, -19.2321), Saturn(sun).computeHourAngle(o1), 0.0001)
        assertDuadEquals(Duad(4.05365, 13.4192), Uranus(sun).computeHourAngle(o1), 0.0001)
        assertDuadEquals(Duad(6.99005, -5.172), Neptune(sun).computeHourAngle(o1), 0.0001)
    }

    @Test
    fun hourAngleApparent() {
        val sun = Sun()
        val earth = Earth(sun)

        val o = Observer(
            earth,
            ObservationSite.PICO_DOS_DIAS_OBSERVATORY,
            DateTime(2021, 2, 5, 12, 0, 0), // 2459251.000000
        )

        assertDuadEquals(Duad(20.72897, -15.7730), sun.computeHourAngle(o, true), 0.0001)
        assertDuadEquals(Duad(20.37327, -10.6740), Mercury(sun).computeHourAngle(o, true), 0.0001)
        assertDuadEquals(Duad(21.52185, -19.7966), Venus(sun).computeHourAngle(o, true), 0.0001)
        assertDuadEquals(Duad(15.21116, 17.5901), Mars(sun).computeHourAngle(o, true), 0.0001)
        assertDuadEquals(Duad(21.10297, -17.9499), Jupiter(sun).computeHourAngle(o, true), 0.0001)
        assertDuadEquals(Duad(21.46598, -19.2344), Saturn(sun).computeHourAngle(o, true), 0.0001)
        assertDuadEquals(Duad(15.70202, 13.4193), Uranus(sun).computeHourAngle(o, true), 0.0001)
        assertDuadEquals(Duad(18.64339, -5.2019), Neptune(sun).computeHourAngle(o, true), 0.0001)
    }
}