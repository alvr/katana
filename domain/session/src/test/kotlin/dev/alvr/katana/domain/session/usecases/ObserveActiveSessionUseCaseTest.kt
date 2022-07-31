package dev.alvr.katana.domain.session.usecases

import app.cash.turbine.testIn
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.session.repositories.SessionPreferencesRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.flow.flowOf

internal class ObserveActiveSessionUseCaseTest : FunSpec({
    val repo = mockk<SessionPreferencesRepository>()
    val useCase = spyk(ObserveActiveSessionUseCase(repo))

    context("active session observer") {
        coEvery { repo.isSessionActive() } returns flowOf(false, true, false, true, true, false)
        useCase()

        test("invoke should observe if the session is active") {
            useCase.flow.testIn(this).run {
                awaitItem().shouldBeFalse()
                awaitItem().shouldBeTrue()
                awaitItem().shouldBeFalse()
                awaitItem().shouldBeTrue()
                awaitItem().shouldBeFalse()
                cancelAndConsumeRemainingEvents()
            }
            coVerify(exactly = 1) { repo.isSessionActive() }
        }
    }

    test("invoke the use case should call the invoke operator") {
        coEvery { repo.isSessionActive() } returns mockk()

        useCase(Unit)

        coVerify(exactly = 1) { useCase.invoke(Unit) }
    }
},)
