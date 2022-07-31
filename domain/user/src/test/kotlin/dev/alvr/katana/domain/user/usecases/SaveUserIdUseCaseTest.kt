package dev.alvr.katana.domain.user.usecases

import arrow.core.left
import arrow.core.right
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.base.usecases.sync
import dev.alvr.katana.domain.user.failures.UserFailure
import dev.alvr.katana.domain.user.repositories.UserRemoteRepository
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FunSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk

internal class SaveUserIdUseCaseTest : FunSpec({
    val repo = mockk<UserRemoteRepository>()
    val useCase = spyk(SaveUserIdUseCase(repo))

    context("successful userId") {
        coEvery { repo.saveUserId() } returns Unit.right()

        test("invoke should return user") {
            useCase().shouldBeRight()
            coVerify(exactly = 1) { repo.saveUserId() }
        }

        test("sync should return user") {
            useCase.sync().shouldBeRight()
            coVerify(exactly = 1) { repo.saveUserId() }
        }
    }

    context("failure userId") {
        context("is a UserFailure.FetchingFailure") {
            coEvery { repo.saveUserId() } returns UserFailure.FetchingFailure.left()

            test("invoke should return failure") {
                useCase().shouldBeLeft(UserFailure.FetchingFailure)
                coVerify(exactly = 1) { repo.saveUserId() }
            }

            test("sync should return failure") {
                useCase.sync().shouldBeLeft(UserFailure.FetchingFailure)
                coVerify(exactly = 1) { repo.saveUserId() }
            }
        }

        context("is a UserFailure.SavingFailure") {
            coEvery { repo.saveUserId() } returns UserFailure.SavingFailure.left()

            test("invoke should return failure") {
                useCase().shouldBeLeft(UserFailure.SavingFailure)
                coVerify(exactly = 1) { repo.saveUserId() }
            }

            test("sync should return failure") {
                useCase.sync().shouldBeLeft(UserFailure.SavingFailure)
                coVerify(exactly = 1) { repo.saveUserId() }
            }
        }

        context("is a Failure.Unknown") {
            coEvery { repo.saveUserId() } returns Failure.Unknown.left()

            test("invoke should return failure") {
                useCase().shouldBeLeft(Failure.Unknown)
                coVerify(exactly = 1) { repo.saveUserId() }
            }

            test("sync should return failure") {
                useCase.sync().shouldBeLeft(Failure.Unknown)
                coVerify(exactly = 1) { repo.saveUserId() }
            }
        }
    }

    test("invoke the use case should call the invoke operator") {
        coEvery { repo.saveUserId() } returns mockk()

        useCase()

        coVerify(exactly = 1) { useCase.invoke(Unit) }
    }

    test("sync the use case should call the invoke operator") {
        coEvery { repo.saveUserId() } returns mockk()

        useCase.sync()

        coVerify(exactly = 1) { useCase.sync(Unit) }
    }
},)
