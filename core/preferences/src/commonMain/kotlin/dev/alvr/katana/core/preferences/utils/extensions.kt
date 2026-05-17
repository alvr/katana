package dev.alvr.katana.core.preferences.utils

import dev.alvr.katana.core.preferences.KatanaPreferenceKey
import eu.anifantakis.lib.ksafe.KSafe
import kotlinx.coroutines.flow.Flow

inline operator fun <reified T> KSafe.get(key: KatanaPreferenceKey<T>, defaultValue: T): T =
    getDirect(key.toString(), defaultValue)

inline fun <reified T> KSafe.flow(key: KatanaPreferenceKey<T>, defaultValue: T): Flow<T> =
    getFlow(key.toString(), defaultValue)

inline operator fun <reified T> KSafe.set(key: KatanaPreferenceKey<T>, value: T) {
    putDirect(key.toString(), value)
}
