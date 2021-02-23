import br.tiagohm.astrum.sky.algorithms.time.DateTime
import br.tiagohm.astrum.sky.algorithms.time.EspenakMeeus
import br.tiagohm.astrum.sky.algorithms.time.MeeusSimons
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.math.abs

@ExperimentalUnsignedTypes
class TimeCorrectionTest {

    @Test
    fun deltaTByMeeusSimons() {
        val data = mapOf(
            1619 to 0.00,
            1974 to 44.49,
            1975 to 45.48,
            1976 to 46.46,
            1977 to 47.52,
            1978 to 48.53,
            1979 to 49.59,
            1980 to 50.54,
            1981 to 51.38,
            1982 to 52.17,
            1983 to 52.96,
            1984 to 53.79,
            1985 to 54.34,
            1986 to 54.87,
            1987 to 55.32,
            1988 to 55.82,
            1989 to 56.30,
            1990 to 56.86,
            1991 to 57.57,
            1992 to 58.31,
            1993 to 59.12,
            1994 to 59.99,
            1995 to 60.79,
            1996 to 61.63,
            1997 to 62.30,
            1999 to 64.00,
            2001 to 0.00,
        )

        for (year in data.keys) {
            val jd = DateTime.computeJDFromDate(year, 1, 1, 0, 0, 0, 0, 0.0)
            val deltaT = MeeusSimons.compute(jd)
            assertEquals(data[year]!!, deltaT, 1.0)
        }
    }

    @Test
    fun deltaTByEspenakMeeus() {
        val data = mapOf(
            -500 to (17190.0 to 430.0),
            -400 to (15530.0 to 390.0),
            -300 to (14080.0 to 360.0),
            -200 to (12790.0 to 330.0),
            -100 to (11640.0 to 290.0),
            0 to (10580.0 to 260.0),
            100 to (9600.0 to 240.0),
            200 to (8640.0 to 210.0),
            300 to (7680.0 to 180.0),
            400 to (6700.0 to 160.0),
            500 to (5710.0 to 140.0),
            600 to (4740.0 to 120.0),
            700 to (3810.0 to 100.0),
            800 to (2960.0 to 80.0),
            900 to (2200.0 to 70.0),
            1000 to (1570.0 to 55.0),
            1100 to (1090.0 to 40.0),
            1200 to (740.0 to 30.0),
            1300 to (490.0 to 20.0),
            1400 to (320.0 to 20.0),
            1500 to (200.0 to 20.0),
            1600 to (120.0 to 20.0),
            1700 to (9.0 to 5.0),
            1750 to (13.0 to 2.0),
            1800 to (14.0 to 1.0),
            1850 to (7.0 to 1.0),
            1870 to (-0.1 to 1.0), // https://eclipse.gsfc.nasa.gov/SEsearch/SEsearchmap.php?Ecl=18701222
            1900 to (-3.0 to 1.0),
            1910 to (10.8 to 1.0), // https://eclipse.gsfc.nasa.gov/SEplot/SEplot1901/SE1910May09T.GIF
            1925 to (23.6 to 0.5), // https://eclipse.gsfc.nasa.gov/SEplot/SEplot1901/SE1925Jan24T.GIF
            1945 to (26.8 to 0.5), // https://eclipse.gsfc.nasa.gov/SEplot/SEplot1901/SE1945Jan14A.GIF
            1950 to (29.0 to 0.1),
            1955 to (31.1 to 0.1),
            1960 to (33.2 to 0.1),
            1965 to (35.7 to 0.1),
            1970 to (40.2 to 0.1),
            1975 to (45.5 to 0.1),
            1980 to (50.5 to 0.1),
            1985 to (54.3 to 0.1),
            1990 to (56.9 to 0.1),
            1995 to (60.8 to 0.1),
            2000 to (63.8 to 0.1),
            2005 to (64.7 to 0.1),
            2010 to (66.6 to 1.0), // https://eclipse.gsfc.nasa.gov/SEplot/SEplot2001/SE2010Jan15A.GIF
            2015 to (67.6 to 2.0), // https://eclipse.gsfc.nasa.gov/SEplot/SEplot2001/SE2015Mar20T.GIF
            2020 to (77.2 to 7.0), // https://eclipse.gsfc.nasa.gov/SEplot/SEplot2001/SE2020Jun21A.GIF
            2030 to (87.9 to 12.0), // https://eclipse.gsfc.nasa.gov/SEplot/SEplot2001/SE2030Jun01A.GIF
            2050 to (111.6 to 20.0), // https://eclipse.gsfc.nasa.gov/SEplot/SEplot2001/SE2050May20H.GIF
            2060 to (124.6 to 15.0), // https://eclipse.gsfc.nasa.gov/SEplot/SEplot2051/SE2060Apr30T.GIF
            2070 to (138.5 to 15.0), // https://eclipse.gsfc.nasa.gov/SEplot/SEplot2051/SE2070Apr11T.GIF
            2090 to (170.3 to 15.0), // https://eclipse.gsfc.nasa.gov/SEplot/SEplot2051/SE2090Sep23T.GIF
            2100 to (187.3 to 20.0), // https://eclipse.gsfc.nasa.gov/SEplot/SEplot2051/SE2100Sep04T.GIF
        )

        for (year in data.keys) {
            val jd = DateTime.computeJDFromDate(year, 1, 1, 0, 0, 0, 0, 0.0)
            val deltaT = EspenakMeeus.compute(jd)
            val result = data[year]!!.first
            val error = data[year]!!.second
            assertTrue(abs(abs(result) - abs(deltaT)) <= error)
        }
    }
}