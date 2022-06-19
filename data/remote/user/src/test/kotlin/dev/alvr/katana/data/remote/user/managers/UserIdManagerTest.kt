package dev.alvr.katana.data.remote.user.managers

import com.apollographql.apollo3.annotations.ApolloExperimental
import dev.alvr.katana.data.remote.user.UserIdQuery
import dev.alvr.katana.data.remote.user.test.UserIdQuery_TestBuilder.Data
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.user.models.UserId
import dev.alvr.katana.domain.user.usecases.GetUserIdUseCase
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk

@OptIn(ApolloExperimental::class)
internal class UserIdManagerTest : BehaviorSpec({
    xgiven("an userIdManager") {
        val getUserId = mockk<GetUserIdUseCase>()
        val manager = UserIdManagerImpl(getUserId)

        `when`("server return null") {
            coEvery { getUserId() } returns UserId(2)

            then("the mapper should throw an exception") {
                shouldThrowExactly<IllegalStateException> {
                    manager.getId()
                }.message shouldBe "UserId is required"
            }
        }

        `when`("server return viewer is null") {
            val query = UserIdQuery.Data {
                viewer = null
            }

//            client.enqueueTestResponse(UserIdQuery(), query)

            then("the mapper should throw an exception") {
                shouldThrowExactly<IllegalStateException> {
                    manager.getId()
                }.message shouldBe "UserId is required"
            }
        }

        `when`("server return viewer is valid") {
            val query = UserIdQuery.Data {
                viewer = viewer {
                    id = 37_384
                }
            }

//            client.enqueueTestResponse(UserIdQuery(), query)

            then("it should return the id of the user") {
                manager.getId() shouldBeExactly 37_384
            }
        }
    }
},)
