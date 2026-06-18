package dev.alvr.katana.common.media.domain.repositories

import arrow.core.Either
import dev.alvr.katana.common.media.domain.models.MediaCollection
import dev.alvr.katana.common.media.domain.models.entries.MediaEntry
import dev.alvr.katana.common.media.domain.models.lists.MediaListStatus
import dev.alvr.katana.core.domain.failures.Failure
import kotlinx.coroutines.flow.Flow

interface MediaCollectionRepository {
    fun animeCollection(status: MediaListStatus): Flow<Either<Failure, MediaCollection<MediaEntry.Anime>>>

    fun mangaCollection(status: MediaListStatus): Flow<Either<Failure, MediaCollection<MediaEntry.Manga>>>
}
