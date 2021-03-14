package br.tiagohm.astrum.indi.protocol

import br.tiagohm.astrum.indi.client.Command
import org.redundent.kotlin.xml.xml

interface Element<T> : Command<T> {

    /**
     * The property name.
     */
    val propName: String

    /**
     * The element name.
     */
    val elementName: String

    /**
     * The element type.
     */
    val type: ElementType

    override fun toXML(device: String, value: T) = xml("new${type.text}Vector") {
        attribute("device", device)
        attribute("name", propName)

        "one${type.text}" {
            attribute("name", elementName)
            text(valueToText(value))
        }
    }.toString(false)

    /**
     * Converts the [value] to XML text.
     */
    fun valueToText(value: T): String
}