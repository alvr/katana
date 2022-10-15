package dev.alvr.katana.domain.session.usecases

import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.tests.valueMockk
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.session.failures.SessionFailure
import dev.alvr.katana.domain.session.models.AnilistToken
import dev.alvr.katana.domain.session.repositories.SessionRepository
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FunSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify

internal class SaveAnilistTokenUseCaseTest : FunSpec() {
    private val repo = mockk<SessionRepository>()
    private val useCase = spyk(SaveSessionUseCase(repo))

    private val token = valueMockk<AnilistToken>()

    init {
        context("successful saving") {
            coEvery { repo.saveSession(token) } returns Unit.right()

            test("invoke should save the token") {
                useCase(token).shouldBeRight()
                coVerify(exactly = 1) { repo.saveSession(token) }
            }

            test("sync should return user") {
                useCase.sync(token).shouldBeRight()
                coVerify(exactly = 1) { repo.saveSession(token) }
            }
        }

        context("failure saving") {
            context("is a TokenPreferencesFailure.DeletingFailure") {
                coEvery { repo.saveSession(token) } returns SessionFailure.SavingSession.left()

                test("invoke should return failure") {
                    useCase(token).shouldBeLeft(SessionFailure.SavingSession)
                    coVerify(exactly = 1) { repo.saveSession(token) }
                }

                test("sync should return failure") {
                    useCase.sync(token).shouldBeLeft(SessionFailure.SavingSession)
                    coVerify(exactly = 1) { repo.saveSession(token) }
                }
            }

            context("is a Failure.Unknown") {
                coEvery { repo.saveSession(token) } returns Failure.Unknown.left()

                test("invoke should return failure") {
                    useCase(token).shouldBeLeft(Failure.Unknown)
                    coVerify(exactly = 1) { repo.saveSession(token) }
                }

                test("sync should return failure") {
                    useCase.sync(token).shouldBeLeft(Failure.Unknown)
                    coVerify(exactly = 1) { repo.saveSession(token) }
                }
            }
        }

        test("invoke the use case should call the invoke operator") {
            coEvery { repo.saveSession(any()) } returns mockk()

            useCase(token)

            coVerify(exactly = 1) { useCase.invoke(token) }
            coVerify(exactly = 1) { repo.saveSession(any()) }
        }

        test("sync the use case should call the invoke operator") {
            coEvery { repo.saveSession(any()) } returns mockk()

            useCase.sync(token)

            verify(exactly = 1) { useCase.sync(token) }
            coVerify(exactly = 1) { repo.saveSession(any()) }
        }
    }
}
