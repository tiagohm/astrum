package br.tiagohm.astrum.indi.drivers.telescope

import br.tiagohm.astrum.indi.protocol.NumberElement

enum class MountAxis : NumberElement {
    PRIMARY,
    SECONDARY;

    override val propName = "MOUNT_AXES"

    override val elementName = name
}