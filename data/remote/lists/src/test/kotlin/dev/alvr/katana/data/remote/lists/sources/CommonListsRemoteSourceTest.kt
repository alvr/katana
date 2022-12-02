package dev.alvr.katana.data.remote.lists.sources

import app.cash.turbine.test
import arrow.core.right
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.annotations.ApolloExperimental
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloHttpException
import com.apollographql.apollo3.exception.ApolloNetworkException
import com.apollographql.apollo3.exception.ApolloParseException
import com.apollographql.apollo3.exception.CacheMissException
import com.apollographql.apollo3.exception.HttpCacheMissException
import com.apollographql.apollo3.exception.JsonDataException
import com.apollographql.apollo3.exception.JsonEncodingException
import com.apollographql.apollo3.exception.MissingValueException
import com.apollographql.apollo3.mockserver.MockResponse
import com.apollographql.apollo3.mockserver.MockServer
import com.apollographql.apollo3.testing.QueueTestNetworkTransport
import com.apollographql.apollo3.testing.enqueueTestResponse
import com.benasher44.uuid.uuid4
import dev.alvr.katana.data.remote.base.interceptors.ReloadInterceptor
import dev.alvr.katana.data.remote.base.type.MediaType
import dev.alvr.katana.data.remote.lists.MediaListCollectionQuery
import dev.alvr.katana.data.remote.lists.MediaListEntriesMutation
import dev.alvr.katana.domain.base.failures.Failure
import dev.alvr.katana.domain.lists.failures.ListsFailure
import dev.alvr.katana.domain.lists.models.MediaCollection
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
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
import kotlin.time.Duration.Companion.seconds

@OptIn(ApolloExperimental::class)
internal class CommonListsRemoteSourceTest : BehaviorSpec() {
    private val mockServer = MockServer()
    private val apolloBuilder = ApolloClient.Builder().networkTransport(QueueTestNetworkTransport())
    private val client = spyk(apolloBuilder.build())
    private val userIdManager = mockk<UserIdManager>()
    private val reloadInterceptor = mockk<ReloadInterceptor>()

    private val source: CommonListsRemoteSource = CommonListsRemoteSourceImpl(client, userIdManager, reloadInterceptor)

    private val mediaList = Arb.bind<MediaList>(
        mapOf(
            LocalDate::class to Arb.localDate(),
            LocalDateTime::class to Arb.localDateTime(),
        ),
    ).next()

    private val mutation = Arb.bind<MediaListEntriesMutation>(
        mapOf(
            Optional::class to Arb.constant(Optional.Absent),
        ),
    ).next()

    private val mutationData = Arb.bind<MediaListEntriesMutation.Data>().next()

    init {
        afterSpec {
            mockServer.stop()
        }

        given("a ListsRemoteSource") {
            coEvery { userIdManager.getId() } returns 37_384.right()

            `when`("executing the updateList mutation") {
                and("the server returns no data") {
                    client.enqueueTestResponse(mutation)

                    then("it should be a right") {
                        source.updateList(mediaList).shouldBeRight()
                    }
                }

                and("the server returns some data") {
                    client.enqueueTestResponse(mutation, mutationData)

                    then("it should be a right") {
                        source.updateList(mediaList).shouldBeRight()
                    }
                }
            }

            `when`("updating the list") {
                and("is successful") {
                    coEvery {
                        client.mutation(any<MediaListEntriesMutation>()).execute()
                    } returns mockk()

                    then("it just execute the MediaListEntriesMutation") {
                        source.updateList(mediaList).shouldBeRight()
                        coVerify(exactly = 1) {
                            client.mutation(any<MediaListEntriesMutation>()).execute()
                        }
                    }
                }

                and("a HTTP error occurs") {
                    coEvery {
                        client.mutation(any<MediaListEntriesMutation>()).execute()
                    } throws mockk<ApolloHttpException>()

                    then("it returns a left of ListsFailure.UpdatingList") {
                        source.updateList(mediaList).shouldBeLeft(ListsFailure.UpdatingList)
                        coVerify(exactly = 1) {
                            client.mutation(any<MediaListEntriesMutation>()).execute()
                        }
                    }
                }

                and("a network error occurs") {
                    coEvery {
                        client.mutation(any<MediaListEntriesMutation>()).execute()
                    } throws ApolloNetworkException()

                    then("it returns a left of ListsFailure.UpdatingList") {
                        source.updateList(mediaList).shouldBeLeft(ListsFailure.UpdatingList)
                        coVerify(exactly = 1) {
                            client.mutation(any<MediaListEntriesMutation>()).execute()
                        }
                    }
                }

                and("a cache miss occurs") {
                    coEvery {
                        client.mutation(any<MediaListEntriesMutation>()).execute()
                    } throws mockk<CacheMissException>()

                    then("it returns a left of Failure.Unknown") {
                        source.updateList(mediaList).shouldBeLeft(Failure.Unknown)
                        coVerify(exactly = 1) {
                            client.mutation(any<MediaListEntriesMutation>()).execute()
                        }
                    }
                }

                and("a HTTP cache miss occurs") {
                    coEvery {
                        client.mutation(any<MediaListEntriesMutation>()).execute()
                    } throws mockk<HttpCacheMissException>()

                    then("it returns a left of Failure.Unknown") {
                        source.updateList(mediaList).shouldBeLeft(Failure.Unknown)
                        coVerify(exactly = 1) {
                            client.mutation(any<MediaListEntriesMutation>()).execute()
                        }
                    }
                }

                and("a json parser problem occurs") {
                    coEvery {
                        client.mutation(any<MediaListEntriesMutation>()).execute()
                    } throws ApolloParseException()

                    then("it returns a left of ListsFailure.UpdatingList") {
                        source.updateList(mediaList).shouldBeLeft(ListsFailure.UpdatingList)
                        coVerify(exactly = 1) {
                            client.mutation(any<MediaListEntriesMutation>()).execute()
                        }
                    }
                }

                and("a json data problem occurs") {
                    coEvery {
                        client.mutation(any<MediaListEntriesMutation>()).execute()
                    } throws mockk<JsonDataException>()

                    then("it returns a left of ListsFailure.UpdatingList") {
                        source.updateList(mediaList).shouldBeLeft(ListsFailure.UpdatingList)
                        coVerify(exactly = 1) {
                            client.mutation(any<MediaListEntriesMutation>()).execute()
                        }
                    }
                }

                and("a json encoding problem occurs") {
                    coEvery {
                        client.mutation(any<MediaListEntriesMutation>()).execute()
                    } throws mockk<JsonEncodingException>()

                    then("it returns a left of ListsFailure.UpdatingList") {
                        source.updateList(mediaList).shouldBeLeft(ListsFailure.UpdatingList)
                        coVerify(exactly = 1) {
                            client.mutation(any<MediaListEntriesMutation>()).execute()
                        }
                    }
                }

                and("another Apollo error occurs") {
                    coEvery {
                        client.mutation(any<MediaListEntriesMutation>()).execute()
                    } throws MissingValueException()

                    then("it returns a left of Failure.Unknown") {
                        source.updateList(mediaList).shouldBeLeft(Failure.Unknown)
                        coVerify(exactly = 1) {
                            client.mutation(any<MediaListEntriesMutation>()).execute()
                        }
                    }
                }
            }

            `when`("querying the list") {
                and("the data is null") {
                    val response = ApolloResponse.Builder(
                        operation = mockk<MediaListCollectionQuery>(),
                        requestUuid = uuid4(),
                        data = null,
                    ).build()
                    client.enqueueTestResponse(response)

                    then("the anime collection should be empty") {
                        source.getMediaCollection<MediaEntry.Anime>(MediaType.ANIME).test(5.seconds) {
                            awaitItem().shouldBeRight(MediaCollection(emptyList()))
                            cancelAndIgnoreRemainingEvents()
                        }
                    }

                    then("the manga collection should be empty") {
                        source.getMediaCollection<MediaEntry.Manga>(MediaType.MANGA).test(5.seconds) {
                            awaitItem().shouldBeRight(MediaCollection(emptyList()))
                            cancelAndIgnoreRemainingEvents()
                        }
                    }
                }

                and("the data has errors") {
                    val response = ApolloResponse.Builder(
                        operation = mockk<MediaListCollectionQuery>(),
                        requestUuid = uuid4(),
                        data = null,
                    ).errors(listOf(mockk())).build()
                    client.enqueueTestResponse(response)

                    then("the anime collection should be empty") {
                        source.getMediaCollection<MediaEntry.Anime>(MediaType.ANIME).test(5.seconds) {
                            awaitItem().shouldBeRight(MediaCollection(emptyList()))
                            cancelAndIgnoreRemainingEvents()
                        }
                    }

                    then("the manga collection should be empty") {
                        source.getMediaCollection<MediaEntry.Manga>(MediaType.MANGA).test(5.seconds) {
                            awaitItem().shouldBeRight(MediaCollection(emptyList()))
                            cancelAndIgnoreRemainingEvents()
                        }
                    }
                }
            }

            `when`("an error occurs") {
                val badClient = ApolloClient.Builder().serverUrl(mockServer.url()).build()
                val source: CommonListsRemoteSource = CommonListsRemoteSourceImpl(
                    badClient,
                    userIdManager,
                    reloadInterceptor,
                )

                and("mocking the response") {
                    `when`("a 500 error occurs") {
                        enqueueResponse { statusCode(500) }

                        then("it returns a left of ListsFailure.GetMediaCollection") {
                            source.getMediaCollection<MediaEntry.Anime>(MediaType.ANIME).test(5.seconds) {
                                awaitItem().shouldBeLeft(ListsFailure.GetMediaCollection)
                                cancelAndIgnoreRemainingEvents()
                            }

                            source.getMediaCollection<MediaEntry.Manga>(MediaType.MANGA).test(5.seconds) {
                                awaitItem().shouldBeLeft(ListsFailure.GetMediaCollection)
                                cancelAndIgnoreRemainingEvents()
                            }
                        }
                    }

                    `when`("a malformed body is returned") {
                        enqueueResponse { body("Malformed body") }

                        then("it returns a left of ListsFailure.GetMediaCollection") {
                            source.getMediaCollection<MediaEntry.Anime>(MediaType.ANIME).test(5.seconds) {
                                awaitItem().shouldBeLeft(ListsFailure.GetMediaCollection)
                                cancelAndIgnoreRemainingEvents()
                            }

                            source.getMediaCollection<MediaEntry.Manga>(MediaType.MANGA).test(5.seconds) {
                                awaitItem().shouldBeLeft(ListsFailure.GetMediaCollection)
                                cancelAndIgnoreRemainingEvents()
                            }
                        }
                    }

                    `when`("a JSON that does not match the scheme") {
                        enqueueResponse { body("""{"data": {"random": 42}}""") }

                        then("it returns a left of ListsFailure.GetMediaCollection") {
                            source.getMediaCollection<MediaEntry.Anime>(MediaType.ANIME).test(5.seconds) {
                                awaitItem().shouldBeLeft(ListsFailure.GetMediaCollection)
                                cancelAndIgnoreRemainingEvents()
                            }

                            source.getMediaCollection<MediaEntry.Manga>(MediaType.MANGA).test(5.seconds) {
                                awaitItem().shouldBeLeft(ListsFailure.GetMediaCollection)
                                cancelAndIgnoreRemainingEvents()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun enqueueResponse(builder: MockResponse.Builder.() -> Unit) {
        repeat(4) {
            mockServer.enqueue(MockResponse.Builder().apply(builder).build())
        }
    }
}