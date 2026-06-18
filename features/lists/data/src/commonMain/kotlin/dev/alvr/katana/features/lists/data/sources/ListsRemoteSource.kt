package dev.alvr.katana.features.lists.data.sources

import arrow.core.Either
import dev.alvr.katana.common.media.domain.models.lists.MediaList
import dev.alvr.katana.core.domain.failures.Failure

internal interface ListsRemoteSource {
    suspend fun updateList(entry: MediaList): Either<Failure, Unit>
}
