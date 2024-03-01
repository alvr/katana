package dev.alvr.katana.features.lists.data.sources.manga

import arrow.core.Either
import dev.alvr.katana.core.domain.failures.Failure
import dev.alvr.katana.features.lists.domain.models.MediaCollection
import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import kotlinx.coroutines.flow.Flow

internal interface MangaListsRemoteSource {
    val mangaCollection: Flow<Either<Failure, MediaCollection<MediaEntry.Manga>>>
}
