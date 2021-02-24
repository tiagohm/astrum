package br.tiagohm.astrum.sky.core.rotation

import br.tiagohm.astrum.sky.MAT_J2000_TO_VSOP87
import br.tiagohm.astrum.sky.core.Algorithms
import br.tiagohm.astrum.sky.core.units.angle.Angle
import br.tiagohm.astrum.sky.core.units.angle.Radians

data class Rotation(
    val obliquity: Angle,
    val ascendingNode: Angle,
) {

    companion object {

        /**
         * Computes rotation obliquity and ascending node from WGCCRE elements.
         */
        fun compute(ra: Angle, dec: Angle): Rotation {
            val pole = Algorithms.sphericalToRectangularCoordinates(ra, dec)
            val vsop87Pole = MAT_J2000_TO_VSOP87.multiplyWithoutTranslation(pole)
            val (lng, lat) = Algorithms.rectangularToSphericalCoordinates(vsop87Pole)
            return Rotation(lng + Radians.PI_2, -lat + Radians.PI_2)
        }
    }
}