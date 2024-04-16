package dev.alvr.katana.common.session.domain.usecases

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.session.domain.failures.SessionFailure
import dev.alvr.katana.common.session.domain.repositories.SessionRepository
import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.domain.usecases.invoke
import dev.alvr.katana.core.tests.di.coreTestsModule
import dev.alvr.katana.core.tests.koinExtension
import dev.alvr.katana.core.tests.shouldBeLeft
import dev.alvr.katana.core.tests.shouldBeRight
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.verify
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.test.TestCase
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.flow.flowOf
import org.koin.test.KoinTest
import org.koin.test.inject

internal class ObserveActiveSessionUseCaseTest : FreeSpec(), KoinTest {
    private val dispatcher by inject<KatanaDispatcher>()
    private val repo = mock<SessionRepository>()

    private lateinit var useCase: ObserveActiveSessionUseCase

    init {
        "successfully observing the session" {
            every { repo.sessionActive } returns flowOf(
                false.right(),
                true.right(),
                false.right(),
                true.right(),
                true.right(),
                false.right(),
            )

            useCase()

            useCase.flow.test(100.milliseconds) {
                awaitItem().shouldBeRight(false)
                awaitItem().shouldBeRight(true)
                awaitItem().shouldBeRight(false)
                awaitItem().shouldBeRight(true)
                awaitItem().shouldBeRight(false)
                cancelAndConsumeRemainingEvents()
            }

            verify { repo.sessionActive }
        }

        "failure observing the session" {
            every { repo.sessionActive } returns flowOf(SessionFailure.CheckingActiveSession.left())

            useCase()

            useCase.flow.test(100.milliseconds) {
                awaitItem().shouldBeLeft(SessionFailure.CheckingActiveSession)
                cancelAndConsumeRemainingEvents()
            }

            verify { repo.sessionActive }
        }
    }

    override suspend fun beforeEach(testCase: TestCase) {
        useCase = ObserveActiveSessionUseCase(dispatcher, repo)
    }

    override fun extensions() = listOf(koinExtension(coreTestsModule))
}
