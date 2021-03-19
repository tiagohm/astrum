package br.tiagohm.astrum.indi.protocol

import br.tiagohm.astrum.indi.client.Command
import org.redundent.kotlin.xml.xml

interface Element<T> : Command<T> {

    val propName: String

    val elementName: String

    val type: ElementType

    override fun toXML(device: String, value: T) = xml("new${type.text}Vector") {
        attribute("device", device)
        attribute("name", propName)

        "one${type.text}" {
            attribute("name", elementName)
            text(convert(value))
        }
    }.toString(false)

    fun convert(value: T): String
}