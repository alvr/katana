package dev.alvr.katana.domain.session.usecases

import arrow.core.left
import arrow.core.right
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.base.usecases.sync
import dev.alvr.katana.domain.session.failures.SessionPreferencesFailure
import dev.alvr.katana.domain.session.repositories.SessionPreferencesRepository
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FunSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk

internal class DeleteAnilistTokenUseCaseTest : FunSpec({
    val repo = mockk<SessionPreferencesRepository>()
    val useCase = spyk(DeleteAnilistTokenUseCase(repo))

    context("successful deletion") {
        coEvery { repo.deleteAnilistToken() } returns Unit.right()

        test("invoke should delete the token") {
            useCase().shouldBeRight()
            coVerify(exactly = 1) { repo.deleteAnilistToken() }
        }

        test("sync should delete the token") {
            useCase.sync().shouldBeRight()
            coVerify(exactly = 1) { repo.deleteAnilistToken() }
        }
    }

    context("failure deletion") {
        context("is a SessionPreferencesFailure.DeletingTokenFailure") {
            coEvery { repo.deleteAnilistToken() } returns SessionPreferencesFailure.DeletingTokenFailure.left()

            test("invoke should return failure") {
                useCase().shouldBeLeft(SessionPreferencesFailure.DeletingTokenFailure)
                coVerify(exactly = 1) { repo.deleteAnilistToken() }
            }

            test("sync should return failure") {
                useCase.sync().shouldBeLeft(SessionPreferencesFailure.DeletingTokenFailure)
                coVerify(exactly = 1) { repo.deleteAnilistToken() }
            }
        }

        context("is a Failure.Unknown") {
            coEvery { repo.deleteAnilistToken() } returns Failure.Unknown.left()

            test("invoke should return failure") {
                useCase().shouldBeLeft(Failure.Unknown)
                coVerify(exactly = 1) { repo.deleteAnilistToken() }
            }

            test("sync should return failure") {
                useCase.sync().shouldBeLeft(Failure.Unknown)
                coVerify(exactly = 1) { repo.deleteAnilistToken() }
            }
        }
    }

    test("invoke the use case should call the invoke operator") {
        coEvery { repo.deleteAnilistToken() } returns mockk()

        useCase()

        coVerify(exactly = 1) { useCase.invoke(Unit) }
    }

    test("sync the use case should call the invoke operator") {
        coEvery { repo.deleteAnilistToken() } returns mockk()

        useCase.sync()

        coVerify(exactly = 1) { useCase.sync(Unit) }
    }
},)
