import br.tiagohm.astrum.common.LIGHT_YEAR
import br.tiagohm.astrum.common.units.angle.Degrees
import br.tiagohm.astrum.common.units.angle.Radians
import br.tiagohm.astrum.common.units.distance.AU
import br.tiagohm.astrum.common.units.distance.Kilometer
import br.tiagohm.astrum.common.units.distance.LightYear
import br.tiagohm.astrum.common.units.distance.Meter
import br.tiagohm.astrum.common.units.pressure.Atmosphere
import br.tiagohm.astrum.common.units.pressure.Millibar
import br.tiagohm.astrum.common.units.pressure.Pascal
import br.tiagohm.astrum.common.units.temperature.Celsius
import br.tiagohm.astrum.common.units.temperature.Fahrenheit
import br.tiagohm.astrum.common.units.temperature.Kelvin
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UnitTest {

    @Test
    fun pressure() {
        val pa = Pascal.ONE
        val atm = Atmosphere.ONE
        val mbar = Millibar.ONE

        assertEquals(0.01, pa.millibar.value, 0.001)
        assertEquals(9.869E-6, pa.atmosphere.value, 0.0000001)

        assertEquals(1013.25, atm.millibar.value, 0.001)
        assertEquals(101325.0, atm.pascal.value, 0.001)

        assertEquals(0.0009869, mbar.atmosphere.value, 0.000001)
        assertEquals(100.0, mbar.pascal.value, 0.001)
    }

    @Test
    fun temperature() {
        val c = Celsius(35.0)
        val f = Fahrenheit(67.0)
        val k = Kelvin(100.6)

        assertEquals(95.0, c.fahrenheit.value, 0.001)
        assertEquals(308.15, c.kelvin.value, 0.001)

        assertEquals(19.444, f.celsius.value, 0.001)
        assertEquals(292.594, f.kelvin.value, 0.001)

        assertEquals(-172.549, k.celsius.value, 0.001)
        assertEquals(-278.59, k.fahrenheit.value, 0.001)
    }

    @Test
    fun angle() {
        val d = Degrees(178.6)
        val r = Radians.PI

        assertEquals(3.117158, d.radians.value, 0.000001)
        assertEquals(180.0, r.degrees.value, 0.0000001)
    }

    @Test
    fun distance() {
        val km = Kilometer(1.0)
        val m = Meter(1000.0)
        val au = AU(1.0)
        val ly = LightYear(1.0)

        assertEquals(1000.0, km.meter.value, 0.000001)
        assertEquals(1.0, m.kilometer.value, 0.0000001)

        assertEquals(149597870.6996262, au.kilometer.value, 0.001)
        assertEquals(149597870699.6262, au.meter.value, 0.001)
        assertEquals(1.5812507409781147E-5, au.lightYear.value, 0.000001)
        // assertEquals(1.5812507409781147E-5, au.parsec.value, 0.000001)

        assertEquals(LIGHT_YEAR / 1000.0, ly.kilometer.value, 0.001)
        assertEquals(LIGHT_YEAR, ly.meter.value, 0.001)
        assertEquals(63241.07708442430066362006, ly.au.value, 0.000001)
        // assertEquals(1.5812507409781147E-5, ly.parsec.value, 0.000001)

        // TODO: Parsec
    }
}