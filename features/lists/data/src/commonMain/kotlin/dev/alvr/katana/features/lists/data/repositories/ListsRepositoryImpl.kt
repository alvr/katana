package dev.alvr.katana.features.lists.data.repositories

import dev.alvr.katana.features.lists.data.sources.ListsRemoteSource
import dev.alvr.katana.features.lists.domain.models.lists.MediaList
import dev.alvr.katana.features.lists.domain.repositories.ListsRepository

internal class ListsRepositoryImpl(private val remoteSource: ListsRemoteSource) : ListsRepository {
    override val animeCollection = remoteSource.animeCollection
    override val mangaCollection = remoteSource.mangaCollection

    override suspend fun updateList(entry: MediaList) = remoteSource.updateList(entry)
}
