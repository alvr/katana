package dev.alvr.katana.domain.lists.repositories

import arrow.core.Either
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.lists.models.MediaCollection
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.models.lists.MediaList
import kotlinx.coroutines.flow.Flow

interface ListsRepository {
    val animeCollection: Flow<Either<Failure, MediaCollection<MediaEntry.Anime>>>
    val mangaCollection: Flow<Either<Failure, MediaCollection<MediaEntry.Manga>>>

    suspend fun updateList(entry: MediaList): Either<Failure, Unit>
}
