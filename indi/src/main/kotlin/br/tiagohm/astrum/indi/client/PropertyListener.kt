package br.tiagohm.astrum.indi.client

import br.tiagohm.astrum.indi.protocol.properties.Property

interface PropertyListener {

    fun onProperty(property: Property<*>)
}