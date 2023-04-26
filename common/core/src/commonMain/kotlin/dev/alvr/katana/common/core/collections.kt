@file:Suppress("NOTHING_TO_INLINE")

package dev.alvr.katana.common.core

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

inline fun <T> ImmutableList<T>?.orEmpty() = this ?: persistentListOf()
