package br.tiagohm.astrum.sky.core.coordinates

import br.tiagohm.astrum.sky.core.cos
import br.tiagohm.astrum.sky.core.sin
import br.tiagohm.astrum.sky.core.tan
import br.tiagohm.astrum.sky.core.units.Radians
import kotlin.math.asin
import kotlin.math.atan2

/**
 * Represents the Equatorial Coordinate System.
 */
@Suppress("NOTHING_TO_INLINE")
class Equatorial(val ra: Radians, val dec: Radians) : Spherical(ra, dec) {

    /**
     * Converts Equatorial Coordinate System to Ecliptic Coordinate System.
     */
    fun toEcliptic(ecl: Radians): Ecliptic {
        val lambda = atan2(sin(ra) * cos(ecl) + tan(dec) * sin(ecl), cos(ra))
        val beta = asin(sin(dec) * cos(ecl) - cos(dec) * sin(ecl) * sin(ra))
        return Ecliptic(Radians(lambda), Radians(beta))
    }
}