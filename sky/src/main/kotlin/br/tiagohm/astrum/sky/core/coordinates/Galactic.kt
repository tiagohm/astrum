package br.tiagohm.astrum.sky.core.coordinates

import br.tiagohm.astrum.sky.core.units.Radians

/**
 * Represents the Galatic Coordinate System.
 */
class Galactic(val longitude: Radians, val latitude: Radians) : Spherical(longitude, latitude)