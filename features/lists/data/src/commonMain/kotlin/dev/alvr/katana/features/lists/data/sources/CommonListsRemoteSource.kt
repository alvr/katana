package dev.alvr.katana.features.lists.data.sources

import arrow.core.Either
import dev.alvr.katana.core.domain.failures.Failure
import dev.alvr.katana.core.remote.type.MediaType
import dev.alvr.katana.features.lists.domain.models.MediaCollection
import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import dev.alvr.katana.features.lists.domain.models.lists.MediaList
import kotlinx.coroutines.flow.Flow

internal interface CommonListsRemoteSource {
    suspend fun updateList(entry: MediaList): Either<Failure, Unit>
    fun <T : MediaEntry> getMediaCollection(type: MediaType): Flow<Either<Failure, MediaCollection<T>>>
}
