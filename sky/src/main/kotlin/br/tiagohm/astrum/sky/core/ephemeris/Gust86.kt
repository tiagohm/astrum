package br.tiagohm.astrum.sky.core.ephemeris

import br.tiagohm.astrum.sky.M_2_PI
import br.tiagohm.astrum.sky.M_PI
import br.tiagohm.astrum.sky.core.Algorithms
import kotlin.math.cos
import kotlin.math.sin

/**
 * Computation of the coordinates of the Uranian satellites,
 * version 0.1 (1988,1995) by LASKAR J. and JACOBSON, R.
 * ftp://ftp.imcce.fr/pub/ephem/satel/gust86
 */
object Gust86 {

    fun computeCoordinates(jd: Double, body: Int): DoubleArray {
        return computeOsculatingCoordinates(jd, jd, body)
    }

    fun computeOsculatingCoordinates(jd0: Double, jd: Double, body: Int): DoubleArray {
        val t = jd0 - 2444239.5

        val ts = doubleArrayOf(t0, t1, t2)
        val es = arrayOf(elem0, elem1, elem2)

        Algorithms.computeInterpolatedElements(t, elem, DIM, ::computeGust86Elem, DELTA_T, ts, es)

        t0 = ts[0]
        t1 = ts[1]
        t2 = ts[2]

        val be = elem.sliceArray(body * 6 until body * 6 + 6)
        val x = Algorithms.ellipticToRectangularN(RMU[body], be, jd - jd0)

        return doubleArrayOf(
            GUST86_TO_VSOP87[0] * x[0] + GUST86_TO_VSOP87[1] * x[1] + GUST86_TO_VSOP87[2] * x[2],
            GUST86_TO_VSOP87[3] * x[0] + GUST86_TO_VSOP87[4] * x[1] + GUST86_TO_VSOP87[5] * x[2],
            GUST86_TO_VSOP87[6] * x[0] + GUST86_TO_VSOP87[7] * x[1] + GUST86_TO_VSOP87[8] * x[2],
            GUST86_TO_VSOP87[0] * x[3] + GUST86_TO_VSOP87[1] * x[4] + GUST86_TO_VSOP87[2] * x[5],
            GUST86_TO_VSOP87[3] * x[3] + GUST86_TO_VSOP87[4] * x[4] + GUST86_TO_VSOP87[5] * x[5],
            GUST86_TO_VSOP87[6] * x[3] + GUST86_TO_VSOP87[7] * x[4] + GUST86_TO_VSOP87[8] * x[5],
        )
    }

    private fun computeGust86Elem(t: Double, elem: DoubleArray) {
        val an = DoubleArray(5)
        val ae = DoubleArray(5)
        val ai = DoubleArray(5)

        for (i in 0..4) {
            an[i] = (FQN[i] * t + PHN[i]) % M_2_PI
            ae[i] = (FQE[i] * t + PHE[i]) % M_2_PI
            ai[i] = (FQI[i] * t + PHI[i]) % M_2_PI
        }

        elem[0 * 6 + 0] = 4.44352267 -
                cos(an[0] - an[1] * 3.0 + an[2] * 2.0) * 3.492E-5 +
                cos(an[0] * 2.0 - an[1] * 6.0 + an[2] * 4.0) * 8.47E-6 +
                cos(an[0] * 3.0 - an[1] * 9.0 + an[2] * 6.0) * 1.31E-6 -
                cos(an[0] - an[1]) * 5.228E-5 -
                cos(an[0] * 2.0 - an[1] * 2.0) * 1.3665E-4
        elem[0 * 6 + 1] = sin(an[0] - an[1] * 3.0 + an[2] * 2.0) * 0.02547217 -
                sin(an[0] * 2.0 - an[1] * 6.0 + an[2] * 4.0) * 0.00308831 -
                sin(an[0] * 3.0 - an[1] * 9.0 + an[2] * 6.0) * 3.181E-4 -
                sin(an[0] * 4.0 - an[1] * 12 + an[2] * 8.0) * 3.749E-5 -
                sin(an[0] - an[1]) * 5.785E-5 -
                sin(an[0] * 2.0 - an[1] * 2.0) * 6.232E-5 -
                sin(an[0] * 3.0 - an[1] * 3.0) * 2.795E-5 +
                t * 4.44519055 - 0.23805158
        elem[0 * 6 + 2] = cos(ae[0]) * 0.00131238 +
                cos(ae[1]) * 7.181E-5 +
                cos(ae[2]) * 6.977E-5 +
                cos(ae[3]) * 6.75E-6 +
                cos(ae[4]) * 6.27E-6 +
                cos(an[0]) * 1.941E-4 -
                cos(-an[0] + an[1] * 2.0) * 1.2331E-4 +
                cos(an[0] * -2.0 + an[1] * 3.0) * 3.952E-5
        elem[0 * 6 + 3] = sin(ae[0]) * 0.00131238 +
                sin(ae[1]) * 7.181E-5 +
                sin(ae[2]) * 6.977E-5 +
                sin(ae[3]) * 6.75E-6 +
                sin(ae[4]) * 6.27E-6 +
                sin(an[0]) * 1.941E-4 -
                sin(-an[0] + an[1] * 2.0) * 1.2331E-4 +
                sin(an[0] * -2.0 + an[1] * 3.0) * 3.952E-5
        elem[0 * 6 + 4] = cos(ai[0]) * 0.03787171 +
                cos(ai[1]) * 2.701E-5 +
                cos(ai[2]) * 3.076E-5 +
                cos(ai[3]) * 1.218E-5 +
                cos(ai[4]) * 5.37E-6
        elem[0 * 6 + 5] = sin(ai[0]) * 0.03787171 +
                sin(ai[1]) * 2.701E-5 +
                sin(ai[2]) * 3.076E-5 +
                sin(ai[3]) * 1.218E-5 +
                sin(ai[4]) * 5.37E-6

        elem[1 * 6 + 0] = 2.49254257 +
                cos(an[0] - an[1] * 3.0 + an[2] * 2.0) * 2.55E-6 -
                cos(an[1] - an[2]) * 4.216E-5 -
                cos(an[1] * 2.0 - an[2] * 2.0) * 1.0256E-4
        elem[1 * 6 + 1] = -sin(an[0] - an[1] * 3.0 + an[2] * 2.0) * 0.0018605 +
                sin(an[0] * 2.0 - an[1] * 6.0 + an[2] * 4.0) * 2.1999E-4 +
                sin(an[0] * 3.0 - an[1] * 9.0 + an[2] * 6.0) * 2.31E-5 +
                sin(an[0] * 4.0 - an[1] * 12 + an[2] * 8.0) * 4.3E-6 -
                sin(an[1] - an[2]) * 9.011E-5 -
                sin(an[1] * 2.0 - an[2] * 2.0) * 9.107E-5 -
                sin(an[1] * 3.0 - an[2] * 3.0) * 4.275E-5 -
                sin(an[1] * 2.0 - an[3] * 2.0) * 1.649E-5 +
                t * 2.49295252 + 3.09804641
        elem[1 * 6 + 2] = cos(ae[0]) * -3.35E-6 +
                cos(ae[1]) * 0.00118763 +
                cos(ae[2]) * 8.6159E-4 +
                cos(ae[3]) * 7.15E-5 +
                cos(ae[4]) * 5.559E-5 -
                cos(-an[1] + an[2] * 2.0) * 8.46E-5 +
                cos(an[1] * -2.0 + an[2] * 3.0) * 9.181E-5 +
                cos(-an[1] + an[3] * 2.0) * 2.003E-5 +
                cos(an[1]) * 8.977E-5
        elem[1 * 6 + 3] = sin(ae[0]) * -3.35E-6 +
                sin(ae[1]) * 0.00118763 +
                sin(ae[2]) * 8.6159E-4 +
                sin(ae[3]) * 7.15E-5 +
                sin(ae[4]) * 5.559E-5 -
                sin(-an[1] + an[2] * 2.0) * 8.46E-5 +
                sin(an[1] * -2.0 + an[2] * 3.0) * 9.181E-5 +
                sin(-an[1] + an[3] * 2.0) * 2.003E-5 +
                sin(an[1]) * 8.977E-5
        elem[1 * 6 + 4] = cos(ai[0]) * -1.2175E-4 +
                cos(ai[1]) * 3.5825E-4 + cos(ai[2]) * 2.9008E-4 +
                cos(ai[3]) * 9.778E-5 + cos(ai[4]) * 3.397E-5
        elem[1 * 6 + 5] = sin(ai[0]) * -1.2175E-4 +
                sin(ai[1]) * 3.5825E-4 + sin(ai[2]) * 2.9008E-4 +
                sin(ai[3]) * 9.778E-5 + sin(ai[4]) * 3.397E-5

        elem[2 * 6 + 0] = (1.5159549
                + cos(an[2] - an[3] * 2.0 + ae[2]) * 9.74E-6
                - cos(an[1] - an[2]) * 1.06E-4
                + cos(an[1] * 2.0 - an[2] * 2.0) * 5.416E-5) -
                cos(an[2] - an[3]) * 2.359E-5 -
                cos(an[2] * 2.0 - an[3] * 2.0) * 7.07E-5 -
                cos(an[2] * 3.0 - an[3] * 3.0) * 3.628E-5
        elem[2 * 6 + 1] = sin(an[0] - an[1] * 3.0 + an[2] * 2.0) * 6.6057E-4 -
                sin(an[0] * 2.0 - an[1] * 6.0 + an[2] * 4.0) * 7.651E-5 -
                sin(an[0] * 3.0 - an[1] * 9.0 + an[2] * 6.0) * 8.96E-6 -
                sin(an[0] * 4.0 - an[1] * 12.0 + an[2] * 8.0) * 2.53E-6 -
                sin(an[2] - an[3] * 4.0 + an[4] * 3.0) * 5.291E-5 -
                sin(an[2] - an[3] * 2.0 + ae[4]) * 7.34E-6 -
                sin(an[2] - an[3] * 2.0 + ae[3]) * 1.83E-6 +
                sin(an[2] - an[3] * 2.0 + ae[2]) * 1.4791E-4 +
                sin(an[2] - an[3] * 2.0 + ae[1]) * -7.77E-6 +
                sin(an[1] - an[2]) * 9.776E-5 +
                sin(an[1] * 2.0 - an[2] * 2.0) * 7.313E-5 +
                sin(an[1] * 3.0 - an[2] * 3.0) * 3.471E-5 +
                sin(an[1] * 4.0 - an[2] * 4.0) * 1.889E-5 -
                sin(an[2] - an[3]) * 6.789E-5 -
                sin(an[2] * 2.0 - an[3] * 2.0) * 8.286E-5 +
                sin(an[2] * 3.0 - an[3] * 3.0) * -3.381E-5 -
                sin(an[2] * 4.0 - an[3] * 4.0) * 1.579E-5 -
                sin(an[2] - an[4]) * 1.021E-5 -
                sin(an[2] * 2.0 - an[4] * 2.0) * 1.708E-5 +
                t * 1.51614811 + 2.28540169
        elem[2 * 6 + 2] = cos(ae[0]) * -2.1E-7 -
                cos(ae[1]) * 2.2795E-4 +
                cos(ae[2]) * 0.00390469 +
                cos(ae[3]) * 3.0917E-4 +
                cos(ae[4]) * 2.2192E-4 +
                cos(an[1]) * 2.934E-5 +
                cos(an[2]) * 2.62E-5 +
                cos(-an[1] + an[2] * 2.0) * 5.119E-5 -
                cos(an[1] * -2.0 + an[2] * 3.0) * 1.0386E-4 -
                cos(an[1] * -3.0 + an[2] * 4.0) * 2.716E-5 +
                cos(an[3]) * -1.622E-5 +
                cos(-an[2] + an[3] * 2.0) * 5.4923E-4 +
                cos(an[2] * -2.0 + an[3] * 3.0) * 3.47E-5 +
                cos(an[2] * -3.0 + an[3] * 4.0) * 1.281E-5 +
                cos(-an[2] + an[4] * 2.0) * 2.181E-5 +
                cos(an[2]) * 4.625E-5
        elem[2 * 6 + 3] = sin(ae[0]) * -2.1E-7 -
                sin(ae[1]) * 2.2795E-4 +
                sin(ae[2]) * 0.00390469 +
                sin(ae[3]) * 3.0917E-4 +
                sin(ae[4]) * 2.2192E-4 +
                sin(an[1]) * 2.934E-5 +
                sin(an[2]) * 2.62E-5 +
                sin(-an[1] + an[2] * 2.0) * 5.119E-5 -
                sin(an[1] * -2.0 + an[2] * 3.0) * 1.0386E-4 -
                sin(an[1] * -3.0 + an[2] * 4.0) * 2.716E-5 +
                sin(an[3]) * -1.622E-5 +
                sin(-an[2] + an[3] * 2.0) * 5.4923E-4 +
                sin(an[2] * -2.0 + an[3] * 3.0) * 3.47E-5 +
                sin(an[2] * -3.0 + an[3] * 4.0) * 1.281E-5 +
                sin(-an[2] + an[4] * 2.0) * 2.181E-5 +
                sin(an[2]) * 4.625E-5
        elem[2 * 6 + 4] = cos(ai[0]) * -1.086E-5 -
                cos(ai[1]) * 8.151E-5 +
                cos(ai[2]) * 0.00111336 +
                cos(ai[3]) * 3.5014E-4 +
                cos(ai[4]) * 1.065E-4
        elem[2 * 6 + 5] = sin(ai[0]) * -1.086E-5 -
                sin(ai[1]) * 8.151E-5 +
                sin(ai[2]) * 0.00111336 +
                sin(ai[3]) * 3.5014E-4 +
                sin(ai[4]) * 1.065E-4

        elem[3 * 6 + 0] = 0.72166316 -
                cos(an[2] - an[3] * 2.0 + ae[2]) * 2.64E-6 -
                cos(an[3] * 2.0 - an[4] * 3.0 + ae[4]) * 2.16E-6 +
                cos(an[3] * 2.0 - an[4] * 3.0 + ae[3]) * 6.45E-6 -
                cos(an[3] * 2.0 - an[4] * 3.0 + ae[2]) * 1.11E-6 +
                cos(an[1] - an[3]) * -6.223E-5 -
                cos(an[2] - an[3]) * 5.613E-5 -
                cos(an[3] - an[4]) * 3.994E-5 -
                cos(an[3] * 2.0 - an[4] * 2.0) * 9.185E-5 -
                cos(an[3] * 3.0 - an[4] * 3.0) * 5.831E-5 -
                cos(an[3] * 4.0 - an[4] * 4.0) * 3.86E-5 -
                cos(an[3] * 5.0 - an[4] * 5.0) * 2.618E-5 -
                cos(an[3] * 6.0 - an[4] * 6.0) * 1.806E-5
        elem[3 * 6 + 1] = sin(an[2] - an[3] * 4.0 + an[4] * 3.0) * 2.061E-5 -
                sin(an[2] - an[3] * 2.0 + ae[4]) * 2.07E-6 -
                sin(an[2] - an[3] * 2.0 + ae[3]) * 2.88E-6 -
                sin(an[2] - an[3] * 2.0 + ae[2]) * 4.079E-5 +
                sin(an[2] - an[3] * 2.0 + ae[1]) * 2.11E-6 -
                sin(an[3] * 2.0 - an[4] * 3.0 + ae[4]) * 5.183E-5 +
                sin(an[3] * 2.0 - an[4] * 3.0 + ae[3]) * 1.5987E-4 +
                sin(an[3] * 2.0 - an[4] * 3.0 + ae[2]) * -3.505E-5 -
                sin(an[3] * 3.0 - an[4] * 4.0 + ae[4]) * 1.56E-6 +
                sin(an[1] - an[3]) * 4.054E-5 +
                sin(an[2] - an[3]) * 4.617E-5 -
                sin(an[3] - an[4]) * 3.1776E-4 -
                sin(an[3] * 2.0 - an[4] * 2.0) * 3.0559E-4 -
                sin(an[3] * 3.0 - an[4] * 3.0) * 1.4836E-4 -
                sin(an[3] * 4.0 - an[4] * 4.0) * 8.292E-5 +
                sin(an[3] * 5.0 - an[4] * 5.0) * -4.998E-5 -
                sin(an[3] * 6.0 - an[4] * 6.0) * 3.156E-5 -
                sin(an[3] * 7.0 - an[4] * 7.0) * 2.056E-5 -
                sin(an[3] * 8.0 - an[4] * 8.0) * 1.369E-5 +
                t * 0.72171851 + 0.85635879
        elem[3 * 6 + 2] = cos(ae[0]) * -2E-8 -
                cos(ae[1]) * 1.29E-6 -
                cos(ae[2]) * 3.2451E-4 +
                cos(ae[3]) * 9.3281E-4 +
                cos(ae[4]) * 0.00112089 +
                cos(an[1]) * 3.386E-5 +
                cos(an[3]) * 1.746E-5 +
                cos(-an[1] + an[3] * 2.0) * 1.658E-5 +
                cos(an[2]) * 2.889E-5 -
                cos(-an[2] + an[3] * 2.0) * 3.586E-5 +
                cos(an[3]) * -1.786E-5 -
                cos(an[4]) * 3.21E-5 -
                cos(-an[3] + an[4] * 2.0) * 1.7783E-4 +
                cos(an[3] * -2.0 + an[4] * 3.0) * 7.9343E-4 +
                cos(an[3] * -3.0 + an[4] * 4.0) * 9.948E-5 +
                cos(an[3] * -4.0 + an[4] * 5.0) * 4.483E-5 +
                cos(an[3] * -5.0 + an[4] * 6.0) * 2.513E-5 +
                cos(an[3] * -6.0 + an[4] * 7.0) * 1.543E-5
        elem[3 * 6 + 3] = sin(ae[0]) * -2E-8 -
                sin(ae[1]) * 1.29E-6 -
                sin(ae[2]) * 3.2451E-4 +
                sin(ae[3]) * 9.3281E-4 +
                sin(ae[4]) * 0.00112089 +
                sin(an[1]) * 3.386E-5 +
                sin(an[3]) * 1.746E-5 +
                sin(-an[1] + an[3] * 2.0) * 1.658E-5 +
                sin(an[2]) * 2.889E-5 -
                sin(-an[2] + an[3] * 2.0) * 3.586E-5 +
                sin(an[3]) * -1.786E-5 -
                sin(an[4]) * 3.21E-5 -
                sin(-an[3] + an[4] * 2.0) * 1.7783E-4 +
                sin(an[3] * -2.0 + an[4] * 3.0) * 7.9343E-4 +
                sin(an[3] * -3.0 + an[4] * 4.0) * 9.948E-5 +
                sin(an[3] * -4.0 + an[4] * 5.0) * 4.483E-5 +
                sin(an[3] * -5.0 + an[4] * 6.0) * 2.513E-5 +
                sin(an[3] * -6.0 + an[4] * 7.0) * 1.543E-5
        elem[3 * 6 + 4] = cos(ai[0]) * -1.43E-6 -
                cos(ai[1]) * 1.06E-6 -
                cos(ai[2]) * 1.4013E-4 +
                cos(ai[3]) * 6.8572E-4 +
                cos(ai[4]) * 3.7832E-4
        elem[3 * 6 + 5] = sin(ai[0]) * -1.43E-6 -
                sin(ai[1]) * 1.06E-6 -
                sin(ai[2]) * 1.4013E-4 +
                sin(ai[3]) * 6.8572E-4 +
                sin(ai[4]) * 3.7832E-4

        elem[4 * 6 + 0] = 0.46658054 +
                cos(an[3] * 2.0 - an[4] * 3.0 + ae[4]) * 2.08E-6 -
                cos(an[3] * 2.0 - an[4] * 3.0 + ae[3]) * 6.22E-6 +
                cos(an[3] * 2.0 - an[4] * 3.0 + ae[2]) * 1.07E-6 -
                cos(an[1] - an[4]) * 4.31E-5 +
                cos(an[2] - an[4]) * -3.894E-5 -
                cos(an[3] - an[4]) * 8.011E-5 +
                cos(an[3] * 2.0 - an[4] * 2.0) * 5.906E-5 +
                cos(an[3] * 3.0 - an[4] * 3.0) * 3.749E-5 +
                cos(an[3] * 4.0 - an[4] * 4.0) * 2.482E-5 +
                cos(an[3] * 5.0 - an[4] * 5.0) * 1.684E-5
        elem[4 * 6 + 1] = -sin(an[2] - an[3] * 4.0 + an[4] * 3.0) * 7.82E-6 +
                sin(an[3] * 2.0 - an[4] * 3.0 + ae[4]) * 5.129E-5 -
                sin(an[3] * 2.0 - an[4] * 3.0 + ae[3]) * 1.5824E-4 +
                sin(an[3] * 2.0 - an[4] * 3.0 + ae[2]) * 3.451E-5 +
                sin(an[1] - an[4]) * 4.751E-5 +
                sin(an[2] - an[4]) * 3.896E-5 +
                sin(an[3] - an[4]) * 3.5973E-4 +
                sin(an[3] * 2.0 - an[4] * 2.0) * 2.8278E-4 +
                sin(an[3] * 3.0 - an[4] * 3.0) * 1.386E-4 +
                sin(an[3] * 4.0 - an[4] * 4.0) * 7.803E-5 +
                sin(an[3] * 5.0 - an[4] * 5.0) * 4.729E-5 +
                sin(an[3] * 6.0 - an[4] * 6.0) * 3E-5 +
                sin(an[3] * 7.0 - an[4] * 7.0) * 1.962E-5 +
                sin(an[3] * 8.0 - an[4] * 8.0) * 1.311E-5 +
                t * 0.46669212 - 0.9155918
        elem[4 * 6 + 2] = cos(ae[1]) * -3.5E-7 +
                cos(ae[2]) * 7.453E-5 -
                cos(ae[3]) * 7.5868E-4 +
                cos(ae[4]) * 0.00139734 +
                cos(an[1]) * 3.9E-5 +
                cos(-an[1] + an[4] * 2.0) * 1.766E-5 +
                cos(an[2]) * 3.242E-5 +
                cos(an[3]) * 7.975E-5 +
                cos(an[4]) * 7.566E-5 +
                cos(-an[3] + an[4] * 2.0) * 1.3404E-4 -
                cos(an[3] * -2.0 + an[4] * 3.0) * 9.8726E-4 -
                cos(an[3] * -3.0 + an[4] * 4.0) * 1.2609E-4 -
                cos(an[3] * -4.0 + an[4] * 5.0) * 5.742E-5 -
                cos(an[3] * -5.0 + an[4] * 6.0) * 3.241E-5 -
                cos(an[3] * -6.0 + an[4] * 7.0) * 1.999E-5 -
                cos(an[3] * -7.0 + an[4] * 8.0) * 1.294E-5
        elem[4 * 6 + 3] = sin(ae[1]) * -3.5E-7 +
                sin(ae[2]) * 7.453E-5 -
                sin(ae[3]) * 7.5868E-4 +
                sin(ae[4]) * 0.00139734 +
                sin(an[1]) * 3.9E-5 +
                sin(-an[1] + an[4] * 2.0) * 1.766E-5 +
                sin(an[2]) * 3.242E-5 +
                sin(an[3]) * 7.975E-5 +
                sin(an[4]) * 7.566E-5 +
                sin(-an[3] + an[4] * 2.0) * 1.3404E-4 -
                sin(an[3] * -2.0 + an[4] * 3.0) * 9.8726E-4 -
                sin(an[3] * -3.0 + an[4] * 4.0) * 1.2609E-4 -
                sin(an[3] * -4.0 + an[4] * 5.0) * 5.742E-5 -
                sin(an[3] * -5.0 + an[4] * 6.0) * 3.241E-5 -
                sin(an[3] * -6.0 + an[4] * 7.0) * 1.999E-5 -
                sin(an[3] * -7.0 + an[4] * 8.0) * 1.294E-5
        elem[4 * 6 + 4] = cos(ai[0]) * -4.4E-7 -
                cos(ai[1]) * 3.1E-7 +
                cos(ai[2]) * 3.689E-5 -
                cos(ai[3]) * 5.9633E-4 +
                cos(ai[4]) * 4.5169E-4
        elem[4 * 6 + 5] = sin(ai[0]) * -4.4E-7 -
                sin(ai[1]) * 3.1E-7 +
                sin(ai[2]) * 3.689E-5 -
                sin(ai[3]) * 5.9633E-4 +
                sin(ai[4]) * 4.5169E-4
    }

    private const val DIM = 5 * 6
    private const val DELTA_T = 1.0 // 1 day

    private var t0 = -1E100
    private var t1 = -1E100
    private var t2 = -1E100

    private val elem = DoubleArray(DIM)
    private val elem0 = DoubleArray(DIM)
    private val elem1 = DoubleArray(DIM)
    private val elem2 = DoubleArray(DIM)

    private val GUST86_TO_VSOP87 = doubleArrayOf(
        9.753206632086812015E-01, 6.194425668001473004E-02, 2.119257251551559653E-01,
        -2.006444610981783542E-01, -1.519328516640849367E-01, 9.678110398294910731E-01,
        9.214881523275189928E-02, -9.864478281437795399E-01, -1.357544776485127136E-01
    )

    private val FQN = doubleArrayOf(
        4.44519055,
        2.492952519,
        1.516148111,
        0.721718509,
        0.46669212,
    )

    private val FQE = doubleArrayOf(
        20.082 * M_PI / (180 * 365.25),
        6.217 * M_PI / (180 * 365.25),
        2.865 * M_PI / (180 * 365.25),
        2.078 * M_PI / (180 * 365.25),
        0.386 * M_PI / (180 * 365.25)
    )

    private val FQI = doubleArrayOf(
        -20.309 * M_PI / (180 * 365.25),
        -6.288 * M_PI / (180 * 365.25),
        -2.836 * M_PI / (180 * 365.25),
        -1.843 * M_PI / (180 * 365.25),
        -0.259 * M_PI / (180 * 365.25)
    )

    private val PHN = doubleArrayOf(
        -0.238051,
        3.098046,
        2.285402,
        0.856359,
        -0.915592
    )

    private val PHE = doubleArrayOf(
        0.611392,
        2.408974,
        2.067774,
        0.735131,
        0.426767
    )

    private val PHI = doubleArrayOf(
        5.702313,
        0.395757,
        0.589326,
        1.746237,
        4.206896
    )

    private val RMU = doubleArrayOf(
        1.291892353675174E-08,
        1.291910570526396E-08,
        1.291910102284198E-08,
        1.291942656265575E-08,
        1.291935967091320E-08
    )
}