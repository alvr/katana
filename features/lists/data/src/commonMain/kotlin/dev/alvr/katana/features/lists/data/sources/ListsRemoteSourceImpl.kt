package dev.alvr.katana.features.lists.data.sources

import arrow.core.Either
import co.touchlab.kermit.Logger
import com.apollographql.apollo.ApolloClient
import dev.alvr.katana.common.media.domain.models.lists.MediaList
import dev.alvr.katana.core.common.catchUnit
import dev.alvr.katana.core.remote.executeOrThrow
import dev.alvr.katana.core.remote.toFailure
import dev.alvr.katana.features.lists.data.mappers.requests.toMutation
import dev.alvr.katana.features.lists.domain.failures.ListsFailure
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
internal class ListsRemoteSourceImpl(private val client: ApolloClient) : ListsRemoteSource {
    override suspend fun updateList(entry: MediaList) =
        Either.catchUnit { client.mutation(entry.toMutation()).executeOrThrow() }
            .mapLeft { error ->
                Logger.e(error) { "There was an error updating the entry" }

                error.toFailure(network = ListsFailure.UpdatingList, response = ListsFailure.UpdatingList)
            }
}
