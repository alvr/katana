package dev.alvr.katana.data.remote.lists.sources

import arrow.core.Either
import dev.alvr.katana.data.remote.base.type.MediaType
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.lists.models.MediaCollection
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.models.lists.MediaList
import kotlinx.coroutines.flow.Flow

internal interface CommonListsRemoteSource {
    suspend fun updateList(entry: MediaList): Either<Failure, Unit>
    fun <T : MediaEntry> getMediaCollection(type: MediaType): Flow<Either<Failure, MediaCollection<T>>>
}
