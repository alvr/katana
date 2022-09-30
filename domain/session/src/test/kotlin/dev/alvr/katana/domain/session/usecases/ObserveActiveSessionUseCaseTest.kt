package dev.alvr.katana.domain.session.usecases

import app.cash.turbine.test
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.session.repositories.SessionRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.flow.flowOf

internal class ObserveActiveSessionUseCaseTest : FunSpec({
    val repo = mockk<SessionRepository>()
    val useCase = spyk(ObserveActiveSessionUseCase(repo))

    context("active session observer") {
        test("invoke should observe if the session is active") {
            every { repo.sessionActive } returns flowOf(false, true, false, true, true, false)

            useCase()

            useCase.flow.test(5.seconds) {
                awaitItem().shouldBeFalse()
                awaitItem().shouldBeTrue()
                awaitItem().shouldBeFalse()
                awaitItem().shouldBeTrue()
                awaitItem().shouldBeFalse()
                cancelAndConsumeRemainingEvents()
            }

            coVerify(exactly = 1) { useCase.invoke(Unit) }
            coVerify(exactly = 1) { repo.sessionActive }
        }

        test("invoke the use case should call the invoke operator") {
            every { repo.sessionActive } returns flowOf(mockk())

            useCase(Unit)

            useCase.flow.test(5.seconds) {
                awaitItem()
                cancelAndConsumeRemainingEvents()
            }

            coVerify(exactly = 1) { useCase.invoke(Unit) }
            coVerify(exactly = 1) { repo.sessionActive }
        }
    }
},)
