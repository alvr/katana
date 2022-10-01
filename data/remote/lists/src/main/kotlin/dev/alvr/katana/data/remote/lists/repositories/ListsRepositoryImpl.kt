package dev.alvr.katana.data.remote.lists.repositories

import dev.alvr.katana.data.remote.lists.sources.ListsRemoteSource
import dev.alvr.katana.domain.lists.models.lists.MediaList
import dev.alvr.katana.domain.lists.repositories.ListsRepository
import javax.inject.Inject

internal class ListsRepositoryImpl @Inject constructor(
    private val source: ListsRemoteSource,
) : ListsRepository {
    override val animeCollection get() = source.animeCollection
    override val mangaCollection get() = source.mangaCollection

    override suspend fun updateList(entry: MediaList) = source.updateList(entry)
}
