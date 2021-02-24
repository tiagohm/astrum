package br.tiagohm.astrum.sky.core.math

import br.tiagohm.astrum.sky.core.units.angle.Angle
import br.tiagohm.astrum.sky.core.units.angle.Radians
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.sqrt

@Suppress("NOTHING_TO_INLINE", "RESERVED_MEMBER_INSIDE_INLINE_CLASS", "EXPERIMENTAL_FEATURE_WARNING")
inline class Triad(val data: DoubleArray) : Iterable<Double> {

    constructor(
        a0: Double = 0.0,
        a1: Double = 0.0,
        a2: Double = 0.0
    ) : this(doubleArrayOf(a0, a1, a2))

    inline operator fun get(index: Int) = data[index]

    inline operator fun component1() = data[0]

    inline operator fun component2() = data[1]

    inline operator fun component3() = data[2]

    inline val indices: IntRange
        get() = data.indices

    override fun iterator(): Iterator<Double> = data.iterator()

    inline val lengthSquared: Double
        get() = this[0] * this[0] + this[1] * this[1] + this[2] * this[2]

    inline val length: Double
        get() = sqrt(lengthSquared)

    inline val latitude: Angle
        get() = Radians(asin(this[2] / length))

    inline val longitude: Angle
        get() = Radians(atan2(this[1], this[0]))

    val normalized: Triad
        get() = (1.0 / length).let { Triad(this[0] * it, this[1] * it, this[2] * it) }

    inline fun dot(a: Triad): Double {
        return this[0] * a[0] + this[1] * a[1] + this[2] * a[2]
    }

    /**
     * Angle between two vectors.
     */
    fun angle(a: Triad): Radians {
        val cosAngle = dot(a) / sqrt(lengthSquared * a.lengthSquared)
        return if (cosAngle >= 1.0) Radians.ZERO
        else if (cosAngle <= -1) Radians.PI
        else Radians(acos(cosAngle))
    }

    /**
     * Angle in radian between two normalized vectors
     */
    fun angleNormalized(a: Triad): Radians {
        val cosAngle = dot(a)
        return if (cosAngle >= 1.0) Radians.ZERO
        else if (cosAngle <= -1) Radians.PI
        else Radians(acos(cosAngle))
    }

    inline operator fun plus(a: Triad): Triad {
        return Triad(this[0] + a[0], this[1] + a[1], this[2] + a[2])
    }

    inline operator fun minus(a: Triad): Triad {
        return Triad(this[0] - a[0], this[1] - a[1], this[2] - a[2])
    }

    inline operator fun unaryMinus(): Triad {
        return Triad(-this[0], -this[1], -this[2])
    }

    /**
     * Cross product
     */
    inline operator fun times(a: Triad): Triad {
        return Triad(
            this[1] * a[2] - this[2] * a[1],
            this[2] * a[0] - this[0] * a[2],
            this[0] * a[1] - this[1] * a[0]
        )
    }

    inline operator fun times(a: Double): Triad {
        return Triad(this[0] * a, this[1] * a, this[2] * a)
    }

    inline operator fun times(a: Int): Triad {
        return Triad(this[0] * a, this[1] * a, this[2] * a)
    }

    inline operator fun times(a: Long): Triad {
        return Triad(this[0] * a, this[1] * a, this[2] * a)
    }

    inline operator fun div(a: Double): Triad {
        return Triad(this[0] / a, this[1] / a, this[2] / a)
    }

    inline operator fun div(a: Int): Triad {
        return Triad(this[0] / a, this[1] / a, this[2] / a)
    }

    inline operator fun div(a: Long): Triad {
        return Triad(this[0] / a, this[1] / a, this[2] / a)
    }

    fun transform(a: Mat4): Triad {
        val a0 = a[0] * this[0] + a[4] * this[1] + a[8] * this[2] + a[12]
        val a1 = a[1] * this[0] + a[5] * this[1] + a[9] * this[2] + a[13]
        val a2 = a[2] * this[0] + a[6] * this[1] + a[10] * this[2] + a[14]
        return Triad(a0, a1, a2)
    }

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as Triad

        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        return data.contentHashCode()
    }

    companion object {

        val ZERO = Triad()
    }
}