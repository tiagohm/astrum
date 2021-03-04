package br.tiagohm.astrum.sky.core.coordinates

import br.tiagohm.astrum.sky.core.math.cos
import br.tiagohm.astrum.sky.core.math.sin
import br.tiagohm.astrum.sky.core.math.tan
import br.tiagohm.astrum.sky.core.units.angle.Angle
import br.tiagohm.astrum.sky.core.units.angle.Radians
import kotlin.math.asin
import kotlin.math.atan2

/**
 * Represents the Equatorial Coordinate System.
 */
@Suppress("NOTHING_TO_INLINE")
open class EquatorialCoord(val ra: Angle, val dec: Angle) : SphericalCoord(ra, dec) {

    /**
     * Converts Equatorial Coordinate System to Ecliptic Coordinate System.
     */
    fun toEcliptic(ecl: Angle): EclipticCoord {
        val lambda = atan2(sin(ra) * cos(ecl) + tan(dec) * sin(ecl), cos(ra))
        val beta = asin(sin(dec) * cos(ecl) - cos(dec) * sin(ecl) * sin(ra))
        return EclipticCoord(Radians(lambda), Radians(beta))
    }
}