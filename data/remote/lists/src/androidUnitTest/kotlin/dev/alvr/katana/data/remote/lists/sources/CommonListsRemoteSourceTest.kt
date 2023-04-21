package dev.alvr.katana.data.remote.lists.sources

import app.cash.turbine.test
import arrow.core.right
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.annotations.ApolloExperimental
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.exception.ApolloHttpException
import com.apollographql.apollo3.exception.ApolloNetworkException
import com.apollographql.apollo3.exception.ApolloParseException
import com.apollographql.apollo3.exception.CacheMissException
import com.apollographql.apollo3.exception.HttpCacheMissException
import com.apollographql.apollo3.exception.JsonDataException
import com.apollographql.apollo3.exception.JsonEncodingException
import com.apollographql.apollo3.exception.MissingValueException
import com.apollographql.apollo3.interceptor.ApolloInterceptor
import com.apollographql.apollo3.mockserver.MockResponse
import com.apollographql.apollo3.mockserver.MockServer
import com.apollographql.apollo3.testing.QueueTestNetworkTransport
import com.apollographql.apollo3.testing.enqueueTestResponse
import com.benasher44.uuid.uuid4
import dev.alvr.katana.common.tests.TestBase
import dev.alvr.katana.common.tests.shouldBeLeft
import dev.alvr.katana.common.tests.shouldBeRight
import dev.alvr.katana.data.remote.base.type.MediaType
import dev.alvr.katana.data.remote.base.type.buildMediaListCollection
import dev.alvr.katana.data.remote.lists.MediaListCollectionQuery
import dev.alvr.katana.data.remote.lists.MediaListEntriesMutation
import dev.alvr.katana.data.remote.lists.enqueueResponse
import dev.alvr.katana.data.remote.lists.mappers.requests.toMutation
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.lists.failures.ListsFailure
import dev.alvr.katana.domain.lists.models.MediaCollection
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.models.lists.MediaList
import dev.alvr.katana.domain.user.managers.UserIdManager
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.localDate
import io.kotest.property.arbitrary.localDateTime
import io.kotest.property.arbitrary.next
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkStatic
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.stream.Stream
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource

@ApolloExperimental
@ExperimentalCoroutinesApi
internal class CommonListsRemoteSourceTest : TestBase() {
    private val client = ApolloClient.Builder().networkTransport(QueueTestNetworkTransport()).build()

    private val mediaList = Arb.bind<MediaList>(
        mapOf(
            LocalDate::class to Arb.localDate(),
            LocalDateTime::class to Arb.localDateTime(),
        ),
    ).next()

    @MockK
    private lateinit var userIdManager: UserIdManager
    @MockK
    private lateinit var reloadInterceptor: ApolloInterceptor

    private lateinit var source: CommonListsRemoteSource

    override suspend fun beforeEach() {
        source = CommonListsRemoteSourceImpl(client, userIdManager, reloadInterceptor)
    }

    @Nested
    @DisplayName("WHEN querying the list")
    inner class Querying {
        @ArgumentsSource(QueryProvider::class)
        @ParameterizedTest(name = "AND the data is {0} THEN the collection should be empty for {1}")
        fun `the server responded with data or not`(data: MediaListCollectionQuery.Data?, type: MediaType) = runTest {
            // GIVEN
            coEvery { userIdManager.getId() } returns 37_384.right()
            val response = ApolloResponse.Builder(
                operation = mockk<MediaListCollectionQuery>(),
                requestUuid = uuid4(),
                data = data,
            ).build()
            client.enqueueTestResponse(response)

            // WHEN
            val result = source.getMediaCollection<MediaEntry>(type)

            // THEN
            result.test(5.seconds) {
                awaitItem().shouldBeRight(MediaCollection(emptyList()))
                cancelAndIgnoreRemainingEvents()
            }
            coVerify(exactly = 1) { userIdManager.getId() }
        }

        @ArgumentsSource(QueryProvider::class)
        @ParameterizedTest(name = "AND a error occurs THEN the collection should be empty for {1}")
        fun `a HTTP error occurs`(data: MediaListCollectionQuery.Data?, type: MediaType) = runTest {
            // GIVEN
            coEvery { userIdManager.getId() } returns 37_384.right()
            val response = ApolloResponse.Builder(
                operation = mockk<MediaListCollectionQuery>(),
                requestUuid = uuid4(),
                data = data,
            ).errors(listOf(mockk())).build()
            client.enqueueTestResponse(response)

            // WHEN
            val result = source.getMediaCollection<MediaEntry>(type)

            // THEN
            result.test(5.seconds) {
                awaitItem().shouldBeRight(MediaCollection(emptyList()))
                cancelAndIgnoreRemainingEvents()
            }
            coVerify(exactly = 1) { userIdManager.getId() }
        }
    }

    @Nested
    @DisplayName("WHEN updating the list")
    inner class Updating {
        @Test
        @DisplayName("AND the server returns some data THEN it should be a right")
        fun `the server returns some data`() = runTest {
            // GIVEN
            client.enqueueTestResponse(mockk<MediaListEntriesMutation>(), mockk())

            // WHEN
            val result = source.updateList(mediaList)

            // THEN
            result.shouldBeRight()
        }

        @ArgumentsSource(FailuresProvider::class)
        @ParameterizedTest(name = "AND a HTTP error occurs THEN it should return a left of {0}")
        fun `a HTTP error occurs`(input: Failure, exception: Exception) = runTest {
            // GIVEN
            mockkStatic(MediaList::toMutation)
            every { any<MediaList>().toMutation() } throws exception
            client.enqueueTestResponse(mockk<MediaListEntriesMutation>(), mockk())

            // WHEN
            val result = source.updateList(mediaList)

            // THEN
            result.shouldBeLeft(input)
        }
    }

    @Nested
    @DisplayName("WHEN an error occurs")
    inner class WithErrors {
        private val mockServer = MockServer()

        private lateinit var badClient: ApolloClient
        private lateinit var source: CommonListsRemoteSource

        @BeforeEach
        fun setUp() {
            runBlocking {
                badClient = ApolloClient.Builder().serverUrl(mockServer.url()).build()
                source = CommonListsRemoteSourceImpl(
                    badClient,
                    userIdManager,
                    reloadInterceptor,
                )
            }
        }

        @AfterEach
        fun tearDown() {
            runBlocking { mockServer.stop() }
        }

        @ArgumentsSource(BadClient::class)
        @ParameterizedTest(name = "AND a HTTP error occurs THEN it should return a left of {0}")
        fun `a HTTP error occurs`(type: MediaType, action: MockResponse.Builder.() -> Unit) = runTest {
            // GIVEN
            coEvery { userIdManager.getId() } returns 37_384.right()
            mockServer.enqueueResponse(action)

            // WHEN
            val result = source.getMediaCollection<MediaEntry>(type)

            // THEN
            result.test(5.seconds) {
                awaitItem().shouldBeLeft(ListsFailure.GetMediaCollection)
                cancelAndIgnoreRemainingEvents()
            }
            coVerify(exactly = 1) { userIdManager.getId() }
        }
    }

    private class QueryProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            val empty = MediaListCollectionQuery.Data {
                this["collection"] = buildMediaListCollection {
                    lists = emptyList()
                    user = null
                }
            }

            val values = buildList {
                add(null)
                add(empty)
            }
            val types = MediaType.knownValues()

            return buildList {
                values.forEach { v -> types.forEach { t -> add(Arguments.of(v, t)) } }
            }.stream()
        }
    }

    private class BadClient : ArgumentsProvider {
        private fun apolloCommand(
            block: MockResponse.Builder.() -> Unit,
        ): MockResponse.Builder.() -> Unit = { MockResponse.Builder().apply(block) }

        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            val commands = buildList {
                add(apolloCommand { statusCode(500) })
                add(apolloCommand { body("Malformed body") })
                add(apolloCommand { body("""{"data": {"random": 42}}""") })
            }
            val types = MediaType.knownValues()

            return buildList {
                commands.forEach { c -> types.forEach { t -> add(Arguments.of(t, c)) } }
            }.stream()
        }
    }

    private class FailuresProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext): Stream<Arguments> =
            Stream.of(
                Arguments.of(ListsFailure.UpdatingList, mockk<ApolloHttpException>()),
                Arguments.of(ListsFailure.UpdatingList, mockk<ApolloNetworkException>()),
                Arguments.of(ListsFailure.UpdatingList, mockk<ApolloParseException>()),
                Arguments.of(ListsFailure.UpdatingList, mockk<JsonDataException>()),
                Arguments.of(ListsFailure.UpdatingList, mockk<JsonEncodingException>()),
                Arguments.of(Failure.Unknown, mockk<CacheMissException>()),
                Arguments.of(Failure.Unknown, mockk<HttpCacheMissException>()),
                Arguments.of(Failure.Unknown, mockk<MissingValueException>()),
            )
    }
}
