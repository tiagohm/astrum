package br.tiagohm.astrum.sky.core.math

import br.tiagohm.astrum.sky.core.units.Radians
import br.tiagohm.astrum.sky.core.units.cos
import br.tiagohm.astrum.sky.core.units.sin

@Suppress("NOTHING_TO_INLINE", "RESERVED_MEMBER_INSIDE_INLINE_CLASS", "EXPERIMENTAL_FEATURE_WARNING")
inline class Mat4(val data: DoubleArray) : Iterable<Double> {

    constructor(
        a0: Double = 0.0,
        a1: Double = 0.0,
        a2: Double = 0.0,
        a3: Double = 0.0,
        b0: Double = 0.0,
        b1: Double = 0.0,
        b2: Double = 0.0,
        b3: Double = 0.0,
        c0: Double = 0.0,
        c1: Double = 0.0,
        c2: Double = 0.0,
        c3: Double = 0.0,
        d0: Double = 0.0,
        d1: Double = 0.0,
        d2: Double = 0.0,
        d3: Double = 0.0,
    ) : this(doubleArrayOf(a0, a1, a2, a3, b0, b1, b2, b3, c0, c1, c2, c3, d0, d1, d2, d3))

    inline operator fun get(index: Int) = data[index]

    inline val indices: IntRange
        get() = data.indices

    override fun iterator(): Iterator<Double> = data.iterator()

    fun multiplyWithoutTranslation(a: Triad): Triad {
        return Triad(
            this[0] * a[0] + this[4] * a[1] + this[8] * a[2],
            this[1] * a[0] + this[5] * a[1] + this[9] * a[2],
            this[2] * a[0] + this[6] * a[1] + this[10] * a[2],
        )
    }

    /**
     * Multiply column vector by a 4x4 matrix in homogeneous coordinate (it uses a[3]=1)
     */
    inline operator fun times(a: Triad): Triad {
        return Triad(
            this[0] * a[0] + this[4] * a[1] + this[8] * a[2] + this[12],
            this[1] * a[0] + this[5] * a[1] + this[9] * a[2] + this[13],
            this[2] * a[0] + this[6] * a[1] + this[10] * a[2] + this[14],
        )
    }

    operator fun times(a: Mat4): Mat4 {
        fun multiply(r: Int, c: Int): Double {
            return (this[r] * a[c] + this[r + 4] * a[c + 1] + this[r + 8] * a[c + 2] + this[r + 12] * a[c + 3])
        }

        return Mat4(
            multiply(0, 0), multiply(1, 0), multiply(2, 0), multiply(3, 0),
            multiply(0, 4), multiply(1, 4), multiply(2, 4), multiply(3, 4),
            multiply(0, 8), multiply(1, 8), multiply(2, 8), multiply(3, 8),
            multiply(0, 12), multiply(1, 12), multiply(2, 12), multiply(3, 12),
        )
    }

    inline operator fun plus(a: Mat4): Mat4 {
        return Mat4(
            this[0] + a[0], this[1] + a[1], this[2] + a[2], this[3] + a[3],
            this[4] + a[4], this[5] + a[5], this[6] + a[6], this[7] + a[7],
            this[8] + a[8], this[9] + a[9], this[10] + a[10], this[11] + a[11],
            this[12] + a[12], this[13] + a[13], this[14] + a[14], this[15] + a[15]
        )
    }

    inline operator fun minus(a: Mat4): Mat4 {
        return Mat4(
            this[0] - a[0], this[1] - a[1], this[2] - a[2], this[3] - a[3],
            this[4] - a[4], this[5] - a[5], this[6] - a[6], this[7] - a[7],
            this[8] - a[8], this[9] - a[9], this[10] - a[10], this[11] - a[11],
            this[12] - a[12], this[13] - a[13], this[14] - a[14], this[15] - a[15]
        )
    }

    inline fun transpose(): Mat4 {
        return Mat4(
            this[0], this[4], this[8], this[12],
            this[1], this[5], this[9], this[13],
            this[2], this[6], this[10], this[14],
            this[3], this[7], this[11], this[15]
        )
    }

    fun inverse(): Mat4 {
        val inv = DoubleArray(16)

        inv[0] = this[5] * this[10] * this[15] -
                this[5] * this[11] * this[14] -
                this[9] * this[6] * this[15] +
                this[9] * this[7] * this[14] +
                this[13] * this[6] * this[11] -
                this[13] * this[7] * this[10]

        inv[4] = -this[4] * this[10] * this[15] +
                this[4] * this[11] * this[14] +
                this[8] * this[6] * this[15] -
                this[8] * this[7] * this[14] -
                this[12] * this[6] * this[11] +
                this[12] * this[7] * this[10]

        inv[8] = this[4] * this[9] * this[15] -
                this[4] * this[11] * this[13] -
                this[8] * this[5] * this[15] +
                this[8] * this[7] * this[13] +
                this[12] * this[5] * this[11] -
                this[12] * this[7] * this[9]

        inv[12] = -this[4] * this[9] * this[14] +
                this[4] * this[10] * this[13] +
                this[8] * this[5] * this[14] -
                this[8] * this[6] * this[13] -
                this[12] * this[5] * this[10] +
                this[12] * this[6] * this[9]

        inv[1] = -this[1] * this[10] * this[15] +
                this[1] * this[11] * this[14] +
                this[9] * this[2] * this[15] -
                this[9] * this[3] * this[14] -
                this[13] * this[2] * this[11] +
                this[13] * this[3] * this[10]

        inv[5] = this[0] * this[10] * this[15] -
                this[0] * this[11] * this[14] -
                this[8] * this[2] * this[15] +
                this[8] * this[3] * this[14] +
                this[12] * this[2] * this[11] -
                this[12] * this[3] * this[10]

        inv[9] = -this[0] * this[9] * this[15] +
                this[0] * this[11] * this[13] +
                this[8] * this[1] * this[15] -
                this[8] * this[3] * this[13] -
                this[12] * this[1] * this[11] +
                this[12] * this[3] * this[9]

        inv[13] = this[0] * this[9] * this[14] -
                this[0] * this[10] * this[13] -
                this[8] * this[1] * this[14] +
                this[8] * this[2] * this[13] +
                this[12] * this[1] * this[10] -
                this[12] * this[2] * this[9]

        inv[2] = this[1] * this[6] * this[15] -
                this[1] * this[7] * this[14] -
                this[5] * this[2] * this[15] +
                this[5] * this[3] * this[14] +
                this[13] * this[2] * this[7] -
                this[13] * this[3] * this[6]

        inv[6] = -this[0] * this[6] * this[15] +
                this[0] * this[7] * this[14] +
                this[4] * this[2] * this[15] -
                this[4] * this[3] * this[14] -
                this[12] * this[2] * this[7] +
                this[12] * this[3] * this[6]

        inv[10] = this[0] * this[5] * this[15] -
                this[0] * this[7] * this[13] -
                this[4] * this[1] * this[15] +
                this[4] * this[3] * this[13] +
                this[12] * this[1] * this[7] -
                this[12] * this[3] * this[5]

        inv[14] = -this[0] * this[5] * this[14] +
                this[0] * this[6] * this[13] +
                this[4] * this[1] * this[14] -
                this[4] * this[2] * this[13] -
                this[12] * this[1] * this[6] +
                this[12] * this[2] * this[5]

        inv[3] = -this[1] * this[6] * this[11] +
                this[1] * this[7] * this[10] +
                this[5] * this[2] * this[11] -
                this[5] * this[3] * this[10] -
                this[9] * this[2] * this[7] +
                this[9] * this[3] * this[6]

        inv[7] = this[0] * this[6] * this[11] -
                this[0] * this[7] * this[10] -
                this[4] * this[2] * this[11] +
                this[4] * this[3] * this[10] +
                this[8] * this[2] * this[7] -
                this[8] * this[3] * this[6]

        inv[11] = -this[0] * this[5] * this[11] +
                this[0] * this[7] * this[9] +
                this[4] * this[1] * this[11] -
                this[4] * this[3] * this[9] -
                this[8] * this[1] * this[7] +
                this[8] * this[3] * this[5]

        inv[15] = this[0] * this[5] * this[10] -
                this[0] * this[6] * this[9] -
                this[4] * this[1] * this[10] +
                this[4] * this[2] * this[9] +
                this[8] * this[1] * this[6] -
                this[8] * this[2] * this[5]

        val det = this[0] * inv[0] + this[1] * inv[4] + this[2] * inv[8] + this[3] * inv[12]

        // O determinante da matriz não pode ser 0.
        return if (det == 0.0) {
            EMPTY
        } else {
            val d = 1.0 / det
            for (i in inv.indices) inv[i] = inv[i] * d
            return Mat4(inv)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as Mat4

        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        return data.contentHashCode()
    }

    companion object {

        val EMPTY = Mat4()

        fun rotation(a: Triad, angle: Radians): Mat4 {
            val an = a.normalized
            val c = cos(angle)
            val s = sin(angle)
            val d = 1.0 - c

            return Mat4(
                an[0] * an[0] * d + c, an[1] * an[0] * d + an[2] * s, an[0] * an[2] * d - an[1] * s, 0.0,
                an[0] * an[1] * d - an[2] * s, an[1] * an[1] * d + c, an[1] * an[2] * d + an[0] * s, 0.0,
                an[0] * an[2] * d + an[1] * s, an[1] * an[2] * d - an[0] * s, an[2] * an[2] * d + c, 0.0,
                0.0, 0.0, 0.0, 1.0
            )

        }

        fun xrotation(angle: Radians): Mat4 {
            val c = cos(angle)
            val s = sin(angle)
            return Mat4(a0 = 1.0, b1 = c, b2 = s, c1 = -s, c2 = c, d3 = 1.0)
        }

        fun yrotation(angle: Radians): Mat4 {
            val c = cos(angle)
            val s = sin(angle)
            return Mat4(a0 = c, a2 = -s, b1 = 1.0, c0 = s, c2 = c, d3 = 1.0)
        }

        fun zrotation(angle: Radians): Mat4 {
            val c = cos(angle)
            val s = sin(angle)
            return Mat4(a0 = c, a1 = s, b0 = -s, b1 = c, c2 = 1.0, d3 = 1.0)
        }

        fun translation(a: Triad): Mat4 {
            return Mat4(a0 = 1.0, b1 = 1.0, c2 = 1.0, d0 = a[0], d1 = a[1], d2 = a[2], d3 = 1.0)
        }

        fun scaling(a: Triad): Mat4 {
            return Mat4(a0 = a[0], b1 = a[1], c2 = a[2], d3 = 1.0)
        }

        fun scaling(a: Double): Mat4 {
            return Mat4(a0 = a, b1 = a, c2 = a, d3 = 1.0)
        }

        val IDENTITY = Mat4(a0 = 1.0, b1 = 1.0, c2 = 1.0, d3 = 1.0)
    }
}
