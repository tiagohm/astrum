package br.tiagohm.astrum.core

data class Ring(
    val radiusMin: Double,
    val radiusMax: Double,
) {

    val size = radiusMax
}
