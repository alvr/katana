package dev.alvr.katana.data.remote.user.managers

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.annotations.ApolloExperimental
import com.apollographql.apollo3.testing.QueueTestNetworkTransport
import com.apollographql.apollo3.testing.enqueueTestResponse
import dev.alvr.katana.data.remote.user.UserIdQuery
import dev.alvr.katana.data.remote.user.test.UserIdQuery_TestBuilder.Data
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.shouldBe

@OptIn(ApolloExperimental::class)
internal class UserIdManagerTest : BehaviorSpec({
    given("an userIdManager") {
        val client = ApolloClient.Builder()
            .networkTransport(QueueTestNetworkTransport())
            .build()
        val manager = UserIdManagerImpl(client)

        `when`("server return null") {
            client.enqueueTestResponse(UserIdQuery())

            then("the mapper should throw an exception") {
                shouldThrowExactly<IllegalStateException> {
                    manager.getId()
                }.message shouldBe "UserId is required"
            }
        }

        `when`("server return viewer is null") {
            val query = UserIdQuery.Data {
                viewer = null
            }

            client.enqueueTestResponse(UserIdQuery(), query)

            then("the mapper should throw an exception") {
                shouldThrowExactly<IllegalStateException> {
                    manager.getId()
                }.message shouldBe "UserId is required"
            }
        }

        `when`("server return viewer is valid") {
            val query = UserIdQuery.Data {
                viewer = viewer {
                    id = 37_384
                }
            }

            client.enqueueTestResponse(UserIdQuery(), query)

            then("it should return the id of the user") {
                manager.getId() shouldBeExactly 37_384
            }
        }
    }
})
