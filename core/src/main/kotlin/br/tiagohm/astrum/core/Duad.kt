package br.tiagohm.astrum.core

import kotlin.math.sqrt

@Suppress("NOTHING_TO_INLINE", "RESERVED_MEMBER_INSIDE_INLINE_CLASS")
inline class Duad(val data: DoubleArray) : Iterable<Double> {

    constructor(
        a0: Double = 0.0,
        a1: Double = 0.0,
    ) : this(doubleArrayOf(a0, a1))

    inline operator fun get(index: Int) = data[index]

    inline operator fun component1() = data[0]

    inline operator fun component2() = data[1]

    inline val indices: IntRange
        get() = data.indices

    override fun iterator(): Iterator<Double> = data.iterator()

    inline val lengthSquared: Double
        get() = this[0] * this[0] + this[1] * this[1]

    inline val length: Double
        get() = sqrt(lengthSquared)

    inline val normalized: Duad
        get() = (1.0 / length).let { Duad(this[0] * it, this[1] * it) }

    inline fun dot(a: Duad): Double {
        return this[0] * a[0] + this[1] * a[1]
    }

    inline operator fun plus(a: Duad): Duad {
        return Duad(this[0] + a[0], this[1] + a[1])
    }

    inline operator fun minus(a: Duad): Duad {
        return Duad(this[0] - a[0], this[1] - a[1])
    }

    inline operator fun unaryMinus(): Duad {
        return Duad(-this[0], -this[1])
    }

    inline operator fun times(a: Double): Duad {
        return Duad(this[0] * a, this[1] * a)
    }

    inline operator fun times(a: Int): Duad {
        return Duad(this[0] * a, this[1] * a)
    }

    inline operator fun times(a: Long): Duad {
        return Duad(this[0] * a, this[1] * a)
    }

    inline operator fun div(a: Double): Duad {
        return Duad(this[0] / a, this[1] / a)
    }

    inline operator fun div(a: Int): Duad {
        return Duad(this[0] / a, this[1] / a)
    }

    inline operator fun div(a: Long): Duad {
        return Duad(this[0] / a, this[1] / a)
    }

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as Duad

        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        return data.contentHashCode()
    }

    companion object {

        val ZERO = Duad()
    }
}