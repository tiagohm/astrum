package br.tiagohm.astrum.sky.dso

import br.tiagohm.astrum.sky.CelestialObject
import br.tiagohm.astrum.sky.Observer
import br.tiagohm.astrum.sky.PlanetType
import br.tiagohm.astrum.sky.core.Algorithms
import br.tiagohm.astrum.sky.core.coordinates.EquatorialCoord
import br.tiagohm.astrum.sky.core.units.angle.Angle
import br.tiagohm.astrum.sky.core.units.angle.Degrees
import br.tiagohm.astrum.sky.core.units.distance.Distance
import br.tiagohm.astrum.sky.core.units.distance.LightYear
import kotlin.math.abs
import kotlin.math.min

/**
 * Deep Sky Object.
 *
 * @param posEquJ2000 Equatorial J2000 position.
 * @param mB B Magnitude.
 * @param mV Visual Magnitude.
 * @param majorAxisSize Major axis size.
 * @param minorAxisSize Minor axis size.
 * @param distance Distance from observer.
 */
open class DSO(
    val posEquJ2000: EquatorialCoord = EquatorialCoord.ZERO,
    val mB: Double = 99.0,
    val mV: Double = 99.0,
    val majorAxisSize: Angle = Degrees.ZERO,
    val minorAxisSize: Angle = Degrees.ZERO,
    val distance: Distance = LightYear.ZERO,
) : CelestialObject {

    private val angularSize by lazy { (majorAxisSize + minorAxisSize) * 0.5 }

    private val xyz by lazy { Algorithms.sphericalToRectangularCoordinates(posEquJ2000.ra, posEquJ2000.dec) }

    override val type = PlanetType.DSO

    override fun distance(o: Observer) = distance

    override fun visualMagnitude(o: Observer, extra: Any?) = min(mV, mB)

    override fun angularSize(o: Observer) = angularSize

    override fun computeJ2000EquatorialPosition(o: Observer) = xyz

    override fun toString(): String {
        return "DSO(posEquJ2000=$posEquJ2000, mB=$mB, mV=$mV, majorAxisSize=$majorAxisSize," +
                " minorAxisSize=$minorAxisSize, distance=$distance, type=$type)"
    }

    companion object {

        /**
         * Computes the distance in ly from [parallax] in mas.
         */
        fun computeDistanceFromParallax(parallax: Double): LightYear {
            return if (abs(parallax) > 0.0) {
                // Distance in light years from parallax
                // (648000/PI) * 1000 * (AU_KM/LY_KM) = 3261.5...
                LightYear(3261.56377715928389707856614026952786500443553209103464 / parallax)
            } else {
                LightYear.ZERO
            }
        }
    }
}