package br.tiagohm.astrum.indi.drivers

import br.tiagohm.astrum.indi.client.Client
import br.tiagohm.astrum.indi.client.ElementListener
import br.tiagohm.astrum.indi.client.PropertyElement

abstract class BaseDriver(
    final override val client: Client,
    final override val name: String,
    final override val executable: String,
) : Driver {

    private val elementListeners = ArrayList<ElementListener>(1)

    private val elementListener = object : ElementListener {
        override fun onElement(device: String, element: PropertyElement<*>) {
            if (device == name) {
                elementListeners.forEach { it.onElement(device, element) }
            }
        }
    }

    init {
        client.registerElementListener(elementListener)
    }

    override fun registerElementListener(listener: ElementListener) {
        if (!elementListeners.contains(listener)) elementListeners.add(listener)
    }

    override fun unregisterElementListener(listener: ElementListener) {
        elementListeners.remove(listener)
    }

    override fun initialize() {
        register(Connection::class)
        register(ConnectionMode::class)
        register(Debug::class)
        register(DevicePort::class)
        register(BaudRate::class)

        client.fetchProperties(name)
        client.enableBLOB(name)
    }

    override fun detach() = client.unregisterElementListener(elementListener)
}