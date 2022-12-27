package dev.alvr.katana.domain.user.usecases

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.tests.TestBase
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.base.usecases.sync
import dev.alvr.katana.domain.user.failures.UserFailure
import dev.alvr.katana.domain.user.models.UserId
import dev.alvr.katana.domain.user.repositories.UserRepository
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import io.mockk.verify
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
internal class GetUserIdUseCaseTest : TestBase() {
    @MockK
    private lateinit var repo: UserRepository

    private lateinit var useCase: GetUserIdUseCase

    private val userId = UserId(37_384)

    override suspend fun beforeEach() {
        useCase = spyk(GetUserIdUseCase(repo))
    }

    @Nested
    @DisplayName("GIVEN a successful repo call")
    inner class SuccessfulExecution {
        @Test
        @DisplayName("WHEN successful getUserId THEN invoke should be right")
        fun `successful getUserId (invoke)`() = runTest {
            // GIVEN
            coEvery { repo.getUserId() } returns userId.right()

            // WHEN
            val result = useCase()

            // THEN
            result.shouldBeRight(userId)
            coVerify(exactly = 1) { repo.getUserId() }
            coVerify(exactly = 1) { useCase.invoke(Unit) }
            verify(exactly = 0) { useCase.sync(Unit) }
        }

        @Test
        @DisplayName("WHEN successful getUserId THEN sync should be right")
        fun `successful getUserId (sync)`() = runTest {
            // GIVEN
            coEvery { repo.getUserId() } returns userId.right()

            // WHEN
            val resultSync = useCase.sync()

            // THEN
            resultSync.shouldBeRight(userId)
            coVerify(exactly = 1) { repo.getUserId() }
            coVerify(exactly = 1) { useCase.invoke(Unit) }
            verify(exactly = 1) { useCase.sync(Unit) }
        }
    }

    @Nested
    @DisplayName("GIVEN a failure repo call")
    inner class FailureExecution {
        @ArgumentsSource(FailuresProvider::class)
        @ParameterizedTest(name = "WHEN failure is {0} THEN the result should be {1}")
        fun `failure getUserId (invoke)`(failure: Failure, expected: Either<Failure, UserId>) = runTest {
            // GIVEN
            coEvery { repo.getUserId() } returns expected

            // WHEN
            val result = useCase()

            // THEN
            result.shouldBeLeft(failure)
            coVerify(exactly = 1) { repo.getUserId() }
            coVerify(exactly = 1) { useCase.invoke(Unit) }
            verify(exactly = 0) { useCase.sync(Unit) }
        }

        @ArgumentsSource(FailuresProvider::class)
        @ParameterizedTest(name = "WHEN failure is {0} THEN the result should be {1}")
        fun `failure getUserId (sync)`(failure: Failure, expected: Either<Failure, UserId>) = runTest {
            // GIVEN
            coEvery { repo.getUserId() } returns expected

            // WHEN
            val result = useCase.sync()

            // THEN
            result.shouldBeLeft(failure)
            coVerify(exactly = 1) { repo.getUserId() }
            coVerify(exactly = 1) { useCase.invoke(Unit) }
            verify(exactly = 1) { useCase.sync(Unit) }
        }
    }

    private class FailuresProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext): Stream<Arguments> =
            Stream.of(
                Arguments.of(UserFailure.GettingUserId, UserFailure.GettingUserId.left()),
            )
    }
}
