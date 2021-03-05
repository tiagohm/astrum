package br.tiagohm.astrum.sky.core.coordinates

import br.tiagohm.astrum.sky.core.units.angle.Angle
import br.tiagohm.astrum.sky.core.units.angle.Radians

/**
 * Represents the Galatic Coordinate System.
 */
class GalacticCoord(val longitude: Angle, val latitude: Angle) : SphericalCoord(longitude, latitude) {

    companion object {

        val ZERO = GalacticCoord(Radians.ZERO, Radians.ZERO)
    }
}