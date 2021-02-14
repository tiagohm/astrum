package br.tiagohm.astrum.core

const val AU = 149597870.691
const val PARSEC = 30.857E+12
const val EPSILON = 1E-10
const val M_PI = Math.PI
const val M_2_PI = 2 * M_PI
const val M_3_PI = 3 * M_PI
const val M_PI_2 = M_PI / 2
const val GAUSS_GRAV_K = 0.01720209895
const val GAUSS_GRAV_K_SQ = GAUSS_GRAV_K * GAUSS_GRAV_K
const val J2000 = 2451545.0 // epoch J2000: 12 UT on 1 Jan 2000
const val SPEED_OF_LIGHT = 299792.458 // (km/sec)
const val EPS_0: Degrees = 23.4392803055555555555556 // Ecliptic obliquity of J2000.0, degrees
const val M_PI_180 = M_PI / 180.0
const val M_180_PI = 180.0 / M_PI
const val JD_SECOND = 1.0 / 86400.0
const val JD_MINUTE = 1.0 / 1440.0
const val JD_HOUR = 1.0 / 24.0
const val JD_DAY = 1.0
const val ONE_OVER_JD_SECOND = 86400.0
const val TZ_ERA_BEGINNING = 2395996.5 // December 1, 1847
const val M_ARCSEC_RAD = M_2_PI / (360.0 * 3600.0)

val MAT_J2000_TO_VSOP87 = Mat4.xrotation(-23.4392803055555555556 * M_PI_180) * Mat4.zrotation(0.0000275 * M_PI_180)

val MAT_VSOP87_TO_J2000 = MAT_J2000_TO_VSOP87.transpose()

val MAT_J2000_TO_GALACTIC = Mat4(
    -0.054875539726, 0.494109453312, -0.867666135858, 0.0,
    -0.873437108010, -0.444829589425, -0.198076386122, 0.0,
    -0.483834985808, 0.746982251810, 0.455983795705, 0.0,
    0.0, 0.0, 0.0, 1.0
)

val MAT_GALACTIC_TO_J2000 = MAT_J2000_TO_GALACTIC.transpose()

val MAT_J2000_TO_SUPERGALACTIC = Mat4(
    0.37501548, -0.89832046, 0.22887497, 0.0,
    0.34135896, -0.09572714, -0.93504565, 0.0,
    0.86188018, 0.42878511, 0.27075058, 0.0,
    0.0, 0.0, 0.0, 1.0
)

val MAT_SUPERGALACTIC_TO_J2000 = MAT_J2000_TO_SUPERGALACTIC.transpose()

val MAT_J2000_TO_J1875 by lazy {
    val jdB1875 = DateTime.computeJDFromBesselianEpoch(1875.0)
    val p = Precession.computeVondrak(jdB1875)

    (Mat4.xrotation(84381.406 * 1.0 / 3600.0 * M_PI / 180.0) *
            Mat4.zrotation(-p.psi) *
            Mat4.xrotation(-p.omega) *
            Mat4.zrotation(p.chi)).transpose()
}