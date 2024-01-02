package dev.alvr.katana.data.remote.user.sources.info

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import co.touchlab.kermit.Logger
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import com.apollographql.apollo3.cache.normalized.watch
import dev.alvr.katana.data.remote.base.toFailure
import dev.alvr.katana.data.remote.user.UserInfoQuery
import dev.alvr.katana.data.remote.user.mappers.responses.invoke
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.user.failures.UserFailure
import dev.alvr.katana.domain.user.models.UserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

internal class UserInfoRemoteSourceImpl(
    client: ApolloClient,
) : UserInfoRemoteSource {
    @Suppress("USELESS_CAST")
    override val userInfo: Flow<Either<Failure, UserInfo>> =
        client.query(UserInfoQuery())
            .fetchPolicy(FetchPolicy.CacheAndNetwork)
            .watch()
            .map { res -> res.data().right() as Either<Failure, UserInfo> }
            .catch { error ->
                Logger.e(error) { "Was not possible to get the user info" }

                emit(
                    error.toFailure(
                        network = UserFailure.GettingUserInfo,
                        response = UserFailure.GettingUserInfo,
                    ).left(),
                )
            }
}
