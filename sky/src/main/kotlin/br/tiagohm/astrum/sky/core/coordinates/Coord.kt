package br.tiagohm.astrum.sky.core.coordinates

import br.tiagohm.astrum.sky.core.units.angle.Angle

interface Coord {

    /**
     * Coordinate X.
     */
    val x: Angle

    /**
     * Coordinate Y.
     */
    val y: Angle
}