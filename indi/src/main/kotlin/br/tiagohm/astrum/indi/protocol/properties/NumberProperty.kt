package br.tiagohm.astrum.indi.protocol.properties

import br.tiagohm.astrum.indi.client.INDIConnection
import org.redundent.kotlin.xml.xml

data class NumberProperty(
    override val device: String,
    override val name: String,
    override val value: Double,
) : Number(), Property<Double> {

    override fun send(connection: INDIConnection): Boolean {
        val command = xml("newNumberVector") {
            val (devName, pName) = name.split(":")

            attribute("device", device)
            attribute("name", devName)

            "oneNumber" {
                attribute("name", pName)
                text("$value")
            }
        }.toString(false)

        connection.write(command)

        return true
    }

    operator fun plus(n: Number) = copy(value = value + n.toDouble())

    operator fun minus(n: Number) = copy(value = value - n.toDouble())

    operator fun times(n: Number) = copy(value = value * n.toDouble())

    operator fun div(n: Number) = copy(value = value / n.toDouble())

    operator fun rem(n: Number) = copy(value = value % n.toDouble())

    operator fun unaryMinus() = copy(value = -value)

    operator fun unaryPlus() = this

    override fun toByte() = value.toInt().toByte()

    override fun toChar() = value.toChar()

    override fun toDouble() = value

    override fun toFloat() = value.toFloat()

    override fun toInt() = value.toInt()

    override fun toLong() = value.toLong()

    override fun toShort() = value.toInt().toShort()
}