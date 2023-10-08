package dev.alvr.katana.data.remote.user.repositories

import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.tests.invoke
import dev.alvr.katana.common.tests.shouldBeLeft
import dev.alvr.katana.common.tests.shouldBeRight
import dev.alvr.katana.data.remote.user.sources.id.MockUserIdRemoteSource
import dev.alvr.katana.data.remote.user.sources.id.UserIdRemoteSource
import dev.alvr.katana.data.remote.user.sources.info.MockUserInfoRemoteSource
import dev.alvr.katana.data.remote.user.sources.info.UserInfoRemoteSource
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
@UsesMocks(
    UserIdRemoteSource::class,
    UserInfoRemoteSource::class,
)
internal class UserRepositoryTest : FreeSpec() {
    private val mocker = Mocker()
    private val userIdSource = MockUserIdRemoteSource(mocker)
    private val userInfoSource = MockUserInfoRemoteSource(mocker)

    private val repo: UserRepository = UserRepositoryImpl(userIdSource, userInfoSource)

    init {
        "getting the user id" - {
            "the server returns no data" {
                mocker.everySuspending { userIdSource.getUserId() } returns fakeUserId().right()
                repo.getUserId().shouldBeRight(fakeUserId())
                mocker.verifyWithSuspend { userIdSource.getUserId() }
            }

            "the server returns an empty userId" {
                mocker.everySuspending { userIdSource.getUserId() } returns UserFailure.GettingUserId.left()
                repo.getUserId().shouldBeLeft(UserFailure.GettingUserId)
                mocker.verifyWithSuspend { userIdSource.getUserId() }
            }
        }

        "saving the user id" - {
            "is successful" {
                mocker.everySuspending { userIdSource.saveUserId() } returns Unit.right()
                repo.saveUserId().shouldBeRight()
                mocker.verifyWithSuspend { userIdSource.saveUserId() }
            }

            listOf(
                UserFailure.FetchingUser to UserFailure.FetchingUser.left(),
                UserFailure.SavingUser to UserFailure.SavingUser.left(),
                Failure.Unknown to Failure.Unknown.left(),
            ).forEach { (expected, failure) ->
                "failure getting the user id ($expected)" {
                    mocker.everySuspending { userIdSource.saveUserId() } returns failure
                    repo.saveUserId().shouldBeLeft(expected)
                    mocker.verifyWithSuspend { userIdSource.saveUserId() }
                }
            }
        }
    }

    override fun extensions() = listOf(mocker())
}
