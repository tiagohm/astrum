package br.tiagohm.astrum.core

interface Orbit {

    fun positionAtTimevInVSOP87Coordinates(jde: Double): Triad

    val velocity: Triad

    val semiMajorAxis: Double

    val e: Double

    val siderealPeriod: Double
}