package dev.alvr.katana.data.remote.user.sources

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.annotations.ApolloExperimental
import com.apollographql.apollo3.exception.ApolloHttpException
import com.apollographql.apollo3.exception.ApolloNetworkException
import com.apollographql.apollo3.exception.ApolloParseException
import com.apollographql.apollo3.exception.CacheMissException
import com.apollographql.apollo3.exception.HttpCacheMissException
import com.apollographql.apollo3.exception.JsonDataException
import com.apollographql.apollo3.exception.JsonEncodingException
import com.apollographql.apollo3.exception.MissingValueException
import com.apollographql.apollo3.testing.QueueTestNetworkTransport
import com.apollographql.apollo3.testing.enqueueTestResponse
import dev.alvr.katana.common.tests.TestBase
import dev.alvr.katana.data.remote.base.type.buildUser
import dev.alvr.katana.data.remote.user.UserIdQuery
import dev.alvr.katana.data.remote.user.mappers.responses.invoke
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.user.failures.UserFailure
import dev.alvr.katana.domain.user.models.UserId
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import java.util.stream.Stream
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
internal class UserRemoteSourceTest : TestBase() {
    private val client = ApolloClient.Builder().networkTransport(QueueTestNetworkTransport()).build()

    private lateinit var source: UserRemoteSource

    override suspend fun beforeEach() {
        source = UserRemoteSourceImpl(client)
    }

    @Nested
    @DisplayName("WHEN getting the userId")
    inner class Getting {
        @Test
        @DisplayName("AND the server returns no data THEN it should be a Failure.Unknown")
        fun `the server returns no data`() = runTest {
            // GIVEN
            client.enqueueTestResponse(UserIdQuery())

            // WHEN
            val result = source.getUserId()

            // THEN
            result.shouldBeLeft(Failure.Unknown)
        }

        @Test
        @DisplayName("AND the server returns an empty userId THEN it should be a Failure.Unknown")
        fun `the server returns an empty userId`() = runTest {
            // GIVEN
            val query = UserIdQuery.Data { this["viewer"] = null }
            client.enqueueTestResponse(UserIdQuery(), query)

            // WHEN
            val result = source.getUserId()

            // THEN
            result.shouldBeLeft(Failure.Unknown)
        }

        @Test
        @DisplayName("AND the server returns a valid id THEN it should be a userId with the same id")
        fun `the server returns a valid id`() = runTest {
            // GIVEN
            val query = UserIdQuery.Data { this["viewer"] = buildUser { this["id"] = 37_384 } }
            client.enqueueTestResponse(UserIdQuery(), query)

            // WHEN
            val result = source.getUserId()

            // THEN
            result.shouldBeRight(UserId(37_384))
        }

        @ArgumentsSource(GettingFailuresProvider::class)
        @ParameterizedTest(name = "AND a HTTP error occurs THEN it should return a left of {0}")
        fun `a HTTP error occurs`(input: Failure, exception: Exception) = runTest {
            // GIVEN
            mockkStatic(UserIdQuery.Data::invoke)
            every { any<UserIdQuery.Data>().invoke() } throws exception
            client.enqueueTestResponse(UserIdQuery(), mockk())

            // WHEN
            val result = source.getUserId()

            // THEN
            result.shouldBeLeft(input)
        }
    }

    @Nested
    @DisplayName("WHEN saving the userId")
    inner class Saving {
        @Test
        @DisplayName("AND is successful THEN it should just execute the UserIdQuery")
        fun `is successful`() = runTest {
            // GIVEN
            val query = UserIdQuery.Data { this["viewer"] = buildUser { this["id"] = 37_384 } }
            client.enqueueTestResponse(UserIdQuery(), query)

            // WHEN
            val result = source.saveUserId()

            // THEN
            result.shouldBeRight()
        }

        @ArgumentsSource(SavingFailuresProvider::class)
        @ParameterizedTest(name = "AND a HTTP error occurs THEN it should return a left of {0}")
        fun `a HTTP error occurs`(input: Failure, exception: Exception) = runTest {
            // GIVEN
            mockkStatic(UserIdQuery.Data::invoke)
            every { any<UserIdQuery.Data>().invoke() } throws exception
            client.enqueueTestResponse(UserIdQuery(), mockk())

            // WHEN
            val result = source.saveUserId()

            // THEN
            result.shouldBeLeft(input)
        }
    }

    private class GettingFailuresProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext): Stream<Arguments> =
            Stream.of(
                Arguments.of(UserFailure.GettingUserId, mockk<CacheMissException>()),
                Arguments.of(UserFailure.GettingUserId, mockk<HttpCacheMissException>()),
                Arguments.of(Failure.Unknown, mockk<ApolloHttpException>()),
                Arguments.of(Failure.Unknown, mockk<ApolloNetworkException>()),
                Arguments.of(Failure.Unknown, mockk<ApolloParseException>()),
                Arguments.of(Failure.Unknown, mockk<JsonDataException>()),
                Arguments.of(Failure.Unknown, mockk<JsonEncodingException>()),
                Arguments.of(Failure.Unknown, mockk<MissingValueException>()),
            )
    }

    private class SavingFailuresProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext): Stream<Arguments> =
            Stream.of(
                Arguments.of(UserFailure.FetchingUser, mockk<ApolloHttpException>()),
                Arguments.of(UserFailure.FetchingUser, mockk<ApolloNetworkException>()),
                Arguments.of(UserFailure.SavingUser, mockk<ApolloParseException>()),
                Arguments.of(UserFailure.SavingUser, mockk<JsonDataException>()),
                Arguments.of(UserFailure.SavingUser, mockk<JsonEncodingException>()),
                Arguments.of(Failure.Unknown, mockk<CacheMissException>()),
                Arguments.of(Failure.Unknown, mockk<HttpCacheMissException>()),
                Arguments.of(Failure.Unknown, mockk<MissingValueException>()),
            )
    }
}
