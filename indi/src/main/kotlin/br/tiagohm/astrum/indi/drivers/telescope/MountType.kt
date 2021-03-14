package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.protocol.SwitchProperty

enum class MountType : SwitchProperty {
    ALTAZ,
    EQ_FORK,
    EQ_GEM;

    override val propName = "MOUNT_TYPE"

    override val elementName = name
}