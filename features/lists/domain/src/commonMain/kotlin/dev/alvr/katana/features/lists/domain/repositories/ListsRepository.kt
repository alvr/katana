package dev.alvr.katana.features.lists.domain.repositories

import arrow.core.Either
import dev.alvr.katana.common.media.domain.models.lists.MediaList
import dev.alvr.katana.core.domain.failures.Failure

interface ListsRepository {
    suspend fun updateList(entry: MediaList): Either<Failure, Unit>
}
