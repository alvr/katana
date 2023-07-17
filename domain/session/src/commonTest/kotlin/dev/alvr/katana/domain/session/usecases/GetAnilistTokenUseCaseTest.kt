package dev.alvr.katana.domain.session.usecases

import arrow.core.none
import arrow.core.some
import dev.alvr.katana.common.tests.invoke
import dev.alvr.katana.common.tests.shouldBeNone
import dev.alvr.katana.common.tests.shouldBeSome
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.session.anilistToken
import dev.alvr.katana.domain.session.repositories.MockSessionRepository
import dev.alvr.katana.domain.session.repositories.SessionRepository
import io.kotest.core.spec.style.FreeSpec
import org.kodein.mock.Mocker
import org.kodein.mock.UsesMocks

@UsesMocks(SessionRepository::class)
internal class GetAnilistTokenUseCaseTest : FreeSpec() {
    private val mocker = Mocker()
    private val repo = MockSessionRepository(mocker)

    private val useCase = GetAnilistTokenUseCase(repo)

    init {
        "successfully getting the token" {
            mocker.everySuspending { repo.getAnilistToken() } returns anilistToken.some()
            useCase().shouldBeSome(anilistToken)
            mocker.verifyWithSuspend { repo.getAnilistToken() }
        }

        "failure getting the token" {
            mocker.everySuspending { repo.getAnilistToken() } returns none()
            useCase().shouldBeNone()
            mocker.verifyWithSuspend { repo.getAnilistToken() }
        }
    }

    override fun extensions() = listOf(mocker())
}
