package dev.alvr.katana.core.preferences.utils

import dev.alvr.katana.core.preferences.KatanaPreferenceKey
import eu.anifantakis.lib.ksafe.KSafe
import kotlinx.coroutines.flow.Flow

suspend inline operator fun <reified T> KSafe.get(key: KatanaPreferenceKey<T>, defaultValue: T): T =
    get(key.toString(), defaultValue)

inline fun <reified T> KSafe.getFlow(key: KatanaPreferenceKey<T>, defaultValue: T): Flow<T> =
    getFlow(key.toString(), defaultValue)

suspend inline operator fun <reified T> KSafe.set(key: KatanaPreferenceKey<T>, value: T) {
    put(key.toString(), value)
}
