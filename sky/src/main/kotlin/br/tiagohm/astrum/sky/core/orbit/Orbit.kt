package br.tiagohm.astrum.sky.core.orbit

import br.tiagohm.astrum.common.units.distance.Distance
import br.tiagohm.astrum.sky.core.math.Triad
import br.tiagohm.astrum.sky.core.time.JulianDay

interface Orbit {

    fun positionAndVelocityAtTimevInVSOP87Coordinates(jde: JulianDay): Pair<Triad, Triad>

    val semiMajorAxis: Distance

    /**
     * Excentricity.
     */
    val e: Double

    /**
     * Gets duration of sidereal year, in earth days.
     */
    val siderealPeriod: Double
}