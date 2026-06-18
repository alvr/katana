package dev.alvr.katana.common.media.data.sources

import arrow.core.left
import arrow.core.right
import co.touchlab.kermit.Logger
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.interceptor.ApolloInterceptor
import com.apollographql.cache.normalized.fetchPolicyInterceptor
import dev.alvr.katana.common.media.data.MediaListCollectionQuery
import dev.alvr.katana.common.media.data.mappers.requests.invoke
import dev.alvr.katana.common.media.data.mappers.responses.invoke
import dev.alvr.katana.common.media.domain.failures.MediaFailure
import dev.alvr.katana.common.media.domain.models.MediaCollection
import dev.alvr.katana.common.media.domain.models.entries.MediaEntry
import dev.alvr.katana.common.media.domain.models.lists.MediaListStatus
import dev.alvr.katana.common.user.domain.usecases.GetUserIdUseCase
import dev.alvr.katana.core.domain.usecases.invoke
import dev.alvr.katana.core.remote.optional
import dev.alvr.katana.core.remote.toFailure
import dev.alvr.katana.core.remote.type.MediaType
import dev.alvr.katana.core.remote.watchFiltered
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
internal class MediaCollectionRemoteSourceImpl(
    private val client: ApolloClient,
    private val getUserId: GetUserIdUseCase,
    private val reloadInterceptor: ApolloInterceptor,
) : MediaCollectionRemoteSource {
    override fun animeCollection(status: MediaListStatus) =
        getMediaCollection<MediaEntry.Anime>(MediaType.ANIME, status)

    override fun mangaCollection(status: MediaListStatus) =
        getMediaCollection<MediaEntry.Manga>(MediaType.MANGA, status)

    private inline fun <reified T : MediaEntry> getMediaCollection(type: MediaType, status: MediaListStatus) = flow {
        val response =
            client
                .query(MediaListCollectionQuery(getUserId().map { it.id }.optional, type, status()))
                .fetchPolicyInterceptor(reloadInterceptor)
                .watchFiltered()
                .distinctUntilChanged { old, new -> old.data == new.data }
                .map { res -> MediaCollection(res.dataAssertNoErrors<T>(type)).right() }
                .catch { error ->
                    Logger.e(error) { "There was an error collecting the media" }

                    emit(
                        error
                            .toFailure(
                                network = MediaFailure.GetMediaCollection,
                                response = MediaFailure.GetMediaCollection,
                            )
                            .left()
                    )
                }

        emitAll(response)
    }
}
