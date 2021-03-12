package br.tiagohm.astrum.indi.protocol.commands

import org.redundent.kotlin.xml.xml

/**
 * Command to ask Device to define all Properties, or those for a specific Device or specific
 * Property, for which it is responsible.
 *
 * @param device Name of Device, or all if empty.
 * @param name Name of Property, or all if empty.
 */
data class GetProperties(
    val device: String = "",
    val name: String = "",
) : Command {

    override fun toXml(): String {
        return xml("getProperties") {
            attribute("version", "1.7")
            if (device.isNotEmpty()) attribute("device", device)
            if (name.isNotEmpty()) attribute("name", name)
        }.toString(false)
    }

    companion object {

        val ALL = GetProperties()
    }
}