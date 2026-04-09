package dev.alvr.katana.common.user.data.managers

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.annotations.ApolloExperimental
import com.apollographql.apollo.testing.QueueTestNetworkTransport
import com.apollographql.apollo.testing.enqueueTestResponse
import com.apollographql.cache.normalized.CacheManager
import com.apollographql.cache.normalized.api.DefaultCacheKeyGenerator
import com.apollographql.cache.normalized.api.DefaultCacheResolver
import com.apollographql.cache.normalized.cacheManager
import com.apollographql.cache.normalized.isFromCache
import com.apollographql.cache.normalized.memory.MemoryCacheFactory
import dev.alvr.katana.common.user.data.UserIdQuery
import dev.alvr.katana.core.remote.builder.Data
import dev.alvr.katana.core.remote.builder.buildUser
import dev.alvr.katana.core.remote.cache.Cache.cache
import dev.alvr.katana.core.remote.executeOrThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.test.TestCase
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldNotBeNull

@OptIn(ApolloExperimental::class)
internal class ApolloUserIdManagerTest : FreeSpec() {
    private lateinit var cache: CacheManager
    private lateinit var client: ApolloClient

    init {
        "retrieving the authenticated user" -
            {
                "the first time should make a HTTP request" {
                    val query = buildUserIdQuery()

                    client.enqueueTestResponse(UserIdQuery(), query)
                    client
                        .query(UserIdQuery())
                        .executeOrThrow()
                        .also { res -> res.isFromCache.shouldBeFalse() }
                        .data
                        .shouldNotBeNull()
                        .viewer
                        .shouldNotBeNull() shouldBeEqual query.viewer.shouldNotBeNull()
                }

                "the second onwards it should be read from cache" {
                    val query = buildUserIdQuery()

                    client.enqueueTestResponse(UserIdQuery(), query)
                    client.query(UserIdQuery()).executeOrThrow() // Simulate HTTP request
                    client
                        .query(UserIdQuery())
                        .executeOrThrow() // Next request is from cache
                        .also { res -> res.isFromCache.shouldBeTrue() }
                        .data
                        .shouldNotBeNull()
                        .viewer
                        .shouldNotBeNull() shouldBeEqual query.viewer.shouldNotBeNull()
                }
            }

        "clearing the database" {
            val query = buildUserIdQuery()

            client.enqueueTestResponse(UserIdQuery(), query)
            client.query(UserIdQuery()).executeOrThrow() // Simulate HTTP request
            client
                .query(UserIdQuery())
                .executeOrThrow() // Next request is from cache
                .also { res -> res.isFromCache.shouldBeTrue() }
                .data
                .shouldNotBeNull()
                .viewer
                .shouldNotBeNull() shouldBeEqual query.viewer.shouldNotBeNull()

            cache.clearAll()

            client.enqueueTestResponse(UserIdQuery(), query)
            client
                .query(UserIdQuery())
                .executeOrThrow() // No cache, HTTP request
                .also { res -> res.isFromCache.shouldBeFalse() }
                .data
                .shouldNotBeNull()
                .viewer
                .shouldNotBeNull() shouldBeEqual query.viewer.shouldNotBeNull()
        }
    }

    override suspend fun beforeEach(testCase: TestCase) {
        cache =
            CacheManager(
                normalizedCacheFactory = MemoryCacheFactory(),
                cacheKeyGenerator = DefaultCacheKeyGenerator,
                cacheResolver = DefaultCacheResolver,
            )
        client = ApolloClient.Builder().networkTransport(QueueTestNetworkTransport()).cacheManager(cache).build()
    }

    private fun buildUserIdQuery(): UserIdQuery.Data = UserIdQuery.Data { this["Viewer"] = buildUser { id = 12345 } }
}
