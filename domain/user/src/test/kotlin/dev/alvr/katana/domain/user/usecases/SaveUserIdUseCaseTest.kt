package dev.alvr.katana.domain.user.usecases

import arrow.core.Either
import arrow.core.left
import dev.alvr.katana.common.tests.TestBase
import dev.alvr.katana.common.tests.coEitherJustRun
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.base.usecases.sync
import dev.alvr.katana.domain.user.failures.UserFailure
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
internal class SaveUserIdUseCaseTest : TestBase() {
    @MockK
    private lateinit var repo: UserRepository

    private lateinit var useCase: SaveUserIdUseCase

    override suspend fun beforeEach() {
        useCase = spyk(SaveUserIdUseCase(repo))
    }

    @Nested
    @DisplayName("GIVEN a successful execution")
    inner class SuccessfulExecution {
        @Test
        @DisplayName("WHEN successful saveUserId THEN invoke should be right")
        fun `successful saveUserId (invoke)`() = runTest {
            // GIVEN
            coEitherJustRun { repo.saveUserId() }

            // WHEN
            val result = useCase()

            // THEN
            result.shouldBeRight()
            coVerify(exactly = 1) { repo.saveUserId() }
            coVerify(exactly = 1) { useCase.invoke() }
        }

        @Test
        @DisplayName("WHEN successful saveUserId THEN invoke should be right")
        fun `successful saveUserId (sync)`() = runTest {
            // GIVEN
            coEitherJustRun { repo.saveUserId() }

            // WHEN
            val result = useCase.sync()

            // THEN
            result.shouldBeRight()
            coVerify(exactly = 1) { repo.saveUserId() }
            verify(exactly = 1) { useCase.sync() }
        }
    }

    @Nested
    @DisplayName("GIVEN a failure execution")
    inner class FailureExecution {
        @ArgumentsSource(FailuresArguments::class)
        @ParameterizedTest(name = "WHEN failure is {0} THEN the result should be {1}")
        fun `failure saveUserId (invoke)`(failure: Failure, expected: Either<Failure, Unit>) = runTest {
            // GIVEN
            coEvery { repo.saveUserId() } returns expected

            // WHEN
            val result = useCase()

            // THEN
            result.shouldBeLeft(failure)
            coVerify(exactly = 1) { repo.saveUserId() }
            coVerify(exactly = 1) { useCase.invoke() }
        }

        @ArgumentsSource(FailuresArguments::class)
        @ParameterizedTest(name = "WHEN failure is {0} THEN the result should be {1}")
        fun `failure saveUserId (sync)`(failure: Failure, expected: Either<Failure, Unit>) = runTest {
            // GIVEN
            coEvery { repo.saveUserId() } returns expected

            // WHEN
            val result = useCase.sync()

            // THEN
            result.shouldBeLeft(failure)
            coVerify(exactly = 1) { repo.saveUserId() }
            verify(exactly = 1) { useCase.sync() }
        }
    }

    private class FailuresArguments : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<Arguments> =
            Stream.of(
                Arguments.of(UserFailure.FetchingUser, UserFailure.FetchingUser.left()),
                Arguments.of(UserFailure.SavingUser, UserFailure.SavingUser.left()),
                Arguments.of(Failure.Unknown, Failure.Unknown.left()),
            )
    }
}
