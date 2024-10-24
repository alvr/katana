package dev.alvr.katana.common.user.data.managers

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.annotations.ApolloExperimental
import com.apollographql.apollo.cache.normalized.ApolloStore
import com.apollographql.apollo.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo.cache.normalized.isFromCache
import com.apollographql.apollo.cache.normalized.store
import com.apollographql.apollo.testing.QueueTestNetworkTransport
import com.apollographql.apollo.testing.enqueueTestResponse
import dev.alvr.katana.common.user.data.UserIdQuery
import dev.alvr.katana.core.remote.executeOrThrow
import dev.alvr.katana.core.remote.type.buildUser
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldNotBeNull

@OptIn(ApolloExperimental::class)
internal class ApolloUserIdManagerTest : FreeSpec() {
    private val store = ApolloStore(normalizedCacheFactory = MemoryCacheFactory())
    private val client = ApolloClient.Builder().networkTransport(QueueTestNetworkTransport()).store(store).build()

    init {
        "retrieving the authenticated user" - {
            "the first time should make a HTTP request" {
                val query = UserIdQuery.Data {
                    viewer = buildUser {
                        id = 12345
                    }
                }

                client.enqueueTestResponse(UserIdQuery(), query)
                client.query(UserIdQuery()).executeOrThrow()
                    .also { res -> res.isFromCache.shouldBeFalse() }
                    .data.shouldNotBeNull()
                    .viewer.shouldNotBeNull() shouldBeEqual query.viewer.shouldNotBeNull()
            }

            "the second onwards it should be read from cache" {
                val query = UserIdQuery.Data {
                    viewer = buildUser {
                        id = 12345
                    }
                }

                client.enqueueTestResponse(UserIdQuery(), query)
                client.query(UserIdQuery()).executeOrThrow() // Simulate HTTP request
                client.query(UserIdQuery()).executeOrThrow() // Next request is from cache
                    .also { res -> res.isFromCache.shouldBeTrue() }
                    .data.shouldNotBeNull()
                    .viewer.shouldNotBeNull() shouldBeEqual query.viewer.shouldNotBeNull()
            }
        }

        "clearing the database" {
            val query = UserIdQuery.Data {
                viewer = buildUser {
                    id = 12345
                }
            }

            client.enqueueTestResponse(UserIdQuery(), query)
            client.query(UserIdQuery()).executeOrThrow() // Simulate HTTP request
            client.query(UserIdQuery()).executeOrThrow() // Next request is from cache
                .also { res -> res.isFromCache.shouldBeTrue() }
                .data.shouldNotBeNull()
                .viewer.shouldNotBeNull() shouldBeEqual query.viewer.shouldNotBeNull()

            store.clearAll()

            client.query(UserIdQuery()).executeOrThrow() // No cache, HTTP request
                .also { res -> res.isFromCache.shouldBeFalse() }
                .data.shouldNotBeNull()
                .viewer.shouldNotBeNull() shouldBeEqual query.viewer.shouldNotBeNull()
        }
    }
}
