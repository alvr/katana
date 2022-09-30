package dev.alvr.katana.data.remote.lists.sources

import arrow.core.Either
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.watch
import dev.alvr.katana.data.remote.base.extensions.optional
import dev.alvr.katana.data.remote.base.failures.CommonRemoteFailure
import dev.alvr.katana.data.remote.base.type.MediaType
import dev.alvr.katana.data.remote.lists.MediaListCollectionQuery
import dev.alvr.katana.data.remote.lists.mappers.requests.toMutation
import dev.alvr.katana.data.remote.lists.mappers.responses.mediaList
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.lists.failures.ListsFailure
import dev.alvr.katana.domain.lists.models.MediaCollection
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.models.lists.MediaList
import dev.alvr.katana.domain.user.managers.UserIdManager
import javax.inject.Inject
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

internal class ListsRemoteSource @Inject constructor(
    private val client: ApolloClient,
    private val userId: UserIdManager,
) {
    val animeCollection get() = getMediaCollection<MediaEntry.Anime>(MediaType.ANIME)
    val mangaCollection get() = getMediaCollection<MediaEntry.Manga>(MediaType.MANGA)

    suspend fun updateList(entry: MediaList) = Either.catch(
        f = { client.mutation(entry.toMutation()).execute() },
        fe = { error ->
            when (CommonRemoteFailure(error)) {
                CommonRemoteFailure.NetworkFailure -> ListsFailure.UpdatingList
                CommonRemoteFailure.ResponseFailure -> ListsFailure.UpdatingList
                CommonRemoteFailure.CacheFailure -> Failure.Unknown
                CommonRemoteFailure.UnknownFailure -> Failure.Unknown
            }
        },
    ).void()

    private inline fun <reified T : MediaEntry> getMediaCollection(type: MediaType) = flow {
        val response = client
            .query(MediaListCollectionQuery(userId.getId().optional(), type))
            .watch()
            .distinctUntilChanged()
            .map { res -> MediaCollection(res.data?.mediaList<T>().orEmpty()) }
            .distinctUntilChanged()
        emitAll(response)
    }
}
