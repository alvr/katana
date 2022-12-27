package dev.alvr.katana.domain.session.usecases

import arrow.core.Either
import arrow.core.left
import dev.alvr.katana.common.tests.TestBase
import dev.alvr.katana.common.tests.coEitherJustRun
import dev.alvr.katana.common.tests.valueMockk
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.base.usecases.sync
import dev.alvr.katana.domain.session.failures.SessionFailure
import dev.alvr.katana.domain.session.models.AnilistToken
import dev.alvr.katana.domain.session.repositories.SessionRepository
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
internal class SaveAnilistTokenUseCaseTest : TestBase() {
    @MockK
    private lateinit var repo: SessionRepository

    private lateinit var useCase: SaveSessionUseCase

    private val token = valueMockk<AnilistToken>()

    override suspend fun beforeEach() {
        useCase = spyk(SaveSessionUseCase(repo))
    }

    @Nested
    @DisplayName("GIVEN a successful repo call")
    inner class SuccessfulExecution {
        @Test
        @DisplayName("WHEN successful saveSession THEN invoke should be right")
        fun `successful saveSession (invoke)`() = runTest {
            // GIVEN
            coEitherJustRun { repo.saveSession(any()) }

            // WHEN
            val result = useCase(token)

            // THEN
            result.shouldBeRight()
            coVerify(exactly = 1) { repo.saveSession(any()) }
            coVerify(exactly = 1) { useCase.invoke(token) }
            verify(exactly = 0) { useCase.sync(token) }
        }

        @Test
        @DisplayName("WHEN successful saveSession THEN sync should be right")
        fun `successful saveSession (sync)`() = runTest {
            // GIVEN
            coEitherJustRun { repo.saveSession(any()) }

            // WHEN
            val resultSync = useCase.sync(token)

            // THEN
            resultSync.shouldBeRight()
            coVerify(exactly = 1) { repo.saveSession(any()) }
            coVerify(exactly = 1) { useCase.invoke(token) }
            verify(exactly = 1) { useCase.sync(token) }
        }
    }

    @Nested
    @DisplayName("GIVEN a failure repo call")
    inner class FailureExecution {
        @ArgumentsSource(FailuresProvider::class)
        @ParameterizedTest(name = "WHEN failure is {0} THEN the result should be {1}")
        fun `failure saveSession (invoke)`(failure: Failure, expected: Either<Failure, Unit>) = runTest {
            // GIVEN
            coEvery { repo.saveSession(any()) } returns expected

            // WHEN
            val result = useCase(token)

            // THEN
            result.shouldBeLeft(failure)
            coVerify(exactly = 1) { repo.saveSession(any()) }
            coVerify(exactly = 1) { useCase.invoke(token) }
            verify(exactly = 0) { useCase.sync(token) }
        }

        @ArgumentsSource(FailuresProvider::class)
        @ParameterizedTest(name = "WHEN failure is {0} THEN the result should be {1}")
        fun `failure saveSession (sync)`(failure: Failure, expected: Either<Failure, Unit>) = runTest {
            // GIVEN
            coEvery { repo.saveSession(any()) } returns expected

            // WHEN
            val result = useCase.sync(token)

            // THEN
            result.shouldBeLeft(failure)
            coVerify(exactly = 1) { repo.saveSession(any()) }
            coVerify(exactly = 1) { useCase.invoke(token) }
            verify(exactly = 1) { useCase.sync(token) }
        }
    }

    private class FailuresProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext): Stream<Arguments> =
            Stream.of(
                Arguments.of(SessionFailure.SavingSession, SessionFailure.SavingSession.left()),
                Arguments.of(Failure.Unknown, Failure.Unknown.left()),
            )
    }
}
