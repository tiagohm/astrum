package br.tiagohm.astrum.sky.core.coordinates

import br.tiagohm.astrum.sky.core.units.angle.Angle

/**
 * Represents the Supergalatic Coordinate System.
 */
class SupergalacticCoord(val longitude: Angle, val latitude: Angle) : SphericalCoord(longitude, latitude)