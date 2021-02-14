package br.tiagohm.astrum.core

typealias Degrees = Double
typealias Radians = Double
typealias Celsius = Double

inline val Degrees.rad: Double
    get() = this * M_PI_180

inline val Radians.deg: Double
    get() = this * M_180_PI