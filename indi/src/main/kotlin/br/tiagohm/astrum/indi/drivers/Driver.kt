package br.tiagohm.astrum.indi.drivers

import br.tiagohm.astrum.indi.client.Client
import br.tiagohm.astrum.indi.client.ElementListener
import br.tiagohm.astrum.indi.protocol.*
import kotlin.reflect.KClass

// TODO: Serial/Ethernet (junto com connect --tcp --addrees --port --baudrate)
@Suppress("UNCHECKED_CAST")
interface Driver {

    val client: Client

    val name: String

    val executable: String

    val isOn: Boolean
        get() = switch(Connection.CONNECT)

    fun registerElementListener(listener: ElementListener)

    fun register(element: Element<*>) = client.register(element, name)

    fun <T> register(element: KClass<out Enum<T>>) where T : Enum<T>, T : Element<*> = client.register(element, name)

    fun unregisterElementListener(listener: ElementListener)

    fun unregister(element: Element<*>) = client.unregister(element, name)

    fun <T> unregister(element: KClass<out Enum<T>>) where T : Enum<T>, T : Element<*> = client.unregister(element, name)

    fun initialize()

    fun detach()

    fun <T> send(element: Element<T>, value: T) = client.send(name, element, value)

    fun <E : Element<T>, T> send(elements: Array<E>, values: Array<T>) = client.send(name, Property(*elements), values)

    fun on() = send(Connection.CONNECT, true)

    fun off() = send(Connection.DISCONNECT, true)

    fun info() = emptyMap<String, Any>()

    fun status() = emptyMap<String, Any>()

    fun <T> element(element: Element<T>) = client.element(name, element.propName, element.elementName)

    fun <T> value(element: Element<T>) = element(element)?.value as? T

    fun <T> has(element: Element<T>) = element(element) != null

    fun switch(element: SwitchElement) = value(element) == true

    fun number(element: NumberElement, value: Double = 0.0) = value(element) ?: value

    fun text(element: TextElement, value: String = "") = value(element) ?: value

    fun light(element: LightElement, value: State = State.IDLE) = value(element) ?: value

    fun blob(element: BLOBElement) = value(element)
}