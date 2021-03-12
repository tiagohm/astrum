package br.tiagohm.astrum.indi.protocol.properties

import br.tiagohm.astrum.indi.protocol.vectors.NumberVector

data class NumberProperty(
    override val name: String,
    override val value: Double,
    override val vector: NumberVector,
    override val label: String = name,
) : Property<Double> {

    constructor(
        vector: NumberVector,
        data: Map<String, String>,
    ) : this(
        data["name"]!!,
        data["value"]?.toDoubleOrNull() ?: 0.0, // TODO: Sexagesimal
        vector,
        data["label"] ?: data["name"]!!,
    )
}
