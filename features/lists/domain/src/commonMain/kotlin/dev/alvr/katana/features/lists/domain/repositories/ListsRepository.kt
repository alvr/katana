package dev.alvr.katana.features.lists.domain.repositories

import arrow.core.Either
import dev.alvr.katana.core.domain.failures.Failure
import dev.alvr.katana.features.lists.domain.models.MediaCollection
import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import dev.alvr.katana.features.lists.domain.models.lists.MediaList
import kotlinx.coroutines.flow.Flow

interface ListsRepository {
    val animeCollection: Flow<Either<Failure, MediaCollection<MediaEntry.Anime>>>
    val mangaCollection: Flow<Either<Failure, MediaCollection<MediaEntry.Manga>>>

    suspend fun updateList(entry: MediaList): Either<Failure, Unit>
}
