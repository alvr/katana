package dev.alvr.katana.data.remote.lists.repositories

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.annotations.ApolloExperimental
import com.apollographql.apollo3.api.Optional
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
import dev.alvr.katana.data.remote.lists.MediaListEntriesMutation
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.lists.failures.ListsFailure
import dev.alvr.katana.domain.lists.models.lists.MediaList
import dev.alvr.katana.domain.user.managers.UserIdManager
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.localDate
import io.kotest.property.arbitrary.localDateTime
import io.kotest.property.arbitrary.next
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import java.time.LocalDate
import java.time.LocalDateTime

@OptIn(ApolloExperimental::class)
internal class ListsRemoteRepositoryTest : BehaviorSpec({
    given("a ListsRemoteRepositoryImpl") {
        val apolloBuilder = ApolloClient.Builder().networkTransport(QueueTestNetworkTransport())
        val client = spyk(apolloBuilder.build())
        val userIdManager = mockk<UserIdManager>()
        val repo = ListsRemoteRepositoryImpl(client, userIdManager)

        val mediaList = Arb.bind<MediaList>(
            mapOf(
                LocalDate::class to Arb.localDate(),
                LocalDateTime::class to Arb.localDateTime(),
            ),
        ).next()

        val mutation = Arb.bind<MediaListEntriesMutation>(
            mapOf(
                Optional::class to Arb.constant(Optional.Absent),
            ),
        ).next()

        val mutationData = Arb.bind<MediaListEntriesMutation.Data>().next()

        `when`("executing the updateList mutation") {
            and("the server returns no data") {
                client.enqueueTestResponse(mutation)

                then("it should be a right") {
                    repo.updateList(mediaList).shouldBeRight()
                }
            }

            and("the server returns some data") {
                client.enqueueTestResponse(mutation, mutationData)

                then("it should be a right") {
                    repo.updateList(mediaList).shouldBeRight()
                }
            }
        }

        `when`("updating the list") {
            and("is successful") {
                coEvery { client.mutation(any<MediaListEntriesMutation>()).execute() } returns mockk()

                then("it just execute the MediaListEntriesMutation") {
                    repo.updateList(mediaList).shouldBeRight()
                    coVerify(exactly = 1) { client.mutation(any<MediaListEntriesMutation>()).execute() }
                }
            }

            and("a HTTP error occurs") {
                coEvery {
                    client.mutation(any<MediaListEntriesMutation>()).execute()
                } throws mockk<ApolloHttpException>()

                then("it returns a left of ListsFailure.UpdatingList") {
                    repo.updateList(mediaList).shouldBeLeft(ListsFailure.UpdatingList)
                    coVerify(exactly = 1) { client.mutation(any<MediaListEntriesMutation>()).execute() }
                }
            }

            and("a network error occurs") {
                coEvery {
                    client.mutation(any<MediaListEntriesMutation>()).execute()
                } throws ApolloNetworkException()

                then("it returns a left of ListsFailure.UpdatingList") {
                    repo.updateList(mediaList).shouldBeLeft(ListsFailure.UpdatingList)
                    coVerify(exactly = 1) { client.mutation(any<MediaListEntriesMutation>()).execute() }
                }
            }

            and("a cache miss occurs") {
                coEvery {
                    client.mutation(any<MediaListEntriesMutation>()).execute()
                } throws mockk<CacheMissException>()

                then("it returns a left of Failure.Unknown") {
                    repo.updateList(mediaList).shouldBeLeft(Failure.Unknown)
                    coVerify(exactly = 1) { client.mutation(any<MediaListEntriesMutation>()).execute() }
                }
            }

            and("a HTTP cache miss occurs") {
                coEvery {
                    client.mutation(any<MediaListEntriesMutation>()).execute()
                } throws mockk<HttpCacheMissException>()

                then("it returns a left of Failure.Unknown") {
                    repo.updateList(mediaList).shouldBeLeft(Failure.Unknown)
                    coVerify(exactly = 1) { client.mutation(any<MediaListEntriesMutation>()).execute() }
                }
            }

            and("a json parser problem occurs") {
                coEvery {
                    client.mutation(any<MediaListEntriesMutation>()).execute()
                } throws ApolloParseException()

                then("it returns a left of ListsFailure.UpdatingList") {
                    repo.updateList(mediaList).shouldBeLeft(ListsFailure.UpdatingList)
                    coVerify(exactly = 1) { client.mutation(any<MediaListEntriesMutation>()).execute() }
                }
            }

            and("a json data problem occurs") {
                coEvery {
                    client.mutation(any<MediaListEntriesMutation>()).execute()
                } throws mockk<JsonDataException>()

                then("it returns a left of ListsFailure.UpdatingList") {
                    repo.updateList(mediaList).shouldBeLeft(ListsFailure.UpdatingList)
                    coVerify(exactly = 1) { client.mutation(any<MediaListEntriesMutation>()).execute() }
                }
            }

            and("a json encoding problem occurs") {
                coEvery {
                    client.mutation(any<MediaListEntriesMutation>()).execute()
                } throws mockk<JsonEncodingException>()

                then("it returns a left of ListsFailure.UpdatingList") {
                    repo.updateList(mediaList).shouldBeLeft(ListsFailure.UpdatingList)
                    coVerify(exactly = 1) { client.mutation(any<MediaListEntriesMutation>()).execute() }
                }
            }

            and("another Apollo error occurs") {
                coEvery {
                    client.mutation(any<MediaListEntriesMutation>()).execute()
                } throws MissingValueException()

                then("it returns a left of Failure.Unknown") {
                    repo.updateList(mediaList).shouldBeLeft(Failure.Unknown)
                    coVerify(exactly = 1) { client.mutation(any<MediaListEntriesMutation>()).execute() }
                }
            }
        }
    }
},)
