package dev.alvr.katana.domain.session.usecases

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.tests.TestBase
import dev.alvr.katana.common.tests.shouldBeLeft
import dev.alvr.katana.common.tests.shouldBeRight
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.session.failures.SessionFailure
import dev.alvr.katana.domain.session.repositories.SessionRepository
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class ObserveActiveSessionUseCaseTest : TestBase() {
    @MockK
    private lateinit var repo: SessionRepository

    private lateinit var useCase: ObserveActiveSessionUseCase

    override suspend fun beforeEach() {
        useCase = spyk(ObserveActiveSessionUseCase(repo))
    }

    @Test
    @DisplayName("WHEN invoking THEN observe if the session is active")
    fun `invoking the useCase (success)`() = runTest {
        // GIVEN
        every { repo.sessionActive } returns flowOf(
            false.right(),
            true.right(),
            false.right(),
            true.right(),
            true.right(),
            false.right(),
        )

        // WHEN
        useCase()

        // THEN
        useCase.flow.test(5.seconds) {
            awaitItem().shouldBeRight(false)
            awaitItem().shouldBeRight(true)
            awaitItem().shouldBeRight(false)
            awaitItem().shouldBeRight(true)
            awaitItem().shouldBeRight(false)
            cancelAndConsumeRemainingEvents()
        }

        coVerify(exactly = 1) { useCase.invoke(Unit) }
        coVerify(exactly = 1) { repo.sessionActive }
        verify(exactly = 1) { useCase.flow }
    }

    @Test
    @DisplayName("WHEN invoking THEN observe if the session is active")
    fun `invoking the useCase (failure)`() = runTest {
        // GIVEN
        every { repo.sessionActive } returns flowOf(mockk<SessionFailure>().left())

        // WHEN
        useCase()

        // THEN
        useCase.flow.test(5.seconds) {
            awaitItem().shouldBeLeft()
            cancelAndConsumeRemainingEvents()
        }

        coVerify(exactly = 1) { useCase.invoke(Unit) }
        coVerify(exactly = 1) { repo.sessionActive }
        verify(exactly = 1) { useCase.flow }
    }
}
