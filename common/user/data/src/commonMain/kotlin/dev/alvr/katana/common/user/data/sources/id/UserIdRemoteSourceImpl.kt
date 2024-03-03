package dev.alvr.katana.common.user.data.sources.id

import arrow.core.Either
import co.touchlab.kermit.Logger
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import dev.alvr.katana.common.user.data.UserIdQuery
import dev.alvr.katana.common.user.data.mappers.responses.invoke
import dev.alvr.katana.common.user.domain.failures.UserFailure
import dev.alvr.katana.core.common.catchUnit
import dev.alvr.katana.core.remote.toFailure

internal class UserIdRemoteSourceImpl(
    private val client: ApolloClient,
) : UserIdRemoteSource {
    override suspend fun getUserId() = Either.catch {
        userIdHandler(FetchPolicy.CacheOnly)
    }.mapLeft { error ->
        Logger.e(error) { "Was not possible to get the userId" }

        error.toFailure(cache = UserFailure.GettingUserId)
    }

    override suspend fun saveUserId() = Either.catchUnit {
        userIdHandler(FetchPolicy.NetworkOnly)
    }.mapLeft { error ->
        Logger.e(error) { "Was not possible to save the userId" }

        error.toFailure(
            network = UserFailure.FetchingUser,
            response = UserFailure.SavingUser,
        )
    }

    private suspend fun userIdHandler(policy: FetchPolicy) = client
        .query(UserIdQuery())
        .fetchPolicy(policy)
        .execute()
        .data()
}
