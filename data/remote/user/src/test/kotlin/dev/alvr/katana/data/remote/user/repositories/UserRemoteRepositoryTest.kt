package dev.alvr.katana.data.remote.user.repositories

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.annotations.ApolloExperimental
import dev.alvr.katana.data.remote.user.UserIdQuery
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk

@OptIn(ApolloExperimental::class)
internal class UserRemoteRepositoryTest : BehaviorSpec({
    given("an userIdManager") {
        val client = mockk<ApolloClient>()
        val repo = UserRemoteRepositoryImpl(client)

        `when`("saving the userId") {
            coJustRun { client.query(UserIdQuery()).execute() }

            then("it just execute the UserIdQuery") {
                repo.saveUserId()

                coVerify { client.query(UserIdQuery()).execute() }
            }
        }
    }
},)
