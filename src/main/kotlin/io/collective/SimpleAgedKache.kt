package io.collective

import java.time.Clock

class TimedValue(
        val value: Any,
        val retention: Int,
        val createdAt: Long = System.currentTimeMillis()
)

class SimpleAgedKache {
    private var clock: Clock? = null
    private val cache = HashMap<Any, TimedValue>()

    constructor(clock: Clock?) {
        this.clock = clock;
    }

    constructor() {
    }

    fun put(key: Any, value: Any, retentionInMillis: Int) {
        cache[key] = TimedValue(value, retentionInMillis)
    }

    fun isEmpty(): Boolean {
        return size() == 0
    }

    fun size(): Int {
        val currentTime = clock?.millis() ?: System.currentTimeMillis()
        for ((key, value) in cache.entries) {
            if (value.createdAt + value.retention < currentTime) {
                cache.remove(key)
            }
        }

        return cache.size
    }

    fun get(key: Any): Any? {
        return cache[key]?.value
    }
}