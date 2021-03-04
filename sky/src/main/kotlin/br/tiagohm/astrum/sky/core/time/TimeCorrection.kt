package br.tiagohm.astrum.sky.core.time

interface TimeCorrection {

    fun compute(jd: JulianDay): Double

    companion object {

        val NONE = object : TimeCorrection {
            override fun compute(jd: JulianDay) = 0.0
        }
    }
}