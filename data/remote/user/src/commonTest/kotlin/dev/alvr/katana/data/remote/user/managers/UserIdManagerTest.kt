package dev.alvr.katana.data.remote.user.managers

import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.tests.invoke
import dev.alvr.katana.common.tests.shouldBeLeft
import dev.alvr.katana.common.tests.shouldBeRight
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.user.failures.UserFailure
import dev.alvr.katana.domain.user.managers.UserIdManager
import dev.alvr.katana.domain.user.models.UserId
import dev.alvr.katana.domain.user.repositories.MockUserRepository
import dev.alvr.katana.domain.user.repositories.UserRepository
import dev.alvr.katana.domain.user.usecases.GetUserIdUseCase
import io.kotest.core.spec.style.FreeSpec
import org.kodein.mock.Mocker
import org.kodein.mock.UsesMocks

@UsesMocks(UserRepository::class)
internal class UserIdManagerTest : FreeSpec() {
    private val mocker = Mocker()
    private val repo = MockUserRepository(mocker)

    private val useCase = GetUserIdUseCase(repo)
    private val manager: UserIdManager = UserIdManagerImpl(useCase)

    init {
        "server return viewer is valid" {
            mocker.everySuspending { repo.getUserId() } returns UserId(37_384).right()
            manager.getId().shouldBeRight(37_384)
            mocker.verifyWithSuspend { useCase() }
        }

        "server fails to return something" {
            mocker.everySuspending { repo.getUserId() } returns UserFailure.GettingUserId.left()
            manager.getId().shouldBeLeft(UserFailure.GettingUserId)
            mocker.verifyWithSuspend { useCase() }
        }
    }

    override fun extensions() = listOf(mocker())
}
