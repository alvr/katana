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
import kotlinx.coroutines.flow.flowOf

internal class ObserveActiveSessionUseCaseTest : FunSpec({
    val repo = mockk<SessionPreferencesRepository>()
    val useCase = ObserveActiveSessionUseCase(repo)

    context("successful clearing") {
        coEvery { repo.isSessionActive() } returns flowOf(false, true, false, true, true, false)
        useCase()

        test("invoke should clear the session") {
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
},)
