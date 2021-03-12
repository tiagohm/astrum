package br.tiagohm.astrum.indi.protocol.commands

import br.tiagohm.astrum.indi.protocol.properties.Property
import org.redundent.kotlin.xml.xml

data class NewText(val property: Property<String>) : Command {

    override fun toXml(): String {
        return xml("newTextVector") {
            attribute("device", property.vector.device)
            attribute("name", property.vector.name)

            "oneText" {
                attribute("name", property.name)
                text(property.value)
            }
        }.toString(false)
    }
}