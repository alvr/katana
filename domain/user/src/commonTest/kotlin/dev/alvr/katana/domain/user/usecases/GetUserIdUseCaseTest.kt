package dev.alvr.katana.domain.user.usecases

import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.tests.invoke
import dev.alvr.katana.common.tests.shouldBeLeft
import dev.alvr.katana.common.tests.shouldBeRight
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.user.failures.UserFailure
import dev.alvr.katana.domain.user.models.UserId
import dev.alvr.katana.domain.user.models.fakeUserId
import dev.alvr.katana.domain.user.repositories.MockUserRepository
import dev.alvr.katana.domain.user.repositories.UserRepository
import io.kotest.core.spec.style.FreeSpec
import org.kodein.mock.Mocker
import org.kodein.mock.UsesFakes
import org.kodein.mock.UsesMocks

@UsesFakes(UserId::class)
@UsesMocks(UserRepository::class)
internal class GetUserIdUseCaseTest : FreeSpec() {
    private val mocker = Mocker()
    private val repo = MockUserRepository(mocker)

    private val useCase = GetUserIdUseCase(repo)

    init {
        "successfully getting user id" {
            mocker.everySuspending { repo.getUserId() } returns fakeUserId().right()
            useCase().shouldBeRight(fakeUserId())
            mocker.verifyWithSuspend { repo.getUserId() }
        }

        listOf(
            UserFailure.GettingUserId to UserFailure.GettingUserId.left(),
            Failure.Unknown to Failure.Unknown.left(),
        ).forEach { (expected, failure) ->
            "failure getting user id ($expected)" {
                mocker.everySuspending { repo.getUserId() } returns failure
                useCase().shouldBeLeft(expected)
                mocker.verifyWithSuspend { repo.getUserId() }
            }
        }
    }

    override fun extensions() = listOf(mocker())
}
