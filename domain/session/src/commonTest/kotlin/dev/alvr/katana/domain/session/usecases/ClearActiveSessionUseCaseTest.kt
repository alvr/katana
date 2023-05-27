package dev.alvr.katana.domain.session.usecases

import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.tests.invoke
import dev.alvr.katana.common.tests.shouldBeLeft
import dev.alvr.katana.common.tests.shouldBeRight
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.session.failures.SessionFailure
import dev.alvr.katana.domain.session.repositories.MockSessionRepository
import dev.alvr.katana.domain.session.repositories.SessionRepository
import io.kotest.core.spec.style.FreeSpec
import org.kodein.mock.Mocker
import org.kodein.mock.UsesMocks

@UsesMocks(SessionRepository::class)
internal class ClearActiveSessionUseCaseTest : FreeSpec() {
    private val mocker = Mocker()
    private val repo = MockSessionRepository(mocker)

    private val useCase = ClearActiveSessionUseCase(repo)

    init {
        "successfully clearing the session" {
            mocker.everySuspending { repo.clearActiveSession() } returns Unit.right()
            useCase().shouldBeRight(Unit)
            mocker.verifyWithSuspend { repo.clearActiveSession() }
        }

        listOf(
            SessionFailure.ClearingSession to SessionFailure.ClearingSession.left(),
            Failure.Unknown to Failure.Unknown.left(),
        ).forEach { (expected, failure) ->
            "failure clearing the session ($expected)" {
                mocker.everySuspending { repo.clearActiveSession() } returns failure
                useCase().shouldBeLeft(expected)
                mocker.verifyWithSuspend { repo.clearActiveSession() }
            }
        }
    }

    override fun extensions() = listOf(mocker())
}
