package br.tiagohm.astrum.sky.core.precession

import br.tiagohm.astrum.common.JD_DAY
import br.tiagohm.astrum.common.M_2_PI
import br.tiagohm.astrum.common.M_ARCSEC_RAD
import br.tiagohm.astrum.common.units.angle.Angle
import br.tiagohm.astrum.common.units.angle.Radians
import br.tiagohm.astrum.sky.core.time.JulianDay
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

data class Precession(
    val psi: Angle = Radians.ZERO,
    val omega: Angle = Radians.ZERO,
    val chi: Angle = Radians.ZERO,
    val epsilon: Angle = Radians.ZERO,
) {

    companion object {

        val ZERO = Precession()

        private var lastJDE = 0.0
        private var precession = ZERO

        fun computeVondrakEpsilon(jde: JulianDay) = computeVondrak(jde).epsilon

        fun computeVondrak(jde: JulianDay): Precession {
            val JDE = jde.value

            if (abs(JDE - lastJDE) >= JD_DAY) {
                lastJDE = JDE

                // Julian centuries from J2000.0
                val T = (JDE - 2451545.0) * (1.0 / 36525.0)

                val T2pi = T * M_2_PI

                var psi = 0.0
                var omega = 0.0
                var chi = 0.0
                var epsilon = 0.0

                for (i in 0..17) {
                    val invP = P[i][0]

                    val phase = T2pi * invP
                    val sin2piTP = sin(phase)
                    val cos2piTP = cos(phase)

                    psi += P[i][1] * cos2piTP + P[i][4] * sin2piTP
                    omega += P[i][2] * cos2piTP + P[i][5] * sin2piTP
                    chi += P[i][3] * cos2piTP + P[i][6] * sin2piTP
                }

                for (i in 0..9) {
                    val invP = EPS[i][0]

                    val phase = T2pi * invP
                    val sin2piTP = sin(phase)
                    val cos2piTP = cos(phase)

                    epsilon += EPS[i][2] * cos2piTP + EPS[i][4] * sin2piTP
                }

                psi += ((289E-9 * T - 0.00740913) * T + 5042.7980307) * T + 8473.343527
                omega += ((151E-9 * T + 0.00000146) * T - 0.4436568) * T + 84283.175915
                chi += ((-61E-9 * T + 0.00001472) * T + 0.0790159) * T - 19.657270
                epsilon += ((-110E-9 * T - 0.00004039) * T + 0.3624445) * T + 84028.206305

                precession = Precession(
                    Radians(psi * M_ARCSEC_RAD),
                    Radians(omega * M_ARCSEC_RAD),
                    Radians(chi * M_ARCSEC_RAD),
                    Radians(epsilon * M_ARCSEC_RAD)
                )
            }

            return precession
        }

        private val P = arrayOf(
            doubleArrayOf(
                1 / 402.90, -22206.325946, 1267.727824,
                -13765.924050, -3243.236469, -8571.476251, -2206.967126
            ),
            doubleArrayOf(1 / 256.75, 12236.649447, 1702.324248, 13511.858383, -3969.723769, 5309.796459, -4186.752711),
            doubleArrayOf(1 / 292.00, -1589.008343, -2970.553839, -1455.229106, 7099.207893, -610.393953, 6737.949677),
            doubleArrayOf(1 / 537.22, 2482.103195, 693.790312, 1054.394467, -1903.696711, 923.201931, -856.922846),
            doubleArrayOf(1 / 241.45, 150.322920, -14.724451, 0.0, 146.435014, 3.759055, 0.0),
            doubleArrayOf(1 / 375.22, -13.632066, -516.649401, -112.300144, 1300.630106, -40.691114, 957.149088),
            doubleArrayOf(1 / 157.87, 389.437420, -356.794454, 202.769908, 1727.498039, 80.437484, 1709.440735),
            doubleArrayOf(1 / 274.20, 2031.433792, -129.552058, 1936.050095, 299.854055, 807.300668, 154.425505),
            doubleArrayOf(1 / 203.00, 363.748303, 256.129314, 0.0, -1217.125982, 83.712326, 0.0),
            doubleArrayOf(1 / 440.00, -896.747562, 190.266114, -655.484214, -471.367487, -368.654854, -243.520976),
            doubleArrayOf(1 / 170.72, -926.995700, 95.103991, -891.898637, -441.682145, -191.881064, -406.539008),
            doubleArrayOf(1 / 713.37, 37.070667, -332.907067, 0.0, -86.169171, -4.263770, 0.0),
            doubleArrayOf(1 / 313.00, -597.682468, 131.337633, 0.0, -308.320429, -270.353691, 0.0),
            doubleArrayOf(1 / 128.38, 66.282812, 82.731919, -333.322021, -422.815629, 11.602861, -446.656435),
            doubleArrayOf(1 / 202.00, 0.0, 0.0, 327.517465, 0.0, 0.0, -1049.071786),
            doubleArrayOf(1 / 315.00, 0.0, 0.0, -494.780332, 0.0, 0.0, -301.504189),
            doubleArrayOf(1 / 136.32, 0.0, 0.0, 585.492621, 0.0, 0.0, 41.348740),
            doubleArrayOf(1 / 490.00, 0.0, 0.0, 110.512834, 0.0, 0.0, 142.525186),
        )

        private val EPS = arrayOf(
            doubleArrayOf(1 / 409.90, -6908.287473, 753.872780, -2845.175469, -1704.720302),
            doubleArrayOf(1 / 396.15, -3198.706291, -247.805823, 449.844989, -862.308358),
            doubleArrayOf(1 / 537.22, 1453.674527, 379.471484, -1255.915323, 447.832178),
            doubleArrayOf(1 / 402.90, -857.748557, -53.880558, 886.736783, -889.571909),
            doubleArrayOf(1 / 417.15, 1173.231614, -90.109153, 418.887514, 190.402846),
            doubleArrayOf(1 / 288.92, -156.981465, -353.600190, 997.912441, -56.564991),
            doubleArrayOf(1 / 4043.00, 371.836550, -63.115353, -240.979710, -296.222622),
            doubleArrayOf(1 / 306.00, -216.619040, -28.248187, 76.541307, -75.859952),
            doubleArrayOf(1 / 277.00, 193.691479, 17.703387, -36.788069, 67.473503),
            doubleArrayOf(1 / 203.00, 11.891524, 38.911307, -170.964086, 3.014055),
        )
    }
}