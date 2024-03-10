package dev.alvr.katana.common.session.domain.usecases

import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.session.domain.anilistTokenMock
import dev.alvr.katana.common.session.domain.failures.SessionFailure
import dev.alvr.katana.common.session.domain.repositories.SessionRepository
import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.domain.failures.Failure
import dev.alvr.katana.core.tests.di.coreTestsModule
import dev.alvr.katana.core.tests.koinExtension
import dev.alvr.katana.core.tests.shouldBeLeft
import dev.alvr.katana.core.tests.shouldBeRight
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.test.TestCase
import org.koin.test.KoinTest
import org.koin.test.inject

internal class SaveAnilistTokenUseCaseTest : FreeSpec(), KoinTest {
    private val dispatcher by inject<KatanaDispatcher>()
    private val repo = mock<SessionRepository>()

    private lateinit var useCase: SaveSessionUseCase

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

    override suspend fun beforeEach(testCase: TestCase) {
        useCase = SaveSessionUseCase(dispatcher, repo)
    }

    override fun extensions() = listOf(koinExtension(coreTestsModule))
}
