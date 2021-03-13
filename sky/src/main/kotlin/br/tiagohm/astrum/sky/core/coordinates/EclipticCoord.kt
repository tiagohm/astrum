package br.tiagohm.astrum.sky.core.coordinates

import br.tiagohm.astrum.common.units.angle.Angle
import br.tiagohm.astrum.common.units.angle.Radians
import br.tiagohm.astrum.sky.core.math.cos
import br.tiagohm.astrum.sky.core.math.sin
import kotlin.math.*

/**
 * Represents the Ecliptic Coordinate System.
 */
class EclipticCoord(val lambda: Angle, val beta: Angle) : SphericalCoord(lambda, beta) {

    /**
     * Converts Ecliptic Coordinate System to Equatorial Coordinate System.
     */
    fun toEquatorial(e: Angle): EquatorialCoord {
        val l = lambda.radians.value
        val b = beta.radians.value

        val ra = atan2(sin(l) * cos(e) - tan(b) * sin(e), cos(l))
        val dec = asin(sin(b) * cos(e) + cos(b) * sin(e) * sin(l))
        return EquatorialCoord(Radians(ra), Radians(dec))
    }

    companion object {

        val ZERO = EclipticCoord(Radians.ZERO, Radians.ZERO)
    }
}