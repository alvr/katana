package dev.alvr.katana.common.user.data.repositories

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.user.data.sources.id.UserIdRemoteSource
import dev.alvr.katana.common.user.data.sources.info.UserInfoRemoteSource
import dev.alvr.katana.common.user.data.userIdMock
import dev.alvr.katana.common.user.domain.failures.UserFailure
import dev.alvr.katana.common.user.domain.repositories.UserRepository
import dev.alvr.katana.core.domain.failures.Failure
import dev.alvr.katana.core.tests.shouldBeLeft
import dev.alvr.katana.core.tests.shouldBeRight
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verifySuspend
import io.kotest.core.spec.style.FreeSpec
import kotlinx.coroutines.flow.emptyFlow

internal class UserRepositoryTest : FreeSpec() {
    private val userIdSource = mock<UserIdRemoteSource>()
    private val userInfoSource = mock<UserInfoRemoteSource>()

    private val repo: UserRepository = UserRepositoryImpl(userIdSource, userInfoSource)

    init {
        "userId" - {
            "getting the user id" - {
                "the server returns no data" {
                    everySuspend { userIdSource.getUserId() } returns userIdMock.right()
                    repo.getUserId().shouldBeRight(userIdMock)
                    verifySuspend { userIdSource.getUserId() }
                }

                "the server returns an empty userId" {
                    everySuspend { userIdSource.getUserId() } returns UserFailure.GettingUserId.left()
                    repo.getUserId().shouldBeLeft(UserFailure.GettingUserId)
                    verifySuspend { userIdSource.getUserId() }
                }
            }

            "saving the user id" - {
                "is successful" {
                    everySuspend { userIdSource.saveUserId() } returns Unit.right()
                    repo.saveUserId().shouldBeRight()
                    verifySuspend { userIdSource.saveUserId() }
                }

                listOf(
                    UserFailure.FetchingUser to UserFailure.FetchingUser.left(),
                    UserFailure.SavingUser to UserFailure.SavingUser.left(),
                    Failure.Unknown to Failure.Unknown.left(),
                ).forEach { (expected, failure) ->
                    "failure getting the user id ($expected)" {
                        everySuspend { userIdSource.saveUserId() } returns failure
                        repo.saveUserId().shouldBeLeft(expected)
                        verifySuspend { userIdSource.saveUserId() }
                    }
                }
            }
        }

        "userInfo" - {
            "observing userInfo" - {
                "the server returns no data" {
                    every { userInfoSource.userInfo } returns emptyFlow()
                    repo.userInfo.test { awaitComplete() }
                    verify { userInfoSource.userInfo }
                }
            }
        }
    }
}
