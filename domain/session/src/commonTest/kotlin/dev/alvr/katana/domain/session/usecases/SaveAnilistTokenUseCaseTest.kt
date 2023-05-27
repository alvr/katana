package dev.alvr.katana.domain.session.usecases

import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.tests.invoke
import dev.alvr.katana.common.tests.shouldBeLeft
import dev.alvr.katana.common.tests.shouldBeRight
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.session.anilistToken
import dev.alvr.katana.domain.session.failures.SessionFailure
import dev.alvr.katana.domain.session.repositories.MockSessionRepository
import dev.alvr.katana.domain.session.repositories.SessionRepository
import io.kotest.core.spec.style.FreeSpec
import org.kodein.mock.Mocker
import org.kodein.mock.UsesMocks

@UsesMocks(SessionRepository::class)
internal class SaveAnilistTokenUseCaseTest : FreeSpec() {
    private val mocker = Mocker()
    private val repo = MockSessionRepository(mocker)

    private val useCase = SaveSessionUseCase(repo)

    init {
        "successfully saving the session" {
            mocker.everySuspending { repo.saveSession(isAny()) } returns Unit.right()
            useCase(anilistToken).shouldBeRight(Unit)
            mocker.verifyWithSuspend { repo.saveSession(anilistToken) }
        }

        listOf(
            SessionFailure.SavingSession to SessionFailure.SavingSession.left(),
            Failure.Unknown to Failure.Unknown.left(),
        ).forEach { (expected, failure) ->
            "failure saving the session ($expected)" {
                mocker.everySuspending { repo.saveSession(isAny()) } returns failure
                useCase(anilistToken).shouldBeLeft(expected)
                mocker.verifyWithSuspend { repo.saveSession(anilistToken) }
            }
        }
    }

    override fun extensions() = listOf(mocker())
}
