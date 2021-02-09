package br.tiagohm.astrum.core.sky

import br.tiagohm.astrum.core.Consts
import br.tiagohm.astrum.core.Observer
import br.tiagohm.astrum.core.math.Triad
import br.tiagohm.astrum.core.rad
import kotlin.math.log10
import kotlin.math.max
import kotlin.math.sqrt

class Sun : Planet(
    "Sun",
    695700.0 / Consts.AU,
    0.0,
    -1.0,
    null,
    PlanetType.STAR,
) {

    override val siderealDay = 25.3799949240010151998

    override val siderealPeriod = 0.0

    override val absoluteMagnitude = 4.83

    override val meanOppositionMagnitude = 100.0

    // A mean solar day (equals to Earth's day) has been added here for educational purposes
    override val meanSolarDay = 1.0

    override fun computeJ2000EquatorialPosition(o: Observer): Triad {
        return Consts.MAT_VSOP87_TO_J2000.multiplyWithoutTranslation(o.lightTimeSunPosition - o.computeHeliocentricEclipticPosition())
    }

    override fun computePosition(jde: Double) = Triad.ZERO to Triad.ZERO

    override fun computeRotObliquity(jde: Double) = 0.12653637076958889433

    override fun internalComputeRTSTime(o: Observer, hz: Double, hasAtmosphere: Boolean): Triad {
        return super.internalComputeRTSTime(o, hz - angularSize(o).rad, hasAtmosphere)
    }

    override fun visualMagnitude(o: Observer): Double {
        // Sun, compute the apparent magnitude for the absolute mag (V: 4.83) and observer's distance
        // Hint: Absolute Magnitude of the Sun in Several Bands: http://mips.as.arizona.edu/~cnaw/sun.html
        val dist = sqrt(o.computeHeliocentricEclipticPosition().lengthSquared) * Consts.AU / Consts.PARSEC
        // Check how much of it is visible
        val shadowFactor = max(0.000128, o.computeEclipseFactor(this, o.home))
        // See: Hughes, D. W., Brightness during a solar eclipse, Journal of the British Astronomical Association, vol.110, no.4, p.203-205
        // URL: http://adsabs.harvard.edu/abs/2000JBAA..110..203H
        return 4.83 + 5.0 * (log10(dist) - 1.0) - 2.5 * (log10(shadowFactor))
    }
}