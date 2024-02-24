package dev.alvr.katana.buildlogic.utils

import com.esotericsoftware.yamlbeans.YamlReader
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource
import org.gradle.api.Project

private const val CACHE_DURATION = 15

private typealias YamlBuildConfig = Map<String, FlavorBuildConfig>
private typealias FlavorBuildConfig = Map<String, ArrayList<Map<String, String>>>
private typealias PlatformBuildConfig = Pair<List<BuildConfig>, List<BuildConfig>>

private val ymlMap = TimedCache<String, PlatformBuildConfig>(CACHE_DURATION.minutes)

internal class BuildConfig(map: Map<String, String>) {
    val type: String by map
    val name: String by map
    val value: String by map
}

internal fun Project.katanaBuildConfig(
    android: (List<BuildConfig>) -> Unit,
    ios: (List<BuildConfig>) -> Unit,
) {
    val flavor = providers.gradleProperty("katana.flavor").getOrElse("dev")
    val (androidConfig, iosConfig) = ymlMap.getOrPut(flavor) {
        val yml = YamlReader(rootProject.file("config/build_config.yml").reader()).parse<YamlBuildConfig>()
        yml["android"].map(flavor) to yml["ios"].map(flavor)
    }

    android(androidConfig)
    ios(iosConfig)
}

private fun FlavorBuildConfig?.map(flavor: String?) =
    this?.get(flavor)
        .orEmpty()
        .map(::BuildConfig)

private inline fun <reified T> YamlReader.parse(): T = read(T::class.java)

@OptIn(ExperimentalTime::class)
private class TimedCache<K, V>(private val duration: Duration) {
    private data class TimedValue<T>(val value: T, val duration: TimeSource.Monotonic.ValueTimeMark)

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
