package br.tiagohm.astrum.sky.core.math

import kotlin.math.sqrt

@Suppress("NOTHING_TO_INLINE", "RESERVED_MEMBER_INSIDE_INLINE_CLASS", "EXPERIMENTAL_FEATURE_WARNING")
inline class Quad(val data: DoubleArray) : Iterable<Double> {

    constructor(
        a0: Double = 0.0,
        a1: Double = 0.0,
        a2: Double = 0.0,
        a3: Double = 0.0
    ) : this(doubleArrayOf(a0, a1, a2, a3))

    inline operator fun get(index: Int) = data[index]

    inline val indices: IntRange
        get() = data.indices

    override fun iterator(): Iterator<Double> = data.iterator()

    inline val lengthSquared: Double
        get() = this[0] * this[0] + this[1] * this[1] + this[2] * this[2] + this[3] * this[3]

    inline val length: Double
        get() = sqrt(lengthSquared)

    val normalized: Quad
        get() = (1.0 / length).let { Quad(this[0] * it, this[1] * it, this[2] * it, this[3] * it) }

    inline fun dot(a: Quad): Double {
        return this[0] * a[0] + this[1] * a[1] + this[2] * a[2] + this[3] * a[3]
    }

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as Quad

        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        return data.contentHashCode()
    }

    companion object {

        val ZERO = Quad()
    }
}