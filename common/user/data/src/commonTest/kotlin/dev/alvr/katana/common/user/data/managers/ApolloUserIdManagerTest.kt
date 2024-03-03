package dev.alvr.katana.common.user.data.managers

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.annotations.ApolloExperimental
import com.apollographql.apollo3.cache.normalized.ApolloStore
import com.apollographql.apollo3.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo3.cache.normalized.isFromCache
import com.apollographql.apollo3.cache.normalized.store
import com.apollographql.apollo3.testing.QueueTestNetworkTransport
import com.apollographql.apollo3.testing.enqueueTestResponse
import dev.alvr.katana.common.user.data.UserIdQuery
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
                    this["viewer"] = buildUser {
                        this["id"] = 12345
                    }
                }

                client.enqueueTestResponse(UserIdQuery(), query)
                client.query(UserIdQuery()).execute()
                    .also { res -> res.isFromCache.shouldBeFalse() }
                    .data.shouldNotBeNull()
                    .viewer.shouldNotBeNull() shouldBeEqual query.viewer.shouldNotBeNull()
            }

            "the second onwards it should be read from cache" {
                val query = UserIdQuery.Data {
                    this["viewer"] = buildUser {
                        this["id"] = 12345
                    }
                }

                client.enqueueTestResponse(UserIdQuery(), query)
                client.query(UserIdQuery()).execute() // Simulate HTTP request
                client.query(UserIdQuery()).execute() // Next request is from cache
                    .also { res -> res.isFromCache.shouldBeTrue() }
                    .data.shouldNotBeNull()
                    .viewer.shouldNotBeNull() shouldBeEqual query.viewer.shouldNotBeNull()
            }
        }

        "clearing the database" {
            val query = UserIdQuery.Data {
                this["viewer"] = buildUser {
                    this["id"] = 12345
                }
            }

            client.enqueueTestResponse(UserIdQuery(), query)
            client.query(UserIdQuery()).execute() // Simulate HTTP request
            client.query(UserIdQuery()).execute() // Next request is from cache
                .also { res -> res.isFromCache.shouldBeTrue() }
                .data.shouldNotBeNull()
                .viewer.shouldNotBeNull() shouldBeEqual query.viewer.shouldNotBeNull()

            store.clearAll()

            client.query(UserIdQuery()).execute() // No cache, HTTP request
                .also { res -> res.isFromCache.shouldBeFalse() }
                .data.shouldNotBeNull()
                .viewer.shouldNotBeNull() shouldBeEqual query.viewer.shouldNotBeNull()
        }
    }
}
