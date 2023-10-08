package dev.alvr.katana.data.remote.user.sources.info

import arrow.core.Either
import co.touchlab.kermit.Logger
import com.apollographql.apollo3.ApolloClient
import dev.alvr.katana.data.remote.base.toFailure
import dev.alvr.katana.data.remote.user.UserInfoQuery
import dev.alvr.katana.data.remote.user.mappers.responses.invoke
import dev.alvr.katana.domain.user.failures.UserFailure

internal class UserInfoRemoteSourceImpl(
    private val client: ApolloClient,
) : UserInfoRemoteSource {
    override suspend fun getUserInfo() = Either.catch {
        client.query(UserInfoQuery())
            .execute()
            .data()
    }.mapLeft { error ->
        Logger.e(error) { "Was not possible to get the user info" }

        error.toFailure(
            network = UserFailure.GettingUserInfo,
            response = UserFailure.GettingUserInfo,
        )
    }
}
