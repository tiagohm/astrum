package br.tiagohm.astrum.sky.core.coordinates

import br.tiagohm.astrum.sky.core.units.Radians
import br.tiagohm.astrum.sky.core.units.cos
import br.tiagohm.astrum.sky.core.units.sin
import br.tiagohm.astrum.sky.core.units.tan
import kotlin.math.asin
import kotlin.math.atan2

@Suppress("EXPERIMENTAL_FEATURE_WARNING", "RESERVED_MEMBER_INSIDE_INLINE_CLASS", "NOTHING_TO_INLINE")
inline class Equatorial(val coord: Pair<Radians, Radians>) : Coordinate {

    constructor(ra: Radians, dec: Radians) : this(ra to dec)

    inline val ra: Radians
        get() = coord.first

    inline val dec: Radians
        get() = coord.second

    inline operator fun component1() = ra

    inline operator fun component2() = dec

    override val x: Radians
        get() = ra

    override val y: Radians
        get() = dec

    fun toEcliptic(ecl: Radians): Ecliptic {
        val lambda = atan2(sin(ra) * cos(ecl) + tan(dec) * sin(ecl), cos(ra))
        val beta = asin(sin(dec) * cos(ecl) - cos(dec) * sin(ecl) * sin(ra))
        return Ecliptic(Radians(lambda), Radians(beta))
    }

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as Equatorial

        if (coord != other.coord) return false

        return true
    }

    override fun hashCode(): Int {
        return coord.hashCode()
    }
}