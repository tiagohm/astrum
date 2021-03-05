package br.tiagohm.astrum.sky.core.coordinates

import br.tiagohm.astrum.sky.core.math.cos
import br.tiagohm.astrum.sky.core.math.sin
import br.tiagohm.astrum.sky.core.math.tan
import br.tiagohm.astrum.sky.core.units.angle.Angle
import br.tiagohm.astrum.sky.core.units.angle.Radians
import kotlin.math.asin
import kotlin.math.atan2

/**
 * Represents the Ecliptic Coordinate System.
 */
class EclipticCoord(val lambda: Angle, val beta: Angle) : SphericalCoord(lambda, beta) {

    /**
     * Converts Ecliptic Coordinate System to Equatorial Coordinate System.
     */
    fun toEquatorial(e: Angle): EquatorialCoord {
        val ra = atan2(sin(lambda) * cos(e) - tan(beta) * sin(e), cos(lambda))
        val dec = asin(sin(beta) * cos(e) + cos(beta) * sin(e) * sin(lambda))
        return EquatorialCoord(Radians(ra), Radians(dec))
    }

    companion object {

        val ZERO = EclipticCoord(Radians.ZERO, Radians.ZERO)
    }
}