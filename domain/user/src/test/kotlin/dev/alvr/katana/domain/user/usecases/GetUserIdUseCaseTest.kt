package dev.alvr.katana.domain.user.usecases

import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.tests.valueMockk
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.base.usecases.sync
import dev.alvr.katana.domain.user.failures.UserFailure
import dev.alvr.katana.domain.user.models.UserId
import dev.alvr.katana.domain.user.repositories.UserRepository
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FunSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify

internal class GetUserIdUseCaseTest : FunSpec() {
    private val repo = mockk<UserRepository>()
    private val useCase = spyk(GetUserIdUseCase(repo))

    private val user = valueMockk<UserId>()

    init {
        context("successful userId") {
            coEvery { repo.getUserId() } returns user.right()

            test("invoke should return user") {
                useCase().shouldBeRight(user)
                coVerify(exactly = 1) { repo.getUserId() }
            }

            test("sync should return user") {
                useCase.sync().shouldBeRight(user)
                coVerify(exactly = 1) { repo.getUserId() }
            }
        }

        context("failure userId") {
            coEvery { repo.getUserId() } returns UserFailure.GettingUserId.left()

            test("invoke should return failure") {
                useCase().shouldBeLeft(UserFailure.GettingUserId)
                coVerify(exactly = 1) { repo.getUserId() }
            }

            test("sync should return failure") {
                useCase.sync().shouldBeLeft(UserFailure.GettingUserId)
                coVerify(exactly = 1) { repo.getUserId() }
            }
        }

        test("invoke the use case should call the invoke operator") {
            coEvery { repo.getUserId() } returns mockk()

            useCase()

            coVerify(exactly = 1) { useCase.invoke(Unit) }
            coVerify(exactly = 1) { repo.getUserId() }
        }

        test("sync the use case should call the invoke operator") {
            coEvery { repo.getUserId() } returns mockk()

            useCase.sync()

            verify(exactly = 1) { useCase.sync(Unit) }
            coVerify(exactly = 1) { repo.getUserId() }
        }
    }
}
