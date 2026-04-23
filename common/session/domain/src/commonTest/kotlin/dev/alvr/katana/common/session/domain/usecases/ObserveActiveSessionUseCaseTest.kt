package dev.alvr.katana.common.session.domain.usecases

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.session.domain.di.createSessionDomainTestGraph
import dev.alvr.katana.common.session.domain.failures.SessionFailure
import dev.alvr.katana.common.session.domain.repositories.SessionRepository
import dev.alvr.katana.core.domain.usecases.invoke
import dev.alvr.katana.core.tests.shouldBeLeft
import dev.alvr.katana.core.tests.shouldBeRight
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.verify
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.test.TestCase
import kotlinx.coroutines.flow.flowOf

internal class ObserveActiveSessionUseCaseTest : FreeSpec() {
    private val repo = mock<SessionRepository>()

    private lateinit var useCase: ObserveActiveSessionUseCase

    init {
        "successfully observing the session" {
            every { repo.sessionActive } returns
                flowOf(false.right(), true.right(), false.right(), true.right(), true.right(), false.right())

            useCase()

            useCase.flow.test {
                awaitItem().shouldBeRight(false)
                awaitItem().shouldBeRight(true)
                awaitItem().shouldBeRight(false)
                awaitItem().shouldBeRight(true)
                awaitItem().shouldBeRight(false)
                ensureAllEventsConsumed()
            }

            verify { repo.sessionActive }
        }

        "failure observing the session" {
            every { repo.sessionActive } returns flowOf(SessionFailure.CheckingActiveSession.left())

            useCase()

            useCase.flow.test {
                awaitItem().shouldBeLeft(SessionFailure.CheckingActiveSession)
                ensureAllEventsConsumed()
            }

            verify { repo.sessionActive }
        }
    }

    override suspend fun beforeEach(testCase: TestCase) {
        useCase = createSessionDomainTestGraph(repo).observeActiveSessionUseCase
    }
}
