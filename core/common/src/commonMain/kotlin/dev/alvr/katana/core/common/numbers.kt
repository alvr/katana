package dev.alvr.katana.core.common

val Double.Companion.zero get() = 0.0
fun Double?.orZero() = or(Double.zero)
fun Double?.or(default: Double) = this ?: default

val Float.Companion.zero get() = 0.0f
fun Float?.orZero() = or(Float.zero)
fun Float?.or(default: Float) = this ?: default

val Int.Companion.zero get() = 0
fun Int?.orZero() = or(Int.zero)
fun Int?.or(default: Int) = this ?: default

val Long.Companion.zero get() = 0L
fun Long?.orZero() = or(Long.zero)
fun Long?.or(default: Long) = this ?: default
