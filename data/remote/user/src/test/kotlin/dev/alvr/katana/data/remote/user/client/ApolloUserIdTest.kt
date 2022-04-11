package dev.alvr.katana.data.remote.user.client

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.annotations.ApolloExperimental
import com.apollographql.apollo3.cache.normalized.ApolloStore
import com.apollographql.apollo3.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo3.cache.normalized.isFromCache
import com.apollographql.apollo3.cache.normalized.store
import com.apollographql.apollo3.testing.QueueTestNetworkTransport
import com.apollographql.apollo3.testing.enqueueTestResponse
import dev.alvr.katana.data.remote.user.UserIdQuery
import dev.alvr.katana.data.remote.user.test.UserIdQuery_TestBuilder.Data
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.nulls.shouldNotBeNull

@OptIn(ApolloExperimental::class)
internal class ApolloUserIdTest : BehaviorSpec({
    given("an Apollo client") {
        val store = ApolloStore(normalizedCacheFactory = MemoryCacheFactory())
        val client = ApolloClient.Builder()
            .networkTransport(QueueTestNetworkTransport())
            .store(store)
            .build()

        `when`("retrieving the authenticated user") {
            val query = UserIdQuery.Data {
                viewer = viewer {
                    id = 12345
                }
            }

            client.enqueueTestResponse(UserIdQuery(), query)

            then("the first time should make a HTTP request") {
                val data = client
                    .query(UserIdQuery())
                    .execute().also { res ->
                        res.isFromCache.shouldBeFalse()
                    }.data.shouldNotBeNull()

                data.viewer.shouldNotBeNull() shouldBeEqualToComparingFields query.viewer.shouldNotBeNull()
            }

            then("from the second onwards it should be read from cache") {
                val data = client
                    .query(UserIdQuery())
                    .execute().also { res ->
                        res.isFromCache.shouldBeTrue()
                    }.data.shouldNotBeNull()

                data.viewer.shouldNotBeNull() shouldBeEqualToComparingFields query.viewer.shouldNotBeNull()
            }
        }

        `when`("clearing the database") {
            store.clearAll()

            val query = UserIdQuery.Data {
                viewer = viewer {
                    id = 123_456
                }
            }

            client.enqueueTestResponse(UserIdQuery(), query)

            then("the next request should be fetched again from HTTP") {
                val data = client
                    .query(UserIdQuery())
                    .execute().also { res ->
                        res.isFromCache.shouldBeFalse()
                    }.data.shouldNotBeNull()

                data.viewer.shouldNotBeNull() shouldBeEqualToComparingFields query.viewer.shouldNotBeNull()
            }
        }
    }
})
