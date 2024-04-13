package dev.alvr.katana.common.user.domain.usecases

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.user.domain.failures.UserFailure
import dev.alvr.katana.common.user.domain.repositories.UserRepository
import dev.alvr.katana.common.user.domain.userInfoMock
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

internal class ObserveUserInfoUseCaseTest : FreeSpec(), KoinTest {
    private val dispatcher by inject<KatanaDispatcher>()
    private val repo = mock<UserRepository>()

    private lateinit var useCase: ObserveUserInfoUseCase

    init {
        "successfully observing user info" {
            every { repo.userInfo } returns flowOf(userInfoMock.right())

            useCase()

            useCase.flow.test(100.milliseconds) {
                awaitItem().shouldBeRight(userInfoMock)
                cancelAndConsumeRemainingEvents()
            }

            verify { repo.userInfo }
        }

        "failure observe user info" {
            every { repo.userInfo } returns flowOf(UserFailure.GettingUserInfo.left())

            useCase()

            useCase.flow.test(100.milliseconds) {
                awaitItem().shouldBeLeft(UserFailure.GettingUserInfo)
                cancelAndConsumeRemainingEvents()
            }

            verify { repo.userInfo }
        }
    }

    override suspend fun beforeEach(testCase: TestCase) {
        useCase = ObserveUserInfoUseCase(dispatcher, repo)
    }

    override fun extensions() = listOf(koinExtension(coreTestsModule))
}
