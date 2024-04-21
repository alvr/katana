package dev.alvr.katana.features.lists.data.sources

import app.cash.turbine.test
import arrow.core.right
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.annotations.ApolloExperimental
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.interceptor.ApolloInterceptor
import com.apollographql.apollo3.mockserver.MockServer
import com.apollographql.apollo3.mockserver.enqueueError
import com.apollographql.apollo3.mockserver.enqueueString
import com.apollographql.apollo3.testing.QueueTestNetworkTransport
import com.apollographql.apollo3.testing.enqueueTestResponse
import com.benasher44.uuid.uuid4
import dev.alvr.katana.common.user.domain.managers.UserIdManager
import dev.alvr.katana.core.domain.failures.Failure
import dev.alvr.katana.core.remote.type.MediaType
import dev.alvr.katana.core.remote.type.buildMediaListCollection
import dev.alvr.katana.core.remote.type.buildUser
import dev.alvr.katana.core.tests.shouldBeLeft
import dev.alvr.katana.core.tests.shouldBeRight
import dev.alvr.katana.features.lists.data.MediaListCollectionQuery
import dev.alvr.katana.features.lists.data.apolloErrorMock
import dev.alvr.katana.features.lists.data.mediaListCollectionQueryMock
import dev.alvr.katana.features.lists.data.mediaListEntriesMutationMock
import dev.alvr.katana.features.lists.data.mediaListMock
import dev.alvr.katana.features.lists.domain.failures.ListsFailure
import dev.alvr.katana.features.lists.domain.models.MediaCollection
import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import io.kotest.core.spec.style.FreeSpec

@OptIn(ApolloExperimental::class)
internal class CommonListsRemoteSourceTest : FreeSpec() {
    private val userIdManager = mock<UserIdManager>()
    private val reloadInterceptor = mock<ApolloInterceptor>()

    private val client =
        ApolloClient.Builder().networkTransport(QueueTestNetworkTransport()).build()
    private val source = CommonListsRemoteSourceImpl(client, userIdManager, reloadInterceptor)

    init {
        "querying" - {
            queryList().forEach { (data, type) ->
                "the server responded with data or not ($data, $type)" {
                    everySuspend { userIdManager.getId() } returns 37_384.right()
                    val response = ApolloResponse.Builder(
                        operation = mediaListCollectionQueryMock,
                        requestUuid = uuid4(),
                    ).data(data).build()
                    client.enqueueTestResponse(response)

                    source.getMediaCollection<MediaEntry>(type).test {
                        if (data == null) {
                            awaitItem().shouldBeLeft(ListsFailure.GetMediaCollection)
                        } else {
                            awaitItem().shouldBeRight(MediaCollection(emptyList()))
                        }

                        cancelAndIgnoreRemainingEvents()
                    }
                    verifySuspend { userIdManager.getId() }
                }

                "a HTTP error occurs ($data, $type)" {
                    everySuspend { userIdManager.getId() } returns 37_384.right()
                    val response = ApolloResponse.Builder(
                        operation = mediaListCollectionQueryMock,
                        requestUuid = uuid4(),
                    ).data(data).errors(listOf(apolloErrorMock)).build()
                    client.enqueueTestResponse(response)

                    source.getMediaCollection<MediaEntry>(type).test {
                        awaitItem().shouldBeLeft(Failure.Unknown)
                        cancelAndIgnoreRemainingEvents()
                    }
                    verifySuspend { userIdManager.getId() }
                }
            }
        }

        "updating" - {
            "the server returns some data" {
                client.enqueueTestResponse(mediaListEntriesMutationMock)
                source.updateList(mediaListMock).shouldBeRight()
            }
        }

        "with errors" - {
            val mockServer = MockServer()
            val badClient = ApolloClient.Builder().serverUrl(mockServer.url()).build()
            val source: CommonListsRemoteSource = CommonListsRemoteSourceImpl(
                badClient,
                userIdManager,
                reloadInterceptor,
            )

            afterSpec { mockServer.close() }

            mockServer.badClient().forEach { (type, enqueueAction) ->
                "a HTTP error occurs" {
                    everySuspend { userIdManager.getId() } returns 37_384.right()
                    enqueueAction()

                    source.getMediaCollection<MediaEntry>(type).test {
                        awaitItem().shouldBeLeft(ListsFailure.GetMediaCollection)
                        cancelAndIgnoreRemainingEvents()
                    }
                    verifySuspend { userIdManager.getId() }
                }
            }
        }
    }

    private fun queryList(): List<Pair<MediaListCollectionQuery.Data?, MediaType>> {
        val empty = MediaListCollectionQuery.Data {
            this["MediaListCollection"] = buildMediaListCollection {
                lists = emptyList()
                user = buildUser { }
            }
        }

        val values = buildList {
            add(null)
            add(empty)
        }
        val types = MediaType.knownEntries

        return buildList {
            values.forEach { v -> types.forEach { t -> add(v to t) } }
        }
    }

    private fun MockServer.badClient(): List<Pair<MediaType, (() -> Unit)>> {
        val commands = buildList {
            add { enqueueError(500) }
            add { enqueueString("Malformed body") }
            add { enqueueString("""{"data": {"random": 42}}""") }
        }
        val types = MediaType.knownEntries

        return buildList {
            commands.forEach { c -> types.forEach { t -> add(t to c) } }
        }
    }
}
