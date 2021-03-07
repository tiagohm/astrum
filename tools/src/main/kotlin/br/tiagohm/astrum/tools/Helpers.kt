package br.tiagohm.astrum.tools

import okio.BufferedSource

@Suppress("NOTHING_TO_INLINE")
inline fun BufferedSource.readDouble() = Double.fromBits(readLong())

@Suppress("NOTHING_TO_INLINE")
inline fun BufferedSource.readDoubleLe() = Double.fromBits(readLongLe())