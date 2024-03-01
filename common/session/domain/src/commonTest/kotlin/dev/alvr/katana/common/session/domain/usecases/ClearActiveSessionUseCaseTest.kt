package dev.alvr.katana.common.session.domain.usecases

import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.session.domain.failures.SessionFailure
import dev.alvr.katana.common.session.domain.repositories.SessionRepository
import dev.alvr.katana.core.domain.failures.Failure
import dev.alvr.katana.core.domain.usecases.invoke
import dev.alvr.katana.core.tests.shouldBeLeft
import dev.alvr.katana.core.tests.shouldBeRight
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import io.kotest.core.spec.style.FreeSpec

internal class ClearActiveSessionUseCaseTest : FreeSpec() {
    private val repo = mock<SessionRepository>()

    private val useCase = ClearActiveSessionUseCase(repo)

    init {
        "successfully clearing the session" {
            everySuspend { repo.clearActiveSession() } returns Unit.right()
            useCase().shouldBeRight(Unit)
            verifySuspend { repo.clearActiveSession() }
        }

        listOf(
            SessionFailure.ClearingSession to SessionFailure.ClearingSession.left(),
            Failure.Unknown to Failure.Unknown.left(),
        ).forEach { (expected, failure) ->
            "failure clearing the session ($expected)" {
                everySuspend { repo.clearActiveSession() } returns failure
                useCase().shouldBeLeft(expected)
                verifySuspend { repo.clearActiveSession() }
            }
        }
    }
}
