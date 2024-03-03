package dev.alvr.katana.features.lists.data.repositories

import dev.alvr.katana.features.lists.data.sources.CommonListsRemoteSource
import dev.alvr.katana.features.lists.data.sources.anime.AnimeListsRemoteSource
import dev.alvr.katana.features.lists.data.sources.manga.MangaListsRemoteSource
import dev.alvr.katana.features.lists.domain.models.lists.MediaList
import dev.alvr.katana.features.lists.domain.repositories.ListsRepository

internal class ListsRepositoryImpl(
    private val commonSource: CommonListsRemoteSource,
    private val animeSource: AnimeListsRemoteSource,
    private val mangaSource: MangaListsRemoteSource,
) : ListsRepository {
    override val animeCollection get() = animeSource.animeCollection
    override val mangaCollection get() = mangaSource.mangaCollection

    override suspend fun updateList(entry: MediaList) = commonSource.updateList(entry)
}
