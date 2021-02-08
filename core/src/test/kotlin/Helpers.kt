import br.tiagohm.astrum.core.math.Duad
import br.tiagohm.astrum.core.math.Mat4
import br.tiagohm.astrum.core.math.RaDec
import br.tiagohm.astrum.core.math.Triad
import org.junit.jupiter.api.Assertions.assertEquals

fun assertMat4Equals(expected: Mat4, actual: Mat4, delta: Double = 0.000001) {
    for (i in expected.indices) {
        assertEquals(expected[i], actual[i], delta)
    }
}

fun assertVec3Equals(expected: Triad, actual: Triad, delta: Double = 0.000001) {
    for (i in expected.indices) {
        assertEquals(expected[i], actual[i], delta)
    }
}

fun assertVec2Equals(expected: Duad, actual: Duad, delta: Double = 0.000001) {
    for (i in expected.indices) {
        assertEquals(expected[i], actual[i], delta)
    }
}

fun assertRaDecEquals(expected: RaDec, actual: RaDec, delta: Double = 0.000001) {
    assertEquals(expected.ra, actual.ra, delta)
    assertEquals(expected.dec, actual.dec, delta)
}
