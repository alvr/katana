package dev.alvr.katana.domain.session.usecases

import arrow.core.none
import arrow.core.some
import dev.alvr.katana.common.tests.shouldBeNone
import dev.alvr.katana.common.tests.shouldBeSome
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.session.anilistTokenMock
import dev.alvr.katana.domain.session.repositories.SessionRepository
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import io.kotest.core.spec.style.FreeSpec

internal class GetAnilistTokenUseCaseTest : FreeSpec() {
    private val repo = mock<SessionRepository>()

    private val useCase = GetAnilistTokenUseCase(repo)

    init {
        "successfully getting the token" {
            everySuspend { repo.getAnilistToken() } returns anilistTokenMock.some()
            useCase().shouldBeSome(anilistTokenMock)
            verifySuspend { repo.getAnilistToken() }
        }

        "failure getting the token" {
            everySuspend { repo.getAnilistToken() } returns none()
            useCase().shouldBeNone()
            verifySuspend { repo.getAnilistToken() }
        }
    }
}
