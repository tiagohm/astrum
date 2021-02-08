package br.tiagohm.astrum.core.time

import br.tiagohm.astrum.core.algorithms.Nutation
import br.tiagohm.astrum.core.algorithms.Precession
import br.tiagohm.astrum.core.deg
import br.tiagohm.astrum.core.in360
import kotlin.math.cos
import kotlin.math.floor

object SiderealTime {

    // Compute the mean sidereal time in degrees at the meridian of Greenwich (GMST) of a given date
    // Meeus, Astr. Algorithms, Formula 11.1, 11.4 pg 83. (or 2nd ed. 1998, 12.1, 12.4 pg.87)
    fun computeMean(jd: Double, jde: Double): Double {
        // Time in seconds
        val UT1 = (jd - floor(jd) + 0.5) * 86400
        val t = (jde - 2451545.0) / 36525
        val tu = (jd - 2451545.0) / 36525

        var sidereal = (((-0.000000002454 * t - 0.00000199708) * t - 0.0000002926) * t + 0.092772110) * t * t
        sidereal += (t - tu) * 307.4771013
        sidereal += 8640184.79447825 * tu + 24110.5493771
        sidereal += UT1

        // This is expressed in seconds. We need degrees.
        sidereal *= 1.0 / 240.0

        return sidereal.in360
    }

    // Compute the apparent sidereal time in degrees at the meridian of Greenwich of a given date
    // Formula 11.1, 11.4 pg 83
    fun computeApparent(jd: Double, jde: Double): Double {
        val meanSidereal = computeMean(jd, jde)
        // Add corrections for nutation in longitude and for the true obliquity of the ecliptic
        val nut = Nutation.compute(jde)

        return meanSidereal + nut.deltaPsi * cos(Precession.computeVondrakEpsilon(jde) + nut.deltaEpsilon).deg
    }
}