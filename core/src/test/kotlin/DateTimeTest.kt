import br.tiagohm.astrum.core.algorithms.time.DateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DateTimeTest {

    @Test
    fun dayInYear() {
        assertEquals(1, DateTime.dayInYear(2000, 1, 1))
        assertEquals(1, DateTime.dayInYear(2019, 1, 1))
        assertEquals(31, DateTime.dayInYear(2019, 1, 31))
        assertEquals(59, DateTime.dayInYear(2019, 2, 28))
        assertEquals(69, DateTime.dayInYear(2019, 3, 10))
        assertEquals(70, DateTime.dayInYear(2020, 3, 10))
    }

    @Test
    fun yearAsFraction() {
        assertEquals(2000.00274, DateTime.yearAsFraction(2000, 1, 1), DELTA_4)
        assertEquals(2019.00274, DateTime.yearAsFraction(2019, 1, 1), DELTA_4)
        assertEquals(2019.08493, DateTime.yearAsFraction(2019, 1, 31), DELTA_4)
        assertEquals(2019.16164, DateTime.yearAsFraction(2019, 2, 28), DELTA_4)
        assertEquals(2019.18904, DateTime.yearAsFraction(2019, 3, 10), DELTA_4)
        assertEquals(2020.19126, DateTime.yearAsFraction(2020, 3, 10), DELTA_4)
    }

    @Test
    fun isLeapYear() {
        val data = mapOf(
            1500 to true,
            1600 to true,
            2000 to true,
            2100 to false,
            2200 to false,
            2300 to false,
            2016 to true,
            2017 to false,
            2018 to false,
            2019 to false,
            2020 to true,
            1852 to true,
            1851 to false,
        )

        for (year in data.keys) {
            assertEquals(data[year]!!, DateTime.isLeapYear(year))
        }
    }

    @Test
    fun numberOfDaysInMonth() {
        val data = mapOf(
            (2019 to 1) to 31,
            (2019 to 2) to 28,
            (2019 to 3) to 31,
            (2019 to 4) to 30,
            (2019 to 5) to 31,
            (2019 to 6) to 30,
            (2019 to 7) to 31,
            (2019 to 8) to 31,
            (2019 to 9) to 30,
            (2019 to 10) to 31,
            (2019 to 11) to 30,
            (2019 to 12) to 31,
            (2020 to 1) to 31,
            (2020 to 2) to 29,
            (2020 to 3) to 31,
            (2020 to 4) to 30,
            (2020 to 5) to 31,
            (2020 to 6) to 30,
            (2020 to 7) to 31,
            (2020 to 8) to 31,
            (2020 to 9) to 30,
            (2020 to 10) to 31,
            (2020 to 11) to 30,
            (2020 to 12) to 31,
            (2020 to 0) to 31,
            (2020 to 13) to 31,
            (1852 to 1) to 31,
            (1852 to 2) to 29,
            (1851 to 2) to 28,
        )

        for (date in data.keys) {
            val n = DateTime.numberOfDaysInMonthInYear(date.first, date.second)
            assertEquals(data[date]!!, n)
        }
    }

    @Test
    fun testJDFromDate() {
        val data = mapOf(
            2500000.0 to DateTime(2132, 8, 31, 12),
            2454466.5 to DateTime(2008, 1, 1),
            2454466.0 to DateTime(2007, 12, 31, 12),
            2451545.0 to DateTime(2000, 1, 1, 12),
            2442413.5 to DateTime(1975, 1, 1),
            2433282.5 to DateTime(1950, 1, 1),
            2415020.5 to DateTime(1900, 1, 1),
            2405889.5 to DateTime(1875, 1, 1),
            2400000.0 to DateTime(1858, 11, 16, 12),
            2396758.5 to DateTime(1850, 1, 1),
            2385800.5 to DateTime(1820, 1, 1),
            2378496.5 to DateTime(1800, 1, 1),
            2110516.0 to DateTime(1066, 4, 12, 12),
            1720693.0 to DateTime(-1, 1, 1, 12),
            366.0 to DateTime(-4711, 1, 1, 12),
            -1.0 to DateTime(-4713, 12, 31, 12),
        )

        for (jd in data.keys) {
            val dt = DateTime.fromJulianDay(jd)
            assertEquals(data[jd]!!, dt)
        }
    }

    @Test
    fun utcOffset() {
        val dt0 = DateTime(2021, 2, 5, 12, 0, 0, utcOffset = 0.0) // 2459251.000000
        assertEquals(2459251.0, dt0.jd)

        val dt1 = DateTime(2021, 2, 5, 9, 0, 0, utcOffset = -3.0)
        assertEquals(2459251.0, dt1.jd)

        val dt2 = DateTime(2021, 2, 5, 15, 0, 0, utcOffset = 3.0)
        assertEquals(2459251.0, dt2.jd)

        val dt3 = DateTime(2021, 2, 5, 0, 0, 0, utcOffset = -12.0)
        assertEquals(2459251.0, dt3.jd)

        val dt4 = DateTime(2021, 2, 6, 0, 0, 0, utcOffset = 12.0)
        assertEquals(2459251.0, dt4.jd)

        val dt5 = DateTime(2021, 2, 5, 11, 30, 0, utcOffset = -0.5)
        assertEquals(2459251.0, dt5.jd)
    }

    companion object {

        private const val DELTA_4 = 0.0001
    }
}