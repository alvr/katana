package dev.alvr.katana.common.session.domain.usecases

import arrow.core.none
import arrow.core.some
import dev.alvr.katana.common.session.domain.anilistTokenMock
import dev.alvr.katana.common.session.domain.repositories.SessionRepository
import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.domain.usecases.invoke
import dev.alvr.katana.core.tests.di.coreTestsModule
import dev.alvr.katana.core.tests.koinExtension
import dev.alvr.katana.core.tests.shouldBeNone
import dev.alvr.katana.core.tests.shouldBeSome
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.test.TestCase
import org.koin.test.KoinTest
import org.koin.test.inject

internal class GetAnilistTokenUseCaseTest : FreeSpec(), KoinTest {
    private val dispatcher by inject<KatanaDispatcher>()
    private val repo = mock<SessionRepository>()

    private lateinit var useCase: GetAnilistTokenUseCase

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

    override suspend fun beforeEach(testCase: TestCase) {
        useCase = GetAnilistTokenUseCase(dispatcher, repo)
    }

    override fun extensions() = listOf(koinExtension(coreTestsModule))
}
