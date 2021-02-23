package br.tiagohm.astrum.sky.core.coordinates

import br.tiagohm.astrum.sky.core.cos
import br.tiagohm.astrum.sky.core.sin
import br.tiagohm.astrum.sky.core.tan
import br.tiagohm.astrum.sky.core.units.Radians
import kotlin.math.asin
import kotlin.math.atan2

/**
 * Represents the Ecliptic Coordinate System.
 */
class Ecliptic(val lambda: Radians, val beta: Radians) : Spherical(lambda, beta) {

    /**
     * Converts Ecliptic Coordinate System to Equatorial Coordinate System.
     */
    fun toEquatorial(e: Radians): Equatorial {
        val ra = atan2(sin(lambda) * cos(e) - tan(beta) * sin(e), cos(lambda))
        val dec = asin(sin(beta) * cos(e) + cos(beta) * sin(e) * sin(lambda))
        return Equatorial(Radians(ra), Radians(dec))
    }
}