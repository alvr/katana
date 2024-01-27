package dev.alvr.katana.ui.lists.viewmodel

import dev.alvr.katana.ui.base.decompose.state.UiState
import dev.alvr.katana.ui.lists.entities.item.MediaListItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class ListState<out T : MediaListItem> internal constructor(
    internal val items: ImmutableList<T> = persistentListOf(),
    internal val name: String? = null,
    internal val isEmpty: Boolean = false,
    internal val isLoading: Boolean = false,
    internal val isError: Boolean = false,
) : UiState
