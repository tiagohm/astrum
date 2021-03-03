package br.tiagohm.astrum.sky.planets

import br.tiagohm.astrum.sky.J2000
import br.tiagohm.astrum.sky.PlanetType
import br.tiagohm.astrum.sky.core.orbit.KeplerOrbit
import br.tiagohm.astrum.sky.core.units.angle.Angle
import br.tiagohm.astrum.sky.core.units.angle.Radians
import br.tiagohm.astrum.sky.core.units.distance.Distance

/**
 *  Satellite orbiting Planet [T].
 *
 * @param q Pericenter distance
 * @param e Eccentricity
 * @param i Inclination
 * @param omega Longitude of ascending node
 * @param w Argument of perihelion
 * @param t0 Time at perihelion (JDE)
 * @param n Mean motion
 */
open class Satellite<T : Planet>(
    id: String,
    parent: T,
    radius: Distance,
    q: Distance,
    e: Double,
    i: Angle,
    omega: Angle,
    w: Angle,
    t0: Double,
    albedo: Double,
    n: Angle = KeplerOrbit.computeMeanMotion(e, q),
    override val absoluteMagnitude: Double = -99.0,
    private val rotObliquity: Angle = Radians.ZERO,
    private val rotAscendingNode: Angle = Radians.ZERO,
) : Planet(
    id,
    radius,
    0.0,
    albedo,
    KeplerOrbit(q, e, i, omega, w, t0, n, parent.computeRotObliquity(J2000), parent.computeRotAscendingNode(), centralMass = parent.mass),
    PlanetType.MOON,
    parent,
) {

    override val siderealPeriod by lazy { orbit!!.siderealPeriod }

    override val siderealDay by lazy { siderealPeriod }

    final override fun computeRotObliquity(jde: Double) = rotObliquity

    final override fun computeRotAscendingNode() = rotAscendingNode
}