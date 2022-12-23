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
import com.apollographql.apollo3.mockserver.MockResponse
import com.apollographql.apollo3.mockserver.MockServer
import com.apollographql.apollo3.testing.QueueTestNetworkTransport
import com.apollographql.apollo3.testing.enqueueTestResponse
import com.benasher44.uuid.uuid4
import dev.alvr.katana.common.tests.TestBase
import dev.alvr.katana.data.remote.base.interceptors.ReloadInterceptor
import dev.alvr.katana.data.remote.base.type.MediaType
import dev.alvr.katana.data.remote.lists.MediaListCollectionQuery
import dev.alvr.katana.data.remote.lists.MediaListEntriesMutation
import dev.alvr.katana.data.remote.lists.enqueueResponse
import dev.alvr.katana.data.remote.lists.test.MediaListCollectionQuery_TestBuilder.Data
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.lists.failures.ListsFailure
import dev.alvr.katana.domain.lists.models.MediaCollection
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.models.lists.MediaList
import dev.alvr.katana.domain.user.managers.UserIdManager
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.localDate
import io.kotest.property.arbitrary.localDateTime
import io.kotest.property.arbitrary.next
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.mockk
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
    private val apolloBuilder = ApolloClient.Builder().networkTransport(QueueTestNetworkTransport())

    private val mediaList = Arb.bind<MediaList>(
        mapOf(
            LocalDate::class to Arb.localDate(),
            LocalDateTime::class to Arb.localDateTime(),
        ),
    ).next()

    @MockK
    private lateinit var userIdManager: UserIdManager
    @MockK
    private lateinit var reloadInterceptor: ReloadInterceptor
    @SpyK
    private var client = apolloBuilder.build()

    private lateinit var source: CommonListsRemoteSource

    override suspend fun beforeEach() {
        source = CommonListsRemoteSourceImpl(client, userIdManager, reloadInterceptor)

        coEvery { userIdManager.getId() } returns 37_384.right()
    }

    @Nested
    @DisplayName("WHEN querying the list")
    inner class Querying {
        @ArgumentsSource(QueryProvider::class)
        @ParameterizedTest(name = "AND the data is {0} THEN the collection should be empty for {1}")
        fun `the data is null`(data: MediaListCollectionQuery.Data?, type: MediaType) = runTest {
            // GIVEN
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
        }

        @ArgumentsSource(QueryProvider::class)
        @ParameterizedTest(name = "AND a error occurs THEN the collection should be empty for {1}")
        fun `a HTTP error occurs`(data: MediaListCollectionQuery.Data?, type: MediaType) = runTest {
            // GIVEN
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
        }
    }

    @Nested
    @DisplayName("WHEN updating the list")
    inner class Updating {
        @Test
        @DisplayName("AND is successful THEN it just execute the MediaListEntriesMutation")
        fun `is successful`() = runTest {
            // GIVEN
            coEvery { client.mutation(any<MediaListEntriesMutation>()).execute() } returns mockk()

            // WHEN
            val result = source.updateList(mediaList)

            // THEN
            result.shouldBeRight()
            coVerify(exactly = 1) { client.mutation(any<MediaListEntriesMutation>()).execute() }
        }

        @Test
        @DisplayName("AND the server returns no data THEN it should be a right")
        fun `the server returns no data`() = runTest {
            // GIVEN
            client.enqueueTestResponse(mockk<MediaListEntriesMutation>())

            // WHEN
            val result = source.updateList(mediaList)

            // THEN
            result.shouldBeRight()
        }

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
            coEvery { client.mutation(any<MediaListEntriesMutation>()).execute() } throws exception

            // WHEN
            val result = source.updateList(mediaList)

            // THEN
            result.shouldBeLeft(input)
            coVerify(exactly = 1) { client.mutation(any<MediaListEntriesMutation>()).execute() }
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
            mockServer.enqueueResponse(action)

            // WHEN
            val result = source.getMediaCollection<MediaEntry>(type)

            // THEN
            result.test(5.seconds) {
                awaitItem().shouldBeLeft(ListsFailure.GetMediaCollection)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    private class QueryProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            val empty = MediaListCollectionQuery.Data {
                collection = collection {
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
