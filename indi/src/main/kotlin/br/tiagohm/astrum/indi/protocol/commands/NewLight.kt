package br.tiagohm.astrum.indi.protocol.commands

import br.tiagohm.astrum.indi.protocol.State
import br.tiagohm.astrum.indi.protocol.properties.Property
import org.redundent.kotlin.xml.xml

data class NewLight(val property: Property<State>) : Command {

    override fun toXml(): String {
        return xml("newLightVector") {
            attribute("device", property.vector.device)
            attribute("name", property.vector.name)

            "oneLight" {
                attribute("name", property.name)
                text(property.value.label)
            }
        }.toString(false)
    }
}