import br.tiagohm.astrum.core.algorithms.math.Triad
import br.tiagohm.astrum.core.sky.atmosphere.Extinction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ExtinctionTest {

    @Test
    fun forward() {
        val a = Extinction()
        assertTrue(a.forward(Triad(1.0), 4.0) >= 4.0)

        val b = Extinction(0.25)
        assertEquals(2.25, b.forward(Triad(a2 = 1.0), 2.0), 0.0001)
    }
}