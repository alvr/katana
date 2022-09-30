package dev.alvr.katana.domain.session.usecases

import arrow.core.left
import arrow.core.right
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.base.usecases.sync
import dev.alvr.katana.domain.session.failures.SessionPreferencesFailure
import dev.alvr.katana.domain.session.repositories.SessionRepository
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FunSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify

internal class ClearActiveSessionUseCaseTest : FunSpec({
    val repo = mockk<SessionRepository>()
    val useCase = spyk(ClearActiveSessionUseCase(repo))

    context("successful clearing") {
        coEvery { repo.clearActiveSession() } returns Unit.right()

        test("invoke should clear the session") {
            useCase().shouldBeRight()
            coVerify(exactly = 1) { repo.clearActiveSession() }
        }

        test("sync should clear the session") {
            useCase.sync().shouldBeRight()
            coVerify(exactly = 1) { repo.clearActiveSession() }
        }
    }

    context("failure clearing") {
        context("is a SessionPreferencesFailure.ClearingSessionFailure") {
            coEvery { repo.clearActiveSession() } returns SessionPreferencesFailure.ClearingSessionFailure.left()

            test("invoke should return failure") {
                useCase().shouldBeLeft(SessionPreferencesFailure.ClearingSessionFailure)
                coVerify(exactly = 1) { repo.clearActiveSession() }
            }

            test("sync should return failure") {
                useCase.sync().shouldBeLeft(SessionPreferencesFailure.ClearingSessionFailure)
                coVerify(exactly = 1) { repo.clearActiveSession() }
            }
        }

        context("is a Failure.Unknown") {
            coEvery { repo.clearActiveSession() } returns Failure.Unknown.left()

            test("invoke should return failure") {
                useCase().shouldBeLeft(Failure.Unknown)
                coVerify(exactly = 1) { repo.clearActiveSession() }
            }

            test("sync should return failure") {
                useCase.sync().shouldBeLeft(Failure.Unknown)
                coVerify(exactly = 1) { repo.clearActiveSession() }
            }
        }
    }

    test("invoke the use case should call the invoke operator") {
        coEvery { repo.clearActiveSession() } returns mockk()

        useCase(Unit)

        coVerify(exactly = 1) { useCase.invoke(Unit) }
        coVerify(exactly = 1) { repo.clearActiveSession() }
    }

    test("sync the use case should call the invoke operator") {
        coEvery { repo.clearActiveSession() } returns mockk()

        useCase.sync()

        verify(exactly = 1) { useCase.sync(Unit) }
        coVerify(exactly = 1) { repo.clearActiveSession() }
    }
},)
