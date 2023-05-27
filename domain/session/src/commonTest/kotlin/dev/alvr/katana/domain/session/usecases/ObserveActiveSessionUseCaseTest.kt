package dev.alvr.katana.domain.session.usecases

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.tests.invoke
import dev.alvr.katana.common.tests.shouldBeLeft
import dev.alvr.katana.common.tests.shouldBeRight
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.session.failures.SessionFailure
import dev.alvr.katana.domain.session.repositories.MockSessionRepository
import dev.alvr.katana.domain.session.repositories.SessionRepository
import io.kotest.core.spec.style.FreeSpec
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.flow.flowOf
import org.kodein.mock.Mocker
import org.kodein.mock.UsesMocks

@UsesMocks(SessionRepository::class)
internal class ObserveActiveSessionUseCaseTest : FreeSpec() {
    private val mocker = Mocker()
    private val repo = MockSessionRepository(mocker)

    private val useCase = ObserveActiveSessionUseCase(repo)

    init {
        "successfully observing the session" {
            mocker.every { repo.sessionActive } returns flowOf(
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

            mocker.verify { repo.sessionActive }
        }

        "failure observing the session" {
            mocker.every { repo.sessionActive } returns flowOf(SessionFailure.CheckingActiveSession.left())

            useCase()

            useCase.flow.test(5.seconds) {
                awaitItem().shouldBeLeft(SessionFailure.CheckingActiveSession)
                cancelAndConsumeRemainingEvents()
            }

            mocker.verify { repo.sessionActive }
        }
    }

    override fun extensions() = listOf(mocker())
}
