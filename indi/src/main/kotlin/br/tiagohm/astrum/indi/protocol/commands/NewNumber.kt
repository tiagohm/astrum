package br.tiagohm.astrum.indi.protocol.commands

import br.tiagohm.astrum.indi.protocol.properties.Property
import org.redundent.kotlin.xml.xml

data class NewNumber(val property: Property<Double>) : Command {

    override fun toXml(): String {
        return xml("newNumberVector") {
            attribute("device", property.vector.device)
            attribute("name", property.vector.name)

            "oneNumber" {
                attribute("name", property.name)
                text("${property.value}")
            }
        }.toString(false)
    }
}