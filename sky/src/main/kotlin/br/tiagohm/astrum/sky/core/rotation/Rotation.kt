package br.tiagohm.astrum.sky.core.rotation

import br.tiagohm.astrum.sky.MAT_J2000_TO_VSOP87
import br.tiagohm.astrum.sky.M_PI_2
import br.tiagohm.astrum.sky.core.Algorithms
import br.tiagohm.astrum.sky.core.units.Radians

data class Rotation(
    val obliquity: Radians,
    val ascendingNode: Radians,
) {

    companion object {

        /**
         * Computes rotation obliquity and ascending node from WGCCRE elements.
         */
        fun compute(ra: Radians, dec: Radians): Rotation {
            val pole = Algorithms.sphericalToRectangularCoordinates(ra, dec)
            val vsop87Pole = MAT_J2000_TO_VSOP87.multiplyWithoutTranslation(pole)
            val (lng, lat) = Algorithms.rectangularToSphericalCoordinates(vsop87Pole)
            return Rotation(lng + M_PI_2, -lat + M_PI_2)
        }
    }
}