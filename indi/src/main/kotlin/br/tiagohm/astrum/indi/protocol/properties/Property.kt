package br.tiagohm.astrum.indi.protocol.properties

import br.tiagohm.astrum.indi.client.INDIConnection

interface Property<T> {

    val device: String

    val name: String

    val value: T

    fun send(connection: INDIConnection): Boolean
}