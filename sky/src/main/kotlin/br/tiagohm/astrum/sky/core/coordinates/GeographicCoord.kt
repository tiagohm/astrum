package br.tiagohm.astrum.sky.core.coordinates

import br.tiagohm.astrum.common.units.angle.Angle
import br.tiagohm.astrum.common.units.angle.Radians

/**
 * Represents the Geographic Coordinate System (GCS).
 */
class GeographicCoord(val longitude: Angle, val latitude: Angle) : SphericalCoord(longitude.radians, latitude.radians) {

    companion object {

        val ZERO = GeographicCoord(Radians.ZERO, Radians.ZERO)
    }
}