package br.tiagohm.astrum.indi.protocol.commands

import org.redundent.kotlin.xml.xml

/**
 * Command to control whether setBLOBs should be sent to this channel from a given [device].
 * They can be turned off completely by setting NEVER (the default),
 * allowed to be intermixed with other INDI commands by setting ALSO or made the
 * only command by setting ONLY.
 */
class EnableBLOB(
    val device: String,
    val name: String = "",
    val value: EnableBLOBState = EnableBLOBState.NEVER,
) : Command {

    override fun toXml(): String {
        return xml("enableBLOB") {
            attribute("device", device)
            if (name.isNotEmpty()) attribute("name", name)
            text(value.label)
        }.toString(true)
    }
}