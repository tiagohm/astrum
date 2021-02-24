package br.tiagohm.astrum.sky.atmosphere

import br.tiagohm.astrum.sky.core.math.Triad
import kotlin.math.exp
import kotlin.math.min

data class Extinction(
    val coefficient: Double = DEFAULT_COEFFICIENT,
) {

    /**
     * Computes extinction effect for given position vector and magnitude.
     */
    fun forward(altAzPos: Triad, mag: Double) = mag + airmass(altAzPos[2], false) * coefficient

    /**
     * Computes inverse extinction effect for given position vector and magnitude.
     */
    fun backward(altAzPos: Triad, mag: Double) = mag - airmass(altAzPos[2], false) * coefficient

    companion object {

        const val DEFAULT_COEFFICIENT = 0.13

        /**
         * Computes airmass for cosine of zenith angle z.
         */
        fun airmass(
            cosZ: Double,
            apparentZ: Boolean = true,
        ): Double {
            var cz = cosZ

            // About -2 degrees. Here, RozenbergZ>574 and climbs fast!
            if (cosZ < -0.035) {
                cz = min(1.0, -0.035 - (cosZ + 0.035))
            }

            return if (apparentZ) {
                // Rozenberg 1966, reported by Schaefer (1993-2000).
                1.0 / (cz + 0.025 * exp(-11.0 * cz))
            } else {
                // Young 1994
                val nom = (1.002432 * cz + 0.148386) * cz + 0.0096467
                val denum = ((cz + 0.149864) * cz + 0.0102963) * cz + 0.000303978
                nom / denum
            }
        }
    }
}