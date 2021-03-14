package br.tiagohm.astrum.indi.client

interface Command<T> {

    fun toXML(device: String, value: T): String
}