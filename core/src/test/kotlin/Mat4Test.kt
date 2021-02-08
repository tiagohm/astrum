import br.tiagohm.astrum.core.Consts
import br.tiagohm.astrum.core.math.Mat4
import br.tiagohm.astrum.core.math.Triad
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
        assertVec3Equals(Triad(38.0, 46.0, 62.0), m0)

        val m1 = a.multiplyWithoutTranslation(v)
        assertVec3Equals(Triad(37.0, 38.0, 57.0), m1)
    }

    @Test
    fun identity() {
        val i = Mat4.IDENTITY
        assertEquals(Mat4(1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0), i)
    }

    @Test
    fun translation() {
        val t = Mat4.translation(v)
        assertMat4Equals(Mat4(1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 8.0, 5.0, -1.0, 1.0), t)
    }

    @Test
    fun rotation() {
        val r = Mat4.rotation(v, Consts.M_PI)
        assertMat4Equals(
            Mat4(
                0.422222, 0.888889, -0.177778, 0.0, 0.888889,
                -0.444444, -0.111111, 0.0, -0.177778, -0.111111, -0.977778,
                0.0, 0.0, 0.0, 0.0, 1.0
            ), r
        )
    }

    @Test
    fun xrotation() {
        val r = Mat4.xrotation(Consts.M_PI)
        assertMat4Equals(
            Mat4(
                1.0, 0.0, 0.0, 0.0, 0.0, -1.0, 0.0, 0.0, 0.0,
                0.0, -1.0, 0.0, 0.0, 0.0, 0.0, 1.0
            ), r
        )
    }

    @Test
    fun yrotation() {
        val r = Mat4.yrotation(Consts.M_PI)
        assertMat4Equals(
            Mat4(
                -1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0,
                0.0, 0.0, -1.0, 0.0, 0.0, 0.0, 0.0, 1.0
            ), r
        )
    }

    @Test
    fun zrotation() {
        val r = Mat4.zrotation(Consts.M_PI)
        assertMat4Equals(
            Mat4(
                -1.0, 0.0, 0.0, 0.0, 0.0, -1.0, 0.0, 0.0,
                0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0
            ), r
        )
    }

    @Test
    fun scaling() {
        val r = Mat4.scaling(v)
        assertMat4Equals(
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

        assertMat4Equals(a.transpose(), n)
        assertMat4Equals(a.transpose().transpose(), a)
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
}