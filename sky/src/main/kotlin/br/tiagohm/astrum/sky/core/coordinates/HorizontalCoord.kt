package br.tiagohm.astrum.sky.core.coordinates

import br.tiagohm.astrum.common.units.angle.Angle
import br.tiagohm.astrum.common.units.angle.Radians

/**
 * Represents the Horizontal Coordinate System.
 */
class HorizontalCoord(val azimuth: Angle, val altitude: Angle) : SphericalCoord(azimuth, altitude) {

    /**
     * Gets direction of the [azimuth].
     */
    val direction: Direction
        get() = Direction.values()[(azimuth.degrees.value / 22.5).toInt()]

    companion object {

        val ZERO = HorizontalCoord(Radians.ZERO, Radians.ZERO)
    }
}