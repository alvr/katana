package dev.alvr.katana.ui.lists.viewmodel.manga

import dev.alvr.katana.ui.lists.entities.MediaListItem

internal data class MangaListsState(
    val loading: Boolean = false,
    val currentListItems: List<MediaListItem.MangaListItem> = emptyList(),
)
