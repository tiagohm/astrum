package br.tiagohm.astrum.sky.core.coordinates

import br.tiagohm.astrum.sky.core.units.angle.Angle

/**
 * Represents the Geographic Coordinate System (GCS).
 */
class GeographicCoord(val longitude: Angle, val latitude: Angle) : SphericalCoord(longitude.radians, latitude.radians)