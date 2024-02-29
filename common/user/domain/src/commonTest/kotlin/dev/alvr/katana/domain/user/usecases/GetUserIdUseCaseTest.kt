package dev.alvr.katana.domain.user.usecases

import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.tests.shouldBeLeft
import dev.alvr.katana.common.tests.shouldBeRight
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.user.failures.UserFailure
import dev.alvr.katana.domain.user.repositories.UserRepository
import dev.alvr.katana.domain.user.userIdMock
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import io.kotest.core.spec.style.FreeSpec

internal class GetUserIdUseCaseTest : FreeSpec() {
    private val repo = mock<UserRepository>()

    private val useCase = GetUserIdUseCase(repo)

    init {
        "successfully getting user id" {
            everySuspend { repo.getUserId() } returns userIdMock.right()
            useCase().shouldBeRight(userIdMock)
            verifySuspend { repo.getUserId() }
        }

        listOf(
            UserFailure.GettingUserId to UserFailure.GettingUserId.left(),
            Failure.Unknown to Failure.Unknown.left(),
        ).forEach { (expected, failure) ->
            "failure getting user id ($expected)" {
                everySuspend { repo.getUserId() } returns failure
                useCase().shouldBeLeft(expected)
                verifySuspend { repo.getUserId() }
            }
        }
    }
}
