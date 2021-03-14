package br.tiagohm.astrum.indi.client.parser

data class XMLTag(
    val name: String,
    val attributes: Map<String, String> = emptyMap(),
    val text: String = "",
    val children: List<XMLTag> = emptyList(),
)