package br.tiagohm.astrum.sky.core.orbit

import br.tiagohm.astrum.sky.core.math.Triad
import br.tiagohm.astrum.sky.core.units.distance.Distance

interface Orbit {

    fun positionAtTimevInVSOP87Coordinates(jde: Double): Triad

    val velocity: Triad

    val semiMajorAxis: Distance

    val e: Double

    val siderealPeriod: Double
}