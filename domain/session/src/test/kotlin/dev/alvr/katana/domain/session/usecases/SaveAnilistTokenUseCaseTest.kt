package dev.alvr.katana.domain.session.usecases

import arrow.core.left
import arrow.core.right
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.session.failures.SessionPreferencesFailure
import dev.alvr.katana.domain.session.models.AnilistToken
import dev.alvr.katana.domain.session.repositories.SessionPreferencesRepository
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FunSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.next
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

internal class SaveAnilistTokenUseCaseTest : FunSpec({
    val repo = mockk<SessionPreferencesRepository>()
    val useCase = SaveAnilistTokenUseCase(repo)

    val token = Arb.bind<AnilistToken>().next()

    context("successful saving") {
        coEvery { repo.saveAnilistToken(token) } returns Unit.right()

        test("invoke should save the token") {
            useCase(token).shouldBeRight()
            coVerify(exactly = 1) { repo.saveAnilistToken(token) }
        }

        test("sync should return user") {
            useCase.sync(token).shouldBeRight()
            coVerify(exactly = 1) { repo.saveAnilistToken(token) }
        }
    }

    context("failure saving") {
        context("is a TokenPreferencesFailure.DeletingFailure") {
            coEvery { repo.saveAnilistToken(token) } returns SessionPreferencesFailure.SavingFailure.left()

            test("invoke should return failure") {
                useCase(token).shouldBeLeft(SessionPreferencesFailure.SavingFailure)
                coVerify(exactly = 1) { repo.saveAnilistToken(token) }
            }

            test("sync should return failure") {
                useCase.sync(token).shouldBeLeft(SessionPreferencesFailure.SavingFailure)
                coVerify(exactly = 1) { repo.saveAnilistToken(token) }
            }
        }

        context("is a Failure.Unknown") {
            coEvery { repo.saveAnilistToken(token) } returns Failure.Unknown.left()

            test("invoke should return failure") {
                useCase(token).shouldBeLeft(Failure.Unknown)
                coVerify(exactly = 1) { repo.saveAnilistToken(token) }
            }

            test("sync should return failure") {
                useCase.sync(token).shouldBeLeft(Failure.Unknown)
                coVerify(exactly = 1) { repo.saveAnilistToken(token) }
            }
        }
    }
},)
