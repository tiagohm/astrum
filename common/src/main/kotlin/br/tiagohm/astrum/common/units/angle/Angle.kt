package br.tiagohm.astrum.common.units.angle

import br.tiagohm.astrum.common.units.Unit

interface Angle : Unit, Comparable<Angle> {

    val radians: Radians

    val degrees: Degrees

    val normalized: Angle

    operator fun minus(a: Angle): Angle

    operator fun unaryMinus(): Angle

    operator fun plus(a: Angle): Angle

    operator fun times(a: Angle): Angle

    operator fun times(n: Number): Angle

    operator fun div(a: Angle): Angle

    operator fun div(n: Number): Angle
}