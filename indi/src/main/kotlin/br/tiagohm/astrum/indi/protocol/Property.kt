package br.tiagohm.astrum.indi.protocol

import br.tiagohm.astrum.indi.client.Command
import org.redundent.kotlin.xml.xml

class Property<T>(vararg val elements: Element<T>) : Command<Array<T>> {

    override fun toXML(device: String, value: Array<T>): String {
        val propName = elements[0].propName
        val type = elements[0].type

        return xml("new${type}Vector") {
            attribute("device", device)
            attribute("name", propName)

            for (i in value.indices) {
                "one${type}" {
                    attribute("name", elements[i].elementName)
                    text(elements[i].valueToText(value[i]))
                }
            }
        }.toString(false)
    }
}