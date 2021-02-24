package br.tiagohm.astrum.sky.core.coordinates

import br.tiagohm.astrum.sky.core.units.angle.Angle

/**
 * Represents the Horizontal Coordinate System.
 */
@Suppress("NOTHING_TO_INLINE")
class Horizontal(val az: Angle, val alt: Angle) : Spherical(az, alt)