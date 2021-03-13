package br.tiagohm.astrum.indi.drivers

interface Driver {

    val name: String

    val executable: String

    val manufacturer: String

    val version: String

    fun <T> property(name: String): T?

    fun on()

    fun off()
}