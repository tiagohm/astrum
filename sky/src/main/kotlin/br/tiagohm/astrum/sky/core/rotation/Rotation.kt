package br.tiagohm.astrum.sky.core.rotation

import br.tiagohm.astrum.common.units.angle.Angle
import br.tiagohm.astrum.common.units.angle.Radians
import br.tiagohm.astrum.sky.MAT_J2000_TO_VSOP87
import br.tiagohm.astrum.sky.core.Algorithms

data class Rotation(
    val obliquity: Angle,
    val ascendingNode: Angle,
    val offset: Angle,
) {

    companion object {

        /**
         * Computes rotation obliquity and ascending node from WGCCRE elements.
         */
        fun compute(ra: Angle, dec: Angle, w0: Angle = Radians.ZERO): Rotation {
            val pole = Algorithms.sphericalToRectangularCoordinates(ra, dec)
            val vsop87Pole = MAT_J2000_TO_VSOP87.multiplyWithoutTranslation(pole)
            val (lng, lat) = Algorithms.rectangularToSphericalCoordinates(vsop87Pole)
            return Rotation(Radians.PI_2 - lat, lng + Radians.PI_2, w0.radians + lng)
        }
    }
}