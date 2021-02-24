import br.tiagohm.astrum.sky.M_PI_180
import br.tiagohm.astrum.sky.atmosphere.Extinction
import br.tiagohm.astrum.sky.core.math.Triad
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.math.cos

class ExtinctionTest {

    @Test
    fun forward() {
        val a = Extinction()
        assertTrue(a.forward(Triad(1.0), 4.0) >= 4.0)

        val b = Extinction(0.25)
        assertEquals(2.25, b.forward(Triad(a2 = 1.0), 2.0), 0.0001)
    }

    @Test
    fun airmass() {
        assertEquals(1.0, Extinction.airmass(cos(0.0 * M_PI_180)), 0.05)
        assertEquals(1.02, Extinction.airmass(cos(10.0 * M_PI_180)), 0.05)
        assertEquals(1.06, Extinction.airmass(cos(20.0 * M_PI_180)), 0.05)
        assertEquals(1.15, Extinction.airmass(cos(30.0 * M_PI_180)), 0.05)
        assertEquals(1.30, Extinction.airmass(cos(40.0 * M_PI_180)), 0.05)
        assertEquals(1.55, Extinction.airmass(cos(50.0 * M_PI_180)), 0.05)
        assertEquals(2.0, Extinction.airmass(cos(60.0 * M_PI_180)), 0.05)
        assertEquals(2.90, Extinction.airmass(cos(70.0 * M_PI_180)), 0.05)
        assertEquals(5.60, Extinction.airmass(cos(80.0 * M_PI_180)), 0.05)
    }
}