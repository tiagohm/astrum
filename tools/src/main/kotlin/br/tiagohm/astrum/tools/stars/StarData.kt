package br.tiagohm.astrum.tools.stars

import br.tiagohm.astrum.sky.core.math.Triad

interface StarData {

    val x0: Int

    val x1: Int

    val bvIndex: Int

    val magnitude: Int

    val dx0: Int

    val dx1: Int

    fun computeJ2000Position(z: ZoneData<*>, movementFactor: Double = 0.0): Triad {
        var pos = z.axis0
        pos *= x0 + movementFactor * dx0
        pos += z.axis1 * (x1 + movementFactor * dx1)
        pos += z.center
        return pos
    }

    /*
               _______________
     0    hip |               |
     1        |               |
     2        |_______________|
     3   cIds |_______________|
     4     x0 |               |
     5        |               |
     6        |               |
     7        |_______________|
     8     x1 |               |
     9        |               |
     10       |               |
     11       |_______________|
     12    bV |_______________|
     13   mag |_______________|
     14 spInt |               |
     15       |_______________|
     16   dx0 |               |
     17       |               |
     18       |               |
     19       |_______________|
     20   dx1 |               |
     21       |               |
     22       |               |
     23       |_______________|
     24   plx |               |
     25       |               |
     26       |               |
     27       |_______________|
     */
    data class Hipparcos(
        val hip: Int,
        override val x0: Int,
        override val x1: Int,
        override val bvIndex: Int,
        override val magnitude: Int,
        val spInt: Int,
        override val dx0: Int,
        override val dx1: Int,
        val plx: Int,
    ) : StarData {

        constructor(data: ByteArray) : this(
            data.readIntLe(0) and 0x00FFFFFF,
            data.readIntLe(4),
            data.readIntLe(8),
            data[12].toInt() and 0xFF,
            data[13].toInt() and 0xFF,
            data.readShort(14),
            data.readIntLe(16),
            data.readIntLe(20),
            data.readIntLe(24),
        )
    }

    /*
               _______________
	 0     x0 |               |
	 1        |_______        |
	 2     x1 |       |_______|
	 3        |               |
	 4        |_______________|
	 5    dx0 |___            |
	 6    dx1 |   |___________|
	 7        |_______        |
	 8     bV |_______|_______|
	 9    mag |_________|_____| bV
     */
    data class Tycho(
        override val x0: Int,
        override val x1: Int,
        override val bvIndex: Int,
        override val magnitude: Int,
        override val dx0: Int,
        override val dx1: Int,
    ) : StarData {

        constructor(data: ByteArray) : this(
            (data[0].toInt() and 0xFF) or
                    ((data[1].toInt() and 0xFF) shl 8) or
                    ((data[2].toInt() and 0x0F) shl 16),
            ((data[2].toInt() and 0xF0) shr 4) or
                    ((data[3].toInt() and 0xFF) shl 4) or
                    ((data[4].toInt() and 0x0F) shl 12),
            ((data[8].toInt() and 0xF0) shr 4) or
                    ((data[9].toInt() and 0x07) shl 4),
            data[9].toInt() and 0xFF shr 3,
            (data[5].toInt() and 0xFF) or
                    ((data[6].toInt() and 0x3F) shl 8),
            ((data[6].toInt() and 0xFF) shr 6) or
                    ((data[7].toInt() and 0xFF) shl 2) or
                    ((data[8].toInt() and 0x0F) shl 10)
        )
    }

    /*
               _______________
	 0     x0 |               |
	 1        |___________    |
	 2     x1 |           |___|
	 3        |_______        |
	 4     bV |_______|_______|
	 5    mag |_________|_____| bV
     */
    data class Nomad(
        override val x0: Int,
        override val x1: Int,
        override val bvIndex: Int,
        override val magnitude: Int,
    ) : StarData {

        constructor(data: ByteArray) : this(
            (data[0].toInt() and 0xFF) or
                    ((data[1].toInt() and 0xFF) shl 8) or
                    ((data[2].toInt() and 0x03) shl 16),
            ((data[2].toInt() and 0xF0) shr 2) or
                    ((data[3].toInt() and 0xFF) shl 6) or
                    ((data[4].toInt() and 0x0F) shl 14),
            ((data[4].toInt() and 0xF0) shr 4) or
                    ((data[5].toInt() and 0x07) shl 4),
            data[5].toInt() and 0xFF shr 3,
        )

        override val dx0 = 0

        override val dx1 = 0
    }

    companion object {

        private fun ByteArray.readShort(index: Int) = ((this[index].toInt() and 0xFF) shl 8) or
                ((this[index + 1].toInt() and 0xFF) shl 0)

        private fun ByteArray.readInt(index: Int) = ((this[index].toInt() and 0xFF) shl 24) or
                ((this[index + 1].toInt() and 0xFF) shl 16) or
                ((this[index + 2].toInt() and 0xFF) shl 8) or
                ((this[index + 3].toInt() and 0xFF) shl 0)

        private fun ByteArray.readIntLe(index: Int) = ((this[index].toInt() and 0xFF) shl 0) or
                ((this[index + 1].toInt() and 0xFF) shl 8) or
                ((this[index + 2].toInt() and 0xFF) shl 16) or
                ((this[index + 3].toInt() and 0xFF) shl 24)
    }
}