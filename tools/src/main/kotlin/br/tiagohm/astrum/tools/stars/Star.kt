package br.tiagohm.astrum.tools.stars

import br.tiagohm.astrum.sky.*
import br.tiagohm.astrum.sky.core.math.Triad
import br.tiagohm.astrum.sky.core.units.angle.Radians
import br.tiagohm.astrum.sky.core.units.distance.Distance
import br.tiagohm.astrum.sky.core.units.distance.LightYear
import kotlin.math.log10

open class Star<T : StarData>(
    private val a: ZoneArray<out T>,
    private val z: ZoneData<out T>,
    val star: T,
) : CelestialObject {

    // TODO: Usar ComponentIds para HIP
    override val id = if (star is StarData.Hipparcos) "HIP ${star.hip}" else ""

    override val type = PlanetType.STAR

    val spectralType by lazy { if (star is StarData.Hipparcos && star.spInt >= 0 && star.spInt < HIP_SPECTRAL_TYPES.size) HIP_SPECTRAL_TYPES[star.spInt] else "" }

    fun absoluteMagnitude(o: Observer): Double {
        return if (star is StarData.Hipparcos && star.plx != 0) {
            visualMagnitude(o) + 5.0 * (1.0 + log10(star.plx  / 100000.0))
        } else {
            -99.0
        }
    }

    override fun visualMagnitude(o: Observer, extra: Any?): Double {
        return 0.001 * a.magMin + star.magnitude * (0.001 * a.magRange) / a.magStep
    }

    override fun angularSize(o: Observer) = Radians.ZERO

    override fun distance(o: Observer): Distance {
        return if (star is StarData.Hipparcos && star.plx != 0) {
            val k = AU_KM / (SPEED_OF_LIGHT * 86400 * 365.25)
            val d = 0.00001 / 3600.0 * M_PI_180
            LightYear(k / (star.plx * d))
        } else {
            LightYear.ZERO
        }
    }

    override fun computeJ2000EquatorialPosition(o: Observer): Triad {
        return star.computeJ2000Position(z, M_PI_180 * (0.0001 / 3600.0) * ((o.jde - J2000).value / 365.25) / a.starPositionScale)
    }

    override fun toString() = "$star"

    companion object {

        private val HIP_SPECTRAL_TYPES = getResourceAsStream("HIP_SPECTRAL_TYPES.dat")!!.bufferedReader().readLines()
    }
}