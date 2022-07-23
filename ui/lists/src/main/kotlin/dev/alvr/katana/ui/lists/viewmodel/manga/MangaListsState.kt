package dev.alvr.katana.ui.lists.viewmodel.manga

import dev.alvr.katana.ui.lists.entities.MediaListItem

internal data class MangaListsState(
    val currentListItems: List<MediaListItem.MangaListItem> = emptyList(),
    val isEmpty: Boolean = false,
    val isLoading: Boolean = false,
)
