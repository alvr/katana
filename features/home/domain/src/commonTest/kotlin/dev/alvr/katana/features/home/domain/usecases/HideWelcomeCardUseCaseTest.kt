package dev.alvr.katana.features.home.domain.usecases

import arrow.core.left
import arrow.core.right
import dev.alvr.katana.core.domain.failures.Failure
import dev.alvr.katana.core.domain.usecases.invoke
import dev.alvr.katana.core.tests.shouldBeLeft
import dev.alvr.katana.core.tests.shouldBeRight
import dev.alvr.katana.features.home.domain.di.createHomeDomainTestGraph
import dev.alvr.katana.features.home.domain.failures.HomeFailure
import dev.alvr.katana.features.home.domain.repositories.HomeRepository
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.test.TestCase

internal class HideWelcomeCardUseCaseTest : FreeSpec() {
    private val repo = mock<HomeRepository>()

    private lateinit var useCase: HideWelcomeCardUseCase

    init {
        "successfully hiding the welcome card" {
            everySuspend { repo.hideWelcomeCard() } returns Unit.right()
            useCase().shouldBeRight(Unit)
            verifySuspend { repo.hideWelcomeCard() }
        }

        listOf(
                HomeFailure.HidingWelcomeCard to HomeFailure.HidingWelcomeCard.left(),
                Failure.Unknown to Failure.Unknown.left(),
            )
            .forEach { (expected, failure) ->
                "failure hiding the welcome card ($expected)" {
                    everySuspend { repo.hideWelcomeCard() } returns failure
                    useCase().shouldBeLeft(expected)
                    verifySuspend { repo.hideWelcomeCard() }
                }
            }
    }

    override suspend fun beforeEach(testCase: TestCase) {
        useCase = createHomeDomainTestGraph(repo).hideWelcomeCardUseCase
    }
}
