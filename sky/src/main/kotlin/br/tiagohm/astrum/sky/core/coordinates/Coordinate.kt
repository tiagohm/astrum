package br.tiagohm.astrum.sky.core.coordinates

import br.tiagohm.astrum.sky.core.units.Radians

interface Coordinate {

    /**
     * Coordinate X in radians.
     */
    val x: Radians

    /**
     * Coordinate Y in radians.
     */
    val y: Radians
}