package br.tiagohm.astrum.core

interface TimeCorrection {

    fun compute(jd: Double): Double

    companion object {

        val NONE = object : TimeCorrection {
            override fun compute(jd: Double) = 0.0
        }
    }
}