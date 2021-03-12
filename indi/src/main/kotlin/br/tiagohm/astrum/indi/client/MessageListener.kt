package br.tiagohm.astrum.indi.client

import br.tiagohm.astrum.indi.protocol.Message

interface MessageListener {

    fun onMessage(message: Message)
}