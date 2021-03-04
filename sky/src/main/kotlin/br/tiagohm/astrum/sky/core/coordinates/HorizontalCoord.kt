package br.tiagohm.astrum.sky.core.coordinates

import br.tiagohm.astrum.sky.core.units.angle.Angle

/**
 * Represents the Horizontal Coordinate System.
 */
@Suppress("NOTHING_TO_INLINE")
class HorizontalCoord(val az: Angle, val alt: Angle) : SphericalCoord(az, alt)