package br.tiagohm.astrum.sky.core.coordinates

import br.tiagohm.astrum.sky.core.math.cos
import br.tiagohm.astrum.sky.core.math.sin
import br.tiagohm.astrum.sky.core.units.angle.Angle
import br.tiagohm.astrum.sky.core.units.angle.Radians
import kotlin.math.*

/**
 * Represents the Equatorial Coordinate System.
 */
@Suppress("NOTHING_TO_INLINE")
open class EquatorialCoord(val ra: Angle, val dec: Angle) : SphericalCoord(ra, dec) {

    /**
     * Converts Equatorial Coordinate System to Ecliptic Coordinate System.
     */
    fun toEcliptic(ecl: Angle): EclipticCoord {
        val r = ra.radians.value
        val d = dec.radians.value

        val lambda = atan2(sin(r) * cos(ecl) + tan(d) * sin(ecl), cos(r))
        val beta = asin(sin(d) * cos(ecl) - cos(d) * sin(ecl) * sin(r))
        return EclipticCoord(Radians(lambda), Radians(beta))
    }

    companion object {

        val ZERO = EquatorialCoord(Radians.ZERO, Radians.ZERO)
    }
}