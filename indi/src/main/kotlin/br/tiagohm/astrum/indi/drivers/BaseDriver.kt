package br.tiagohm.astrum.indi.drivers

import br.tiagohm.astrum.indi.client.Client
import br.tiagohm.astrum.indi.client.ElementListener
import br.tiagohm.astrum.indi.client.PropertyElement

abstract class BaseDriver : Driver {

    private val elementListeners = ArrayList<ElementListener>(1)

    override var client: Client? = null
        protected set

    private val elementListener = object : ElementListener {
        override fun onElement(device: String, element: PropertyElement<*>) {
            if (device == name) {
                elementListeners.forEach { it.onElement(device, element) }
            }
        }
    }

    override fun registerElementListener(listener: ElementListener) {
        if (!elementListeners.contains(listener)) elementListeners.add(listener)
    }

    override fun unregisterElementListener(listener: ElementListener) {
        elementListeners.remove(listener)
    }

    override fun attach(client: Client) = also {
        this.client = client
        client.registerElementListener(elementListener)
    }

    override fun detach() = also {
        client?.unregisterElementListener(elementListener)
        client = null
    }
}