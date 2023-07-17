package dev.alvr.katana.data.remote.lists.sources

import app.cash.turbine.test
import arrow.core.right
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.annotations.ApolloExperimental
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Error
import com.apollographql.apollo3.api.fakeError
import com.apollographql.apollo3.interceptor.ApolloInterceptor
import com.apollographql.apollo3.interceptor.MockApolloInterceptor
import com.apollographql.apollo3.mockserver.MockResponse
import com.apollographql.apollo3.mockserver.MockServer
import com.apollographql.apollo3.testing.QueueTestNetworkTransport
import com.apollographql.apollo3.testing.enqueueTestResponse
import com.benasher44.uuid.uuid4
import dev.alvr.katana.common.tests.invoke
import dev.alvr.katana.common.tests.shouldBeLeft
import dev.alvr.katana.common.tests.shouldBeRight
import dev.alvr.katana.data.remote.base.type.MediaType
import dev.alvr.katana.data.remote.base.type.buildMediaListCollection
import dev.alvr.katana.data.remote.lists.MediaListCollectionQuery
import dev.alvr.katana.data.remote.lists.MediaListEntriesMutation
import dev.alvr.katana.data.remote.lists.enqueueResponse
import dev.alvr.katana.data.remote.lists.fakeMediaListCollectionQuery
import dev.alvr.katana.data.remote.lists.fakeMediaListEntriesMutation
import dev.alvr.katana.domain.lists.failures.ListsFailure
import dev.alvr.katana.domain.lists.models.MediaCollection
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.models.lists.MediaList
import dev.alvr.katana.domain.lists.models.lists.fakeMediaList
import dev.alvr.katana.domain.user.managers.MockUserIdManager
import dev.alvr.katana.domain.user.managers.UserIdManager
import io.kotest.core.spec.style.FreeSpec
import kotlin.time.Duration.Companion.seconds
import org.kodein.mock.Mocker
import org.kodein.mock.UsesFakes
import org.kodein.mock.UsesMocks

@UsesFakes(
    Error::class,
    MediaList::class,
    MediaListCollectionQuery::class,
    MediaListEntriesMutation::class,
)
@UsesMocks(UserIdManager::class, ApolloInterceptor::class)
@ApolloExperimental
internal class CommonListsRemoteSourceTest : FreeSpec() {
    private val mocker = Mocker()
    private val userIdManager = MockUserIdManager(mocker)
    private val reloadInterceptor = MockApolloInterceptor(mocker)

    private val client =
        ApolloClient.Builder().networkTransport(QueueTestNetworkTransport()).build()
    private val source = CommonListsRemoteSourceImpl(client, userIdManager, reloadInterceptor)

    init {
        "querying" - {
            queryList().forEach { (data, type) ->
                "the server responded with data or not ($data, $type)" {
                    mocker.everySuspending { userIdManager.getId() } returns 37_384.right()
                    val response = ApolloResponse.Builder(
                        operation = fakeMediaListCollectionQuery(),
                        requestUuid = uuid4(),
                        data = data,
                    ).build()
                    client.enqueueTestResponse(response)

                    source.getMediaCollection<MediaEntry>(type).test(5.seconds) {
                        awaitItem().shouldBeRight(MediaCollection(emptyList()))
                        cancelAndIgnoreRemainingEvents()
                    }
                    mocker.verifyWithSuspend { userIdManager.getId() }
                }

                "a HTTP error occurs ($data, $type)" {
                    mocker.everySuspending { userIdManager.getId() } returns 37_384.right()
                    val response = ApolloResponse.Builder(
                        operation = fakeMediaListCollectionQuery(),
                        requestUuid = uuid4(),
                        data = data,
                    ).errors(listOf(fakeError())).build()
                    client.enqueueTestResponse(response)

                    source.getMediaCollection<MediaEntry>(type).test(5.seconds) {
                        awaitItem().shouldBeRight(MediaCollection(emptyList()))
                        cancelAndIgnoreRemainingEvents()
                    }
                    mocker.verifyWithSuspend { userIdManager.getId() }
                }
            }
        }

        "updating" - {
            "the server returns some data" {
                client.enqueueTestResponse(fakeMediaListEntriesMutation())
                source.updateList(fakeMediaList()).shouldBeRight()
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

            afterSpec { mockServer.stop() }

            badClient().forEach { (type, action) ->
                "a HTTP error occurs" {
                    mocker.everySuspending { userIdManager.getId() } returns 37_384.right()
                    mockServer.enqueueResponse(action)

                    source.getMediaCollection<MediaEntry>(type).test(5.seconds) {
                        awaitItem().shouldBeLeft(ListsFailure.GetMediaCollection)
                        cancelAndIgnoreRemainingEvents()
                    }
                    mocker.verifyWithSuspend { userIdManager.getId() }
                }
            }
        }
    }

    override fun extensions() = listOf(mocker())

    private fun queryList(): List<Pair<MediaListCollectionQuery.Data?, MediaType>> {
        val empty = MediaListCollectionQuery.Data {
            this["collection"] = buildMediaListCollection {
                lists = emptyList()
                user = null
            }
        }

        val values = buildList {
            add(null)
            add(empty)
        }
        val types = MediaType.knownValues()

        return buildList {
            values.forEach { v -> types.forEach { t -> add(v to t) } }
        }
    }

    private fun badClient(): List<Pair<MediaType, (MockResponse.Builder.() -> Unit)>> {
        fun apolloCommand(
            block: MockResponse.Builder.() -> Unit,
        ): MockResponse.Builder.() -> Unit = { MockResponse.Builder().apply(block) }

        val commands = buildList {
            add(apolloCommand { statusCode(500) })
            add(apolloCommand { body("Malformed body") })
            add(apolloCommand { body("""{"data": {"random": 42}}""") })
        }
        val types = MediaType.knownValues()

        return buildList {
            commands.forEach { c -> types.forEach { t -> add(t to c) } }
        }
    }
}
