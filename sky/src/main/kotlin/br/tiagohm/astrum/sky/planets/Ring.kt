package br.tiagohm.astrum.sky.planets

data class Ring(
    val radiusMin: Double,
    val radiusMax: Double,
) {

    val size = radiusMax
}
