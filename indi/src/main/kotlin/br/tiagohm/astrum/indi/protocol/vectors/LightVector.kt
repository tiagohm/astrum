package br.tiagohm.astrum.indi.protocol.vectors

import br.tiagohm.astrum.indi.protocol.Permission
import br.tiagohm.astrum.indi.protocol.State

data class LightVector(
    override val device: String,
    override val name: String,
    override val label: String = name,
    override val group: String = "",
    override val state: State = State.IDLE,
) : PropertyVector<State> {

    override val permission = Permission.READ

    override val timeout = 0
}