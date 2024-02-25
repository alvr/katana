package dev.alvr.katana.domain.session.usecases

import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.tests.shouldBeLeft
import dev.alvr.katana.common.tests.shouldBeRight
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.session.anilistTokenMock
import dev.alvr.katana.domain.session.failures.SessionFailure
import dev.alvr.katana.domain.session.repositories.SessionRepository
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import io.kotest.core.spec.style.FreeSpec

internal class SaveAnilistTokenUseCaseTest : FreeSpec() {
    private val repo = mock<SessionRepository>()

    private val useCase = SaveSessionUseCase(repo)

    init {
        "successfully saving the session" {
            everySuspend { repo.saveSession(anilistTokenMock) } returns Unit.right()
            useCase(anilistTokenMock).shouldBeRight(Unit)
            verifySuspend { repo.saveSession(anilistTokenMock) }
        }

        listOf(
            SessionFailure.SavingSession to SessionFailure.SavingSession.left(),
            Failure.Unknown to Failure.Unknown.left(),
        ).forEach { (expected, failure) ->
            "failure saving the session ($expected)" {
                everySuspend { repo.saveSession(anilistTokenMock) } returns failure
                useCase(anilistTokenMock).shouldBeLeft(expected)
                verifySuspend { repo.saveSession(anilistTokenMock) }
            }
        }
    }
}
