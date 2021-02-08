import br.tiagohm.astrum.core.math.Triad
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
        assertVec3Equals(Triad(10.4, 1.24, 2.8), a + b)
    }

    @Test
    fun minus() {
        assertVec3Equals(Triad(-1.4, 1.16, -4.2), a - b)
        assertVec3Equals(Triad(1.4, -1.16, 4.2), b - a)
    }

    @Test
    fun unaryMinus() {
        assertVec3Equals(Triad(-4.5, -1.2, 0.7), -a)
    }

    @Test
    fun times() {
        assertVec3Equals(Triad(4.228, -19.88, -6.9), a * b)
        assertVec3Equals(Triad(13.5, 3.6, -2.1), a * 3)
    }

    @Test
    fun div() {
        assertVec3Equals(Triad(2.25, 0.6, -0.35), a / 2)
    }

    @Test
    fun normalized() {
        assertVec3Equals(Triad(0.9555023133800681, 0.2548006169013515, -0.14863369319245504), a.normalized)
    }

    companion object {

        private const val DELTA_6 = 0.000001
    }
}