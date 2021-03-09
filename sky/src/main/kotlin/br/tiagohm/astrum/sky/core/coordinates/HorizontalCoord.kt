package br.tiagohm.astrum.sky.core.coordinates

import br.tiagohm.astrum.sky.core.units.angle.Angle
import br.tiagohm.astrum.sky.core.units.angle.Radians

/**
 * Represents the Horizontal Coordinate System.
 */
class HorizontalCoord(val az: Angle, val alt: Angle) : SphericalCoord(az, alt) {

    companion object {

        val ZERO = HorizontalCoord(Radians.ZERO, Radians.ZERO)
    }
}