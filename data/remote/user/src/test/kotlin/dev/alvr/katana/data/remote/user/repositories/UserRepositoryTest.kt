package dev.alvr.katana.data.remote.user.repositories

import arrow.core.left
import arrow.core.right
import dev.alvr.katana.data.remote.user.sources.UserRemoteSource
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.user.failures.UserFailure
import dev.alvr.katana.domain.user.models.UserId
import dev.alvr.katana.domain.user.repositories.UserRepository
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

internal class UserRepositoryTest : BehaviorSpec({
    val userId = UserId(37_384)

    given("an UserRepository") {
        val source = mockk<UserRemoteSource>()
        val repo: UserRepository = UserRepositoryImpl(source)

        `when`("getting the userId") {
            and("the server returns no data") {
                coEvery { source.getUserId() } returns userId.right()

                then("it should be a right") {
                    repo.getUserId().shouldBeRight(userId)
                    coVerify(exactly = 1) { source.getUserId() }
                }
            }

            and("the server returns an empty userId") {
                coEvery { source.getUserId() } returns UserFailure.UserIdFailure.left()

                then("it should be a UserFailure.UserIdFailure") {
                    repo.getUserId().shouldBeLeft(UserFailure.UserIdFailure)
                    coVerify(exactly = 1) { source.getUserId() }
                }
            }
        }

        `when`("saving the userId") {
            and("is successful") {
                coEvery { source.saveUserId() } returns Unit.right()

                then("it just execute the UserIdQuery") {
                    repo.saveUserId().shouldBeRight()
                    coVerify(exactly = 1) { source.saveUserId() }
                }
            }

            and("a HTTP error occurs") {
                coEvery { source.saveUserId() } returns UserFailure.FetchingFailure.left()

                then("it returns a left of UserFailure.FetchingFailure") {
                    repo.saveUserId().shouldBeLeft(UserFailure.FetchingFailure)
                    coVerify(exactly = 1) { source.saveUserId() }
                }
            }

            and("a HTTP error occurs") {
                coEvery { source.saveUserId() } returns UserFailure.SavingFailure.left()

                then("it returns a left of UserFailure.SavingFailure") {
                    repo.saveUserId().shouldBeLeft(UserFailure.SavingFailure)
                    coVerify(exactly = 1) { source.saveUserId() }
                }
            }

            and("a HTTP error occurs") {
                coEvery { source.saveUserId() } returns Failure.Unknown.left()

                then("it returns a left of Failure.Unknown") {
                    repo.saveUserId().shouldBeLeft(Failure.Unknown)
                    coVerify(exactly = 1) { source.saveUserId() }
                }
            }
        }
    }
},)
