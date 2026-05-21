package dev.alvr.katana.core.preferences

import kotlin.jvm.JvmInline

@JvmInline
value class KatanaPreferenceKey<T>(@PublishedApi internal val prefKey: String) {
    override fun toString(): String = prefKey
}
