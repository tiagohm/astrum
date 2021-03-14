package br.tiagohm.astrum.indi.client

import br.tiagohm.astrum.indi.protocol.Permission

data class PropertyAttribute(
    val permission: Permission = Permission.READ_WRITE,
    val size: Int = 0,
    val format: String = "",
)
