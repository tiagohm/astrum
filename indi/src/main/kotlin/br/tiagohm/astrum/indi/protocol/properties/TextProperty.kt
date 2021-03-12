package br.tiagohm.astrum.indi.protocol.properties

import br.tiagohm.astrum.indi.protocol.vectors.PropertyVector

data class TextProperty(
    override val name: String,
    override val value: String,
    override val vector: PropertyVector<String>,
    override val label: String = name,
) : Property<String> {

    constructor(
        vector: PropertyVector<String>,
        data: Map<String, String>,
    ) : this(
        data["name"]!!,
        data["value"] ?: "",
        vector,
        data["label"] ?: data["name"]!!,
    )
}
