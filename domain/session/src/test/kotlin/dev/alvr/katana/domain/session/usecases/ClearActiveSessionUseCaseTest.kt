package dev.alvr.katana.domain.session.usecases

import arrow.core.Either
import arrow.core.left
import dev.alvr.katana.common.tests.TestBase
import dev.alvr.katana.common.tests.coEitherJustRun
import dev.alvr.katana.common.tests.shouldBeLeft
import dev.alvr.katana.common.tests.shouldBeRight
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.base.usecases.sync
import dev.alvr.katana.domain.session.failures.SessionFailure
import dev.alvr.katana.domain.session.repositories.SessionRepository
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
internal class ClearActiveSessionUseCaseTest : TestBase() {
    @MockK
    private lateinit var repo: SessionRepository

    private lateinit var useCase: ClearActiveSessionUseCase

    override suspend fun beforeEach() {
        useCase = spyk(ClearActiveSessionUseCase(repo))
    }

    @Nested
    @DisplayName("GIVEN a successful repo call")
    inner class SuccessfulExecution {
        @Test
        @DisplayName("WHEN successful clearActiveSession THEN invoke should be right")
        fun `successful clearActiveSession (invoke)`() = runTest {
            // GIVEN
            coEitherJustRun { repo.clearActiveSession() }

            // WHEN
            val result = useCase()

            // THEN
            result.shouldBeRight()
            coVerify(exactly = 1) { repo.clearActiveSession() }
            coVerify(exactly = 1) { useCase.invoke(Unit) }
            verify(exactly = 0) { useCase.sync(Unit) }
        }

        @Test
        @DisplayName("WHEN successful clearActiveSession THEN sync should be right")
        fun `successful clearActiveSession (sync)`() = runTest {
            // GIVEN
            coEitherJustRun { repo.clearActiveSession() }

            // WHEN
            val resultSync = useCase.sync()

            // THEN
            resultSync.shouldBeRight()
            coVerify(exactly = 1) { repo.clearActiveSession() }
            coVerify(exactly = 1) { useCase.invoke(Unit) }
            verify(exactly = 1) { useCase.sync(Unit) }
        }
    }

    @Nested
    @DisplayName("GIVEN a failure repo call")
    inner class FailureExecution {
        @ArgumentsSource(FailuresProvider::class)
        @ParameterizedTest(name = "WHEN failure is {0} THEN the result should be {1}")
        fun `failure clearActiveSession (invoke)`(failure: Failure, expected: Either<Failure, Unit>) = runTest {
            // GIVEN
            coEvery { repo.clearActiveSession() } returns expected

            // WHEN
            val result = useCase()

            // THEN
            result.shouldBeLeft(failure)
            coVerify(exactly = 1) { repo.clearActiveSession() }
            coVerify(exactly = 1) { useCase.invoke(Unit) }
            verify(exactly = 0) { useCase.sync(Unit) }
        }

        @ArgumentsSource(FailuresProvider::class)
        @ParameterizedTest(name = "WHEN failure is {0} THEN the result should be {1}")
        fun `failure clearActiveSession (sync)`(failure: Failure, expected: Either<Failure, Unit>) = runTest {
            // GIVEN
            coEvery { repo.clearActiveSession() } returns expected

            // WHEN
            val result = useCase.sync()

            // THEN
            result.shouldBeLeft(failure)
            coVerify(exactly = 1) { repo.clearActiveSession() }
            coVerify(exactly = 1) { useCase.invoke(Unit) }
            verify(exactly = 1) { useCase.sync(Unit) }
        }
    }

    private class FailuresProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext): Stream<Arguments> =
            Stream.of(
                Arguments.of(SessionFailure.ClearingSession, SessionFailure.ClearingSession.left()),
                Arguments.of(Failure.Unknown, Failure.Unknown.left()),
            )
    }
}
