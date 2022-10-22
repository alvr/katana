package dev.alvr.katana.domain.session.usecases

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.session.failures.SessionFailure
import dev.alvr.katana.domain.session.repositories.SessionRepository
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FunSpec
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.flow.flowOf

internal class ObserveActiveSessionUseCaseTest : FunSpec() {
    private val repo = mockk<SessionRepository>()
    private val useCase = spyk(ObserveActiveSessionUseCase(repo))

    init {
        context("right active session observer") {
            test("invoke should observe if the session is active") {
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

                coVerify(exactly = 1) { useCase.invoke(Unit) }
                coVerify(exactly = 1) { repo.sessionActive }
            }

            test("invoke the use case should call the invoke operator") {
                every { repo.sessionActive } returns flowOf(
                    false.right(),
                    true.right(),
                    false.right(),
                    true.right(),
                    true.right(),
                    false.right(),
                )

                useCase(Unit)

                useCase.flow.test(5.seconds) {
                    awaitItem().shouldBeRight(false)
                    awaitItem().shouldBeRight(true)
                    awaitItem().shouldBeRight(false)
                    awaitItem().shouldBeRight(true)
                    awaitItem().shouldBeRight(false)
                    cancelAndConsumeRemainingEvents()
                }

                coVerify(exactly = 1) { useCase.invoke(Unit) }
                coVerify(exactly = 1) { repo.sessionActive }
            }
        }

        context("left active session observer") {
            test("invoke should observe if the session is active") {
                every { repo.sessionActive } returns flowOf(mockk<SessionFailure>().left())

                useCase()

                useCase.flow.test(5.seconds) {
                    awaitItem().shouldBeLeft()
                    cancelAndConsumeRemainingEvents()
                }

                coVerify(exactly = 1) { useCase.invoke(Unit) }
                coVerify(exactly = 1) { repo.sessionActive }
            }

            test("invoke the use case should call the invoke operator") {
                every { repo.sessionActive } returns flowOf(mockk<SessionFailure>().left())

                useCase(Unit)

                useCase.flow.test(5.seconds) {
                    awaitItem().shouldBeLeft()
                    cancelAndConsumeRemainingEvents()
                }

                coVerify(exactly = 1) { useCase.invoke(Unit) }
                coVerify(exactly = 1) { repo.sessionActive }
            }
        }
    }
}
