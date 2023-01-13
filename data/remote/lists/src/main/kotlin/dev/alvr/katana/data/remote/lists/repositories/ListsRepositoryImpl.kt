package dev.alvr.katana.data.remote.lists.repositories

import dev.alvr.katana.data.remote.lists.sources.CommonListsRemoteSource
import dev.alvr.katana.data.remote.lists.sources.anime.AnimeListsRemoteSource
import dev.alvr.katana.data.remote.lists.sources.manga.MangaListsRemoteSource
import dev.alvr.katana.domain.lists.models.lists.MediaList
import dev.alvr.katana.domain.lists.repositories.ListsRepository

internal class ListsRepositoryImpl(
    private val commonSource: CommonListsRemoteSource,
    private val animeSource: AnimeListsRemoteSource,
    private val mangaSource: MangaListsRemoteSource,
) : ListsRepository {
    override val animeCollection get() = animeSource.animeCollection
    override val mangaCollection get() = mangaSource.mangaCollection

    override suspend fun updateList(entry: MediaList) = commonSource.updateList(entry)
}
