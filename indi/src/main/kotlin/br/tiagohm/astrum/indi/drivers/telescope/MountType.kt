package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.protocol.SwitchElement

enum class MountType : SwitchElement {
    ALTAZ,
    EQUATORIAL;

    override val propName = "MOUNT_TYPE"

    override val elementName = name
}