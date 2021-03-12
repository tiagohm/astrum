package br.tiagohm.astrum.indi.protocol.properties

import br.tiagohm.astrum.indi.protocol.State
import br.tiagohm.astrum.indi.protocol.vectors.LightVector

data class LightProperty(
    override val name: String,
    override val value: State,
    override val vector: LightVector,
    override val label: String = name,
) : Property<State> {

    constructor(
        vector: LightVector,
        data: Map<String, String>,
    ) : this(
        data["name"]!!,
        data["value"]?.toUpperCase()?.let { State.valueOf(it) } ?: State.IDLE,
        vector,
        data["label"] ?: data["name"]!!,
    )
}
