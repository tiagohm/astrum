import br.tiagohm.astrum.core.Duad
import br.tiagohm.astrum.core.Mat4
import br.tiagohm.astrum.core.Triad
import org.junit.jupiter.api.Assertions.assertEquals

fun assertEquals(expected: Mat4, actual: Mat4, delta: Double) {
    for (i in expected.indices) {
        assertEquals(expected[i], actual[i], delta)
    }
}

fun assertEquals(expected: Triad, actual: Triad, delta: Double) {
    for (i in expected.indices) {
        assertEquals(expected[i], actual[i], delta)
    }
}

fun assertEquals(expected: Duad, actual: Duad, delta: Double) {
    for (i in expected.indices) {
        assertEquals(expected[i], actual[i], delta)
    }
}
