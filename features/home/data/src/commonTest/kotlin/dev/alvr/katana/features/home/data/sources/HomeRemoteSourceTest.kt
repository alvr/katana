package dev.alvr.katana.features.home.data.sources

import app.cash.turbine.test
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.annotations.ApolloExperimental
import com.apollographql.apollo.testing.MapTestNetworkTransport
import com.apollographql.apollo.testing.registerTestNetworkError
import com.apollographql.apollo.testing.registerTestResponse
import dev.alvr.katana.common.media.domain.models.ItemMediaId
import dev.alvr.katana.common.media.domain.models.entries.CommonMediaEntry
import dev.alvr.katana.common.media.domain.models.lists.MediaListType
import dev.alvr.katana.core.remote.type.MediaFormat
import dev.alvr.katana.core.remote.type.MediaType
import dev.alvr.katana.core.tests.shouldBeLeft
import dev.alvr.katana.core.tests.shouldBeRight
import dev.alvr.katana.features.home.data.PopularMediaQuery
import dev.alvr.katana.features.home.data.TrendingMediaQuery
import dev.alvr.katana.features.home.data.UpcomingAnimeQuery
import dev.alvr.katana.features.home.data.di.createHomeRemoteSourceTestGraph
import dev.alvr.katana.features.home.data.fragment.HomeMediaEntry
import dev.alvr.katana.features.home.domain.failures.HomeFailure
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.test.TestCase
import io.kotest.matchers.collections.shouldContainExactly
import kotlin.time.Clock

@OptIn(ApolloExperimental::class)
internal class HomeRemoteSourceTest : FreeSpec() {
    private val client = ApolloClient.Builder().networkTransport(MapTestNetworkTransport()).build()

    private lateinit var remoteSource: HomeRemoteSource

    init {
        listOf(
                HomeMediaScenario(
                    name = "trending anime",
                    registerMedia = { registerTestResponse(TrendingMediaQuery(MediaType.ANIME), trendingMediaQuery()) },
                    registerNull = { registerTestResponse(TrendingMediaQuery(MediaType.ANIME), null) },
                    registerError = { registerTestNetworkError(TrendingMediaQuery(MediaType.ANIME)) },
                    flow = { trendingMedia(MediaListType.Anime) },
                    failure = HomeFailure.GettingTrendingMedia,
                ),
                HomeMediaScenario(
                    name = "trending manga",
                    registerMedia = { registerTestResponse(TrendingMediaQuery(MediaType.MANGA), trendingMediaQuery()) },
                    registerNull = { registerTestResponse(TrendingMediaQuery(MediaType.MANGA), null) },
                    registerError = { registerTestNetworkError(TrendingMediaQuery(MediaType.MANGA)) },
                    flow = { trendingMedia(MediaListType.Manga) },
                    failure = HomeFailure.GettingTrendingMedia,
                ),
                HomeMediaScenario(
                    name = "popular anime",
                    registerMedia = { registerTestResponse(PopularMediaQuery(MediaType.ANIME), popularMediaQuery()) },
                    registerNull = { registerTestResponse(PopularMediaQuery(MediaType.ANIME), null) },
                    registerError = { registerTestNetworkError(PopularMediaQuery(MediaType.ANIME)) },
                    flow = { popularMedia(MediaListType.Anime) },
                    failure = HomeFailure.GettingPopularMedia,
                ),
                HomeMediaScenario(
                    name = "popular manga",
                    registerMedia = { registerTestResponse(PopularMediaQuery(MediaType.MANGA), popularMediaQuery()) },
                    registerNull = { registerTestResponse(PopularMediaQuery(MediaType.MANGA), null) },
                    registerError = { registerTestNetworkError(PopularMediaQuery(MediaType.MANGA)) },
                    flow = { popularMedia(MediaListType.Manga) },
                    failure = HomeFailure.GettingPopularMedia,
                ),
            )
            .forEach { scenario ->
                "querying ${scenario.name}" -
                    {
                        "the server responds with media" {
                            scenario.registerMedia(client)

                            scenario.flow(remoteSource).test {
                                awaitItem().shouldBeRight().shouldContainExactly(mediaEntry)
                                awaitComplete()
                            }
                        }

                        "the returned data is null" {
                            scenario.registerNull(client)

                            scenario.flow(remoteSource).test {
                                awaitItem().shouldBeLeft(scenario.failure)
                                awaitComplete()
                            }
                        }

                        "a network error occurs" {
                            scenario.registerError(client)

                            scenario.flow(remoteSource).test {
                                awaitItem().shouldBeLeft(scenario.failure)
                                awaitComplete()
                            }
                        }
                    }
            }

        "querying upcoming anime" -
            {
                "the server responds with media" {
                    val query = UpcomingAnimeQuery(Clock.System.now().epochSeconds.toInt())
                    client.registerTestResponse(query, upcomingAnimeQuery())

                    remoteSource.upcomingAnime().test {
                        awaitItem().shouldBeRight().shouldContainExactly(mediaEntry)
                        awaitComplete()
                    }
                }

                "the returned data is null" {
                    val query = UpcomingAnimeQuery(Clock.System.now().epochSeconds.toInt())
                    client.registerTestResponse(query, null)

                    remoteSource.upcomingAnime().test {
                        awaitItem().shouldBeLeft(HomeFailure.GettingUpcomingMedia)
                        awaitComplete()
                    }
                }

                "a network error occurs" {
                    val query = UpcomingAnimeQuery(Clock.System.now().epochSeconds.toInt())
                    client.registerTestNetworkError(query)

                    remoteSource.upcomingAnime().test {
                        awaitItem().shouldBeLeft(HomeFailure.GettingUpcomingMedia)
                        awaitComplete()
                    }
                }
            }
    }

    override suspend fun beforeEach(testCase: TestCase) {
        remoteSource = createHomeRemoteSourceTestGraph(client).homeRemoteSource
    }
}

private data class HomeMediaScenario(
    val name: String,
    val registerMedia: ApolloClient.() -> Unit,
    val registerNull: ApolloClient.() -> Unit,
    val registerError: ApolloClient.() -> Unit,
    val flow: HomeRemoteSource.() -> HomeMediaFlow,
    val failure: HomeFailure,
)

private typealias HomeMediaFlow =
    kotlinx.coroutines.flow.Flow<
        arrow.core.Either<dev.alvr.katana.core.domain.failures.Failure, List<CommonMediaEntry>>
    >

private val mediaEntry =
    CommonMediaEntry(
        id = ItemMediaId(100),
        title = "Frieren",
        coverImage = "https://example.com/frieren.jpg",
        format = CommonMediaEntry.Format.TV,
    )

private fun homeMediaEntry() =
    HomeMediaEntry(
        __typename = "Media",
        id = mediaEntry.id.value,
        title = HomeMediaEntry.Title(__typename = "MediaTitle", userPreferred = mediaEntry.title),
        format = MediaFormat.TV,
        coverImage = HomeMediaEntry.CoverImage(__typename = "MediaCoverImage", large = mediaEntry.coverImage),
    )

private fun trendingMediaQuery() =
    TrendingMediaQuery.Data(
        page =
            TrendingMediaQuery.Page(
                __typename = "Page",
                media =
                    listOf(
                        TrendingMediaQuery.Medium(
                            __typename = "Media",
                            id = mediaEntry.id.value,
                            homeMediaEntry = homeMediaEntry(),
                        )
                    ),
            )
    )

private fun popularMediaQuery() =
    PopularMediaQuery.Data(
        page =
            PopularMediaQuery.Page(
                __typename = "Page",
                media =
                    listOf(
                        PopularMediaQuery.Medium(
                            __typename = "Media",
                            id = mediaEntry.id.value,
                            homeMediaEntry = homeMediaEntry(),
                        )
                    ),
            )
    )

private fun upcomingAnimeQuery() =
    UpcomingAnimeQuery.Data(
        page =
            UpcomingAnimeQuery.Page(
                __typename = "Page",
                airingSchedules =
                    listOf(
                        UpcomingAnimeQuery.AiringSchedule(
                            __typename = "AiringSchedule",
                            media =
                                UpcomingAnimeQuery.Media(
                                    __typename = "Media",
                                    id = mediaEntry.id.value,
                                    homeMediaEntry = homeMediaEntry(),
                                ),
                            id = 1,
                            mediaId = mediaEntry.id.value,
                        ),
                        UpcomingAnimeQuery.AiringSchedule(
                            __typename = "AiringSchedule",
                            media =
                                UpcomingAnimeQuery.Media(
                                    __typename = "Media",
                                    id = mediaEntry.id.value,
                                    homeMediaEntry = homeMediaEntry(),
                                ),
                            id = 2,
                            mediaId = mediaEntry.id.value,
                        ),
                    ),
            )
    )
