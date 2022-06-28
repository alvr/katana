package dev.alvr.katana.data.remote.user.managers

import arrow.core.left
import arrow.core.right
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.user.failures.UserFailure
import dev.alvr.katana.domain.user.managers.UserIdManager
import dev.alvr.katana.domain.user.models.UserId
import dev.alvr.katana.domain.user.usecases.GetUserIdUseCase
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.coEvery
import io.mockk.mockk

internal class UserIdManagerTest : BehaviorSpec({
    given("an userIdManager") {
        val getUserId = mockk<GetUserIdUseCase>()
        val manager: UserIdManager = UserIdManagerImpl(getUserId)

        `when`("server fails to return something") {
            coEvery { getUserId() } returns UserFailure.UserIdFailure.left()

            then("the mapper should throw an exception") {
                manager.getId().shouldBeLeft(UserFailure.UserIdFailure)
            }
        }

        `when`("server return viewer is valid") {
            coEvery { getUserId() } returns UserId(37_384).right()

            then("it should return the id of the user") {
                manager.getId().shouldBeRight(37_384)
            }
        }
    }
},)
