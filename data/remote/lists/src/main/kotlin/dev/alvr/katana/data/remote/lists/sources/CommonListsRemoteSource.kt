package dev.alvr.katana.data.remote.lists.sources

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.fetchPolicyInterceptor
import com.apollographql.apollo3.cache.normalized.watch
import dev.alvr.katana.data.remote.base.extensions.optional
import dev.alvr.katana.data.remote.base.failures.CommonRemoteFailure
import dev.alvr.katana.data.remote.base.interceptors.ReloadInterceptor
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
import io.github.aakira.napier.Napier
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

internal class CommonListsRemoteSource @Inject constructor(
    private val client: ApolloClient,
    private val userId: UserIdManager,
    private val reloadInterceptor: ReloadInterceptor,
) {
    suspend fun updateList(entry: MediaList) = Either.catch(
        f = { client.mutation(entry.toMutation()).execute() },
        fe = { error ->
            Napier.e(error) { "There was an error updating the entry" }

            when (CommonRemoteFailure(error)) {
                CommonRemoteFailure.Network -> ListsFailure.UpdatingList
                CommonRemoteFailure.Response -> ListsFailure.UpdatingList
                CommonRemoteFailure.Cache -> Failure.Unknown
                CommonRemoteFailure.Unknown -> Failure.Unknown
            }
        },
    ).void()

    inline fun <reified T : MediaEntry> getMediaCollection(type: MediaType) = flow {
        val response = client
            .query(MediaListCollectionQuery(userId.getId().optional(), type))
            .fetchPolicyInterceptor(reloadInterceptor)
            .watch()
            .distinctUntilChanged()
            .map { res -> MediaCollection(res.data?.mediaList<T>().orEmpty()).right() }
            .distinctUntilChanged()
            .catch { error ->
                Napier.e(error) { "There was an error collecting the lists" }

                emit(
                    when (CommonRemoteFailure(error)) {
                        CommonRemoteFailure.Network -> ListsFailure.GetMediaCollection
                        CommonRemoteFailure.Response -> ListsFailure.GetMediaCollection
                        CommonRemoteFailure.Cache -> Failure.Unknown
                        CommonRemoteFailure.Unknown -> Failure.Unknown
                    }.left(),
                )
            }

        emitAll(response)
    }
}