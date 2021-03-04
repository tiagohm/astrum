package br.tiagohm.astrum.sky.core

import br.tiagohm.astrum.sky.*
import br.tiagohm.astrum.sky.core.coordinates.GeographicCoord
import br.tiagohm.astrum.sky.core.coordinates.HorizontalCoord
import br.tiagohm.astrum.sky.core.coordinates.SphericalCoord
import br.tiagohm.astrum.sky.core.math.Triad
import br.tiagohm.astrum.sky.core.math.cos
import br.tiagohm.astrum.sky.core.math.sin
import br.tiagohm.astrum.sky.core.math.tan
import br.tiagohm.astrum.sky.core.units.angle.Angle
import br.tiagohm.astrum.sky.core.units.angle.Radians
import br.tiagohm.astrum.sky.planets.Planet
import java.lang.Math.cbrt
import kotlin.math.*

@Suppress("NOTHING_TO_INLINE")
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
        val L = (elem[1] + n * dt) % M_2_PI
        var Le = L - elem[2] * sin(L) + elem[3] * cos(L)

        while (true) {
            val cLe = cos(Le)
            val sLe = sin(Le)

            // For excentricity < 1 we have denominator > 0
            val dLe = (L - Le + elem[2] * sLe - elem[3] * cLe) / (1.0 - elem[2] * cLe - elem[3] * sLe)
            Le += dLe

            if (abs(dLe) <= EPSILON) break
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

    inline fun rectangularToSphericalCoordinates(a: Triad) = SphericalCoord(a.longitude, a.latitude)

    fun sphericalToRectangularCoordinates(longitude: Angle, latitude: Angle): Triad {
        return cos(latitude).let { Triad(cos(longitude) * it, sin(longitude) * it, sin(latitude)) }
    }

    inline fun j2000ToGalactic(a: Triad) = MAT_J2000_TO_GALACTIC * a

    inline fun j2000ToSupergalactic(a: Triad) = MAT_J2000_TO_SUPERGALACTIC * a

    inline fun j2000ToJ1875(a: Triad) = MAT_J2000_TO_J1875 * a

    fun distanceKm(planet: Planet, a: GeographicCoord, b: GeographicCoord): Double {
        val f = planet.oblateness // Flattening
        val radius = planet.radius.kilometer.value

        val F = ((a.latitude + b.latitude).radians.value * 0.5) // Latitude
        val G = ((a.latitude - b.latitude).radians.value * 0.5)
        val L = ((a.longitude - b.longitude).radians.value * 0.5) // Longitude

        val sinG = sin(G)
        val cosG = cos(G)
        val sinF = sin(F)
        val cosF = cos(F)
        val sinL = sin(L)
        val cosL = cos(L)

        val S = sinG * sinG * cosL * cosL + cosF * cosF * sinL * sinL
        val C = cosG * cosG * cosL * cosL + sinF * sinF * sinL * sinL
        val om = atan(sqrt(S / C))
        val R = 3.0 * sqrt(S * C) / om
        val D = 2.0 * om * radius
        val H1 = (R - 1.0) / (2.0 * C)
        val H2 = (R + 1.0) / (2.0 * S)

        return D * (1.0 + f * (H1 * sinF * sinF * cosG * cosG - H2 * cosF * cosF * sinG * sinG))
    }

    fun azimuth(from: HorizontalCoord, to: HorizontalCoord, southAzimuth: Boolean = false): Radians {
        val (a, b) = from
        val (c, d) = to

        val az = atan2(sin(c - a), cos(b) * tan(d) - sin(b) * cos(c - a)) + if (southAzimuth) M_PI else 0.0
        return Radians(az).normalized
    }

    fun azimuth(from: GeographicCoord, to: GeographicCoord, southAzimuth: Boolean = false): Radians {
        val (a, b) = from
        val (c, d) = to

        val az = atan2(sin(c - a), cos(b) * tan(d) - sin(b) * cos(c - a)) + if (southAzimuth) M_PI else 0.0
        return Radians(az).normalized
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