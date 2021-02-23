package br.tiagohm.astrum.sky.core.coordinates

import br.tiagohm.astrum.sky.core.units.Degrees
import br.tiagohm.astrum.sky.core.units.Radians

@Suppress("EXPERIMENTAL_FEATURE_WARNING", "RESERVED_MEMBER_INSIDE_INLINE_CLASS", "NOTHING_TO_INLINE")
inline class Geographic(val coord: Pair<Degrees, Degrees>) : Coordinate {

    constructor(longitude: Degrees, latitude: Degrees) : this(longitude to latitude)

    /**
     * Longitude in degrees.
     */
    inline val longitude: Degrees
        get() = coord.first

    /**
     * Latitude in degrees.
     */
    inline val latitude: Degrees
        get() = coord.second

    inline operator fun component1() = longitude

    inline operator fun component2() = latitude

    override val x: Radians
        get() = longitude.radians

    override val y: Radians
        get() = latitude.radians

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as Geographic

        if (coord != other.coord) return false

        return true
    }

    override fun hashCode(): Int {
        return coord.hashCode()
    }
}