package dev.alvr.katana.buildlogic

import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource
import kotlin.time.TimeSource.Monotonic.ValueTimeMark
import kotlin.time.TimedValue

@OptIn(ExperimentalTime::class)
class TimedCache<K, V>(private val duration: Duration) {
    private data class TimedValue<T>(val value: T, val duration: ValueTimeMark)

    private val map = ConcurrentHashMap<K, TimedValue<V>>()

    @PublishedApi
    internal operator fun set(key: K, value: V) {
        map.putIfAbsent(key, TimedValue(value, TimeSource.Monotonic.markNow()))
    }

    @PublishedApi
    internal operator fun get(key: K): V? = map[key]?.let { timedValue ->
        if (timedValue.duration.elapsedNow() > duration) {
            map.remove(key)
            null
        } else {
            timedValue.value
        }
    }

    inline fun getOrPut(key: K, defaultValue: () -> V): V =
        this[key] ?: defaultValue().also { this[key] = it }
}
