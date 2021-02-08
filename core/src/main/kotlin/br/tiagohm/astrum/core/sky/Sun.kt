package br.tiagohm.astrum.core.sky

import br.tiagohm.astrum.core.Consts
import br.tiagohm.astrum.core.Observer
import br.tiagohm.astrum.core.math.Triad

class Sun : Planet(
    "Sun",
    695700.0,
    0.0,
    -1.0,
    null,
    PlanetType.STAR,
) {

    override val siderealDay = 25.3799949240010151998

    override val siderealPeriod = 0.0

    override val absoluteMagnitude = 4.83

    // A mean solar day (equals to Earth's day) has been added here for educational purposes
    override val meanSolarDay = 1.0

    override fun computeJ2000EquatorialPosition(o: Observer): Triad {
        return Consts.MAT_VSOP87_TO_J2000.multiplyWithoutTranslation(o.lightTimeSunPosition - o.computeHeliocentricEclipticPosition())
    }

    override fun computePosition(jde: Double) = Triad.ZERO to Triad.ZERO

    override fun computeRotObliquity(jde: Double) = 0.12653637076958889433
}