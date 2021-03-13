package br.tiagohm.astrum.sky.core.ephemeris

import br.tiagohm.astrum.common.M_PI
import br.tiagohm.astrum.sky.core.Algorithms
import br.tiagohm.astrum.sky.core.time.JulianDay
import br.tiagohm.astrum.sky.readByteArrayFromResources
import br.tiagohm.astrum.sky.readDoubleArrayFromResources
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Ephemeride Lunaire Parisienne by Chapront-Touze M., Chapront J.
 * ftp://ftp.imcce.fr/pub/ephem/moon/elp82b
 */
object Elp82b {

    fun computeCoordinates(jd: JulianDay): DoubleArray {
        val t = (jd.value - 2451545.0) / 36525.0
        val r = DoubleArray(3)
        val ts = doubleArrayOf(t0, t1, t2)
        val es = arrayOf(r0, r1, r2)

        Algorithms.computeInterpolatedElements(t, r, 3, Elp82b::computeSphericalCoordinates, DElTA_T, ts, es)

        t0 = ts[0]
        t1 = ts[1]
        t2 = ts[2]

        val rh = r[2] * cos(r[1])
        val x3 = r[2] * sin(r[1])
        val x1 = rh * cos(r[0])
        val x2 = rh * sin(r[0])

        var pw = t * (P1 + t * (P2 + t * (P3 + t * (P4 + t * P5))))
        var qw = t * (Q1 + t * (Q2 + t * (Q3 + t * (Q5 + t * Q5))))
        val pwq = pw * pw
        val qwq = qw * qw
        val pwqw = 2.0 * pw * qw
        val pw2 = 1.0 - 2.0 * pwq
        val qw2 = 1.0 - 2.0 * qwq
        val h = 1.0 - pwq - qwq
        val ra = if (h > 0.0) (2.0 * sqrt(h)) else 0.0

        pw *= ra
        qw *= ra

        val xyz = DoubleArray(3)

        // VSOP87 coordinates
        xyz[0] = pw2 * x1 + pwqw * x2 + pw * x3
        xyz[1] = pwqw * x1 + qw2 * x2 - qw * x3
        xyz[2] = -pw * x1 + qw * x2 + (pw2 + qw2 - 1.0) * x3

        return xyz
    }

    private fun computeSphericalCoordinates(t: Double, r: DoubleArray) {
        val lambda = DoubleArray(17)
        val cosSinLambda = DoubleArray(303 * 4)
        val accu = DoubleArray(9)
        val stack = DoubleArray(17 * 2)

        for (i in 0..3) {
            lambda[i] = 0.0

            for (k in 3 downTo 0) {
                lambda[i] += DEL[k * 4 + i]
                lambda[i] *= t
            }

            lambda[5 + i] = DEL[i] * t
        }

        lambda[4] = ZETA * t

        for (i in 0..7) {
            lambda[9 + i] = P[i] * t
        }

        prepareLambdaArray(17, MAX_LAMBDA_FACTOR, lambda, cosSinLambda)
        CONSTANTS.copyInto(accu)
        accumulateElp82bTerms(INSTRUCTIONS, COEFFICIENTS, cosSinLambda, accu, stack)

        r[0] = (accu[0] + W[0]
                + t * (accu[3] + W[1]
                + t * (accu[6] + W[2]
                + t * (W[3]
                + t * W[4])))) * (M_PI / (180 * 3600))
        r[1] = (accu[1] + t * (accu[4] + t * accu[7])) * (M_PI / (180 * 3600))
        r[2] = (accu[2] + t * (accu[5] + t * accu[8])) * A0_DIV_ATH_TIMES_AU
    }

    private fun prepareLambdaArray(
        n: Int,
        maxLambdaFactor: IntArray,
        lambda: DoubleArray,
        cosSinLambda: DoubleArray,
    ) {
        var csli = 0

        for (i in 0 until n) {
            val maxFactor = maxLambdaFactor[i]
            var cslp = csli

            cosSinLambda[0 + cslp] = cos(lambda[i])
            cosSinLambda[1 + cslp] = sin(lambda[i])
            cosSinLambda[2 + cslp] = cosSinLambda[0 + cslp]
            cosSinLambda[3 + cslp] = -cosSinLambda[1 + cslp]

            for (m in 2..maxFactor) {
                val m0 = csli + ((((m + 0) shr 1) - 1) shl 2)
                val m1 = csli + ((((m + 1) shr 1) - 1) shl 2)

                cslp += 4

                cosSinLambda[0 + cslp] =
                    cosSinLambda[m0] * cosSinLambda[m1] - cosSinLambda[m0 + 1] * cosSinLambda[m1 + 1]
                cosSinLambda[1 + cslp] =
                    cosSinLambda[m0] * cosSinLambda[m1 + 1] + cosSinLambda[m0 + 1] * cosSinLambda[m1]
                cosSinLambda[2 + cslp] = cosSinLambda[0 + cslp]
                cosSinLambda[3 + cslp] = -cosSinLambda[1 + cslp]
            }

            csli += (maxFactor shl 2)
        }
    }

    private fun accumulateElp82bTerms(
        instructions: ByteArray,
        coefficients: DoubleArray,
        cosSinLambda: DoubleArray,
        accu: DoubleArray,
        sp: DoubleArray,
    ) {
        sp[0] = 1.0
        sp[1] = 0.0

        var i = 0
        var ci = 0
        var spi = 0

        while (true) {
            var termCount = instructions[i++].toInt() and 0xFF

            when (termCount) {
                0xFF -> break
                0xFE -> spi -= 2 // Pop argument from the stack
                else -> {
                    val lambdaIndex = ((termCount and 15) shl 8) or (instructions[i++].toInt() and 0xFF)
                    val csli = lambdaIndex shl 1

                    sp[2 + spi] = cosSinLambda[0 + csli] * sp[0 + spi] - cosSinLambda[1 + csli] * sp[1 + spi]
                    sp[3 + spi] = cosSinLambda[0 + csli] * sp[1 + spi] + cosSinLambda[1 + csli] * sp[0 + spi]

                    spi += 2
                    termCount = termCount shr 4

                    while (--termCount >= 0) {
                        accu[instructions[i++].toInt() and 0xFF] += coefficients[0 + ci] * sp[0 + spi] + coefficients[1 + ci] * sp[1 + spi]
                        ci += 2
                    }
                }
            }
        }
    }

    private const val DElTA_T = 1.0 / (24.0 * 36525.0)

    private const val P1 = 1.0180391E-5
    private const val P2 = 4.7020439E-7
    private const val P3 = -5.417367E-10
    private const val P4 = -2.507948E-12
    private const val P5 = 4.63486E-15
    private const val Q1 = -1.13469002E-4
    private const val Q2 = 1.2372674E-7
    private const val Q3 = 1.265417E-9
    private const val Q4 = -1.371808E-12
    private const val Q5 = -3.20334E-15

    private const val A0_DIV_ATH_TIMES_AU = 384747.9806448954 / (384747.9806743165 * 149597870.691)

    private val DEL = doubleArrayOf(
        (1732559343.73604 - 129597742.2758) * (M_PI / (180 * 3600)),
        (129597742.2758 - 1161.2283) * (M_PI / (180 * 3600)),
        (1732559343.73604 - 14643420.2632) * (M_PI / (180 * 3600)),
        (1732559343.73604 - -6967919.3622) * (M_PI / (180 * 3600)),
        (-5.8883 - -0.0202) * (M_PI / (180 * 3600)),
        (-0.0202 - 0.5327) * (M_PI / (180 * 3600)),
        (-5.8883 - -38.2776) * (M_PI / (180 * 3600)),
        (-5.8883 - 6.3622) * (M_PI / (180 * 3600)),
        (0.006604 - 9E-6) * (M_PI / (180 * 3600)),
        (9E-6 - -1.38E-4) * (M_PI / (180 * 3600)),
        (0.006604 - -0.045047) * (M_PI / (180 * 3600)),
        (0.006604 - 0.007625) * (M_PI / (180 * 3600)),
        (-3.169E-5 - 1.5E-7) * (M_PI / (180 * 3600)),
        (1.5E-7 - 0.0) * (M_PI / (180 * 3600)),
        (-3.169E-5 - 2.1301E-4) * (M_PI / (180 * 3600)),
        (-3.169E-5 - -3.586E-5) * (M_PI / (180 * 3600)),
    )

    private val P = doubleArrayOf(
        538101628.68898 * (M_PI / (180 * 3600)),
        210664136.43355 * (M_PI / (180 * 3600)),
        129597742.2758 * (M_PI / (180 * 3600)),
        68905077.59284 * (M_PI / (180 * 3600)),
        10925660.42861 * (M_PI / (180 * 3600)),
        4399609.65932 * (M_PI / (180 * 3600)),
        1542481.19393 * (M_PI / (180 * 3600)),
        786550.32074 * (M_PI / (180 * 3600)),
    )

    private val W = doubleArrayOf(
        218 * 3600 + 18 * 60 + 59.95571,
        1732559343.73604,
        -5.8883,
        0.006604,
        -3.169E-5,
    )

    private const val ZETA = (1732559343.73604 + 5029.0966) * (M_PI / (180 * 3600))

    private val MAX_LAMBDA_FACTOR = intArrayOf(
        10, 6, 8, 6,
        2, 8, 4, 6,
        5, 17, 61, 65,
        71, 14, 12, 4, 4,
    )

    private val CONSTANTS = doubleArrayOf(
        0.000000000000000000E+00,
        0.000000000000000000E+00,
        3.850005584468033630E+05,
        -1.100000000000000039E-04,
        0.000000000000000000E+00,
        2.059999999999999765E-03,
        0.000000000000000000E+00,
        0.000000000000000000E+00,
        0.000000000000000000E+00,
    )

    private val INSTRUCTIONS by lazy { readByteArrayFromResources("ELP82B_INSTRUCTIONS.dat") }

    private val COEFFICIENTS by lazy { readDoubleArrayFromResources("ELP82B_COEFFICIENTS.dat") }

    private var t0 = -1E+100
    private var t1 = -1E+100
    private var t2 = -1E+100

    private val r0 = DoubleArray(3)
    private val r1 = DoubleArray(3)
    private val r2 = DoubleArray(3)
}