package dev.alvr.katana.common.user.domain.usecases

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.user.domain.di.createUserDomainTestGraph
import dev.alvr.katana.common.user.domain.failures.UserFailure
import dev.alvr.katana.common.user.domain.repositories.UserRepository
import dev.alvr.katana.common.user.domain.userInfoMock
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

internal class ObserveUserInfoUseCaseTest : FreeSpec() {
    private val repo = mock<UserRepository>()

    private lateinit var useCase: ObserveUserInfoUseCase

    init {
        "successfully observing user info" {
            every { repo.userInfo } returns flowOf(userInfoMock.right())

            useCase()

            useCase.flow.test {
                awaitItem().shouldBeRight(userInfoMock)
                ensureAllEventsConsumed()
            }

            verify { repo.userInfo }
        }

        "failure observe user info" {
            every { repo.userInfo } returns flowOf(UserFailure.GettingUserInfo.left())

            useCase()

            useCase.flow.test {
                awaitItem().shouldBeLeft(UserFailure.GettingUserInfo)
                ensureAllEventsConsumed()
            }

            verify { repo.userInfo }
        }
    }

    override suspend fun beforeEach(testCase: TestCase) {
        useCase = createUserDomainTestGraph(repo).observeUserInfoUseCase
    }
}
