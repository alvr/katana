package dev.alvr.katana.data.remote.user.repositories

import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.tests.invoke
import dev.alvr.katana.common.tests.shouldBeLeft
import dev.alvr.katana.common.tests.shouldBeRight
import dev.alvr.katana.data.remote.user.sources.MockUserRemoteSource
import dev.alvr.katana.data.remote.user.sources.UserRemoteSource
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.user.failures.UserFailure
import dev.alvr.katana.domain.user.models.UserId
import dev.alvr.katana.domain.user.models.fakeUserId
import dev.alvr.katana.domain.user.repositories.UserRepository
import io.kotest.core.spec.style.FreeSpec
import org.kodein.mock.Mocker
import org.kodein.mock.UsesFakes
import org.kodein.mock.UsesMocks

@UsesFakes(UserId::class)
@UsesMocks(UserRemoteSource::class)
internal class UserRepositoryTest : FreeSpec() {
    private val mocker = Mocker()
    private val source = MockUserRemoteSource(mocker)

    private val repo: UserRepository = UserRepositoryImpl(source)

    init {
        "getting the user id" - {
            "the server returns no data" {
                mocker.everySuspending { source.getUserId() } returns fakeUserId().right()
                repo.getUserId().shouldBeRight(fakeUserId())
                mocker.verifyWithSuspend { source.getUserId() }
            }

            "the server returns an empty userId" {
                mocker.everySuspending { source.getUserId() } returns UserFailure.GettingUserId.left()
                repo.getUserId().shouldBeLeft(UserFailure.GettingUserId)
                mocker.verifyWithSuspend { source.getUserId() }
            }
        }

        "saving the user id" - {
            "is successful" {
                mocker.everySuspending { source.saveUserId() } returns Unit.right()
                repo.saveUserId().shouldBeRight()
                mocker.verifyWithSuspend { source.saveUserId() }
            }

            listOf(
                UserFailure.FetchingUser to UserFailure.FetchingUser.left(),
                UserFailure.SavingUser to UserFailure.SavingUser.left(),
                Failure.Unknown to Failure.Unknown.left(),
            ).forEach { (expected, failure) ->
                "failure getting the user id ($expected)" {
                    mocker.everySuspending { source.saveUserId() } returns failure
                    repo.saveUserId().shouldBeLeft(expected)
                    mocker.verifyWithSuspend { source.saveUserId() }
                }
            }
        }
    }

    override fun extensions() = listOf(mocker())
}
