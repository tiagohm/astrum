package br.tiagohm.astrum.indi.protocol

import br.tiagohm.astrum.indi.client.Command
import org.redundent.kotlin.xml.xml

interface Property<T> : Command<T> {

    val propName: String

    val elementName: String

    val type: String

    override fun toXML(device: String, value: T) = xml("new${type}Vector") {
        attribute("device", device)
        attribute("name", propName)

        "one$type" {
            attribute("name", elementName)
            text(valueToText(value))
        }
    }.toString(false)

    /**
     * Converts the [value] to XML text.
     */
    fun valueToText(value: T): String
}