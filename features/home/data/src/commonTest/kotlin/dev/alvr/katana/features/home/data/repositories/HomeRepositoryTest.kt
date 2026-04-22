package dev.alvr.katana.features.home.data.repositories

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import dev.alvr.katana.core.tests.shouldBeLeft
import dev.alvr.katana.core.tests.shouldBeRight
import dev.alvr.katana.features.home.data.di.createHomeRepositoryTestGraph
import dev.alvr.katana.features.home.data.sources.HomeLocalSource
import dev.alvr.katana.features.home.data.sources.HomeRemoteSource
import dev.alvr.katana.features.home.domain.failures.HomeFailure
import dev.alvr.katana.features.home.domain.repositories.HomeRepository
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verifySuspend
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.test.TestCase
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

internal class HomeRepositoryTest : FreeSpec() {
    private val localSource = mock<HomeLocalSource> { every { welcomeCardVisible } returns emptyFlow() }
    private val remoteSource = mock<HomeRemoteSource>()

    private lateinit var repo: HomeRepository

    init {
        "observing welcome card visibility" -
            {
                every { localSource.welcomeCardVisible } returns flowOf(true.right(), true.right(), false.right())

                "observing if the session is active" {
                    repo.welcomeCardVisible.test {
                        awaitItem().shouldBeRight(true)
                        awaitItem().shouldBeRight(true)
                        awaitItem().shouldBeRight(false)
                        awaitComplete()
                    }

                    verify { localSource.welcomeCardVisible }
                }
            }

        "getting welcome card visibility" -
            {
                "is successful" {
                    everySuspend { localSource.hideWelcomeCard() } returns Unit.right()
                    repo.hideWelcomeCard().shouldBeRight()
                    verifySuspend { localSource.hideWelcomeCard() }
                }

                "is failure" {
                    everySuspend { localSource.hideWelcomeCard() } returns HomeFailure.HidingWelcomeCard.left()
                    repo.hideWelcomeCard().shouldBeLeft(HomeFailure.HidingWelcomeCard)
                    verifySuspend { localSource.hideWelcomeCard() }
                }
            }
    }

    override suspend fun beforeEach(testCase: TestCase) {
        repo = createHomeRepositoryTestGraph(localSource, remoteSource).homeRepository
    }
}
