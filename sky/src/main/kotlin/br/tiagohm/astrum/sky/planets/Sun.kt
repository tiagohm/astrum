package br.tiagohm.astrum.sky.planets

import br.tiagohm.astrum.common.AU_KM
import br.tiagohm.astrum.common.PARSEC
import br.tiagohm.astrum.common.units.angle.Angle
import br.tiagohm.astrum.common.units.angle.Radians
import br.tiagohm.astrum.common.units.distance.Kilometer
import br.tiagohm.astrum.sky.MAT_VSOP87_TO_J2000
import br.tiagohm.astrum.sky.Observer
import br.tiagohm.astrum.sky.PlanetType
import br.tiagohm.astrum.sky.core.math.Triad
import br.tiagohm.astrum.sky.core.time.JulianDay
import br.tiagohm.astrum.sky.planets.major.earth.Moon
import kotlin.math.log10
import kotlin.math.max
import kotlin.math.sqrt

class Sun : Planet(
    Kilometer(695700.0),
    0.0,
    -1.0,
    null,
    PlanetType.STAR,
) {

    override val siderealDay = 360 / 14.1844000

    override val siderealPeriod = 0.0

    override val absoluteMagnitude = 4.83

    override val meanOppositionMagnitude = 100.0

    // A mean solar day (equals to Earth's day) has been added here for educational purposes
    override val meanSolarDay = 1.0

    override val mass = 1.0

    override fun computeJ2000EquatorialPosition(o: Observer): Triad {
        return MAT_VSOP87_TO_J2000.multiplyWithoutTranslation(o.lightTimeSunPosition - o.computeHeliocentricEclipticPosition())
    }

    override fun computePosition(jde: JulianDay) = Triad.ZERO to Triad.ZERO

    override fun computeRotObliquity(jde: JulianDay) = Radians(0.12653637076958889433)

    override fun computeRotAscendingNode() = Radians(1.3223623836794924)

    override fun internalComputeRTSTime(o: Observer, hz: Angle, hasAtmosphere: Boolean): Triad {
        return super.internalComputeRTSTime(o, hz - angularSize(o).radians, hasAtmosphere)
    }

    override fun visualMagnitude(o: Observer, extra: Any?): Double {
        // Sun, compute the apparent magnitude for the absolute mag (V: 4.83) and observer's distance
        // Hint: Absolute Magnitude of the Sun in Several Bands: http://mips.as.arizona.edu/~cnaw/sun.html
        val dist = sqrt(o.computeHeliocentricEclipticPosition().lengthSquared) * AU_KM / PARSEC
        // Check how much of it is visible
        val shadowFactor = max(0.000128, if (extra is Moon) o.computeEclipseFactor(extra) else 1.0)
        // See: Hughes, D. W., Brightness during a solar eclipse, Journal of the British Astronomical Association, vol.110, no.4, p.203-205
        // URL: http://adsabs.harvard.edu/abs/2000JBAA..110..203H
        return 4.83 + 5.0 * (log10(dist) - 1.0) - 2.5 * (log10(shadowFactor))
    }

    override fun info(o: Observer): Map<String, Any> {
        // TODO: Solar Eclipse
        return super.info(o).also {}
    }
}