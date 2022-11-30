package dev.alvr.katana.data.remote.lists.sources.manga

import arrow.core.Either
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.lists.models.MediaCollection
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import kotlinx.coroutines.flow.Flow

internal interface MangaListsRemoteSource {
    val mangaCollection: Flow<Either<Failure, MediaCollection<MediaEntry.Manga>>>
}
