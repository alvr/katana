package dev.alvr.katana.buildlogic.utils

import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource
import kotlin.time.TimeSource.Monotonic.ValueTimeMark

@OptIn(ExperimentalTime::class)
internal class TimedCache<K, V>(private val duration: Duration) {
    private data class TimedValue<T>(val value: T, val duration: ValueTimeMark)

    private val map = ConcurrentHashMap<K, TimedValue<V>>()

    private operator fun set(key: K, value: V) {
        map.putIfAbsent(key, TimedValue(value, TimeSource.Monotonic.markNow()))
    }

    private operator fun get(key: K): V? = map[key]?.let { timedValue ->
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
