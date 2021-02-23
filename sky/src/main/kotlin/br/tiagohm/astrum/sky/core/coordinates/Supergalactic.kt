package br.tiagohm.astrum.sky.core.coordinates

import br.tiagohm.astrum.sky.core.units.Radians

@Suppress("EXPERIMENTAL_FEATURE_WARNING", "RESERVED_MEMBER_INSIDE_INLINE_CLASS", "NOTHING_TO_INLINE")
inline class Supergalactic(val coord: Pair<Radians, Radians>) : Coordinate {

    constructor(longitude: Radians, latitude: Radians) : this(longitude to latitude)

    /**
     * Longitude in radians.
     */
    inline val longitude: Radians
        get() = coord.first

    /**
     * Latitude in radians.
     */
    inline val latitude: Radians
        get() = coord.second

    inline operator fun component1() = longitude

    inline operator fun component2() = latitude

    override val x: Radians
        get() = longitude

    override val y: Radians
        get() = latitude

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as Supergalactic

        if (coord != other.coord) return false

        return true
    }

    override fun hashCode(): Int {
        return coord.hashCode()
    }
}