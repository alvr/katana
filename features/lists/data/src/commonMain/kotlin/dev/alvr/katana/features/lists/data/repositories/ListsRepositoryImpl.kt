package dev.alvr.katana.features.lists.data.repositories

import dev.alvr.katana.features.lists.data.sources.ListsRemoteSource
import dev.alvr.katana.features.lists.domain.models.lists.MediaList
import dev.alvr.katana.features.lists.domain.repositories.ListsRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
internal class ListsRepositoryImpl(private val remoteSource: ListsRemoteSource) : ListsRepository {
    override val animeCollection = remoteSource.animeCollection
    override val mangaCollection = remoteSource.mangaCollection

    override suspend fun updateList(entry: MediaList) = remoteSource.updateList(entry)
}
