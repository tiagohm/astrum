package br.tiagohm.astrum.sky.core.orbit

import br.tiagohm.astrum.sky.*
import br.tiagohm.astrum.sky.core.math.Triad
import br.tiagohm.astrum.sky.core.math.cos
import br.tiagohm.astrum.sky.core.math.sin
import br.tiagohm.astrum.sky.core.units.angle.Angle
import br.tiagohm.astrum.sky.core.units.angle.Radians
import br.tiagohm.astrum.sky.core.units.distance.AU
import br.tiagohm.astrum.sky.core.units.distance.Distance
import java.lang.Math.cbrt
import kotlin.math.*

/**
 * Keplerian Orbit.
 *
 * @param q Pericenter distance
 * @param e Eccentricity
 * @param i Inclination
 * @param omega Longitude of ascending node
 * @param w Argument of perihelion
 * @param t0 Time at perihelion (JDE)
 * @param n Mean motion
 */
data class KeplerOrbit(
    val q: Distance, // Pericenter Distance (AU)
    override val e: Double, // Eccentricity
    val i: Angle, // Inclination
    val omega: Angle, // Longitude of ascending node
    val w: Angle, // Argument of perihelion
    val t0: Double, // Time at perihelion (JDE)
    val n: Angle, // Mean motion (for parabolic orbits: W/dt in Heafner's presentation, ch5.5) [radians/day]
    val parentRotObliquity: Angle = Radians.ZERO, // Comets/Minor Planets only have parent==sun, no need for these? Oh yes: Double, VSOP/J2000 eq frames!
    val parentRotAscendingnode: Angle = Radians.ZERO,
    val parentRotJ2000Longitude: Angle = computeRotJ2000Longitude(parentRotObliquity, parentRotAscendingnode),
    val centralMass: Double = 1.0, // Mass in Solar masses. Velocity depends on this.
) : Orbit {

    private val rotateToVsop87 = DoubleArray(9)

    private val initOrbit: (KeplerOrbit, Double) -> Pair<Double, Double> = when {
        e < 1.0 -> ::initEllipticalOrbit
        e > 1.0 -> ::initHyperbolicOrbit
        else -> ::initParabolicOrbit
    }

    init {
        setParentOrientation(parentRotObliquity, parentRotAscendingnode, parentRotJ2000Longitude)
    }

    override val semiMajorAxis: Distance = if (e == 1.0) AU.ZERO else AU(q.au.value / (1.0 - e))

    override val siderealPeriod = computeSiderealPeriod(semiMajorAxis, centralMass)

    fun setParentOrientation(
        parentRotObliquity: Angle,
        parentRotAscendingNode: Angle,
        parentRotJ2000Longitude: Angle
    ) {
        val cobl = cos(parentRotObliquity)
        val sobl = sin(parentRotObliquity)
        val cnod = cos(parentRotAscendingNode)
        val snod = sin(parentRotAscendingNode)
        val cj = cos(parentRotJ2000Longitude)
        val sj = sin(parentRotJ2000Longitude)

        rotateToVsop87[0] = cnod * cj - snod * cobl * sj
        rotateToVsop87[1] = -cnod * sj - snod * cobl * cj
        rotateToVsop87[2] = snod * sobl
        rotateToVsop87[3] = snod * cj + cnod * cobl * sj
        rotateToVsop87[4] = -snod * sj + cnod * cobl * cj
        rotateToVsop87[5] = -cnod * sobl
        rotateToVsop87[6] = sobl * sj
        rotateToVsop87[7] = sobl * cj
        rotateToVsop87[8] = cobl
    }

    override fun positionAndVelocityAtTimevInVSOP87Coordinates(jde: Double): Pair<Triad, Triad> {
        // Laguerre-Conway seems stable enough to go for <1.0
        val (rCosNu, rSinNu) = initOrbit(this, jde - t0)

        val cw = cos(w)
        val sw = sin(w)
        val cOm = cos(omega)
        val sOm = sin(omega)
        val ci = cos(i)
        val si = sin(i)

        val Px = -sw * sOm * ci + cw * cOm // Heafner, 5.3.1 Px
        val Qx = -cw * sOm * ci - sw * cOm // Heafner, 5.3.4 Qx
        val Py = sw * cOm * ci + cw * sOm // Heafner, 5.3.2 Py
        val Qy = cw * cOm * ci - sw * sOm // Heafner, 5.3.5 Qy
        val Pz = sw * si            // Heafner, 5.3.3 Pz
        val Qz = cw * si            // Heafner, 5.3.6 Qz
        val p0 = Px * rCosNu + Qx * rSinNu // rx: x component of position vector, AU Heafner, 5.3.18 r
        val p1 = Py * rCosNu + Qy * rSinNu // ry: y component of position vector, AU
        val p2 = Pz * rCosNu + Qz * rSinNu // rz: z component of position vector, AU
        val r = sqrt(rSinNu * rSinNu + rCosNu * rCosNu)
        val sinNu = rSinNu / r
        val cosNu = rCosNu / r
        val p = q.au.value * (1.0 + e) // Heafner: semilatus rectum
        val sqrtMuP = sqrt(GAUSS_GRAV_K_SQ * centralMass / p)

        val s0 = sqrtMuP * ((e + cosNu) * Qx - sinNu * Px) // rdotx (AU/d)
        val s1 = sqrtMuP * ((e + cosNu) * Qy - sinNu * Py) // rdoty (AU/d)
        val s2 = sqrtMuP * ((e + cosNu) * Qz - sinNu * Pz) // rdotz (AU/d)

        val v0 = rotateToVsop87[0] * p0 + rotateToVsop87[1] * p1 + rotateToVsop87[2] * p2
        val v1 = rotateToVsop87[3] * p0 + rotateToVsop87[4] * p1 + rotateToVsop87[5] * p2
        val v2 = rotateToVsop87[6] * p0 + rotateToVsop87[7] * p1 + rotateToVsop87[8] * p2

        val rdot0 = rotateToVsop87[0] * s0 + rotateToVsop87[1] * s1 + rotateToVsop87[2] * s2
        val rdot1 = rotateToVsop87[3] * s0 + rotateToVsop87[4] * s1 + rotateToVsop87[5] * s2
        val rdot2 = rotateToVsop87[6] * s0 + rotateToVsop87[7] * s1 + rotateToVsop87[8] * s2

        return Triad(v0, v1, v2) to Triad(rdot0, rdot1, rdot2)
    }

    companion object {

        fun computeSiderealPeriod(semiMajorAxis: Distance, centralMass: Double): Double {
            val a = semiMajorAxis.au.value
            // Solution for non-Solar central mass (Moons) we need to take central mass (in Solar units) into account. Tested with comparison of preconfigured Moon data.
            return if (a <= 0) 0.0 else M_2_PI / GAUSS_GRAV_K * sqrt(a * a * a / centralMass)
        }

        /**
         * Computes Mean Motion using Gaussian gravitational constant.
         */
        fun computeMeanMotion(e: Double, q: Distance): Radians {
            val qp = q.au.value

            return if (e == 1.0) {
                Radians(0.01720209895 * (1.5 / qp) * sqrt(0.5 / qp))
            } else {
                val a = qp / (1.0 - e)
                Radians(0.01720209895 / (abs(a) * sqrt(abs(a))))
            }
        }

        fun computeRotJ2000Longitude(obliquity: Angle, ascendingNode: Angle): Radians {
            val cobl = cos(obliquity)
            val sobl = sin(obliquity)
            val cnod = cos(ascendingNode)
            val snod = sin(ascendingNode)

            val orbitAxis0 = Triad(cnod, snod, 0.0)
            val orbitAxis1 = Triad(-snod * cobl, cnod * cobl, sobl)
            val orbitPole = Triad(snod * sobl, -cnod * sobl, cobl)
            val J2000NodeOrigin = (J2000_POLE * orbitPole).normalized

            return Radians(atan2(J2000NodeOrigin.dot(orbitAxis1), J2000NodeOrigin.dot(orbitAxis0)))
        }

        // Solve true anomaly nu for elliptical orbit with Laguerre-Conway's method. (May have high e)
        private fun initEllipticalOrbit(orbit: KeplerOrbit, dt: Double): Pair<Double, Double> {
            val e = orbit.e
            val q = orbit.q.au.value
            val n = orbit.n
            val a = q / (1.0 - e)
            val M = (n.radians.value * dt).pmod(M_2_PI)

            //	Comet orbits are quite often near-parabolic, where this may still only converge slowly.
            //	Better always use Laguerre-Conway. See Heafner, Ch. 5.3

            var E = M + 0.85 * e * sin(M).sign

            var escape = 0

            while (escape++ < 10) {
                val Ep = E
                val f2 = e * sin(E)
                val f = E - f2 - M
                val f1 = 1.0 - e * cos(E)

                E += (-5.0 * f) / (f1 + f1.sign * sqrt(abs(16.0 * f1 * f1 - 20.0 * f * f2)))

                if (abs(E - Ep) < EPSILON) {
                    break
                }
            }

            // Note: q=a*(1-e)
            // elsewhere: a sqrt(1-e²) ... q / (1-e) sqrt( (1+e)(1-e)) = q sqrt((1+e)/(1-e))
            val h1 = q * sqrt((1.0 + e) / (1.0 - e))
            val rCosNu = a * (cos(E) - e)
            val rSinNu = h1 * sin(E)

            return Pair(rCosNu, rSinNu)
        }

        // Solve true anomaly nu for hyperbolic "orbit" (better: trajectory) around the sun.
        private fun initHyperbolicOrbit(orbit: KeplerOrbit, dt: Double): Pair<Double, Double> {
            val e = orbit.e
            val q = orbit.q.au.value
            val n = orbit.n
            val a = q / (e - 1.0)
            val M = n.radians.value * dt

            assert(a > 0.0)

            // Heafner, ch.5.4
            var E = M.sign * ln(2.0 * abs(M) / e + 1.85)

            while (true) {
                val Ep = E
                val f2 = e * sin(E)
                val f = f2 - E - M
                val f1 = e * cosh(E) - 1.0

                E += (-5.0 * f) / (f1 + f1.sign * sqrt(abs(16.0 * f1 * f1 - 20.0 * f * f2)))

                if (abs(E - Ep) < EPSILON) {
                    break
                }
            }

            val rCosNu = a * (e - cosh(E))
            val rSinNu = a * sqrt(e * e - 1.0) * sinh(E)

            return Pair(rCosNu, rSinNu)
        }

        // Solve true anomaly nu for parabolic orbit around the sun.
        private fun initParabolicOrbit(orbit: KeplerOrbit, dt: Double): Pair<Double, Double> {
            val q = orbit.q.au.value
            val n = orbit.n.radians.value
            val W = dt * n
            val Y = cbrt(W + sqrt(W * W + 1.0))
            val tanNu2 = Y - 1.0 / Y // Heafner (5.5.8) has an error here, writes (Y-1)/Y.
            val rCosNu = q * (1.0 - tanNu2 * tanNu2)
            val rSinNu = 2.0 * q * tanNu2

            return Pair(rCosNu, rSinNu)
        }
    }
}