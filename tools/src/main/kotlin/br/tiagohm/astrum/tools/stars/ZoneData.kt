package br.tiagohm.astrum.tools.stars

import br.tiagohm.astrum.sky.core.math.Triad

data class ZoneData<T : StarData>(
    val center: Triad, // Normalized center of triangle
    val axis0: Triad,  // Normalized direction vector of axis 0 (use for storing stars position in 2D relative to this axis)
    val axis1: Triad,  // Normalized direction vector of axis 1 (use for storing stars position in 2D relative to this axis)
    val data: Array<T>,
) {

    inline val numberOfStars: Int
        get() = data.size

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ZoneData<*>

        if (center != other.center) return false
        if (axis0 != other.axis0) return false
        if (axis1 != other.axis1) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = center.hashCode()
        result = 31 * result + axis0.hashCode()
        result = 31 * result + axis1.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }
}