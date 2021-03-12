package br.tiagohm.astrum.indi.protocol.properties

import br.tiagohm.astrum.indi.protocol.vectors.SwitchVector

data class SwitchProperty(
    override val name: String,
    override val value: Boolean,
    override val vector: SwitchVector,
    override val label: String = name,
) : Property<Boolean> {

    constructor(
        vector: SwitchVector,
        data: Map<String, String>,
    ) : this(
        data["name"]!!,
        data["value"] == "On",
        vector,
        data["label"] ?: data["name"]!!,
    )
}
