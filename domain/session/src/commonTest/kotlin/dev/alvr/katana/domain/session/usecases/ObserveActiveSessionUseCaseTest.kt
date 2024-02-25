package dev.alvr.katana.domain.session.usecases

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.tests.shouldBeLeft
import dev.alvr.katana.common.tests.shouldBeRight
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.session.failures.SessionFailure
import dev.alvr.katana.domain.session.repositories.SessionRepository
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.verify
import io.kotest.core.spec.style.FreeSpec
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.flow.flowOf

internal class ObserveActiveSessionUseCaseTest : FreeSpec() {
    private val repo = mock<SessionRepository>()

    private val useCase = ObserveActiveSessionUseCase(repo)

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

            useCase.flow.test(5.seconds) {
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

            useCase.flow.test(5.seconds) {
                awaitItem().shouldBeLeft(SessionFailure.CheckingActiveSession)
                cancelAndConsumeRemainingEvents()
            }

            verify { repo.sessionActive }
        }
    }
}
