package br.tiagohm.astrum.sky.core.time

import br.tiagohm.astrum.common.SECONDS_PER_DAY
import br.tiagohm.astrum.common.units.angle.Degrees
import br.tiagohm.astrum.sky.core.math.cos
import br.tiagohm.astrum.sky.core.nutation.Nutation
import br.tiagohm.astrum.sky.core.precession.Precession
import kotlin.math.floor

object SiderealTime {

    /**
     * Compute the mean sidereal time at the meridian of Greenwich (GMST) of a given date
     */
    fun computeMean(jd: JulianDay, jde: JulianDay): Degrees {
        // Time in seconds
        val UT1 = (jd.value - floor(jd.value) + 0.5) * SECONDS_PER_DAY
        val t = (jde.value - 2451545.0) / 36525
        val tu = (jd.value - 2451545.0) / 36525

        // Meeus, Astr. Algorithms, Formula 11.1, 11.4 pg 83. (or 2nd ed. 1998, 12.1, 12.4 pg.87)
        var sidereal = (((-0.000000002454 * t - 0.00000199708) * t - 0.0000002926) * t + 0.092772110) * t * t
        sidereal += (t - tu) * 307.4771013
        sidereal += 8640184.79447825 * tu + 24110.5493771
        sidereal += UT1

        // This is expressed in seconds. We need degrees.
        sidereal *= 1.0 / 240.0

        return Degrees(sidereal).normalized
    }

    /**
     * Compute the apparent sidereal time at the meridian of Greenwich of a given date
     */
    fun computeApparent(jd: JulianDay, jde: JulianDay): Degrees {
        // Formula 11.1, 11.4 pg 83
        val meanSidereal = computeMean(jd, jde)
        // Add corrections for nutation in longitude and for the true obliquity of the ecliptic
        val nut = Nutation.compute(jde)

        return meanSidereal + (nut.deltaPsi * cos(Precession.computeVondrakEpsilon(jde) + nut.deltaEpsilon))
    }
}