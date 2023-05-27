package dev.alvr.katana.data.remote.user.sources

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.annotations.ApolloExperimental
import com.apollographql.apollo3.testing.QueueTestNetworkTransport
import com.apollographql.apollo3.testing.enqueueTestResponse
import dev.alvr.katana.common.tests.shouldBeLeft
import dev.alvr.katana.common.tests.shouldBeRight
import dev.alvr.katana.data.remote.base.type.buildUser
import dev.alvr.katana.data.remote.user.UserIdQuery
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.user.models.UserId
import io.kotest.core.spec.style.FreeSpec

@OptIn(ApolloExperimental::class)
internal class UserRemoteSourceTest : FreeSpec() {
    private val client = ApolloClient.Builder().networkTransport(QueueTestNetworkTransport()).build()
    private val source: UserRemoteSource = UserRemoteSourceImpl(client)

    init {
        "getting the userId" - {
            "the server returns no data" {
                client.enqueueTestResponse(UserIdQuery())
                source.getUserId().shouldBeLeft(Failure.Unknown)
            }

            "the server returns an empty userId" {
                val query = UserIdQuery.Data { this["viewer"] = null }
                client.enqueueTestResponse(UserIdQuery(), query)
                source.getUserId().shouldBeLeft(Failure.Unknown)
            }

            "the server returns a valid id" {
                val query = UserIdQuery.Data { this["viewer"] = buildUser { this["id"] = 37_384 } }
                client.enqueueTestResponse(UserIdQuery(), query)
                source.getUserId().shouldBeRight(UserId(37_384))
            }
        }

        "saving" - {
            "is successful" {
                val query = UserIdQuery.Data { this["viewer"] = buildUser { this["id"] = 37_384 } }
                client.enqueueTestResponse(UserIdQuery(), query)
                source.saveUserId().shouldBeRight()
            }
        }
    }
}
