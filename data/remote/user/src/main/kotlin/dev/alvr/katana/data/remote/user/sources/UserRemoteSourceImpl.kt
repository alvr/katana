package dev.alvr.katana.data.remote.user.sources

import arrow.core.Either
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import dev.alvr.katana.data.remote.base.toFailure
import dev.alvr.katana.data.remote.user.UserIdQuery
import dev.alvr.katana.data.remote.user.mappers.responses.invoke
import dev.alvr.katana.domain.user.failures.UserFailure
import io.github.aakira.napier.Napier
import javax.inject.Inject

internal class UserRemoteSourceImpl @Inject constructor(
    private val client: ApolloClient,
) : UserRemoteSource {
    override suspend fun getUserId() = Either.catch(
        f = { userIdHandler(FetchPolicy.CacheOnly) },
        fe = { error ->
            Napier.e(error) { "Was not possible to get the userId" }

            error.toFailure(cache = UserFailure.GettingUserId)
        },
    )

    override suspend fun saveUserId() = Either.catch(
        f = { userIdHandler(FetchPolicy.NetworkOnly) },
        fe = { error ->
            Napier.e(error) { "Was not possible to save the userId" }

            error.toFailure(
                network = UserFailure.FetchingUser,
                response = UserFailure.SavingUser,
            )
        },
    ).void()

    private suspend fun userIdHandler(policy: FetchPolicy) = client
        .query(UserIdQuery())
        .fetchPolicy(policy)
        .execute()
        .data()
}
