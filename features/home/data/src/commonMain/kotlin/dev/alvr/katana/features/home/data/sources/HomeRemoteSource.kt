package dev.alvr.katana.features.home.data.sources

import arrow.core.Either
import dev.alvr.katana.common.media.domain.models.entries.CommonMediaEntry
import dev.alvr.katana.common.media.domain.models.lists.MediaListType
import dev.alvr.katana.core.domain.failures.Failure
import kotlinx.coroutines.flow.Flow

internal interface HomeRemoteSource {
    fun trendingMedia(type: MediaListType): Flow<Either<Failure, List<CommonMediaEntry>>>

    fun popularMedia(type: MediaListType): Flow<Either<Failure, List<CommonMediaEntry>>>

    fun upcomingAnime(): Flow<Either<Failure, List<CommonMediaEntry>>>
}
