import br.tiagohm.astrum.core.*
import br.tiagohm.astrum.core.math.RaDec
import br.tiagohm.astrum.core.sky.*
import br.tiagohm.astrum.core.time.DateTime
import br.tiagohm.astrum.core.time.DeltaTAlgorithmType
import org.junit.jupiter.api.Assertions.assertEquals
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
                RaDec(319.36244, -15.7656),
                RaDec(324.68691, -10.6681),
                RaDec(307.46573, -19.7921),
                null,
                RaDec(42.11161, 17.5918),
                RaDec(313.74888, -17.9443),
                RaDec(308.30091, -19.2301),
                RaDec(34.74791, 13.4208),
                RaDec(350.70116, -5.1698),
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
                RaDec(319.35828, -15.7673),
                RaDec(324.68284, -10.6698),
                RaDec(307.46139, -19.7936),
                null,
                RaDec(42.10713, 17.5909),
                RaDec(313.74464, -17.9460),
                RaDec(308.29659, -19.2317),
                RaDec(34.74358, 13.4197),
                RaDec(350.69722, -5.1716),
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
                RaDec(319.36441, -15.7649),
                RaDec(324.68987, -10.6669),
                RaDec(307.46674, -19.7918),
                null,
                RaDec(42.11281, 17.5922),
                RaDec(313.74919, -17.9442),
                RaDec(308.30106, -19.2301),
                RaDec(34.74799, 13.4208),
                RaDec(350.70124, -5.1698),
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
                RaDec(319.35672, -15.7673),
                RaDec(324.69637, -10.6657),
                RaDec(307.45908, -19.7933),
                null,
                RaDec(42.10792, 17.5905),
                RaDec(313.74629, -17.945),
                RaDec(308.29908, -19.2305),
                RaDec(34.74669, 13.4203),
                RaDec(350.70019, -5.1702),
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
                RaDec(319.35536, -15.7681),
                RaDec(324.69444, -10.6664),
                RaDec(307.45683, -19.7944),
                null,
                RaDec(42.10508, 17.5902),
                RaDec(313.74255, -17.9465),
                RaDec(308.29502, -19.2320),
                RaDec(34.74246, 13.4193),
                RaDec(350.69634, -5.1720),
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
                RaDec(254.64116, 0.0353),
                null,
                RaDec(244.47535, 3.0177),
                RaDec(94.977, -10.2287),
                RaDec(26.7884, -3.6063),
                RaDec(261.03025, 5.9501),
                RaDec(256.07273, 6.1676),
                RaDec(350.31127, 0.637),
                RaDec(301.58492, 4.8377),
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
                RaDec(348.11758, 2.0377),
                RaDec(352.57937, 3.4949),
                null,
                RaDec(4.44972, 0.6831),
                RaDec(331.53259, 1.898),
                RaDec(193.00046, -0.1601),
                RaDec(185.50387, -0.2444),
                RaDec(281.21562, 0.8482),
                RaDec(231.20865, -0.1019),
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
                RaDec(181.46025, -0.4373),
                RaDec(172.96494, -3.0603),
                RaDec(188.43485, 2.7639),
                RaDec(145.38434, -17.3767),
                null,
                RaDec(213.85139, 14.8264),
                RaDec(213.55342, 14.9409),
                RaDec(316.2515, 18.6091),
                RaDec(263.66439, 25.4280),
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
                RaDec(151.8999, -0.4077),
                RaDec(151.46675, -0.0566),
                RaDec(155.3689, -0.4846),
                RaDec(153.01722, -0.4725),
                RaDec(141.67622, -0.5988),
                null,
                RaDec(321.1847, 1.0994),
                RaDec(76.0703, -2.4142),
                RaDec(19.48327, -1.8694),
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
                RaDec(318.65432, 19.893),
                RaDec(318.66756, 20.0986),
                RaDec(319.86314, 19.3473),
                RaDec(319.71552, 19.4682),
                RaDec(313.4937, 21.729),
                RaDec(313.11683, 21.5618),
                null,
                RaDec(253.86393, 26.9318),
                RaDec(196.45235, 7.2672),
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
                RaDec(189.01196, 51.2506),
                RaDec(188.5445, 50.368),
                RaDec(189.78901, 53.1305),
                RaDec(188.14745, 48.491),
                RaDec(187.8229, 47.9974),
                RaDec(196.38444, 65.183),
                RaDec(210.70511, 75.4602),
                null,
                RaDec(352.4839, 38.2398),
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
                RaDec(123.83216, -22.7148),
                RaDec(123.33756, -22.7713),
                RaDec(125.14225, -22.4131),
                RaDec(122.77255, -23.0061),
                RaDec(120.70892, -23.4092),
                RaDec(131.26003, -20.6209),
                RaDec(141.26626, -17.1594),
                RaDec(78.32872, -26.4601),
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
            var radec = computeRaDec(oi, mercury)

            when (i) {
                5 -> assertRaDecEquals(RaDec(324.69526, -10.6663), radec, 0.0001)
                6 -> assertRaDecEquals(RaDec(323.64525, -10.8355), radec, 0.0001)
                7 -> assertRaDecEquals(RaDec(322.52248, -11.0620), radec, 0.0001)
                8 -> assertRaDecEquals(RaDec(321.35815, -11.3369), radec, 0.0001)
                9 -> assertRaDecEquals(RaDec(320.18409, -11.6507), radec, 0.0001)
                10 -> assertRaDecEquals(RaDec(319.03114, -11.9929), radec, 0.0001)
                11 -> assertRaDecEquals(RaDec(317.92765, -12.3538), radec, 0.0001)
            }

            radec = computeRaDec(oi, sun)

            when (i) {
                5 -> assertRaDecEquals(RaDec(319.35453, -15.7683), radec, 0.0001)
                6 -> assertRaDecEquals(RaDec(320.35724, -15.4602), radec, 0.0001)
                7 -> assertRaDecEquals(RaDec(321.35671, -15.1476), radec, 0.0001)
                8 -> assertRaDecEquals(RaDec(322.35295, -14.8307), radec, 0.0001)
                9 -> assertRaDecEquals(RaDec(323.34596, -14.5096), radec, 0.0001)
                10 -> assertRaDecEquals(RaDec(324.33576, -14.1845), radec, 0.0001)
                11 -> assertRaDecEquals(RaDec(325.32234, -13.8555), radec, 0.0001)
            }
        }

        for (i in 4 downTo 1) {
            val oi = o0.copy(dateTime = DateTime(2021, 2, i, 12, 0, 0))
            var radec = computeRaDec(oi, mercury)

            when (i) {
                4 -> assertRaDecEquals(RaDec(325.64359, -10.5614), radec, 0.0001)
                3 -> assertRaDecEquals(RaDec(326.46494, -10.5262), radec, 0.0001)
                2 -> assertRaDecEquals(RaDec(327.13875, -10.5637), radec, 0.0001)
                1 -> assertRaDecEquals(RaDec(327.64970, -10.6746), radec, 0.0001)
            }

            radec = computeRaDec(oi, sun)

            when (i) {
                4 -> assertRaDecEquals(RaDec(318.34857, -16.0720), radec, 0.0001)
                3 -> assertRaDecEquals(RaDec(317.33935, -16.3710), radec, 0.0001)
                2 -> assertRaDecEquals(RaDec(316.32684, -16.6652), radec, 0.0001)
                1 -> assertRaDecEquals(RaDec(315.31102, -16.9546), radec, 0.0001)
            }
        }
    }

    fun testFrom(
        sun: Sun,
        home: Planet,
        site: ObservationSite,
        dateTime: DateTime,
        positions: List<RaDec?>,
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
            useTopocentricCoordinates,
            useNutation,
            useLightTravelTime
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
                val radec = computeRaDec(o, planets[i])
                assertEquals(it.ra, radec.ra, delta)
                assertEquals(it.dec, radec.dec, delta)
            }
        }
    }

    fun computeRaDec(o: Observer, planet: Planet): RaDec {
        val pos = planet.computeEquinoxEquatorialPosition(o)
        val equ = Algorithms.rectangularToSphericalCoordinates(pos)
        return RaDec(equ[0].deg.in360, equ[1].deg)
    }
}