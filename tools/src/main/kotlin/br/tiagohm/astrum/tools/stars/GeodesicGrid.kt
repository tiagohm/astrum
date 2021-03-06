package br.tiagohm.astrum.tools.stars

import br.tiagohm.astrum.sky.core.math.Triad

class GeodesicGrid(val level: Int) {

    private val triangles: Array<Array<Triangle>>

    init {
        var numberOfTriangles = 20

        triangles = Array(level + 1) {
            Array(numberOfTriangles) { Triangle.ZERO }.also { numberOfTriangles *= 4 }
        }

        for (i in 0..19) {
            val corners = ICOSAHEDRON_TRIANGLES[i]

            initTriangle(
                0, i,
                ICOSAHEDRON_CORNERS[corners[0]],
                ICOSAHEDRON_CORNERS[corners[1]],
                ICOSAHEDRON_CORNERS[corners[2]],
            )
        }
    }

    private fun initTriangle(
        level: Int,
        index: Int,
        c0: Triad,
        c1: Triad,
        c2: Triad,
    ) {
        val e0 = (c1 + c2).normalized
        val e1 = (c2 + c0).normalized
        val e2 = (c0 + c1).normalized

        triangles[level][index] = Triangle(e0, e1, e2)

        val nextLevel = level + 1

        if (nextLevel <= this.level) {
            val nextIndex = index * 4

            initTriangle(nextLevel, nextIndex + 0, c0, e2, e1)
            initTriangle(nextLevel, nextIndex + 1, e2, c1, e0)
            initTriangle(nextLevel, nextIndex + 2, e1, e0, c2)
            initTriangle(nextLevel, nextIndex + 3, e0, e1, e2)
        }
    }

    fun visitTriangles(
        level: Int,
        visitor: (Int, Int, Triad, Triad, Triad) -> Unit,
    ) {
        for (i in 0..19) {
            val corners = ICOSAHEDRON_TRIANGLES[i]

            visitTriangles(
                0, i,
                ICOSAHEDRON_CORNERS[corners[0]],
                ICOSAHEDRON_CORNERS[corners[1]],
                ICOSAHEDRON_CORNERS[corners[2]],
                level,
                visitor,
            )
        }
    }

    private fun visitTriangles(
        level: Int,
        index: Int,
        c0: Triad,
        c1: Triad,
        c2: Triad,
        maxLevel: Int,
        visitor: (Int, Int, Triad, Triad, Triad) -> Unit,
    ) {
        visitor(level, index, c0, c1, c2)

        val nextLevel = level + 1

        if (nextLevel <= maxLevel) {
            val t = triangles[level][index]
            val nextIndex = index * 4

            visitTriangles(nextLevel, nextIndex + 0, c0, t.e2, t.e1, maxLevel, visitor)
            visitTriangles(nextLevel, nextIndex + 1, t.e2, c1, t.e0, maxLevel, visitor)
            visitTriangles(nextLevel, nextIndex + 2, t.e1, t.e0, c2, maxLevel, visitor)
            visitTriangles(nextLevel, nextIndex + 3, t.e0, t.e1, t.e2, maxLevel, visitor)
        }
    }

    companion object {

        private const val ICOSAHEDRON_G = 1.618033988749895 // 0.5*(1.0+sqrt(5.0))
        private const val ICOSAHEDRON_B = 0.5257311121191336 // 1.0/sqrt(1.0+ICOSAHEDRON_G*ICOSAHEDRON_G)
        private const val ICOSAHEDRON_A = ICOSAHEDRON_B * ICOSAHEDRON_G

        private val ICOSAHEDRON_CORNERS = arrayOf(
            Triad(ICOSAHEDRON_A, -ICOSAHEDRON_B, 0.0),
            Triad(ICOSAHEDRON_A, ICOSAHEDRON_B, 0.0),
            Triad(-ICOSAHEDRON_A, ICOSAHEDRON_B, 0.0),
            Triad(-ICOSAHEDRON_A, -ICOSAHEDRON_B, 0.0),
            Triad(0.0, ICOSAHEDRON_A, -ICOSAHEDRON_B),
            Triad(0.0, ICOSAHEDRON_A, ICOSAHEDRON_B),
            Triad(0.0, -ICOSAHEDRON_A, ICOSAHEDRON_B),
            Triad(0.0, -ICOSAHEDRON_A, -ICOSAHEDRON_B),
            Triad(-ICOSAHEDRON_B, 0.0, ICOSAHEDRON_A),
            Triad(ICOSAHEDRON_B, 0.0, ICOSAHEDRON_A),
            Triad(ICOSAHEDRON_B, 0.0, -ICOSAHEDRON_A),
            Triad(-ICOSAHEDRON_B, 0.0, -ICOSAHEDRON_A),
        )

        private val ICOSAHEDRON_TRIANGLES = arrayOf(
            intArrayOf(1, 0, 10), //  1
            intArrayOf(0, 1, 9), //  0
            intArrayOf(0, 9, 6), // 12
            intArrayOf(9, 8, 6), //  9
            intArrayOf(0, 7, 10), // 16
            intArrayOf(6, 7, 0), //  6
            intArrayOf(7, 6, 3), //  7
            intArrayOf(6, 8, 3), // 14
            intArrayOf(11, 10, 7), // 11
            intArrayOf(7, 3, 11), // 18
            intArrayOf(3, 2, 11), //  3
            intArrayOf(2, 3, 8), //  2
            intArrayOf(10, 11, 4), // 10
            intArrayOf(2, 4, 11), // 19
            intArrayOf(5, 4, 2), //  5
            intArrayOf(2, 8, 5), // 15
            intArrayOf(4, 1, 10), // 17
            intArrayOf(4, 5, 1), //  4
            intArrayOf(5, 9, 1), // 13
            intArrayOf(8, 9, 5),  //  8
        )
    }
}