package br.tiagohm.astrum.tools.stars

import br.tiagohm.astrum.sky.core.math.Triad

/**
 * The celestial sphere is split into zones,
 * which correspond to the triangular faces of a geodesic sphere.
 */
data class Triangle(
    val e0: Triad = Triad.ZERO,
    val e1: Triad = Triad.ZERO,
    val e2: Triad = Triad.ZERO,
) {

    companion object {

        val ZERO = Triangle()
    }
}