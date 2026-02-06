package dev.alvr.katana.common.user.data.repositories

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.user.data.sources.UserLocalSource
import dev.alvr.katana.common.user.data.sources.UserRemoteSource
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
import io.kotest.core.test.TestCase
import kotlinx.coroutines.flow.emptyFlow

internal class UserRepositoryTest : FreeSpec() {
    private val userLocalSource = mock<UserLocalSource>()
    private val userRemoteSource = mock<UserRemoteSource> { every { userInfo } returns emptyFlow() }

    private lateinit var repo: UserRepository

    init {
        "userId" -
            {
                "getting the user id" -
                    {
                        "the server returns no data" {
                            everySuspend { userRemoteSource.getUserId() } returns userIdMock.right()
                            repo.getUserId().shouldBeRight(userIdMock)
                            verifySuspend { userRemoteSource.getUserId() }
                        }

                        "the server returns an empty userId" {
                            everySuspend { userRemoteSource.getUserId() } returns UserFailure.GettingUserId.left()
                            repo.getUserId().shouldBeLeft(UserFailure.GettingUserId)
                            verifySuspend { userRemoteSource.getUserId() }
                        }
                    }

                "saving the user id" -
                    {
                        "is successful" {
                            everySuspend { userRemoteSource.saveUserId() } returns Unit.right()
                            repo.saveUserId().shouldBeRight()
                            verifySuspend { userRemoteSource.saveUserId() }
                        }

                        listOf(
                                UserFailure.FetchingUser to UserFailure.FetchingUser.left(),
                                UserFailure.SavingUser to UserFailure.SavingUser.left(),
                                Failure.Unknown to Failure.Unknown.left(),
                            )
                            .forEach { (expected, failure) ->
                                "failure getting the user id ($expected)" {
                                    everySuspend { userRemoteSource.saveUserId() } returns failure
                                    repo.saveUserId().shouldBeLeft(expected)
                                    verifySuspend { userRemoteSource.saveUserId() }
                                }
                            }
                    }
            }

        "userInfo" -
            {
                "observing userInfo" -
                    {
                        "the server returns no data" {
                            every { userRemoteSource.userInfo } returns emptyFlow()
                            repo.userInfo.test { awaitComplete() }
                            verify { userRemoteSource.userInfo }
                        }
                    }
            }
    }

    override suspend fun beforeEach(testCase: TestCase) {
        repo = UserRepositoryImpl(userLocalSource, userRemoteSource)
    }
}
