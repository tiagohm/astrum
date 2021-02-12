package br.tiagohm.astrum.core

inline val Double.rad: Double
    get() = this * M_PI_180

inline val Double.deg: Double
    get() = this * M_180_PI
