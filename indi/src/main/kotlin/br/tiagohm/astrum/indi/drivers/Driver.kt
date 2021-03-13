package br.tiagohm.astrum.indi.drivers

interface Driver {

    val name: String

    val executable: String

    fun <T> property(name: String): T?

    fun on()

    fun off()
}