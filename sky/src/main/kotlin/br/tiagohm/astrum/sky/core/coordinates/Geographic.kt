package br.tiagohm.astrum.sky.core.coordinates

import br.tiagohm.astrum.sky.core.units.angle.Angle

/**
 * Represents the Geographic Coordinate System (GCS).
 */
class Geographic(val longitude: Angle, val latitude: Angle) : Spherical(longitude.radians, latitude.radians)