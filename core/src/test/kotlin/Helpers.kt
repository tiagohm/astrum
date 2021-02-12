import br.tiagohm.astrum.core.Duad
import br.tiagohm.astrum.core.Mat4
import br.tiagohm.astrum.core.Triad
import org.junit.jupiter.api.Assertions.assertEquals

fun assertMat4Equals(expected: Mat4, actual: Mat4, delta: Double = 0.000001) {
    for (i in expected.indices) {
        assertEquals(expected[i], actual[i], delta)
    }
}

fun assertTriadEquals(expected: Triad, actual: Triad, delta: Double = 0.000001) {
    for (i in expected.indices) {
        assertEquals(expected[i], actual[i], delta)
    }
}

fun assertDuadEquals(expected: Duad, actual: Duad, delta: Double = 0.000001) {
    for (i in expected.indices) {
        assertEquals(expected[i], actual[i], delta)
    }
}
