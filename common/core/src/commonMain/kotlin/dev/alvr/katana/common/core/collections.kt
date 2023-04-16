package dev.alvr.katana.common.core

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Suppress("NOTHING_TO_INLINE")
inline fun <T> ImmutableList<T>?.orEmpty() = this ?: persistentListOf()
