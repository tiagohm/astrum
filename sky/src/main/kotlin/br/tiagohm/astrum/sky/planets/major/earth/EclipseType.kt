package br.tiagohm.astrum.sky.planets.major.earth

enum class EclipseType {
    NONE,
    PARTIAL, // For solar eclipses.
    ANNULAR, // Solar.
    ANNULAR_TOTAL, // Solar.
    PENUMBRAL, // For lunar eclipses.
    UMBRAL, // Lunar.
    TOTAL // Solar or lunar.
}