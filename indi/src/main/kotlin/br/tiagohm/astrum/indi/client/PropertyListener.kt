package br.tiagohm.astrum.indi.client

interface PropertyListener {

    fun onProperty(device: String, propName: String, elementName: String, attr: PropertyAttribute, value: Any)
}