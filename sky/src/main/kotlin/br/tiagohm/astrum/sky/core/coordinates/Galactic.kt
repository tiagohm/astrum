package br.tiagohm.astrum.sky.core.coordinates

import br.tiagohm.astrum.sky.core.units.angle.Angle

/**
 * Represents the Galatic Coordinate System.
 */
class Galactic(val longitude: Angle, val latitude: Angle) : Spherical(longitude, latitude)