package br.tiagohm.astrum.sky.planets.minor.comets

import br.tiagohm.astrum.sky.Observer
import br.tiagohm.astrum.sky.core.orbit.KeplerOrbit
import br.tiagohm.astrum.sky.core.time.JulianDay
import br.tiagohm.astrum.sky.core.units.angle.Angle
import br.tiagohm.astrum.sky.core.units.distance.Distance
import br.tiagohm.astrum.sky.core.units.distance.Kilometer
import br.tiagohm.astrum.sky.planets.Sun
import br.tiagohm.astrum.sky.planets.minor.MinorPlanet
import kotlin.math.log10
import kotlin.math.pow

/**
 * Comet.
 *
 * @param q Pericenter distance
 * @param e Eccentricity
 * @param i Inclination
 * @param omega Longitude of ascending node
 * @param w Argument of perihelion
 * @param t0 Time at perihelion (JDE)
 * @param n Mean motion
 */
class Comet(
    id: String,
    parent: Sun,
    q: Distance,
    e: Double,
    i: Angle,
    omega: Angle,
    w: Angle,
    t0: JulianDay,
    n: Angle = KeplerOrbit.computeMeanMotion(e, q),
    albedo: Double = 0.15,
    absoluteMagnitude: Double = -99.0,
    slope: Double = -10.0,
    radius: Distance = Kilometer(1.0)
) : MinorPlanet(id, parent, q, e, i, omega, w, t0, n, albedo, absoluteMagnitude, slope, radius) {

    override val siderealPeriod by lazy {
        val a = orbit!!.semiMajorAxis
        if (a.isPositive) KeplerOrbit.computeSiderealPeriod(a, 1.0) else 0.0
    }

    override fun visualMagnitude(o: Observer, extra: Any?): Double {
        //If the two parameter system is not used, use the default radius/albedo mechanism.
        if (slope < -9.99) {
            super.visualMagnitude(o, extra)
        }

        val observerHeliocentricPosition = o.computeHeliocentricEclipticPosition()
        val cometHeliocentricPosition = computeHeliocentricEclipticPosition(o)
        val cometSunDistance = cometHeliocentricPosition.length
        val observerCometDistance = (observerHeliocentricPosition - cometHeliocentricPosition).length

        // Calculate apparent magnitude
        // Sources: http://www.clearskyinstitute.com/xephem/help/xephem.html#mozTocId564354
        // (XEphem manual, section 7.1.2.3 "Magnitude models"), also
        // http://www.ayton.id.au/gary/Science/Astronomy/Ast_comets.htm#Comet%20facts:
        // GZ: Note that Meeus, Astr.Alg.1998 p.231, has m=absoluteMagnitude+5log10(observerCometDistance) + kappa*log10(cometSunDistance)
        // with kappa typically 5..15. MPC provides Slope parameter. So we should expect to have slopeParameter (a word only used for minor planets!) for our comets 2..6
        return absoluteMagnitude + 5.0 * log10(observerCometDistance) + 2.5 * slope * log10(cometSunDistance)
    }

    fun computeComaDiameterAndTailLength(o: Observer): Pair<Distance, Distance> {
        // Formula found at http://www.projectpluto.com/update7b.htm#comet_tail_formula
        val r = computeHeliocentricEclipticPosition(o).length
        val mhelio = absoluteMagnitude + slope * log10(r)
        val Do = 10.0.pow(((-0.0033 * mhelio - 0.07) * mhelio + 3.25))
        val common = 1.0 - 10.0.pow((-2.0 * r))
        val D = Do * common * (1.0 - 10.0.pow(-r)) * 1000.0
        val Lo = 10.0.pow(((-0.0075 * mhelio - 0.19) * mhelio + 2.1))
        val L = Lo * (1.0 - 10.0.pow(-4.0 * r)) * common * 1E+6
        return Kilometer(D) to Kilometer(L)
    }
}