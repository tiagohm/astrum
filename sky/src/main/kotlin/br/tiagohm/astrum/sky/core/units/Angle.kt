package br.tiagohm.astrum.sky.core.units

interface Angle : Unit {

    val radians: Radians

    val degrees: Degrees

    val normalized: Angle
}