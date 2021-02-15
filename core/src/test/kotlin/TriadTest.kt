import br.tiagohm.astrum.core.algorithms.math.Triad
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TriadTest {

    private val a = Triad(4.5, 1.2, -0.7)
    private val b = Triad(5.9, 0.04, 3.5)
    private val an = Triad(4.5, 1.2, -0.7).normalized
    private val bn = Triad(5.9, 0.04, 3.5).normalized

    @Test
    fun length() {
        assertEquals(4.709565, a.length, DELTA_6)
    }

    @Test
    fun lengthSquared() {
        assertEquals(22.180000, a.lengthSquared, DELTA_6)
    }

    @Test
    fun longitude() {
        assertEquals(0.260602, a.longitude, DELTA_6)
    }

    @Test
    fun latitude() {
        assertEquals(-0.149186, a.latitude, DELTA_6)
    }

    @Test
    fun angle() {
        assertEquals(0.726620, a.angle(b), DELTA_6)
        assertEquals(0.726620, b.angle(a), DELTA_6)
        assertEquals(0.726620, an.angleNormalized(bn), DELTA_6)
        assertEquals(0.726620, bn.angleNormalized(an), DELTA_6)
    }

    @Test
    fun dot() {
        assertEquals(24.148000, a.dot(b), DELTA_6)
    }

    @Test
    fun plus() {
        assertEquals(Triad(10.4, 1.24, 2.8), a + b, 0.01)
    }

    @Test
    fun minus() {
        assertEquals(Triad(-1.4, 1.16, -4.2), a - b, 0.01)
        assertEquals(Triad(1.4, -1.16, 4.2), b - a, 0.01)
    }

    @Test
    fun unaryMinus() {
        assertEquals(Triad(-4.5, -1.2, 0.7), -a, 0.01)
    }

    @Test
    fun times() {
        assertEquals(Triad(4.228, -19.88, -6.9), a * b, 0.01)
        assertEquals(Triad(13.5, 3.6, -2.1), a * 3, 0.01)
    }

    @Test
    fun div() {
        assertEquals(Triad(2.25, 0.6, -0.35), a / 2, 0.01)
    }

    @Test
    fun normalized() {
        assertEquals(Triad(0.9555023133800681, 0.2548006169013515, -0.14863369319245504), a.normalized, 1E-9)
    }

    companion object {

        private const val DELTA_6 = 0.000001
    }
}