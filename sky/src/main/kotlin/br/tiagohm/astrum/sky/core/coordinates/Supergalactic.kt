package br.tiagohm.astrum.sky.core.coordinates

import br.tiagohm.astrum.sky.core.units.Radians

/**
 * Represents the Supergalatic Coordinate System.
 */
class Supergalactic(val longitude: Radians, val latitude: Radians) : Spherical(longitude, latitude)