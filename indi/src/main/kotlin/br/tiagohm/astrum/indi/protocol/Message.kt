package br.tiagohm.astrum.indi.protocol

/**
 * A [message] associated with a [device] or entire system.
 */
data class Message(
    val device: String,
    val message: String,
    val fatal: Boolean = false,
)