package dev.alvr.katana.common.media.data.repositories

import dev.alvr.katana.common.media.data.sources.MediaCollectionRemoteSource
import dev.alvr.katana.common.media.domain.models.lists.MediaListStatus
import dev.alvr.katana.common.media.domain.repositories.MediaCollectionRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
internal class MediaCollectionRepositoryImpl(private val remoteSource: MediaCollectionRemoteSource) :
    MediaCollectionRepository {
    override fun animeCollection(status: MediaListStatus) = remoteSource.animeCollection(status)

    override fun mangaCollection(status: MediaListStatus) = remoteSource.mangaCollection(status)
}
