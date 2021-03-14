package br.tiagohm.astrum.indi.client

interface ElementListener {

    fun onElement(device: String, element: PropertyElement<*>)
}