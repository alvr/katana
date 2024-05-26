package dev.alvr.katana.features.lists.data.sources

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import co.touchlab.kermit.Logger
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.fetchPolicyInterceptor
import com.apollographql.apollo3.cache.normalized.watch
import com.apollographql.apollo3.exception.CacheMissException
import com.apollographql.apollo3.interceptor.ApolloInterceptor
import dev.alvr.katana.common.user.domain.managers.UserIdManager
import dev.alvr.katana.core.common.catchUnit
import dev.alvr.katana.core.remote.executeOrThrow
import dev.alvr.katana.core.remote.optional
import dev.alvr.katana.core.remote.toFailure
import dev.alvr.katana.core.remote.type.MediaType
import dev.alvr.katana.features.lists.data.MediaListCollectionQuery
import dev.alvr.katana.features.lists.data.mappers.requests.toMutation
import dev.alvr.katana.features.lists.data.mappers.responses.invoke
import dev.alvr.katana.features.lists.domain.failures.ListsFailure
import dev.alvr.katana.features.lists.domain.models.MediaCollection
import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import dev.alvr.katana.features.lists.domain.models.lists.MediaList
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

internal class CommonListsRemoteSourceImpl(
    private val client: ApolloClient,
    private val userId: UserIdManager,
    private val reloadInterceptor: ApolloInterceptor,
) : CommonListsRemoteSource {
    override suspend fun updateList(entry: MediaList) = Either.catchUnit {
        client.mutation(entry.toMutation()).executeOrThrow()
    }.mapLeft { error ->
        Logger.e(error) { "There was an error updating the entry" }

        error.toFailure(
            network = ListsFailure.UpdatingList,
            response = ListsFailure.UpdatingList,
        )
    }

    override fun <T : MediaEntry> getMediaCollection(type: MediaType) = flow {
        val response = client
            .query(MediaListCollectionQuery(userId.getId().optional, type))
            .fetchPolicyInterceptor(reloadInterceptor)
            .watch()
            .filterNot { it.exception is CacheMissException }
            .distinctUntilChanged { old, new -> old.data == new.data }
            .map { res -> MediaCollection(res.dataAssertNoErrors<T>(type)).right() }
            .catch { error ->
                Logger.e(error) { "There was an error collecting the lists" }

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
