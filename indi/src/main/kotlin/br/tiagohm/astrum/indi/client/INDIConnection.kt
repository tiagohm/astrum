package br.tiagohm.astrum.indi.client

import br.tiagohm.astrum.indi.protocol.commands.Command

interface INDIConnection {

    fun connect()

    fun disconnect()

    fun sendCommand(command: Command)

    fun registerMessageListener(listener: MessageListener)

    fun unregisterMessageListener(listener: MessageListener)

    fun registerPropertyListener(listener: PropertyListener)

    fun unregisterPropertyListener(listener: PropertyListener)

    val isClosed: Boolean

    val isConnected: Boolean
}