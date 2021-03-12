package br.tiagohm.astrum.indi.protocol.vectors

import br.tiagohm.astrum.indi.protocol.Permission
import br.tiagohm.astrum.indi.protocol.State

data class NumberVector(
    override val device: String,
    override val name: String,
    override val label: String = name,
    override val group: String = "",
    override val state: State = State.IDLE,
    override val permission: Permission = Permission.READ_WRITE,
    override val timeout: Int = 0,
) : PropertyVector<Double>