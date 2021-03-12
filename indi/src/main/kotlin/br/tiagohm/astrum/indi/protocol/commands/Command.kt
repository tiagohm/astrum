package br.tiagohm.astrum.indi.protocol.commands

/**
 * Command to send to a Device.
 */
interface Command {

    /**
     * Gets the command text.
     */
    fun toXml(): String
}