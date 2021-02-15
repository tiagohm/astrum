import br.tiagohm.astrum.core.M_PI
import br.tiagohm.astrum.core.algorithms.math.Mat4
import br.tiagohm.astrum.core.algorithms.math.Triad
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Mat4Test {

    private val v = Triad(8.0, 5.0, -1.0)
    private val a = Mat4(5.0, 2.0, 6.0, 1.0, 0.0, 6.0, 2.0, 0.0, 3.0, 8.0, 1.0, 4.0, 1.0, 8.0, 5.0, 6.0)
    private val b = Mat4(7.0, 5.0, 8.0, 0.0, 1.0, 8.0, 2.0, 6.0, 9.0, 4.0, 3.0, 8.0, 5.0, 3.0, 7.0, 9.0)

    @Test
    fun times() {
        assertEquals(
            Mat4(
                96.0, 68.0, 69.0, 69.0, 24.0, 56.0, 18.0, 52.0,
                58.0, 95.0, 71.0, 92.0, 90.0, 107.0, 81.0, 142.0,
            ), b * a
        )

        assertEquals(
            Mat4(
                59.0, 108.0, 60.0, 39.0, 17.0, 114.0, 54.0, 45.0,
                62.0, 130.0, 105.0, 69.0, 55.0, 156.0, 88.0, 87.0,
            ), a * b
        )

        val m0 = a * v
        assertEquals(Triad(38.0, 46.0, 62.0), m0)

        val m1 = a.multiplyWithoutTranslation(v)
        assertEquals(Triad(37.0, 38.0, 57.0), m1)
    }

    @Test
    fun identity() {
        val i = Mat4.IDENTITY
        assertEquals(Mat4(1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0), i)
    }

    @Test
    fun translation() {
        val t = Mat4.translation(v)
        assertEquals(Mat4(1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 8.0, 5.0, -1.0, 1.0), t)
    }

    @Test
    fun rotation() {
        val r = Mat4.rotation(v, M_PI)
        assertEquals(
            Mat4(
                0.422222, 0.888889, -0.177778, 0.0, 0.888889,
                -0.444444, -0.111111, 0.0, -0.177778, -0.111111, -0.977778,
                0.0, 0.0, 0.0, 0.0, 1.0
            ), r, 0.01
        )
    }

    @Test
    fun xrotation() {
        val r = Mat4.xrotation(M_PI)
        assertEquals(
            Mat4(
                1.0, 0.0, 0.0, 0.0, 0.0, -1.0, 0.0, 0.0, 0.0,
                0.0, -1.0, 0.0, 0.0, 0.0, 0.0, 1.0
            ), r, 0.01
        )
    }

    @Test
    fun yrotation() {
        val r = Mat4.yrotation(M_PI)
        assertEquals(
            Mat4(
                -1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0,
                0.0, 0.0, -1.0, 0.0, 0.0, 0.0, 0.0, 1.0
            ), r, 0.01
        )
    }

    @Test
    fun zrotation() {
        val r = Mat4.zrotation(M_PI)
        assertEquals(
            Mat4(
                -1.0, 0.0, 0.0, 0.0, 0.0, -1.0, 0.0, 0.0,
                0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0
            ), r, 0.01
        )
    }

    @Test
    fun scaling() {
        val r = Mat4.scaling(v)
        assertEquals(
            Mat4(
                8.0, 0.0, 0.0, 0.0, 0.0, 5.0, 0.0, 0.0,
                0.0, 0.0, -1.0, 0.0, 0.0, 0.0, 0.0, 1.0
            ), r
        )
    }

    @Test
    fun transpose() {
        val n = Mat4(
            5.0, 0.0, 3.0, 1.0, 2.0, 6.0, 8.0, 8.0,
            6.0, 2.0, 1.0, 5.0, 1.0, 0.0, 4.0, 6.0
        )

        assertEquals(a.transpose(), n)
        assertEquals(a.transpose().transpose(), a)
    }

    @Test
    fun plus() {
        assertEquals(
            Mat4(
                12.0, 7.0, 14.0, 1.0, 1.0, 14.0, 4.0, 6.0,
                12.0, 12.0, 4.0, 12.0, 6.0, 11.0, 12.0, 15.0
            ), a + b
        )
    }

    @Test
    fun minus() {
        assertEquals(
            Mat4(
                -2.0, -3.0, -2.0, 1.0, -1.0, -2.0, 0.0, -6.0,
                -6.0, 4.0, -2.0, -4.0, -4.0, 5.0, -2.0, -3.0
            ), a - b
        )
    }

    @Test
    fun inverse() {
        val m = Mat4(4.0, 1.0, 2.0, 3.0, 1.0, 5.0, 7.0, 7.0, 8.0, 6.0, 9.0, 11.0, 12.0, 13.0, 14.0, 15.0)
        val mi = m.inverse()
        assertEquals(
            Mat4(
                1.25, 0.3333333333333333, -0.6666666666666666, 0.08333333333333333, -2.583333333333333,
                -1.1111111111111112, 1.222222222222222, 0.1388888888888889, 6.416666666666666, 2.888888888888889,
                -3.7777777777777777, 0.1388888888888889, -4.75, -2.0, 3.0, -0.25
            ), mi
        )

        assertEquals(m.transpose().inverse(), m.inverse().transpose())
    }
}