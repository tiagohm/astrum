package br.tiagohm.astrum.sky.atmosphere

import br.tiagohm.astrum.sky.Celsius
import br.tiagohm.astrum.sky.algorithms.math.Triad
import br.tiagohm.astrum.sky.deg
import br.tiagohm.astrum.sky.rad
import kotlin.math.*

data class Refraction(
    val pressure: Double = 1013.0,
    val temperature: Celsius = 15.0,
) {

    private val ptc = pressure / 1010.0 * 283.0 / (273.0 + temperature) / 60.0

    fun forward(pos: Triad): Triad {
        val length = pos.length

        if (length == 0.0) return pos

        val sinGeo = pos[2] / length
        var geomAlt = asin(sinGeo).deg

        if (geomAlt > MIN_GEO_ALTITUDE_DEG) {
            // Refraction from Saemundsson, S&T1986 p70 / in Meeus, Astr.Alg.
            val r = ptc * (1.02 / tan((geomAlt + 10.3 / (geomAlt + 5.11)).rad) + 0.0019279)

            geomAlt += r

            if (geomAlt > 90.0) geomAlt = 90.0
        } else if (geomAlt > MIN_GEO_ALTITUDE_DEG - TRANSITION_WIDTH_GEO_DEG) {
            // Avoids the jump below -5 by interpolating linearly between MIN_GEO_ALTITUDE_DEG and bottom of transition zone
            val rm5 =
                ptc * (1.02 / tan((MIN_GEO_ALTITUDE_DEG + 10.3 / (MIN_GEO_ALTITUDE_DEG + 5.11)).rad) + 0.0019279)

            geomAlt += rm5 * (geomAlt - (MIN_GEO_ALTITUDE_DEG - TRANSITION_WIDTH_GEO_DEG)) / TRANSITION_WIDTH_GEO_DEG
        } else {
            return pos
        }

        val sinRef = sin(geomAlt.rad)

        val s = (if (abs(sinGeo) >= 1.0) 1.0 else sqrt((1.0 - sinRef * sinRef) / (1.0 - sinGeo * sinGeo)))
        return Triad(pos[0] * s, pos[1] * s, sinRef * length)
    }

    fun backward(pos: Triad): Triad {
        val length = pos.length

        if (length == 0.0) return pos

        val sinObs = pos[2] / length
        var obsAlt = asin(sinObs).deg

        if (obsAlt > 0.22879) {
            // Refraction from Bennett, in Meeus, Astr.Alg.
            val r = ptc * (1.0 / tan((obsAlt + 7.31 / (obsAlt + 4.4)).rad) + 0.0013515)
            obsAlt -= r
        } else if (obsAlt > MIN_APP_ALTITUDE_DEG) {
            // Backward refraction from polynomial fit against Saemundson[-5...-0.3]
            val r = (((((0.0444 * obsAlt + 0.7662) * obsAlt + 4.9746) * obsAlt + 13.599) *
                    obsAlt + 8.052) * obsAlt - 11.308) * obsAlt + 34.341
            obsAlt -= ptc * r
        } else if (obsAlt > MIN_APP_ALTITUDE_DEG - TRANSITION_WIDTH_APP_DEG) {
            // Compute top value from polynome, apply linear interpolation
            val rMin = (((((0.0444 * MIN_APP_ALTITUDE_DEG + .7662) * MIN_APP_ALTITUDE_DEG
                    + 4.9746) * MIN_APP_ALTITUDE_DEG + 13.599) * MIN_APP_ALTITUDE_DEG
                    + 8.052) * MIN_APP_ALTITUDE_DEG - 11.308) * MIN_APP_ALTITUDE_DEG + 34.341

            obsAlt -= rMin * ptc * (obsAlt - (MIN_APP_ALTITUDE_DEG - TRANSITION_WIDTH_APP_DEG)) / TRANSITION_WIDTH_APP_DEG
        } else {
            return pos
        }

        val sinGeo = sin(obsAlt.rad)
        val s = if (abs(sinObs) >= 1.0) 1.0 else sqrt((1.0 - sinGeo * sinGeo) / (1.0 - sinObs * sinObs))

        return Triad(pos[0] * s, pos[1] * s, sinGeo * length)
    }

    companion object {

        private const val MIN_GEO_ALTITUDE_DEG = -3.54
        private const val MIN_APP_ALTITUDE_DEG = -3.21783
        private const val TRANSITION_WIDTH_GEO_DEG = 1.46
        private const val TRANSITION_WIDTH_APP_DEG = 1.78217
    }
}