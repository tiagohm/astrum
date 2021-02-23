package br.tiagohm.astrum.sky.algorithms.orbit

import br.tiagohm.astrum.sky.algorithms.math.Triad

interface Orbit {

    fun positionAtTimevInVSOP87Coordinates(jde: Double): Triad

    val velocity: Triad

    val semiMajorAxis: Double

    val e: Double

    val siderealPeriod: Double
}