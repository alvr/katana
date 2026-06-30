package dev.alvr.katana.common.media.data.sources

import app.cash.turbine.test
import arrow.core.Either
import arrow.core.right
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.annotations.ApolloExperimental
import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.interceptor.ApolloInterceptor
import com.apollographql.apollo.testing.MapTestNetworkTransport
import com.apollographql.apollo.testing.registerTestResponse
import com.apollographql.mockserver.MockServer
import com.apollographql.mockserver.enqueueError
import com.apollographql.mockserver.enqueueString
import com.benasher44.uuid.uuid4
import dev.alvr.katana.common.media.data.MediaListCollectionQuery
import dev.alvr.katana.common.media.data.apolloErrorMock
import dev.alvr.katana.common.media.data.di.createMediaCollectionRemoteSourceTestGraph
import dev.alvr.katana.common.media.data.mappers.requests.invoke
import dev.alvr.katana.common.media.data.mediaListCollectionQueryMock
import dev.alvr.katana.common.media.domain.failures.MediaFailure
import dev.alvr.katana.common.media.domain.models.MediaCollection
import dev.alvr.katana.common.media.domain.models.entries.MediaEntry
import dev.alvr.katana.common.media.domain.models.lists.MediaListStatus
import dev.alvr.katana.common.user.domain.models.UserId
import dev.alvr.katana.common.user.domain.usecases.GetUserIdUseCase
import dev.alvr.katana.core.domain.failures.Failure
import dev.alvr.katana.core.remote.builder.Data
import dev.alvr.katana.core.remote.builder.buildMediaListCollection
import dev.alvr.katana.core.remote.builder.buildMediaListOptions
import dev.alvr.katana.core.remote.builder.buildMediaListTypeOptions
import dev.alvr.katana.core.remote.builder.buildUser
import dev.alvr.katana.core.remote.optional
import dev.alvr.katana.core.remote.type.MediaType
import dev.alvr.katana.core.tests.shouldBeLeft
import dev.alvr.katana.core.tests.shouldBeRight
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.resetAnswers
import dev.mokkery.resetCalls
import dev.mokkery.verifySuspend
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.test.TestCase
import io.kotest.engine.test.TestResult
import kotlinx.coroutines.flow.Flow

@OptIn(ApolloExperimental::class)
internal class MediaCollectionRemoteSourceTest : FreeSpec() {
    private val getUserId = mock<GetUserIdUseCase>()
    private val reloadInterceptor = mock<ApolloInterceptor>()
    private val client = ApolloClient.Builder().networkTransport(MapTestNetworkTransport()).build()

    private val source =
        createMediaCollectionRemoteSourceTestGraph(client, getUserId, reloadInterceptor).mediaCollectionRemoteSource

    init {
        "querying" -
            {
                queryList(source).forEach { (data, type, flow) ->
                    "the server responded with data or not ($data, $type)" {
                        everySuspend { getUserId(Unit) } returns UserId.right()
                        val response =
                            ApolloResponse.Builder(operation = mediaListCollectionQueryMock, requestUuid = uuid4())
                                .data(data)
                                .build()
                        client.registerTestResponse(
                            MediaListCollectionQuery(UserId.id.optional, type, MediaListStatus.All()),
                            response,
                        )

                        flow.test {
                            if (data == null) {
                                awaitItem().shouldBeLeft(MediaFailure.GetMediaCollection)
                            } else {
                                awaitItem().shouldBeRight(MediaCollection(emptyList()))
                            }

                            awaitComplete()
                        }

                        verifySuspend { getUserId(Unit) }
                    }

                    "a HTTP error occurs ($data, $type)" {
                        everySuspend { getUserId(Unit) } returns UserId.right()
                        val response =
                            ApolloResponse.Builder(operation = mediaListCollectionQueryMock, requestUuid = uuid4())
                                .data(data)
                                .errors(listOf(apolloErrorMock))
                                .build()
                        client.registerTestResponse(
                            MediaListCollectionQuery(UserId.id.optional, type, MediaListStatus.All()),
                            response,
                        )

                        flow.test {
                            awaitItem().shouldBeLeft(Failure.Unknown)
                            awaitComplete()
                        }
                        verifySuspend { getUserId(Unit) }
                    }
                }

                "with errors" -
                    {
                        val mockServer = MockServer()
                        val badClient = ApolloClient.Builder().serverUrl(mockServer.url()).build()
                        val source =
                            createMediaCollectionRemoteSourceTestGraph(badClient, getUserId, reloadInterceptor)
                                .mediaCollectionRemoteSource

                        afterSpec { mockServer.close() }

                        mockServer.badClient(source).forEach { (type, enqueueAction, flow) ->
                            "a HTTP error occurs ($enqueueAction, $type)" {
                                everySuspend { getUserId(Unit) } returns UserId.right()
                                enqueueAction()

                                flow.test {
                                    awaitItem().shouldBeLeft(MediaFailure.GetMediaCollection)
                                    awaitComplete()
                                }
                                verifySuspend { getUserId(Unit) }
                            }
                        }
                    }
            }
    }

    override suspend fun afterEach(testCase: TestCase, result: TestResult) {
        resetAnswers(getUserId, reloadInterceptor)
        resetCalls(getUserId, reloadInterceptor)
    }

    private fun queryList(source: MediaCollectionRemoteSource): List<GoodQuery> {
        val empty = MediaListCollectionQuery.Data {
            this["MediaListCollection"] = buildMediaListCollection {
                lists = emptyList()
                user = buildUser {
                    id = UserId.id
                    mediaListOptions = buildMediaListOptions {
                        animeList = buildMediaListTypeOptions { sectionOrder = emptyList() }
                        mangaList = buildMediaListTypeOptions { sectionOrder = emptyList() }
                    }
                }
            }
        }

        val values = buildList {
            add(null)
            add(empty)
        }

        return buildList {
            values.forEach { v ->
                MediaType.knownEntries.forEach { t ->
                    add(
                        GoodQuery(
                            first = v,
                            second = t,
                            third =
                                when (t) {
                                    MediaType.ANIME -> source.animeCollection(MediaListStatus.All)
                                    MediaType.MANGA -> source.mangaCollection(MediaListStatus.All)
                                    else -> error("Unknown type")
                                },
                        )
                    )
                }
            }
        }
    }

    private fun MockServer.badClient(source: MediaCollectionRemoteSource): List<BadQuery> {
        val commands = buildList {
            add { enqueueError(500) }
            add { enqueueString("Malformed body") }
            add { enqueueString("""{"data": {"random": 42}}""") }
        }

        return buildList {
            commands.forEach { c ->
                MediaType.knownEntries.forEach { t ->
                    add(
                        BadQuery(
                            first = t,
                            second = c,
                            third =
                                when (t) {
                                    MediaType.ANIME -> source.animeCollection(MediaListStatus.All)
                                    MediaType.MANGA -> source.mangaCollection(MediaListStatus.All)
                                    else -> error("Unknown type")
                                },
                        )
                    )
                }
            }
        }
    }
}

private val UserId = UserId(37_384)

private typealias MediaCollectionFlow = Flow<Either<Failure, MediaCollection<MediaEntry>>>

private typealias GoodQuery = Triple<MediaListCollectionQuery.Data?, MediaType, MediaCollectionFlow>

private typealias BadQuery = Triple<MediaType, (() -> Unit), MediaCollectionFlow>
