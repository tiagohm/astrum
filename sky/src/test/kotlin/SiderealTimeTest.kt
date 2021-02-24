import br.tiagohm.astrum.sky.core.orbit.KeplerOrbit
import br.tiagohm.astrum.sky.core.time.EspenakMeeus
import br.tiagohm.astrum.sky.core.time.SiderealTime
import br.tiagohm.astrum.sky.core.units.distance.AU
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.math.abs

class SiderealTimeTest {

    @Test
    fun greenwichMeanSiderealTime() {
        val data = arrayOf(
            // Year 1942
            // Source: Astronomical Yearbook of USSR for the Year 1942 (Original: Астрономический ежегодник СССР на 1942 г)
            doubleArrayOf(2430360.5, 6.0, 40.0, 3.137),
            doubleArrayOf(2430391.5, 8.0, 42.0, 16.391),
            doubleArrayOf(2430419.5, 10.0, 32.0, 39.890),
            doubleArrayOf(2430450.5, 12.0, 34.0, 52.986),
            doubleArrayOf(2430480.5, 14.0, 33.0, 9.561),
            doubleArrayOf(2430511.5, 16.0, 35.0, 22.787),
            doubleArrayOf(2430541.5, 18.0, 33.0, 39.501),
            doubleArrayOf(2430572.5, 20.0, 35.0, 52.728),
            doubleArrayOf(2430603.5, 22.0, 38.0, 5.879),
            doubleArrayOf(2430633.5, 0.0, 36.0, 22.443),
            doubleArrayOf(2430664.5, 2.0, 38.0, 35.586),
            doubleArrayOf(2430694.5, 4.0, 36.0, 52.244),
            // Year 1993
            // Source: Astronomical Yearbook of USSR for the Year 1993 (Original: Астрономический ежегодник СССР на 1993 г)
            doubleArrayOf(2448988.5, 6.0, 42.0, 36.7508),
            doubleArrayOf(2449019.5, 8.0, 44.0, 49.9672),
            doubleArrayOf(2449047.5, 10.0, 35.0, 13.5175),
            doubleArrayOf(2449078.5, 12.0, 37.0, 26.7339),
            doubleArrayOf(2449108.5, 14.0, 35.0, 43.3949),
            doubleArrayOf(2449139.5, 16.0, 37.0, 56.6113),
            doubleArrayOf(2449169.5, 18.0, 36.0, 13.2723),
            doubleArrayOf(2449200.5, 20.0, 38.0, 26.4887),
            doubleArrayOf(2449231.5, 22.0, 40.0, 39.7051),
            doubleArrayOf(2449261.5, 0.0, 38.0, 56.4662),
            doubleArrayOf(2449292.5, 2.0, 41.0, 9.5825),
            doubleArrayOf(2449322.5, 4.0, 39.0, 26.2436),
            // Year 2017
            // Source: Astronomical Almanac for the Year 2017
            doubleArrayOf(2457844.5, 12.0, 38.0, 11.0891),
            doubleArrayOf(2457874.5, 14.0, 36.0, 27.7502),
            doubleArrayOf(2457905.5, 16.0, 38.0, 40.9666),
            doubleArrayOf(2457935.5, 18.0, 36.0, 57.6276),
            doubleArrayOf(2457966.5, 20.0, 39.0, 10.8440),
        )

        for (d in data) {
            val jd = d[0]
            val jde = jd - EspenakMeeus.compute(jd) / 86400.0
            val h = d[1]
            val m = d[2]
            val s = d[3]

            val est = h + m / 60.0 + s / 3600.0
            var ast = (SiderealTime.computeMean(jd, jde) / 15.0).value % 24.0
            if (ast < 0.0) ast += 24.0

            assertTrue(abs(abs(est) - abs(ast)) <= 0.0002)
        }
    }

    @Test
    fun greenwichApparentSiderealTime() {
        val data = arrayOf(
            // Year 2017
            // Source: Astronomical Almanac for the Year 2017
            doubleArrayOf(2457844.5, 12.0, 38.0, 10.5428),
            doubleArrayOf(2457874.5, 14.0, 36.0, 27.1459),
            doubleArrayOf(2457905.5, 16.0, 38.0, 40.3712),
            doubleArrayOf(2457935.5, 18.0, 36.0, 57.0663),
            doubleArrayOf(2457966.5, 20.0, 39.0, 10.2937),
        )

        for (d in data) {
            val jd = d[0]
            val jde = jd - EspenakMeeus.compute(jd) / 86400.0
            val h = d[1]
            val m = d[2]
            val s = d[3]

            val est = h + m / 60.0 + s / 3600.0
            var ast = (SiderealTime.computeApparent(jd, jde) / 15.0).value % 24.0
            if (ast < 0.0) ast += 24.0

            assertTrue(abs(abs(est) - abs(ast)) <= 0.0002)
        }
    }

    @Test
    fun siderealPeriod() {
        val data = mapOf(
            // According to WolframAlpha
            1.00000011 to 365.25636, // Earth
            0.38709893 to 87.96926,  // Mercury
            0.72333199 to 224.7008,  // Venus
        )

        for (distance in data.keys) {
            assertEquals(data[distance]!!, KeplerOrbit.computeSiderealPeriod(AU(distance), 1.0), 0.001)
        }
    }
}