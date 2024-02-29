package dev.alvr.katana.domain.session.usecases

import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.tests.shouldBeLeft
import dev.alvr.katana.common.tests.shouldBeRight
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.session.failures.SessionFailure
import dev.alvr.katana.domain.session.repositories.SessionRepository
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import io.kotest.core.spec.style.FreeSpec

internal class LogOutUseCaseTest : FreeSpec() {
    private val repo = mock<SessionRepository>()

    private val useCase = LogOutUseCase(repo)

    init {
        "successfully saving the session" {
            everySuspend { repo.logout() } returns Unit.right()
            useCase().shouldBeRight(Unit)
            verifySuspend { repo.logout() }
        }

        listOf(
            SessionFailure.LoggingOut to SessionFailure.LoggingOut.left(),
            Failure.Unknown to Failure.Unknown.left(),
        ).forEach { (expected, failure) ->
            "failure saving the session ($expected)" {
                everySuspend { repo.logout() } returns failure
                useCase().shouldBeLeft(expected)
                verifySuspend { repo.logout() }
            }
        }
    }
}
