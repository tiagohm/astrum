package br.tiagohm.astrum.core.sky.planets.major.earth

import br.tiagohm.astrum.core.*
import br.tiagohm.astrum.core.algorithms.Algorithms
import br.tiagohm.astrum.core.algorithms.math.Duad
import br.tiagohm.astrum.core.algorithms.time.SiderealTime
import br.tiagohm.astrum.core.sky.planets.Sun
import kotlin.math.*

data class SolarEclipse(
    val longitude: Double,
    val latitude: Double,
    val magnitude: Double,
    val distance: Double,
    val azimuth: Double,
) {

    companion object {

        fun compute(
            o: Observer,
            moon: Moon,
            southAzimuth: Boolean = false,
        ): SolarEclipse? {
            val op = o.copy(useTopocentricCoordinates = false)
            val sun = moon.parent!!.parent!! as Sun
            val sEquPos = sun.computeEquinoxEquatorialPosition(op)
            val mEquPos = moon.computeEquinoxEquatorialPosition(op)
            var (raSun, decSun) = Algorithms.rectangularToSphericalCoordinates(sEquPos)
            var (raMoon, decMoon) = Algorithms.rectangularToSphericalCoordinates(mEquPos)

            val raDiff = (raMoon - raSun).deg.pmod(360.0)

            return if (raDiff < 3.0 || raDiff > 357.0) {
                var lon = 0.0
                var lat = 0.0
                var mag = 0.0
                var distance = 0.0
                var azimuth = 0.0

                val sdistanceAu = sEquPos.length
                // Moon's distance in Earth's radius
                val mdistanceER = mEquPos.length * (AU / 6378.1366)
                // Greenwich Apparent Sidereal Time
                val gast = SiderealTime.computeApparent(op.jd, op.jde)

                if (raSun < 0.0) raSun += M_2_PI
                if (raMoon < 0.0) raMoon += M_2_PI

                // Besselian elements
                // based on Explanatory supplement to the astronomical ephemeris
                // and the American ephemeris and nautical almanac (1961)
                val rss = sdistanceAu * 23454.7925 // from 1 AU/Earth's radius : 149597870.8/6378.1366
                var b = mdistanceER / rss
                val a = raSun - ((b * cos(decMoon) * (raMoon - raSun)) / ((1 - b) * cos(decSun)))
                val d = decSun - (b * (decMoon - decSun) / (1 - b))
                var x = cos(decMoon) * sin((raMoon - a))
                x *= mdistanceER
                var y = cos(d) * sin(decMoon)
                y -= cos(decMoon) * sin(d) * cos((raMoon - a))
                y *= mdistanceER
                var z = sin(decMoon) * sin(d)
                z += cos(decMoon) * cos(d) * cos((raMoon - a))
                z *= mdistanceER
                // Parameters of the shadow cone
                val f1 = asin((SUN_EARTH + 0.272488) / (rss * (1 - b)))
                val tf1 = tan(f1)
                val f2 = asin((SUN_EARTH - 0.272281) / (rss * (1 - b)))
                val tf2 = tan(f2)
                var L1 = z * tf1 + (0.272488 / cos(f1))
                var L2 = z * tf2 - (0.272281 / cos(f2))
                var mu = gast - a.deg

                // Find Lat./Long. of center line on Earth's surface
                val cd = cos(d)
                val rho1 = sqrt(1 - 0.00669398 * cd * cd)
                // e^2 = 0.00669398 : Earth flattening parameter
                // IERS 2010 : f = 298.25642 : e^2 = 2f-f^2
                val y1 = y / rho1
                val xi = x
                val sd = sin(d)
                val sd1 = sd / rho1
                val cd1 = sqrt(1 - 0.00669398) * cd / rho1
                val rho2 = sqrt(1 - 0.00669398 * sd * sd)
                val sd1d2 = 0.00669398 * sd * cd / (rho1 * rho2)
                val cd1d2 = sqrt(1 - sd1d2 * sd1d2)

                if ((1 - x * x - y1 * y1) > 0) {
                    val zeta1 = sqrt(1 - x * x - y1 * y1)
                    val zeta = rho2 * (zeta1 * cd1d2 - y1 * sd1d2)
                    // val  sd2 = sd * 1.0033641 / rho2
                    L2 -= zeta * tf2
                    b = -y * sd + zeta * cd
                    var theta = atan2(xi, b).deg
                    if (theta < 0) theta += 360
                    if (mu > 360) mu -= 360
                    lon = mu - theta
                    if (lon < -180) lon += 360
                    if (lon > 180) lon -= 360
                    lon = -lon // + East, - West
                    val sfn1 = y1 * cd1 + zeta1 * sd1
                    val cfn1 = sqrt(1 - sfn1 * sfn1)
                    lat = 1.0033641 * sfn1 / cfn1
                    lat = atan(lat).deg
                    L1 -= zeta * tf1
                    // Magnitude of eclipse
                    // mag < 1 = annular
                    mag = L1 / (L1 + L2)
                }

                // Shadow axis is touching Earth
                if (lat < 90.0) {
                    val obs = Duad(op.site.longitude, op.site.latitude)
                    val target = Duad(lon, lat)
                    distance = Algorithms.distanceKm(op.home, obs, target)
                    azimuth = Algorithms.azimuth(obs, target, southAzimuth)
                }

                SolarEclipse(lon, lat, mag, distance, azimuth)
            } else {
                null
            }
        }

        private const val SUN_EARTH = 109.12278046851489508707 // Ratio of Sun-Earth radius 696000/6378.1366
    }
}