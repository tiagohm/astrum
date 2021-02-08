package br.tiagohm.astrum.core.math

@Suppress("NOTHING_TO_INLINE", "RESERVED_MEMBER_INSIDE_INLINE_CLASS")
inline class RaDec(val data: DoubleArray) {

    constructor(ra: Double, dec: Double) : this(doubleArrayOf(ra, dec))

    inline val ra: Double
        get() = data[0]

    inline val dec: Double
        get() = data[1]

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as RaDec

        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        return data.contentHashCode()
    }
}