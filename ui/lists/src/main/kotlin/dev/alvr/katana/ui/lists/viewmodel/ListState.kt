package dev.alvr.katana.ui.lists.viewmodel

import dev.alvr.katana.common.core.empty
import dev.alvr.katana.ui.lists.entities.MediaListItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal data class ListState<T : MediaListItem>(
    val items: ImmutableList<T> = persistentListOf(),
    val name: String? = null,
    val search: String = String.empty,
    val isEmpty: Boolean = false,
    val isLoading: Boolean = false,
)
