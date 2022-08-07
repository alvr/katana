package dev.alvr.katana.ui.lists.viewmodel

import dev.alvr.katana.ui.lists.entities.MediaListItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

private typealias Collection<T> = ImmutableMap<String, ImmutableList<T>>

internal data class ListsState(
    val animeCollection: Collection<MediaListItem.AnimeListItem> = persistentMapOf(),
    val mangaCollection: Collection<MediaListItem.MangaListItem> = persistentMapOf(),
    val currentAnimeList: ListState<MediaListItem.AnimeListItem> = ListState(),
    val currentMangaList: ListState<MediaListItem.MangaListItem> = ListState(),
    val currentAnimeListName: String? = null,
    val currentMangaListName: String? = null,
    val animeListNames: ImmutableList<String> = persistentListOf(),
    val mangaListNames: ImmutableList<String> = persistentListOf(),
) {
    data class ListState<T : MediaListItem>(
        val items: ImmutableList<T> = persistentListOf(),
        val isEmpty: Boolean = false,
        val isLoading: Boolean = false,
    )
}
