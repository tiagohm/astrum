package br.tiagohm.astrum.sky.core.orbit

import br.tiagohm.astrum.sky.core.math.Triad
import br.tiagohm.astrum.sky.core.units.distance.Distance

interface Orbit {

    fun positionAndVelocityAtTimevInVSOP87Coordinates(jde: Double): Pair<Triad, Triad>

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