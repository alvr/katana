package dev.alvr.katana.domain.token.usecases

import arrow.core.left
import arrow.core.right
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.base.usecases.sync
import dev.alvr.katana.domain.token.failures.TokenPreferencesFailure
import dev.alvr.katana.domain.token.repositories.TokenPreferencesRepository
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FunSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

internal class DeleteAnilistTokenUseCaseTest : FunSpec({
    val repo = mockk<TokenPreferencesRepository>()
    val useCase = DeleteAnilistTokenUseCase(repo)

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
        context("is a TokenPreferencesFailure.DeletingFailure") {
            coEvery { repo.deleteAnilistToken() } returns TokenPreferencesFailure.DeletingFailure.left()

            test("invoke should return failure") {
                useCase().shouldBeLeft(TokenPreferencesFailure.DeletingFailure)
                coVerify(exactly = 1) { repo.deleteAnilistToken() }
            }

            test("sync should return failure") {
                useCase.sync().shouldBeLeft(TokenPreferencesFailure.DeletingFailure)
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
},)
