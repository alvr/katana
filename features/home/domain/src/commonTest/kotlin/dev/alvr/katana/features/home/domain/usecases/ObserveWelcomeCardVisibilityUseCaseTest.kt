package dev.alvr.katana.features.home.domain.usecases

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.domain.usecases.invoke
import dev.alvr.katana.core.tests.shouldBeLeft
import dev.alvr.katana.core.tests.shouldBeRight
import dev.alvr.katana.features.home.domain.failures.HomeFailure
import dev.alvr.katana.features.home.domain.repositories.HomeRepository
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.verify
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.test.TestCase
import kotlinx.coroutines.flow.flowOf
import org.koin.test.KoinTest
import org.koin.test.inject

internal class ObserveWelcomeCardVisibilityUseCaseTest : FreeSpec(), KoinTest {
    private val dispatcher by inject<KatanaDispatcher>()
    private val repo = mock<HomeRepository>()

    private lateinit var useCase: ObserveWelcomeCardVisibilityUseCase

    init {
        "successfully observe the welcome card visibility" {
            every { repo.welcomeCardVisible } returns flowOf(false.right(), true.right())

            useCase()

            useCase.flow.test {
                awaitItem().shouldBeRight(false)
                awaitItem().shouldBeRight(true)
                ensureAllEventsConsumed()
            }

            verify { repo.welcomeCardVisible }
        }

        "failure observe the welcome card visibility" {
            every { repo.welcomeCardVisible } returns flowOf(HomeFailure.GettingWelcomeCardVisibility.left())

            useCase()

            useCase.flow.test {
                awaitItem().shouldBeLeft(HomeFailure.GettingWelcomeCardVisibility)
                ensureAllEventsConsumed()
            }

            verify { repo.welcomeCardVisible }
        }
    }

    override suspend fun beforeEach(testCase: TestCase) {
        useCase = ObserveWelcomeCardVisibilityUseCase(dispatcher, repo)
    }
}
