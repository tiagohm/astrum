package br.tiagohm.astrum.sky.core.coordinates

import br.tiagohm.astrum.sky.core.units.Radians

/**
 * Represents the Horizontal Coordinate System.
 */
@Suppress("NOTHING_TO_INLINE")
class Horizontal(val az: Radians, val alt: Radians) : Spherical(az, alt)