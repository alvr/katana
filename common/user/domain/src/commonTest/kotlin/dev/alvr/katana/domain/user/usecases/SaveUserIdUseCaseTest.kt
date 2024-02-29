package dev.alvr.katana.domain.user.usecases

import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.tests.shouldBeLeft
import dev.alvr.katana.common.tests.shouldBeRight
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.user.failures.UserFailure
import dev.alvr.katana.domain.user.repositories.UserRepository
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import io.kotest.core.spec.style.FreeSpec

internal class SaveUserIdUseCaseTest : FreeSpec() {
    private val repo = mock<UserRepository>()

    private val useCase = SaveUserIdUseCase(repo)

    init {
        "successfully saving user id" {
            everySuspend { repo.saveUserId() } returns Unit.right()
            useCase().shouldBeRight(Unit)
            verifySuspend { repo.saveUserId() }
        }

        listOf(
            UserFailure.FetchingUser to UserFailure.FetchingUser.left(),
            UserFailure.SavingUser to UserFailure.SavingUser.left(),
            Failure.Unknown to Failure.Unknown.left(),
        ).forEach { (expected, failure) ->
            "failure saving user id ($expected)" {
                everySuspend { repo.saveUserId() } returns failure
                useCase().shouldBeLeft(expected)
                verifySuspend { repo.saveUserId() }
            }
        }
    }
}
