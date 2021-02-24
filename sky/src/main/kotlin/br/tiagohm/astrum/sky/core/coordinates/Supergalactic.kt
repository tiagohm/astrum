package br.tiagohm.astrum.sky.core.coordinates

import br.tiagohm.astrum.sky.core.units.angle.Angle

/**
 * Represents the Supergalatic Coordinate System.
 */
class Supergalactic(val longitude: Angle, val latitude: Angle) : Spherical(longitude, latitude)