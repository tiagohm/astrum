import br.tiagohm.astrum.sky.algorithms.math.Duad
import br.tiagohm.astrum.sky.algorithms.math.Mat4
import br.tiagohm.astrum.sky.algorithms.math.Triad
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
