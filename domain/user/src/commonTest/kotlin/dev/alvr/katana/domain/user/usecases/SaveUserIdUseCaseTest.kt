package dev.alvr.katana.domain.user.usecases

import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.tests.invoke
import dev.alvr.katana.common.tests.shouldBeLeft
import dev.alvr.katana.common.tests.shouldBeRight
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.user.failures.UserFailure
import dev.alvr.katana.domain.user.repositories.MockUserRepository
import dev.alvr.katana.domain.user.repositories.UserRepository
import io.kotest.core.spec.style.FreeSpec
import org.kodein.mock.Mocker
import org.kodein.mock.UsesMocks

@UsesMocks(UserRepository::class)
internal class SaveUserIdUseCaseTest : FreeSpec() {
    private val mocker = Mocker()
    private val repo = MockUserRepository(mocker)

    private val useCase = SaveUserIdUseCase(repo)

    init {
        "successfully saving user id" {
            mocker.everySuspending { repo.saveUserId() } returns Unit.right()
            useCase().shouldBeRight(Unit)
            mocker.verifyWithSuspend { repo.saveUserId() }
        }

        listOf(
            UserFailure.FetchingUser to UserFailure.FetchingUser.left(),
            UserFailure.SavingUser to UserFailure.SavingUser.left(),
            Failure.Unknown to Failure.Unknown.left(),
        ).forEach { (expected, failure) ->
            "failure saving user id ($expected)" {
                mocker.everySuspending { repo.saveUserId() } returns failure
                useCase().shouldBeLeft(expected)
                mocker.verifyWithSuspend { repo.saveUserId() }
            }
        }
    }

    override fun extensions() = listOf(mocker())
}
