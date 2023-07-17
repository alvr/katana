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
internal class DeleteAnilistTokenUseCaseTest : FreeSpec() {
    private val mocker = Mocker()
    private val repo = MockSessionRepository(mocker)

    private val useCase = DeleteAnilistTokenUseCase(repo)

    init {
        "successfully deleting the token" {
            mocker.everySuspending { repo.deleteAnilistToken() } returns Unit.right()
            useCase().shouldBeRight(Unit)
            mocker.verifyWithSuspend { repo.deleteAnilistToken() }
        }

        listOf(
            SessionFailure.DeletingToken to SessionFailure.DeletingToken.left(),
            Failure.Unknown to Failure.Unknown.left(),
        ).forEach { (expected, failure) ->
            "failure deleting the token ($expected)" {
                mocker.everySuspending { repo.deleteAnilistToken() } returns failure
                useCase().shouldBeLeft(expected)
                mocker.verifyWithSuspend { repo.deleteAnilistToken() }
            }
        }
    }

    override fun extensions() = listOf(mocker())
}
