import br.tiagohm.astrum.sky.core.coordinates.Coord
import br.tiagohm.astrum.sky.core.math.Duad
import br.tiagohm.astrum.sky.core.math.Mat4
import br.tiagohm.astrum.sky.core.math.Triad
import br.tiagohm.astrum.sky.core.units.angle.Angle
import br.tiagohm.astrum.sky.core.units.distance.Distance
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

fun assertEquals(x: Double, y: Double, actual: Coord, delta: Double, isDegrees: Boolean = false) {
    if (isDegrees) {
        assertEquals(x, actual.x.degrees.value, delta)
        assertEquals(y, actual.y.degrees.value, delta)
    } else {
        assertEquals(x, actual.x.value, delta)
        assertEquals(y, actual.y.value, delta)
    }
}

fun assertEquals(expected: Angle, actual: Angle, delta: Double) {
    assertEquals(expected, actual, delta)
}

fun assertEquals(expected: Distance, actual: Distance, delta: Double) {
    assertEquals(expected, actual, delta)
}

fun assertEquals(expected: Double, actual: Angle, delta: Double, isDegrees: Boolean = false) {
    if (isDegrees) {
        assertEquals(expected, actual.degrees.value, delta)
    } else {
        assertEquals(expected, actual.radians.value, delta)
    }
}

fun assertEquals(expected: Double, actual: Distance, delta: Double) {
    assertEquals(expected, actual.au.value, delta)
}

