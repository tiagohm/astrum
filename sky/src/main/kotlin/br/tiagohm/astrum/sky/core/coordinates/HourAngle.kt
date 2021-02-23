package br.tiagohm.astrum.sky.core.coordinates

import br.tiagohm.astrum.sky.core.units.Degrees
import br.tiagohm.astrum.sky.core.units.Radians

@Suppress("EXPERIMENTAL_FEATURE_WARNING", "RESERVED_MEMBER_INSIDE_INLINE_CLASS", "NOTHING_TO_INLINE")
inline class HourAngle(val coord: Pair<Degrees, Radians>) : Coordinate {

    constructor(ha: Degrees, dec: Radians) : this(ha to dec)

    inline val ha: Degrees
        get() = coord.first

    inline val dec: Radians
        get() = coord.second

    inline operator fun component1() = ha

    inline operator fun component2() = dec

    override val x: Radians
        get() = ha.radians

    override val y: Radians
        get() = dec

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as HourAngle

        if (coord != other.coord) return false

        return true
    }

    override fun hashCode(): Int {
        return coord.hashCode()
    }
}