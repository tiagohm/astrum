import br.tiagohm.astrum.sky.algorithms.math.Duad
import br.tiagohm.astrum.sky.algorithms.math.Mat4
import br.tiagohm.astrum.sky.algorithms.math.Triad
import br.tiagohm.astrum.sky.core.coordinates.Coordinate
import br.tiagohm.astrum.sky.core.units.Degrees
import br.tiagohm.astrum.sky.core.units.Radians
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

fun assertEquals(x: Double, y: Double, actual: Coordinate, delta: Double, isDegrees: Boolean = false) {
    if (isDegrees) {
        assertEquals(x, actual.x.degrees.value, delta)
        assertEquals(y, actual.y.degrees.value, delta)
    } else {
        assertEquals(x, actual.x.value, delta)
        assertEquals(y, actual.y.value, delta)
    }
}

fun assertEquals(expected: Radians, actual: Radians, delta: Double) {
    assertEquals(expected, actual, delta)
}

fun assertEquals(expected: Double, actual: Radians, delta: Double) {
    assertEquals(expected, actual.value, delta)
}

fun assertEquals(expected: Degrees, actual: Degrees, delta: Double) {
    assertEquals(expected, actual, delta)
}

fun assertEquals(expected: Double, actual: Degrees, delta: Double) {
    assertEquals(expected, actual.value, delta)
}
