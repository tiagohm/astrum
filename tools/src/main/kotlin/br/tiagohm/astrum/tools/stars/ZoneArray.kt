package br.tiagohm.astrum.tools.stars

import br.tiagohm.astrum.sky.core.math.Triad
import okio.buffer
import okio.source
import java.io.File
import java.io.IOException
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sqrt

class ZoneArray<T : StarData>(
    val zones: Array<ZoneData<T>>,
    val level: Int,
    val magMin: Int,
    val magRange: Int,
    val magStep: Int,
) {

    var starPositionScale: Double = 0.0
        private set

    fun initTriangle(
        index: Int,
        c0: Triad,
        c1: Triad,
        c2: Triad,
    ) {
        val center = (c0 + c1 + c2).normalized
        val axis0 = (NORTH * center).normalized
        val axis1 = center * axis0

        zones[index] = zones[index].copy(center, axis0, axis1)

        var mu0 = (c0 - center).dot(axis0)
        var mu1 = (c0 - center).dot(axis1)
        var f = 1 / sqrt(1 - mu0 * mu0 - mu1 * mu1)

        starPositionScale = max(abs(mu0) * f, starPositionScale)
        starPositionScale = max(abs(mu1) * f, starPositionScale)

        mu0 = (c1 - center).dot(axis0)
        mu1 = (c1 - center).dot(axis1)
        f = 1 / sqrt(1 - mu0 * mu0 - mu1 * mu1)

        starPositionScale = max(abs(mu0) * f, starPositionScale)
        starPositionScale = max(abs(mu1) * f, starPositionScale)

        mu0 = (c2 - center).dot(axis0)
        mu1 = (c2 - center).dot(axis1)
        f = 1 / sqrt(1 - mu0 * mu0 - mu1 * mu1)

        starPositionScale = max(abs(mu0) * f, starPositionScale)
        starPositionScale = max(abs(mu1) * f, starPositionScale)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ZoneArray<*>

        if (!zones.contentEquals(other.zones)) return false

        return true
    }

    override fun hashCode() = zones.contentHashCode()

    companion object {

        private val NORTH = Triad.Z

        private const val FILE_MAGIC = 0x835F040A

        fun load(file: File, geodesicGrid: GeodesicGrid): ZoneArray<out StarData> {
            return file.source().use {
                val buffer = it.buffer()

                val magic = buffer.readIntLe().toLong() and 0xFFFFFFFF
                val type = buffer.readIntLe()
                val major = buffer.readIntLe()
                val minor = buffer.readIntLe()
                val level = buffer.readIntLe()
                val magMin = buffer.readIntLe()
                val magRange = buffer.readIntLe()
                val magStep = buffer.readIntLe()

                System.err.println("$magic $type $major $minor $level $magMin $magRange $magStep")

                if (magic != FILE_MAGIC) {
                    throw IOException("Not a catalogue file")
                }

                val numberOfZones = 20 shl (level shl 1)
                val zoneSizes = Array(numberOfZones) { buffer.readIntLe() }

                when (type) {
                    0 -> {
                        val zones = Array(numberOfZones) { i ->
                            ZoneData(Triad.ZERO, Triad.ZERO, Triad.ZERO, Array(zoneSizes[i]) { StarData.Hipparcos(buffer.readByteArray(28)) })
                        }

                        ZoneArray(zones, level, magMin, magRange, magStep).also {
                            geodesicGrid.visitTriangles(level) { _, index, c0, c1, c2 ->
                                it.initTriangle(index, c0, c1, c2)
                            }
                        }
                    }
//                  TODO:
//                  1 -> {
//                      val zones = Array(numberOfZones) { i -> Array(zoneSizes[i]) { StarData.Two(buffer.readByteArray(10)) } }
//                      ZoneArray(zones, level, magMin, magRange, magStep)
//                  }
//                  2 -> {
//                      val zones = Array(numberOfZones) { i -> Array(zoneSizes[i]) { StarData.Three(buffer.readByteArray(6)) } }
//                      ZoneArray(zones, level, magMin, magRange, magStep)
//                  }
                    else -> throw IOException("Bad file type")
                }
            }
        }
    }
}