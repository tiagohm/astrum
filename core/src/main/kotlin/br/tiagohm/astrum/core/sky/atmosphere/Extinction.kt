package br.tiagohm.astrum.core.sky.atmosphere

import br.tiagohm.astrum.core.algorithms.math.Triad
import kotlin.math.exp
import kotlin.math.min

data class Extinction(
    val coefficient: Double = 0.13,
    val mode: UndergroundExtinctionMode = UndergroundExtinctionMode.MIRROR,
) {

    /**
     * Airmass computation for cosine of zenith angle z
     */
    fun airmass(cosZ: Double, apparentZ: Boolean = true): Double {
        var cz = cosZ

        // About -2 degrees. Here, RozenbergZ>574 and climbs fast!
        if (cosZ < -0.035) {
            when (mode) {
                UndergroundExtinctionMode.ZERO -> return 0.0
                UndergroundExtinctionMode.MAX -> return 42.0
                else -> cz = min(1.0, -0.035 - (cosZ + 0.035))
            }
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

    // Compute extinction effect for given position vector and magnitude
    fun forward(altAzPos: Triad, mag: Double): Double {
        return mag + airmass(altAzPos[2], false) * coefficient
    }

    // Compute inverse extinction effect for given position vector and magnitude.
    fun backward(altAzPos: Triad, mag: Double): Double {
        return mag - airmass(altAzPos[2], false) * coefficient
    }
}