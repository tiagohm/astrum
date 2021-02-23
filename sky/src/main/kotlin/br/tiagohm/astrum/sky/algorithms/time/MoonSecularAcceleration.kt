package br.tiagohm.astrum.sky.algorithms.time

import kotlin.math.abs

object MoonSecularAcceleration {

    /**
     * Computes the Secular Acceleration estimation for a given year.
     * Method described is here: http://eclipse.gsfc.nasa.gov/SEcat5/secular.html
     * For adapting from -26 to -25.858, use -0.91072 * (-25.858 + 26.0) = -0.12932224
     * For adapting from -26 to -23.895, use -0.91072 * (-23.895 + 26.0) = -1.9170656
     * @param jd the JD
     * @param nd value of n-dot (secular acceleration of the Moon) which should be used in the lunar ephemeris instead of the default values.
     * @param useDE43x true if function should adapt calculation of the secular acceleration of the Moon to the DE43x ephemeris
     */
    fun compute(jd: Double, nd: Double, useDE43x: Boolean = false): Double {
        val (year, month, day) = DateTime.fromJulianDay(jd)
        val t = (DateTime.yearAsFraction(year, month, day) - 1955.5) / 100.0

        // n.dot for secular acceleration of the Moon in ELP2000-82B
        // have value -23.8946 "/cy/cy (or -25.8 for DE43x usage)
        val ephND = if (useDE43x) -25.8 else -23.8946

        return -0.91072 * (ephND + abs(nd)) * t * t
    }
}