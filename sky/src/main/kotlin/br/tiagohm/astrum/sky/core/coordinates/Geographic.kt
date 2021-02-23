package br.tiagohm.astrum.sky.core.coordinates

import br.tiagohm.astrum.sky.core.units.Degrees

/**
 * Represents the Geographic Coordinate System (GCS).
 */
class Geographic(val longitude: Degrees, val latitude: Degrees) : Spherical(longitude.radians, latitude.radians)