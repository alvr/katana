package dev.alvr.katana.data.remote.user.repositories

import arrow.core.left
import arrow.core.right
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.exception.ApolloException
import dev.alvr.katana.data.remote.base.failures.CommonRemoteFailure
import dev.alvr.katana.data.remote.user.UserIdQuery
import dev.alvr.katana.data.remote.user.mappers.responses.invoke
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.user.failures.UserFailure
import dev.alvr.katana.domain.user.repositories.UserRemoteRepository
import javax.inject.Inject

internal class UserRemoteRepositoryImpl @Inject constructor(
    private val client: ApolloClient,
) : UserRemoteRepository {
    override suspend fun getUserId() = client.query(UserIdQuery()).execute().data()

    override suspend fun saveUserId() = try {
        client.query(UserIdQuery()).execute()
        Unit.right()
    } catch (ex: ApolloException) {
        when (CommonRemoteFailure.fromApollo(ex)) {
            CommonRemoteFailure.NetworkFailure -> UserFailure.FetchingFailure
            CommonRemoteFailure.ResponseFailure -> UserFailure.SavingFailure
            CommonRemoteFailure.CacheFailure -> Failure.Unknown
            CommonRemoteFailure.UnknownFailure -> Failure.Unknown
        }.left()
    }
}
