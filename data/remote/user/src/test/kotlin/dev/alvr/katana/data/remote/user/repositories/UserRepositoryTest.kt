package dev.alvr.katana.data.remote.user.repositories

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.tests.TestBase
import dev.alvr.katana.common.tests.coEitherJustRun
import dev.alvr.katana.data.remote.user.sources.UserRemoteSource
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.user.failures.UserFailure
import dev.alvr.katana.domain.user.models.UserId
import dev.alvr.katana.domain.user.repositories.UserRepository
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
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

@ExperimentalCoroutinesApi
internal class UserRepositoryTest : TestBase() {
    @MockK
    private lateinit var source: UserRemoteSource

    private lateinit var repo: UserRepository

    private val userId = UserId(37_384)

    override suspend fun beforeEach() {
        repo = UserRepositoryImpl(source)
    }

    @Nested
    @DisplayName("WHEN getting the userId")
    inner class Getting {
        @Test
        @DisplayName("AND the server returns no data THEN it should be a right")
        fun `the server returns no data`() = runTest {
            // GIVEN
            coEvery { source.getUserId() } returns userId.right()

            // WHEN
            val result = repo.getUserId()

            // THEN
            result.shouldBeRight(userId)
            coVerify(exactly = 1) { source.getUserId() }
        }

        @Test
        @DisplayName("AND the server returns an empty userId THEN it should be a UserFailure.UserIdFailure")
        fun `the server returns an empty userId`() = runTest {
            // GIVEN
            coEvery { source.getUserId() } returns UserFailure.GettingUserId.left()

            // WHEN
            val result = repo.getUserId()

            // THEN
            result.shouldBeLeft(UserFailure.GettingUserId)
            coVerify(exactly = 1) { source.getUserId() }
        }
    }

    @Nested
    @DisplayName("WHEN saving the userId")
    inner class Saving {
        @Test
        @DisplayName("AND is successful THEN it should just execute the UserIdQuery")
        fun `is successful`() = runTest {
            // GIVEN
            coEitherJustRun { source.saveUserId() }

            // WHEN
            val result = repo.saveUserId()

            // THEN
            result.shouldBeRight()
            coVerify(exactly = 1) { source.saveUserId() }
        }

        @ArgumentsSource(FailuresProvider::class)
        @ParameterizedTest(name = "AND a HTTP error occurs THEN it should return a left of {0}")
        fun `a HTTP error occurs`(input: Failure, expected: Either<Failure, Unit>) = runTest {
            // GIVEN
            coEvery { source.saveUserId() } returns expected

            // WHEN
            val result = repo.saveUserId()

            // THEN
            result.shouldBeLeft(input)
            coVerify(exactly = 1) { source.saveUserId() }
        }
    }

    private class FailuresProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext): Stream<Arguments> =
            Stream.of(
                Arguments.of(UserFailure.FetchingUser, UserFailure.FetchingUser.left()),
                Arguments.of(UserFailure.SavingUser, UserFailure.SavingUser.left()),
                Arguments.of(Failure.Unknown, Failure.Unknown.left()),
            )
    }
}
