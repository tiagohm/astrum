package br.tiagohm.astrum.sky.core.coordinates

import br.tiagohm.astrum.common.units.angle.Angle

/**
 * Represents a generic spherical coordinate system.
 */
@Suppress("NOTHING_TO_INLINE")
open class SphericalCoord(
    override val x: Angle,
    override val y: Angle,
) : Coord {

    inline operator fun component1() = x

    inline operator fun component2() = y

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SphericalCoord

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }

    override fun toString() = "${javaClass.simpleName}(x: $x, y: $y)"
}