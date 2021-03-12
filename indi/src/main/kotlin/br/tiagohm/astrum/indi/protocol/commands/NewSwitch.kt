package br.tiagohm.astrum.indi.protocol.commands

import br.tiagohm.astrum.indi.protocol.properties.Property
import org.redundent.kotlin.xml.xml

data class NewSwitch(
    val property: Property<Boolean>,
    val value: Boolean,
) : Command {

    override fun toXml(): String {
        return xml("newSwitchVector") {
            attribute("device", property.vector.device)
            attribute("name", property.vector.name)

            "oneSwitch" {
                attribute("name", property.name)
                text(if (value) "On" else "Off")
            }
        }.toString(false)
    }
}