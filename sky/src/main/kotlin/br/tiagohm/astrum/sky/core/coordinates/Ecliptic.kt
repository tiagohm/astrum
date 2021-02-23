package br.tiagohm.astrum.sky.core.coordinates

import br.tiagohm.astrum.sky.core.units.Radians
import br.tiagohm.astrum.sky.core.units.cos
import br.tiagohm.astrum.sky.core.units.sin
import br.tiagohm.astrum.sky.core.units.tan
import kotlin.math.asin
import kotlin.math.atan2

@Suppress("EXPERIMENTAL_FEATURE_WARNING", "RESERVED_MEMBER_INSIDE_INLINE_CLASS", "NOTHING_TO_INLINE")
inline class Ecliptic(val coord: Pair<Radians, Radians>) : Coordinate {

    constructor(lambda: Radians, beta: Radians) : this(lambda to beta)

    inline val lambda: Radians
        get() = coord.first

    inline val beta: Radians
        get() = coord.second

    inline operator fun component1() = lambda

    inline operator fun component2() = beta

    override val x: Radians
        get() = lambda

    override val y: Radians
        get() = beta

    fun toEquatorial(ecl: Radians): Equatorial {
        val ra = atan2(sin(lambda) * cos(ecl) - tan(beta) * sin(ecl), cos(lambda))
        val dec = asin(sin(beta) * cos(ecl) + cos(beta) * sin(ecl) * sin(lambda))
        return Equatorial(Radians(ra), Radians(dec))
    }

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as Ecliptic

        if (coord != other.coord) return false

        return true
    }

    override fun hashCode(): Int {
        return coord.hashCode()
    }
}