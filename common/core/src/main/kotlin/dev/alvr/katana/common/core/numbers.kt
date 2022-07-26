package dev.alvr.katana.common.core

@Suppress("NOTHING_TO_INLINE")
inline fun Double?.or(default: Double = Double.zero) = this ?: default
val Double.Companion.zero inline get() = 0.0

@Suppress("NOTHING_TO_INLINE")
inline fun Float?.or(default: Float = Float.zero) = this ?: default
val Float.Companion.zero inline get() = 0.0f

@Suppress("NOTHING_TO_INLINE")
inline fun Int?.or(default: Int = Int.zero) = this ?: default
val Int.Companion.zero inline get() = 0

@Suppress("NOTHING_TO_INLINE")
inline fun Long?.or(default: Long = Long.zero) = this ?: default
val Long.Companion.zero inline get() = 0L
