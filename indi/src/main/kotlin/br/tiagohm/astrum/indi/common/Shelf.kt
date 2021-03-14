package br.tiagohm.astrum.indi.common

internal class Shelf {

    private val data = HashMap<String, HashMap<String, HashMap<String, Any>>>()

    @Suppress("UNCHECKED_CAST")
    fun <T> get(first: String, second: String, third: String): T? {
        return data[first]?.get(second)?.get(third) as? T
    }

    fun set(first: String, second: String, third: String, value: Any) {
        if (data.containsKey(first)) {
            if (data[first]!!.containsKey(second)) {
                data[first]!![second]!![third] = value
            } else {
                data[first]!![second] = hashMapOf(third to value)
            }
        } else {
            data[first] = hashMapOf(second to hashMapOf(third to value))
        }
    }

    fun clear() = apply { data.clear() }

    fun clear(first: String) = apply { data[first]?.clear() }

    fun clear(first: String, second: String) = apply { data[first]?.get(second)?.clear() }

    fun remove(first: String) = apply { data.remove(first) }

    fun remove(first: String, second: String) = apply { data[first]?.remove(second) }

    fun remove(first: String, second: String, third: String) = apply { data[first]?.get(second)?.remove(third) }

    fun has(first: String) = data.containsKey(first)

    fun has(first: String, second: String) = has(first) && data[first]!!.containsKey(second)

    fun has(first: String, second: String, third: String) = has(first, second) && data[first]!![second]!!.containsKey(third)

    fun keys(): Set<String> = data.keys

    fun keys(first: String): Set<String> = data[first]?.keys ?: emptySet()

    fun keys(first: String, second: String) = data[first]?.get(second)?.keys ?: emptySet()
}