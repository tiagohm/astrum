package br.tiagohm.astrum.sky.algorithms.rotation

import br.tiagohm.astrum.sky.MAT_J2000_TO_VSOP87
import br.tiagohm.astrum.sky.M_PI_2
import br.tiagohm.astrum.sky.algorithms.Algorithms
import br.tiagohm.astrum.sky.algorithms.math.Duad

object Rotation {

    /**
     * Computes rotation obliquity and ascending node from WGCCRE elements.
     */
    fun compute(ra: Double, dec: Double): Duad {
        val pole = Algorithms.sphericalToRectangularCoordinates(ra, dec)
        val vsop87Pole = MAT_J2000_TO_VSOP87.multiplyWithoutTranslation(pole)
        val (lng, lat) = Algorithms.rectangularToSphericalCoordinates(vsop87Pole)
        return Duad(lng + M_PI_2, M_PI_2 - lat)
    }
}