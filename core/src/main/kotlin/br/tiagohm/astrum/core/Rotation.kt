package br.tiagohm.astrum.core

object Rotation {

    /**
     * Computes rotation obliquity and ascending node WGCCRE elements
     */
    fun compute(ra: Double, dec: Double): Duad {
        val pole = Algorithms.sphericalToRectangularCoordinates(ra, dec)
        val vsop87Pole = MAT_J2000_TO_VSOP87.multiplyWithoutTranslation(pole)
        val (lng, lat) = Algorithms.rectangularToSphericalCoordinates(vsop87Pole)
        return Duad(lng + M_PI_2, M_PI_2 - lat)
    }
}