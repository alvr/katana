package dev.alvr.katana.features.lists.data.sources.anime

import arrow.core.Either
import dev.alvr.katana.core.domain.failures.Failure
import dev.alvr.katana.features.lists.domain.models.MediaCollection
import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import kotlinx.coroutines.flow.Flow

internal interface AnimeListsRemoteSource {
    val animeCollection: Flow<Either<Failure, MediaCollection<MediaEntry.Anime>>>
}
