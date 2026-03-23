package dev.alvr.katana.common.user.data.sources

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import co.touchlab.kermit.Logger
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.cache.normalized.FetchPolicy
import com.apollographql.apollo.cache.normalized.fetchPolicy
import dev.alvr.katana.common.user.data.UserIdQuery
import dev.alvr.katana.common.user.data.UserInfoQuery
import dev.alvr.katana.common.user.data.mappers.responses.invoke
import dev.alvr.katana.common.user.domain.failures.UserFailure
import dev.alvr.katana.common.user.domain.models.UserInfo
import dev.alvr.katana.core.common.catchUnit
import dev.alvr.katana.core.domain.failures.Failure
import dev.alvr.katana.core.remote.executeOrThrow
import dev.alvr.katana.core.remote.toFailure
import dev.alvr.katana.core.remote.watchFiltered
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

internal class UserRemoteSourceImpl(private val client: ApolloClient) : UserRemoteSource {
    override val userInfo =
        client
            .query(UserInfoQuery())
            .fetchPolicy(FetchPolicy.CacheAndNetwork)
            .watchFiltered()
            .map<_, Either<Failure, UserInfo>> { res -> res.dataAssertNoErrors().right() }
            .catch { error ->
                Logger.e(tag = LogTag, throwable = error) { "Was not possible to get the user info" }

                emit(
                    error
                        .toFailure(network = UserFailure.GettingUserInfo, response = UserFailure.GettingUserInfo)
                        .left()
                )
            }

    override suspend fun getUserId() =
        Either.catch { userIdHandler(FetchPolicy.CacheOnly) }
            .mapLeft { error ->
                Logger.e(tag = LogTag, throwable = error) { "Was not possible to get the userId" }

                error.toFailure(cache = UserFailure.GettingUserId)
            }

    override suspend fun saveUserId() =
        Either.catchUnit { userIdHandler(FetchPolicy.NetworkOnly) }
            .mapLeft { error ->
                Logger.e(tag = LogTag, throwable = error) { "Was not possible to save the userId" }

                error.toFailure(network = UserFailure.FetchingUser, response = UserFailure.SavingUser)
            }

    private suspend fun userIdHandler(policy: FetchPolicy) =
        client.query(UserIdQuery()).fetchPolicy(policy).executeOrThrow().dataAssertNoErrors()
}

private const val LogTag = "UserRemoteSource"
