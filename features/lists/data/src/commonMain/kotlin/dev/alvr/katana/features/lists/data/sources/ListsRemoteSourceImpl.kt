package dev.alvr.katana.features.lists.data.sources

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import co.touchlab.kermit.Logger
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.interceptor.ApolloInterceptor
import com.apollographql.cache.normalized.fetchPolicyInterceptor
import dev.alvr.katana.common.user.domain.usecases.GetUserIdUseCase
import dev.alvr.katana.core.common.catchUnit
import dev.alvr.katana.core.domain.usecases.invoke
import dev.alvr.katana.core.remote.executeOrThrow
import dev.alvr.katana.core.remote.optional
import dev.alvr.katana.core.remote.toFailure
import dev.alvr.katana.core.remote.type.MediaType
import dev.alvr.katana.core.remote.watchFiltered
import dev.alvr.katana.features.lists.data.MediaListCollectionQuery
import dev.alvr.katana.features.lists.data.mappers.requests.toMutation
import dev.alvr.katana.features.lists.data.mappers.responses.invoke
import dev.alvr.katana.features.lists.domain.failures.ListsFailure
import dev.alvr.katana.features.lists.domain.models.MediaCollection
import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import dev.alvr.katana.features.lists.domain.models.lists.MediaList
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
internal class ListsRemoteSourceImpl(
    private val client: ApolloClient,
    private val getUserId: GetUserIdUseCase,
    private val reloadInterceptor: ApolloInterceptor,
) : ListsRemoteSource {
    override val animeCollection = getMediaCollection<MediaEntry.Anime>(MediaType.ANIME)
    override val mangaCollection = getMediaCollection<MediaEntry.Manga>(MediaType.MANGA)

    override suspend fun updateList(entry: MediaList) =
        Either.catchUnit { client.mutation(entry.toMutation()).executeOrThrow() }
            .mapLeft { error ->
                Logger.e(error) { "There was an error updating the entry" }

                error.toFailure(network = ListsFailure.UpdatingList, response = ListsFailure.UpdatingList)
            }

    /**
     * Produces a flow that emits the remote media collection for the specified media type and updates when the remote data changes.
     *
     * Emits `Either.Right(MediaCollection)` when a successful response is received; on errors emits `Either.Left(ListsFailure.GetMediaCollection)`.
     *
     * @param type The media type (e.g., `MediaType.ANIME` or `MediaType.MANGA`) to fetch.
     * @return A `Flow<Either<ListsFailure, MediaCollection>>` that publishes distinct updates based on the response `data`.
     */
    private inline fun <reified T : MediaEntry> getMediaCollection(type: MediaType) = flow {
        val response =
            client
                .query(MediaListCollectionQuery(getUserId().map { it.id }.optional, type))
                .fetchPolicyInterceptor(reloadInterceptor)
                .watchFiltered()
                .distinctUntilChanged { old, new -> old.data == new.data }
                .map { res -> MediaCollection(res.dataAssertNoErrors<T>(type)).right() }
                .catch { error ->
                    Logger.e(error) { "There was an error collecting the lists" }

                    emit(
                        error
                            .toFailure(
                                network = ListsFailure.GetMediaCollection,
                                response = ListsFailure.GetMediaCollection,
                            )
                            .left()
                    )
                }

        emitAll(response)
    }
}
