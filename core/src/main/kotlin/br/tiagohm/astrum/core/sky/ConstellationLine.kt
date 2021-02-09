package br.tiagohm.astrum.core.sky

import java.io.BufferedInputStream
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

data class ConstellationLine(
    val raLow: Double,
    val raHigh: Double,
    val decLow: Double,
    val constellation: Constellation,
) {

    companion object {

        val lines by lazy { load() }

        private val LINE_SPLI_REGEX = Regex("\\s+")

        private fun parseHMS(hms: String, declination: Boolean = false): Double {
            return hms.split(":").let {
                if (!declination) it[0].toInt() + it[1].toInt() / 60.0 + it[2].toInt() / 3600.0
                else it[0].toInt().let { h -> (it[1].toInt() / 60.0).let { m -> if (h < 0) h - m else h + m } }
            }
        }

        fun load(): List<ConstellationLine> {
            return Thread.currentThread().contextClassLoader.getResourceAsStream("CONSTELLATION_SPANS.dat")?.let {
                val lines = BufferedInputStream(it).bufferedReader().readLines()
                val res = ArrayList<ConstellationLine>(357)

                for (line in lines) {
                    if (line.isEmpty() || line.startsWith("#")) continue

                    val parts = line.trim().split(LINE_SPLI_REGEX)

                    if (parts.size != 4) throw IOException("Bad line: $line")

                    val raLow = parseHMS(parts[0])
                    val raHigh = parseHMS(parts[1])
                    val decLow = parseHMS(parts[2], true)
                    val constellation = Constellation.valueOf(parts[3].toUpperCase())

                    res.add(ConstellationLine(raLow, raHigh, decLow, constellation))
                }

                Collections.unmodifiableList(res)
            } ?: emptyList()
        }
    }
}
