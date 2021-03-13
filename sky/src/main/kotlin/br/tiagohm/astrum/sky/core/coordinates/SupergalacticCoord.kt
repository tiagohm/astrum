package br.tiagohm.astrum.sky.core.coordinates

import br.tiagohm.astrum.common.units.angle.Angle
import br.tiagohm.astrum.common.units.angle.Radians

/**
 * Represents the Supergalatic Coordinate System.
 */
class SupergalacticCoord(val longitude: Angle, val latitude: Angle) : SphericalCoord(longitude, latitude) {

    companion object {

        val ZERO = SupergalacticCoord(Radians.ZERO, Radians.ZERO)
    }
}