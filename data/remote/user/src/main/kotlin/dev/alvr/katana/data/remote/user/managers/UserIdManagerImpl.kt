package dev.alvr.katana.data.remote.user.managers

import com.apollographql.apollo3.ApolloClient
import dev.alvr.katana.data.remote.user.UserIdQuery
import dev.alvr.katana.data.remote.user.mappers.responses.invoke
import dev.alvr.katana.domain.user.managers.UserIdManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class UserIdManagerImpl @Inject constructor(
    private val client: ApolloClient,
) : UserIdManager {
    override suspend fun getId(): Int =
        client.query(UserIdQuery()).execute().data().id
}
