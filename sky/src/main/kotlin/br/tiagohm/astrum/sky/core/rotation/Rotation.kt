package br.tiagohm.astrum.sky.core.rotation

import br.tiagohm.astrum.sky.MAT_J2000_TO_VSOP87
import br.tiagohm.astrum.sky.M_PI_2
import br.tiagohm.astrum.sky.core.Algorithms
import br.tiagohm.astrum.sky.core.units.Radians
import br.tiagohm.astrum.sky.core.units.minus

object Rotation {

    /**
     * Computes rotation obliquity and ascending node from WGCCRE elements.
     */
    fun compute(ra: Radians, dec: Radians): Pair<Radians, Radians> {
        val pole = Algorithms.sphericalToRectangularCoordinates(ra, dec)
        val vsop87Pole = MAT_J2000_TO_VSOP87.multiplyWithoutTranslation(pole)
        val (lng, lat) = Algorithms.rectangularToSphericalCoordinates(vsop87Pole)
        return lng + M_PI_2 to M_PI_2 - lat
    }
}