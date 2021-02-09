package br.tiagohm.astrum.core.sky

import br.tiagohm.astrum.core.Consts
import br.tiagohm.astrum.core.Observer
import br.tiagohm.astrum.core.algorithms.ApparentMagnitudeAlgorithm
import br.tiagohm.astrum.core.algorithms.Nutation
import br.tiagohm.astrum.core.algorithms.Precession
import br.tiagohm.astrum.core.deg
import br.tiagohm.astrum.core.math.Mat4
import br.tiagohm.astrum.core.math.Triad
import br.tiagohm.astrum.core.time.SiderealTime

class Earth(parent: Sun) : Planet(
    "Earth",
    6378.1366 / Consts.AU,
    0.003352810664747481,
    0.3,
    null,
    PlanetType.PLANET,
    parent,
) {

    init {
        // TODO: For the planet moons with orbits relative to planets' equator...
    }

    override val siderealDay = 0.99726963226279286992

    override var siderealPeriod = 365.256363004

    override var absoluteMagnitude = -3.86

    override fun computeSiderealTime(jd: Double, jde: Double, useNutation: Boolean): Double {
        return if (useNutation) SiderealTime.computeApparent(jd, jde)
        else SiderealTime.computeMean(jd, jde)
    }

    override fun computePosition(jde: Double): Pair<Triad, Triad> {
        val xyz = computeEarthHeliocentricCoordinates(jde)
        return Triad(xyz[0], xyz[1], xyz[2]) to Triad(xyz[3], xyz[4], xyz[5])
    }

    override fun internalComputeTransformationMatrix(jd: Double, jde: Double, useNutation: Boolean): Mat4 {
        // We follow Capitaine's (2003) formulation P=Rz(chi)*Rx(-omega)*Rz(-psi)*Rx(eps). (Explan.Suppl. 2013, 6.28)
        // ADS: 2011A&A...534A..22V = A&A 534, A22 (2011): Vondrak, Capitane, Wallace: New Precession Expressions, valid for long time intervals:
        // See also Hilton et al., Report on Precession and the Ecliptic. Cel.Mech.Dyn.Astr. 94:351-367 (2006), eqn (6) and (21).
        val p = Precession.computeVondrak(jde)

        // Canonical precession rotations: Nodal rotation psi,
        // then rotation by omega, the angle between EclPoleJ2000 and EarthPoleOfDate.
        // The final rotation by chi rotates the equinox (zero degree).
        // To achieve ecliptical coords of date, you just have now to add a rotX by epsilon (obliquity of date).

        var rotLocalToParent = Mat4.zrotation(-p.psi) * Mat4.xrotation(-p.omega) * Mat4.zrotation(p.chi)

        // Plus nutation IAU-2000B:
        if (useNutation) {
            val nut = Nutation.compute(jde)
            // eq.21 in Hilton et al. wrongly had a positive deltaPsi rotation
            val nut2000B = Mat4.xrotation(p.epsilon) *
                    Mat4.zrotation(-nut.deltaPsi) *
                    Mat4.xrotation(-p.epsilon - nut.deltaEpsilon)

            rotLocalToParent *= nut2000B
        }

        return rotLocalToParent
    }

    override fun computeRotObliquity(jde: Double): Double {
        return Precession.computeVondrakEpsilon(jde)
    }

    override fun computeVisualMagnitude(
        o: Observer,
        phaseAngle: Double,
        cosChi: Double,
        observerRq: Double,
        planetRq: Double,
        observerPlanetRq: Double,
        d: Double,
        shadowFactor: Double,
    ): Double {
        val phaseDeg = phaseAngle.deg

        return when (o.apparentMagnitudeAlgorithm) {
            ApparentMagnitudeAlgorithm.EXPLANATORY_SUPPLEMENT_2013 -> {
                -3.87 + d + (((0.48E-6 * phaseDeg + 0.000019) * phaseDeg + 0.0130) * phaseDeg)
            }
            else -> super.computeVisualMagnitude(
                o,
                phaseAngle,
                cosChi,
                observerRq,
                planetRq,
                observerPlanetRq,
                d,
                shadowFactor,
            )
        }
    }
}