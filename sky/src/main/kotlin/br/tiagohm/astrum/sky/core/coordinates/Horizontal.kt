package br.tiagohm.astrum.sky.core.coordinates

import br.tiagohm.astrum.sky.core.units.Radians

@Suppress("EXPERIMENTAL_FEATURE_WARNING", "RESERVED_MEMBER_INSIDE_INLINE_CLASS", "NOTHING_TO_INLINE")
inline class Horizontal(val coord: Pair<Radians, Radians>) : Coordinate {

    constructor(az: Radians, alt: Radians) : this(az to alt)

    inline val az: Radians
        get() = coord.first

    inline val alt: Radians
        get() = coord.second

    inline operator fun component1() = az

    inline operator fun component2() = alt

    override val x: Radians
        get() = az

    override val y: Radians
        get() = alt

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as Horizontal

        if (coord != other.coord) return false

        return true
    }

    override fun hashCode(): Int {
        return coord.hashCode()
    }
}