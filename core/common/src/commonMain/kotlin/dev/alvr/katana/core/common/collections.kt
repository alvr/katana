package dev.alvr.katana.core.common

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

fun <T> ImmutableList<T>?.orEmpty() = this ?: persistentListOf()
