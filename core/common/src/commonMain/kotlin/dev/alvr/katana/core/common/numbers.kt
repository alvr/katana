@file:Suppress("NOTHING_TO_INLINE")

package dev.alvr.katana.core.common

val Double.Companion.zero inline get() = 0.0
inline fun Double?.orZero() = or(Double.zero)
inline fun Double?.or(default: Double) = this ?: default

val Float.Companion.zero inline get() = 0.0f
inline fun Float?.orZero() = or(Float.zero)
inline fun Float?.or(default: Float) = this ?: default

val Int.Companion.zero inline get() = 0
inline fun Int?.orZero() = or(Int.zero)
inline fun Int?.or(default: Int) = this ?: default

val Long.Companion.zero inline get() = 0L
inline fun Long?.orZero() = or(Long.zero)
inline fun Long?.or(default: Long) = this ?: default
