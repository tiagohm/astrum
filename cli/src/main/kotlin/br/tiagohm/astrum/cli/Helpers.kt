package br.tiagohm.astrum.cli

import kotlin.math.abs
import kotlin.math.sign

fun String.sexagesimal(): Double {
    return toDoubleOrNull() ?: run {
        val parts = split(":", " ")

        if (parts.isEmpty()) error("Invalid sexagesimal format: $this")

        val h = parts[0].toIntOrNull() ?: error("Invalid sexagesimal format: $this")
        val m = if (parts.size <= 1) 0 else parts[1].toIntOrNull() ?: error("Invalid sexagesimal format: $this")
        val s = if (parts.size <= 2) 0.0 else parts[2].toDoubleOrNull() ?: error("Invalid sexagesimal format: $this")
        val sign = h.sign

        sign * (abs(h) + m / 60.0 + s / 3600.0)
    }
}