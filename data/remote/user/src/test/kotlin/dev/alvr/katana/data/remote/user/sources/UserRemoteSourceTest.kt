package dev.alvr.katana.data.remote.user.sources

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.annotations.ApolloExperimental
import com.apollographql.apollo3.exception.ApolloHttpException
import com.apollographql.apollo3.exception.ApolloNetworkException
import com.apollographql.apollo3.exception.ApolloParseException
import com.apollographql.apollo3.exception.CacheMissException
import com.apollographql.apollo3.exception.HttpCacheMissException
import com.apollographql.apollo3.exception.JsonDataException
import com.apollographql.apollo3.exception.JsonEncodingException
import com.apollographql.apollo3.exception.MissingValueException
import com.apollographql.apollo3.testing.QueueTestNetworkTransport
import com.apollographql.apollo3.testing.enqueueTestResponse
import dev.alvr.katana.data.remote.user.UserIdQuery
import dev.alvr.katana.data.remote.user.test.UserIdQuery_TestBuilder.Data
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.user.failures.UserFailure
import dev.alvr.katana.domain.user.models.UserId
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk

@OptIn(ApolloExperimental::class)
internal class UserRemoteSourceTest : BehaviorSpec({
    given("an UserRemoteSource") {
        val apolloBuilder = ApolloClient.Builder().networkTransport(QueueTestNetworkTransport())
        val client = spyk(apolloBuilder.build())
        val repo = UserRemoteSource(client)

        `when`("getting the userId") {
            and("the server returns no data") {
                client.enqueueTestResponse(UserIdQuery())

                then("it should be a UserFailure.UserIdFailure") {
                    repo.getUserId().shouldBeLeft(UserFailure.UserIdFailure)
                }
            }

            and("the server returns an empty userId") {
                val query = UserIdQuery.Data { viewer = null }
                client.enqueueTestResponse(UserIdQuery(), query)

                then("it should be a UserFailure.UserIdFailure") {
                    repo.getUserId().shouldBeLeft(UserFailure.UserIdFailure)
                }
            }

            and("the server returns a valid id") {
                val query = UserIdQuery.Data { viewer = viewer { id = 37_384 } }
                client.enqueueTestResponse(UserIdQuery(), query)

                then("it should be a userId with the same id") {
                    repo.getUserId().shouldBeRight(UserId(37_384))
                }
            }
        }

        `when`("saving the userId") {
            and("is successful") {
                coEvery { client.query(UserIdQuery()).execute() } returns mockk()

                then("it just execute the UserIdQuery") {
                    repo.saveUserId().shouldBeRight()
                    coVerify(exactly = 1) { client.query(UserIdQuery()).execute() }
                }
            }

            and("a HTTP error occurs") {
                coEvery { client.query(UserIdQuery()).execute() } throws mockk<ApolloHttpException>()

                then("it returns a left of UserFailure.FetchingFailure") {
                    repo.saveUserId().shouldBeLeft(UserFailure.FetchingFailure)
                    coVerify(exactly = 1) { client.query(UserIdQuery()).execute() }
                }
            }

            and("a network error occurs") {
                coEvery { client.query(UserIdQuery()).execute() } throws ApolloNetworkException()

                then("it returns a left of UserFailure.FetchingFailure") {
                    repo.saveUserId().shouldBeLeft(UserFailure.FetchingFailure)
                    coVerify(exactly = 1) { client.query(UserIdQuery()).execute() }
                }
            }

            and("a cache miss occurs") {
                coEvery { client.query(UserIdQuery()).execute() } throws mockk<CacheMissException>()

                then("it returns a left of Failure.Unknown") {
                    repo.saveUserId().shouldBeLeft(Failure.Unknown)
                    coVerify(exactly = 1) { client.query(UserIdQuery()).execute() }
                }
            }

            and("a HTTP cache miss occurs") {
                coEvery { client.query(UserIdQuery()).execute() } throws mockk<HttpCacheMissException>()

                then("it returns a left of Failure.Unknown") {
                    repo.saveUserId().shouldBeLeft(Failure.Unknown)
                    coVerify(exactly = 1) { client.query(UserIdQuery()).execute() }
                }
            }

            and("a json parser problem occurs") {
                coEvery { client.query(UserIdQuery()).execute() } throws ApolloParseException()

                then("it returns a left of UserFailure.SavingFailure") {
                    repo.saveUserId().shouldBeLeft(UserFailure.SavingFailure)
                    coVerify(exactly = 1) { client.query(UserIdQuery()).execute() }
                }
            }

            and("a json data problem occurs") {
                coEvery { client.query(UserIdQuery()).execute() } throws mockk<JsonDataException>()

                then("it returns a left of UserFailure.SavingFailure") {
                    repo.saveUserId().shouldBeLeft(UserFailure.SavingFailure)
                    coVerify(exactly = 1) { client.query(UserIdQuery()).execute() }
                }
            }

            and("a json encoding problem occurs") {
                coEvery { client.query(UserIdQuery()).execute() } throws mockk<JsonEncodingException>()

                then("it returns a left of UserFailure.SavingFailure") {
                    repo.saveUserId().shouldBeLeft(UserFailure.SavingFailure)
                    coVerify(exactly = 1) { client.query(UserIdQuery()).execute() }
                }
            }

            and("another Apollo error occurs") {
                coEvery { client.query(UserIdQuery()).execute() } throws MissingValueException()

                then("it returns a left of Failure.Unknown") {
                    repo.saveUserId().shouldBeLeft(Failure.Unknown)
                    coVerify(exactly = 1) { client.query(UserIdQuery()).execute() }
                }
            }
        }
    }
},)
