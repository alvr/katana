package dev.alvr.katana.ui.lists.viewmodel.anime

import dev.alvr.katana.ui.lists.entities.MediaListItem

internal data class AnimeListsState(
    val currentListItems: List<MediaListItem.AnimeListItem> = emptyList(),
    val isEmpty: Boolean = false,
    val isLoading: Boolean = false,
)
