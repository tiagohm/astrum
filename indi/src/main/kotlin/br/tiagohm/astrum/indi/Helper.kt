package br.tiagohm.astrum.indi

import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.math.abs

internal val EMPTY_BYTE_ARRAY = byteArrayOf()

internal val ISO_8601 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

internal fun parseUTCOffset(offset: String): ZoneId {
    return offset
        .replace(".", ":")
        .split(":")
        .let {
            val h = if (it[0] == "Z") 0 else it[0].toInt()
            val m = if (it.size == 2) it[1].toInt() else 0

            if (h == 0 && m == 0) {
                ZoneId.of("Z")
            } else {
                val sign = if (h >= 0) "+" else "-"
                ZoneId.of(String.format("$sign%02d:%02d", abs(h), m))
            }
        }
}

internal fun parseISO8601(text: String, offset: String): ZonedDateTime {
    val utc = ISO_8601.parse(text).toInstant()
    return ZonedDateTime.ofInstant(utc, parseUTCOffset(offset))
}