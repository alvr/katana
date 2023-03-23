package dev.alvr.katana.data.remote.user.managers

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.annotations.ApolloExperimental
import com.apollographql.apollo3.cache.normalized.ApolloStore
import com.apollographql.apollo3.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo3.cache.normalized.isFromCache
import com.apollographql.apollo3.cache.normalized.store
import com.apollographql.apollo3.testing.QueueTestNetworkTransport
import com.apollographql.apollo3.testing.enqueueTestResponse
import dev.alvr.katana.common.tests.TestBase
import dev.alvr.katana.data.remote.base.type.buildUser
import dev.alvr.katana.data.remote.user.UserIdQuery
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.nulls.shouldNotBeNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ApolloExperimental
@ExperimentalCoroutinesApi
internal class ApolloUserIdManagerTest : TestBase() {
    private val store = ApolloStore(normalizedCacheFactory = MemoryCacheFactory())
    private val client = ApolloClient.Builder().networkTransport(QueueTestNetworkTransport()).store(store).build()

    @Test
    @DisplayName("WHEN retrieving the authenticated user THEN the first time should make a HTTP request")
    fun `retrieving the authenticated user (HTTP)`() = runTest {
        // GIVEN
        val query = UserIdQuery.Data {
            this["viewer"] = buildUser {
                this["id"] = 12345
            }
        }

        // WHEN
        client.enqueueTestResponse(UserIdQuery(), query)
        val result = client.query(UserIdQuery()).execute()

        // THEN
        val data = result.also { res -> res.isFromCache.shouldBeFalse() }.data.shouldNotBeNull()
        data.viewer.shouldNotBeNull() shouldBeEqualToComparingFields query.viewer.shouldNotBeNull()
    }

    @Test
    @DisplayName("WHEN retrieving the authenticated user THEN from the second onwards it should be read from cache")
    fun `retrieving the authenticated user (cache)`() = runTest {
        // GIVEN
        val query = UserIdQuery.Data {
            this["viewer"] = buildUser {
                this["id"] = 12345
            }
        }

        // WHEN
        client.enqueueTestResponse(UserIdQuery(), query)
        client.query(UserIdQuery()).execute() // HTTP
        val result = client.query(UserIdQuery()).execute() // Cache

        // THEN
        val data = result.also { res -> res.isFromCache.shouldBeTrue() }.data.shouldNotBeNull()
        data.viewer.shouldNotBeNull() shouldBeEqualToComparingFields query.viewer.shouldNotBeNull()
    }

    @Test
    @DisplayName("WHEN clearing the database THEN the next request should be fetched again from HTTP")
    fun `clearing the database`() = runTest {
        // GIVEN
        val query = UserIdQuery.Data {
            this["viewer"] = buildUser {
                this["id"] = 12345
            }
        }

        // WHEN
        store.clearAll()
        client.enqueueTestResponse(UserIdQuery(), query)
        val result = client.query(UserIdQuery()).execute()

        // THEN
        val data = result.also { res -> res.isFromCache.shouldBeFalse() }.data.shouldNotBeNull()
        data.viewer.shouldNotBeNull() shouldBeEqualToComparingFields query.viewer.shouldNotBeNull()
    }
}
