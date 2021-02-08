package br.tiagohm.astrum.core

import br.tiagohm.astrum.core.math.Duad
import br.tiagohm.astrum.core.math.Triad
import java.lang.Math.cbrt
import kotlin.math.*

object Algorithms {

    /**
     * Given the orbital elements at some time t0 calculate the
     * rectangular coordinates at time (t0+dt).
     *
     * mu = G*(m1+m2) .. gravitational constant of the two body problem
     * @param a Semi major axis
     * @param n Mean motion = 2*M_PI/(orbit period)
     */
    fun ellipticToRectangular(a: Double, n: Double, elem: DoubleArray, dt: Double): DoubleArray {
        val L = (elem[1] + n * dt) % Consts.M_2_PI
        var Le = L - elem[2] * sin(L) + elem[3] * cos(L)

        while (true) {
            val cLe = cos(Le)
            val sLe = sin(Le)

            // For excentricity < 1 we have denominator > 0
            val dLe = (L - Le + elem[2] * sLe - elem[3] * cLe) / (1.0 - elem[2] * cLe - elem[3] * sLe)
            Le += dLe

            if (abs(dLe) <= Consts.EPSILON) break
        }

        val cLe = cos(Le)
        val sLe = sin(Le)

        val dlf = -elem[2] * sLe + elem[3] * cLe
        val phi = sqrt(1.0 - elem[2] * elem[2] - elem[3] * elem[3])
        val psi = 1.0 / (1.0 + phi)

        val x1 = a * (cLe - elem[2] - psi * dlf * elem[3])
        val y1 = a * (sLe - elem[3] + psi * dlf * elem[2])

        val elem4q = elem[4] * elem[4] // Q²
        val elem5q = elem[5] * elem[5] // P²
        val dwho = 2.0 * sqrt(1.0 - elem4q - elem5q)
        val rtp = 1.0 - elem5q - elem5q
        val rtq = 1.0 - elem4q - elem4q
        val rdg = 2.0 * elem[5] * elem[4]

        val xyz = DoubleArray(6)

        xyz[0] = x1 * rtp + y1 * rdg
        xyz[1] = x1 * rdg + y1 * rtq
        xyz[2] = (-x1 * elem[5] + y1 * elem[4]) * dwho

        val rsam1 = -elem[2] * cLe - elem[3] * sLe
        val h = a * n / (1.0 + rsam1)
        val vx1 = h * (-sLe - psi * rsam1 * elem[3])
        val vy1 = h * (cLe + psi * rsam1 * elem[2])

        xyz[3] = vx1 * rtp + vy1 * rdg
        xyz[4] = vx1 * rdg + vy1 * rtq
        xyz[5] = (-vx1 * elem[5] + vy1 * elem[4]) * dwho

        return xyz
    }

    fun ellipticToRectangularA(mu: Double, elem: DoubleArray, dt: Double): DoubleArray {
        val a = elem[0]
        val n = sqrt(mu / (a * a * a)) // Mean motion
        return ellipticToRectangular(a, n, elem, dt)
    }

    fun ellipticToRectangularN(mu: Double, elem: DoubleArray, dt: Double): DoubleArray {
        val n = elem[0]
        val a = cbrt(mu / (n * n))
        return ellipticToRectangular(a, n, elem, dt)
    }

    fun rectangularToSphericalCoordinates(a: Triad): Duad {
        val r = a.length
        return Duad(atan2(a[1], a[0]), asin(a[2] / r))
    }

    fun sphericalToRectangularCoordinates(lon: Double, lat: Double): Triad {
        val cosLat = cos(lat)
        return Triad(cos(lon) * cosLat, sin(lon) * cosLat, sin(lat))
    }

    fun equatorialToEcliptic(ra: Double, dec: Double, ecl: Double): Duad {
        val lambda = atan2(sin(ra) * cos(ecl) + tan(dec) * sin(ecl), cos(ra))
        val beta = asin(sin(dec) * cos(ecl) - cos(dec) * sin(ecl) * sin(ra))
        return Duad(lambda, beta)
    }

    fun eclipticToEquatorial(lambda: Double, beta: Double, ecl: Double): Duad {
        val ra = atan2(sin(lambda) * cos(ecl) - tan(beta) * sin(ecl), cos(lambda))
        val dec = asin(sin(beta) * cos(ecl) + cos(beta) * sin(ecl) * sin(lambda))
        return Duad(ra, dec)
    }

    fun computeInterpolatedElements(
        t: Double,
        elem: DoubleArray,
        dim: Int,
        computer: (Double, DoubleArray) -> Unit,
        deltaT: Double,
        ts: DoubleArray,
        es: Array<DoubleArray>,
    ) {
        if (ts[1] < -1E99) {
            ts[0] = -1E100
            ts[2] = -1E100
            ts[1] = t
            computer(ts[1], es[1])
            for (i in 0 until dim) elem[i] = es[1][i]
            return
        }

        if (t <= ts[1]) {
            if (ts[1] - deltaT <= t) {
                if (ts[0] < -1E99) {
                    ts[0] = ts[1] - deltaT
                    computer(ts[0], es[0])
                }
            } else if (ts[1] - 2.0 * deltaT <= t) {
                if (ts[0] < -1E99) {
                    ts[0] = ts[1] - deltaT
                    computer(ts[0], es[0])
                }

                ts[2] = ts[1];ts[1] = ts[0]

                for (i in 0 until dim) {
                    es[2][i] = es[1][i]
                    es[1][i] = es[0][i]
                }

                ts[0] = ts[1] - deltaT

                computer(ts[0], es[0])
            } else {
                ts[0] = -1E100
                ts[2] = -1E100
                ts[1] = t

                computer(ts[1], es[1])

                for (i in 0 until dim) elem[i] = es[1][i]

                return
            }

            val f0 = (ts[1] - t)
            val f1 = (t - ts[0])
            val fact = 1.0 / deltaT

            for (i in 0 until dim) elem[i] = fact * (es[0][i] * f0 + es[1][i] * f1)
        } else {
            if (ts[1] + deltaT >= t) {
                if (ts[2] < -1E99) {
                    ts[2] = ts[1] + deltaT
                    computer(ts[2], es[2])
                }
            } else if (ts[1] + 2.0 * deltaT >= t) {
                if (ts[2] < -1E99) {
                    ts[2] = ts[1] + deltaT
                    computer(ts[2], es[2])
                }

                ts[0] = ts[1]
                ts[1] = ts[2]

                for (i in 0 until dim) {
                    es[0][i] = es[1][i]
                    es[1][i] = es[2][i]
                }

                ts[2] = ts[1] + deltaT

                computer(ts[2], es[2])
            } else {
                ts[0] = -1E100
                ts[2] = -1E100
                ts[1] = t

                computer(ts[1], es[1])

                for (i in 0 until dim) elem[i] = es[1][i]

                return
            }

            val f1 = (ts[2] - t)
            val f2 = (t - ts[1])
            val fact = 1.0 / deltaT

            for (i in 0 until dim) elem[i] = fact * (es[1][i] * f1 + es[2][i] * f2)
        }
    }
}