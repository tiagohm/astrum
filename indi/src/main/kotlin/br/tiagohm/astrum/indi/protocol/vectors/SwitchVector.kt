package br.tiagohm.astrum.indi.protocol.vectors

import br.tiagohm.astrum.indi.protocol.Permission
import br.tiagohm.astrum.indi.protocol.State
import br.tiagohm.astrum.indi.protocol.properties.SwitchRule

data class SwitchVector(
    override val device: String,
    override val name: String,
    override val label: String = name,
    override val group: String = "",
    override val state: State = State.IDLE,
    override val permission: Permission = Permission.READ_WRITE,
    val rule: SwitchRule = SwitchRule.ANY_OF_MANY,
    override val timeout: Int = 0,
) : PropertyVector<Boolean>