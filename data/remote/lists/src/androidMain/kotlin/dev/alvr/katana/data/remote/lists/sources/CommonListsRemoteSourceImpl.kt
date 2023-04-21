package dev.alvr.katana.data.remote.lists.sources

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.fetchPolicyInterceptor
import com.apollographql.apollo3.cache.normalized.watch
import com.apollographql.apollo3.interceptor.ApolloInterceptor
import dev.alvr.katana.common.core.catchUnit
import dev.alvr.katana.data.remote.base.extensions.optional
import dev.alvr.katana.data.remote.base.toFailure
import dev.alvr.katana.data.remote.base.type.MediaType
import dev.alvr.katana.data.remote.lists.MediaListCollectionQuery
import dev.alvr.katana.data.remote.lists.mappers.requests.toMutation
import dev.alvr.katana.data.remote.lists.mappers.responses.mediaList
import dev.alvr.katana.domain.lists.failures.ListsFailure
import dev.alvr.katana.domain.lists.models.MediaCollection
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.models.lists.MediaList
import dev.alvr.katana.domain.user.managers.UserIdManager
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

internal class CommonListsRemoteSourceImpl(
    private val client: ApolloClient,
    private val userId: UserIdManager,
    private val reloadInterceptor: ApolloInterceptor,
) : CommonListsRemoteSource {
    override suspend fun updateList(entry: MediaList) = Either.catchUnit {
        client.mutation(entry.toMutation()).execute()
    }.mapLeft { error ->
        Napier.e(error) { "There was an error updating the entry" }

        error.toFailure(
            network = ListsFailure.UpdatingList,
            response = ListsFailure.UpdatingList,
        )
    }

    override fun <T : MediaEntry> getMediaCollection(type: MediaType) = flow {
        val response = client
            .query(MediaListCollectionQuery(userId.getId().optional(), type))
            .fetchPolicyInterceptor(reloadInterceptor)
            .watch()
            .distinctUntilChanged()
            .map { res -> MediaCollection(res.data?.mediaList<T>(type).orEmpty()).right() }
            .distinctUntilChanged()
            .catch { error ->
                Napier.e(error) { "There was an error collecting the lists" }

                emit(
                    error.toFailure(
                        network = ListsFailure.GetMediaCollection,
                        response = ListsFailure.GetMediaCollection,
                    ).left(),
                )
            }

        emitAll(response)
    }
}
