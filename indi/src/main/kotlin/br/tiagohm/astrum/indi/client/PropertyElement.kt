package br.tiagohm.astrum.indi.client

import br.tiagohm.astrum.indi.protocol.Element
import br.tiagohm.astrum.indi.protocol.State

data class PropertyElement<T>(
    val element: Element<T>,
    val value: T,
    val isReadOnly: Boolean = false,
    val state: State = State.IDLE,
    // Number
    val min: Double = 0.0,
    val max: Double = 0.0,
    val step: Double = 0.0,
    // BLOB
    val size: Int = 0,
    val format: String = "",
) : Element<T> by element