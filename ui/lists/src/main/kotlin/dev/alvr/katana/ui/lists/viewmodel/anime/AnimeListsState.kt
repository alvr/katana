package dev.alvr.katana.ui.lists.viewmodel.anime

import dev.alvr.katana.ui.lists.entities.MediaListItem

internal data class AnimeListsState(
    val loading: Boolean = false,
    val currentListItems: List<MediaListItem.AnimeListItem> = emptyList(),
)
