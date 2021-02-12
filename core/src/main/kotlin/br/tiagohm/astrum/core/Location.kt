package br.tiagohm.astrum.core

data class Location(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val altitude: Double = 0.0,
)
