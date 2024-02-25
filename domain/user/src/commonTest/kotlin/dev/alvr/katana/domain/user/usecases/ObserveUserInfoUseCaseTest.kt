package dev.alvr.katana.domain.user.usecases

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.tests.shouldBeLeft
import dev.alvr.katana.common.tests.shouldBeRight
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.user.failures.UserFailure
import dev.alvr.katana.domain.user.repositories.UserRepository
import dev.alvr.katana.domain.user.userInfoMock
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.verify
import io.kotest.core.spec.style.FreeSpec
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.flow.flowOf

internal class ObserveUserInfoUseCaseTest : FreeSpec() {
    private val repo = mock<UserRepository>()

    private val useCase = ObserveUserInfoUseCase(repo)

    init {
        "successfully observing user info" {
            every { repo.userInfo } returns flowOf(userInfoMock.right())

            useCase()

            useCase.flow.test(5.seconds) {
                awaitItem().shouldBeRight(userInfoMock)
                cancelAndConsumeRemainingEvents()
            }

            verify { repo.userInfo }
        }

        "failure observe user info" {
            every { repo.userInfo } returns flowOf(UserFailure.GettingUserInfo.left())

            useCase()

            useCase.flow.test(5.seconds) {
                awaitItem().shouldBeLeft(UserFailure.GettingUserInfo)
                cancelAndConsumeRemainingEvents()
            }

            verify { repo.userInfo }
        }
    }
}
