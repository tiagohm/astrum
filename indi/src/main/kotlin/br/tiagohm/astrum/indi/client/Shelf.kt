package br.tiagohm.astrum.indi.client

open class Shelf<T> {

    private val data = HashMap<String, HashMap<String, HashMap<String, T>>>()

    @Synchronized
    fun get(first: String, second: String, third: String): T? {
        return data[first]?.get(second)?.get(third)
    }

    @Synchronized
    fun set(first: String, second: String, third: String, value: T) {
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

    @Synchronized
    fun clear() = apply { data.clear() }

    @Synchronized
    fun clear(first: String) = apply { data[first]?.clear() }

    @Synchronized
    fun clear(first: String, second: String) = apply { data[first]?.get(second)?.clear() }

    @Synchronized
    fun remove(first: String) = apply { data.remove(first) }

    @Synchronized
    fun remove(first: String, second: String) = apply { data[first]?.remove(second) }

    @Synchronized
    fun remove(first: String, second: String, third: String) = apply { data[first]?.get(second)?.remove(third) }

    @Synchronized
    fun has(first: String) = data.containsKey(first)

    @Synchronized
    fun has(first: String, second: String) = has(first) && data[first]!!.containsKey(second)

    @Synchronized
    fun has(first: String, second: String, third: String) = has(first, second) && data[first]!![second]!!.containsKey(third)

    @Synchronized
    fun keys() = data.keys.toList()

    @Synchronized
    fun keys(first: String) = data[first]?.keys?.toList() ?: emptyList()

    @Synchronized
    fun keys(first: String, second: String) = data[first]?.get(second)?.keys?.toList() ?: emptyList()

    @Synchronized
    fun values(first: String, second: String) = data[first]?.get(second)?.values?.toList() ?: emptyList()
}