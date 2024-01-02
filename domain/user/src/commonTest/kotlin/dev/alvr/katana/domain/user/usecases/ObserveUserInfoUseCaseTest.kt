package dev.alvr.katana.domain.user.usecases

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.tests.invoke
import dev.alvr.katana.common.tests.shouldBeLeft
import dev.alvr.katana.common.tests.shouldBeRight
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.user.failures.UserFailure
import dev.alvr.katana.domain.user.models.UserInfo
import dev.alvr.katana.domain.user.models.fakeUserInfo
import dev.alvr.katana.domain.user.repositories.MockUserRepository
import dev.alvr.katana.domain.user.repositories.UserRepository
import io.kotest.core.spec.style.FreeSpec
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.flow.flowOf
import org.kodein.mock.Mocker
import org.kodein.mock.UsesFakes
import org.kodein.mock.UsesMocks

@UsesFakes(UserInfo::class)
@UsesMocks(UserRepository::class)
internal class ObserveUserInfoUseCaseTest : FreeSpec() {
    private val mocker = Mocker()
    private val repo = MockUserRepository(mocker)

    private val useCase = ObserveUserInfoUseCase(repo)

    init {
        "successfully observing user info" {
            mocker.every { repo.userInfo } returns flowOf(fakeUserInfo().right())

            useCase()

            useCase.flow.test(5.seconds) {
                awaitItem().shouldBeRight(fakeUserInfo())
                cancelAndConsumeRemainingEvents()
            }

            mocker.verify { repo.userInfo }
        }

        "failure observe user info" {
            mocker.every { repo.userInfo } returns flowOf(UserFailure.GettingUserInfo.left())

            useCase()

            useCase.flow.test(5.seconds) {
                awaitItem().shouldBeLeft(UserFailure.GettingUserInfo)
                cancelAndConsumeRemainingEvents()
            }

            mocker.verify { repo.userInfo }
        }
    }

    override fun extensions() = listOf(mocker())
}
