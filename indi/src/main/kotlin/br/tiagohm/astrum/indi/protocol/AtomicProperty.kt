package br.tiagohm.astrum.indi.protocol

import br.tiagohm.astrum.indi.client.Command
import org.redundent.kotlin.xml.xml

class AtomicProperty<T>(vararg val properties: Property<T>) : Command<Array<T>> {

    override fun toXML(device: String, value: Array<T>): String {
        val propName = properties[0].propName
        val type = properties[0].type

        return xml("new${type}Vector") {
            attribute("device", device)
            attribute("name", propName)

            for (i in value.indices) {
                "one${type}" {
                    attribute("name", properties[i].elementName)
                    text(properties[i].valueToText(value[i]))
                }
            }
        }.toString(false)
    }
}