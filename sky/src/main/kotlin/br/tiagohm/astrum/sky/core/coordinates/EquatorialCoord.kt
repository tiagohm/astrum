package br.tiagohm.astrum.sky.core.coordinates

import br.tiagohm.astrum.common.units.angle.Angle
import br.tiagohm.astrum.common.units.angle.Radians
import br.tiagohm.astrum.sky.core.math.cos
import br.tiagohm.astrum.sky.core.math.sin
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

        /**
         * Computes the angular separation in radians of 2 coordinates.
         */
        fun angularSeparation(a: EquatorialCoord, b: EquatorialCoord): Radians {
            val a1 = a.ra.radians.value
            val d1 = a.dec.radians.value
            val a2 = b.ra.radians.value
            val d2 = b.dec.radians.value

            val x = (cos(d1) * sin(d2)) - (sin(d1) * cos(d2) * cos(a2 - a1))
            val y = cos(d2) * sin(a2 - a1)
            val z = (sin(d1) * sin(d2)) + (cos(d1) * cos(d2) * cos(a2 - a1))

            val d = atan2(sqrt(x * x + y * y), z)

            return Radians(d)
        }

        /**
         * Computes the position angle of a body with respect to another body.
         */
        fun positionAngle(a: EquatorialCoord, b: EquatorialCoord): Radians {
            val a1 = a.ra.radians.value
            val d1 = a.dec.radians.value
            val a2 = b.ra.radians.value
            val d2 = b.dec.radians.value

            val x = (cos(d2) * tan(d1)) - (sin(d2) * cos(a1 - a2))
            val y = sin(a1 - a2)

            val p = atan2(y, x)

            return Radians(p)
        }
    }
}