package dev.alvr.katana.ui.lists.viewmodel

import dev.alvr.katana.ui.lists.entities.MediaListItem

internal data class ListsState<T : MediaListItem>(
    val currentListItems: List<T> = emptyList(),
    val isEmpty: Boolean = false,
    val isLoading: Boolean = false,
)
