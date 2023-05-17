package dev.alvr.katana.data.remote.lists.sources

import app.cash.turbine.test
import arrow.core.right
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.annotations.ApolloExperimental
import com.apollographql.apollo3.interceptor.ApolloInterceptor
import com.apollographql.apollo3.testing.MapTestNetworkTransport
import com.apollographql.apollo3.testing.registerTestNetworkError
import com.apollographql.apollo3.testing.registerTestResponse
import dev.alvr.katana.common.core.zero
import dev.alvr.katana.common.tests.TestBase
import dev.alvr.katana.common.tests.shouldBeLeft
import dev.alvr.katana.common.tests.shouldBeRight
import dev.alvr.katana.data.remote.base.extensions.optional
import dev.alvr.katana.data.remote.base.type.MediaFormat
import dev.alvr.katana.data.remote.base.type.MediaType
import dev.alvr.katana.data.remote.base.type.buildAiringSchedule
import dev.alvr.katana.data.remote.base.type.buildFuzzyDate
import dev.alvr.katana.data.remote.base.type.buildMedia
import dev.alvr.katana.data.remote.base.type.buildMediaCoverImage
import dev.alvr.katana.data.remote.base.type.buildMediaList
import dev.alvr.katana.data.remote.base.type.buildMediaListCollection
import dev.alvr.katana.data.remote.base.type.buildMediaListGroup
import dev.alvr.katana.data.remote.base.type.buildMediaListOptions
import dev.alvr.katana.data.remote.base.type.buildMediaListTypeOptions
import dev.alvr.katana.data.remote.base.type.buildMediaTitle
import dev.alvr.katana.data.remote.base.type.buildUser
import dev.alvr.katana.data.remote.lists.MediaListCollectionQuery
import dev.alvr.katana.data.remote.lists.sources.anime.AnimeListsRemoteSource
import dev.alvr.katana.data.remote.lists.sources.anime.AnimeListsRemoteSourceImpl
import dev.alvr.katana.data.remote.lists.sources.manga.MangaListsRemoteSource
import dev.alvr.katana.data.remote.lists.sources.manga.MangaListsRemoteSourceImpl
import dev.alvr.katana.domain.lists.failures.ListsFailure
import dev.alvr.katana.domain.lists.models.entries.CommonMediaEntry
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.user.managers.UserIdManager
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.comparables.shouldBeEqualComparingTo
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEmpty
import io.kotest.matchers.types.shouldBeTypeOf
import io.kotest.matchers.types.shouldNotBeTypeOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@ApolloExperimental
@ExperimentalCoroutinesApi
internal class ApolloListsRemoteSourceTest : TestBase() {
    @MockK
    private lateinit var userIdManager: UserIdManager
    @MockK
    private lateinit var reloadInterceptor: ApolloInterceptor

    private lateinit var source: CommonListsRemoteSource
    private lateinit var animeSource: AnimeListsRemoteSource
    private lateinit var mangaSource: MangaListsRemoteSource

    private val client = ApolloClient.Builder().networkTransport(MapTestNetworkTransport()).build()
    private val userId = 37_384.right()
    private val userIdOpt = userId.optional()

    override suspend fun beforeEach() {
        source = CommonListsRemoteSourceImpl(client, userIdManager, reloadInterceptor)
        animeSource = AnimeListsRemoteSourceImpl(source)
        mangaSource = MangaListsRemoteSourceImpl(source)

        coEvery { userIdManager.getId() } returns userId
    }

    @Nested
    @DisplayName("GIVEN an anime collection")
    inner class Anime {
        @Test
        @DisplayName("WHEN the collection has no lists THEN the result list should be also empty")
        fun `the collection has no lists`() = runTest {
            // GIVEN
            val query = MediaListCollectionQuery.Data {
                this["collection"] = buildMediaListCollection {
                    lists = emptyList()
                }
            }

            // WHEN
            client.registerTestResponse(
                MediaListCollectionQuery(userIdOpt, MediaType.ANIME),
                query,
            )

            // THEN
            animeSource.animeCollection.test(5.seconds) {
                awaitItem().shouldBeRight().lists.shouldBeEmpty()
                awaitComplete()
            }
            coVerify(exactly = 1) { userIdManager.getId() }
        }

        @Test
        @DisplayName("WHEN the entries are empty THEN the result lists' entries should also be empty")
        fun `the entries are empty`() = runTest {
            // GIVEN
            val query = MediaListCollectionQuery.Data {
                this["collection"] = buildMediaListCollection {
                    lists = listOf(
                        buildMediaListGroup {
                            name = "Watching"
                            entries = emptyList()
                        },
                        buildMediaListGroup {
                            name = "Completed TV"
                            entries = emptyList()
                        },
                        buildMediaListGroup {
                            name = "Custom List"
                            entries = emptyList()
                        },
                        buildMediaListGroup {
                            name = null
                            entries = emptyList()
                        },
                    )
                    user = buildUser {
                        this["mediaListOptions"] = buildMediaListOptions {
                            animeList = buildMediaListTypeOptions {
                                sectionOrder = listOf("Watching", "Completed TV", "Custom List")
                            }
                        }
                    }
                }
            }

            // WHEN
            client.registerTestResponse(
                MediaListCollectionQuery(userIdOpt, MediaType.ANIME),
                query,
            )

            // THEN
            animeSource.animeCollection.test(5.seconds) {
                awaitItem().shouldBeRight().lists
                    .shouldHaveSize(4)
                    .also { lists ->
                        with(lists.first()) {
                            entries.shouldBeEmpty()
                            name shouldBe "Watching"
                        }
                    }
                awaitComplete()
            }
            coVerify(exactly = 1) { userIdManager.getId() }
        }

        @Test
        @DisplayName("WHEN the entry has null values THEN the result entry should have the default values")
        fun `the entry has null values`() = runTest {
            // GIVEN
            val query = MediaListCollectionQuery.Data {
                this["collection"] = buildMediaListCollection {
                    lists = listOf(
                        buildMediaListGroup {
                            name = "Watching"
                            entries = listOf(
                                buildMediaList {
                                    id = Int.zero
                                    score = null
                                    progress = null
                                    progressVolumes = null
                                    repeat = null
                                    private = null
                                    notes = null
                                    hiddenFromStatusLists = null
                                    startedAt = null
                                    completedAt = null
                                    media = buildMedia {
                                        id = Int.zero
                                        title = null
                                        episodes = null
                                        format = null
                                        coverImage = null
                                        nextAiringEpisode = null
                                    }
                                },
                            )
                        },
                    )
                }
            }

            // WHEN
            client.registerTestResponse(
                MediaListCollectionQuery(userIdOpt, MediaType.ANIME),
                query,
            )

            // THEN
            animeSource.animeCollection.test(5.seconds) {
                awaitItem().shouldBeRight().lists.also { lists ->
                    val entry = lists.first().entries.shouldHaveSize(1).first()

                    with(entry.list) {
                        id shouldBe Int.zero
                        score shouldBe Double.zero
                        progress shouldBe Int.zero
                        progressVolumes.shouldBeNull()
                        repeat shouldBe Int.zero
                        private.shouldBeFalse()
                        notes.shouldBeEmpty()
                        hiddenFromStatusLists.shouldBeFalse()
                        startedAt.shouldBeNull()
                        completedAt.shouldBeNull()
                    }

                    with(entry.entry) {
                        shouldBeTypeOf<MediaEntry.Anime>()
                        shouldNotBeTypeOf<MediaEntry>()
                        shouldNotBeTypeOf<MediaEntry.Manga>()

                        id shouldBe Int.zero
                        title.shouldBeEmpty()
                        coverImage.shouldBeEmpty()
                        format shouldBe CommonMediaEntry.Format.UNKNOWN
                        episodes.shouldBeNull()
                        nextEpisode.shouldBeNull()
                    }
                }
                awaitComplete()
            }
            coVerify(exactly = 1) { userIdManager.getId() }
        }

        @Test
        @DisplayName("WHEN the entry has values THEN the result entry should have the default values")
        @Suppress("LongMethod")
        fun `the entry has values`() = runTest {
            // GIVEN
            val query = MediaListCollectionQuery.Data {
                this["collection"] = buildMediaListCollection {
                    lists = listOf(
                        buildMediaListGroup {
                            name = "Watching"
                            entries = listOf(
                                buildMediaList {
                                    id = 100
                                    score = 7.3
                                    progress = 12
                                    progressVolumes = null
                                    repeat = 2
                                    private = true
                                    notes = "My notes :)"
                                    hiddenFromStatusLists = true
                                    startedAt = buildFuzzyDate {
                                        day = 23
                                        month = 12
                                        year = 1999
                                    }
                                    completedAt = buildFuzzyDate {
                                        day = 5
                                        month = 5
                                        year = 2009
                                    }
                                    media = buildMedia {
                                        id = 100
                                        title = buildMediaTitle {
                                            userPreferred = "My anime entry"
                                        }
                                        episodes = 23
                                        format = MediaFormat.TV
                                        coverImage = buildMediaCoverImage {
                                            large = "https://placehold.co/128x256"
                                        }
                                        nextAiringEpisode = buildAiringSchedule {
                                            airingAt = 1_649_790_000
                                            episode = 13
                                        }
                                    }
                                },
                            )
                        },
                    )
                }
            }

            // WHEN
            client.registerTestResponse(
                MediaListCollectionQuery(userIdOpt, MediaType.ANIME),
                query,
            )

            // THEN
            animeSource.animeCollection.test(5.seconds) {
                awaitItem().shouldBeRight().lists.also { lists ->
                    val entry = lists.first().entries.shouldHaveSize(1).first()

                    with(entry.list) {
                        id shouldBe 100
                        score shouldBe 7.3
                        progress shouldBe 12
                        progressVolumes.shouldBeNull()
                        repeat shouldBe 2
                        private.shouldBeTrue()
                        notes shouldBe "My notes :)"
                        hiddenFromStatusLists.shouldBeTrue()
                        startedAt?.shouldBeEqualComparingTo(LocalDate.of(1999, 12, 23))
                        completedAt?.shouldBeEqualComparingTo(LocalDate.of(2009, 5, 5))
                    }

                    with(entry.entry) {
                        shouldBeTypeOf<MediaEntry.Anime>()
                        shouldNotBeTypeOf<MediaEntry>()
                        shouldNotBeTypeOf<MediaEntry.Manga>()

                        id shouldBe 100
                        title shouldBe "My anime entry"
                        coverImage shouldBe "https://placehold.co/128x256"
                        format shouldBe CommonMediaEntry.Format.TV
                        episodes shouldBe 23
                        with(nextEpisode.shouldNotBeNull()) {
                            at shouldBeEqualComparingTo LocalDateTime.of(2022, 4, 12, 19, 0, 0)
                            number shouldBe 13
                        }
                    }
                }
                awaitComplete()
            }
            coVerify(exactly = 1) { userIdManager.getId() }
        }

        @Test
        @DisplayName("WHEN the returned data is null THEN the result list should be empty")
        fun `the returned data is null`() = runTest {
            // GIVEN
            client.registerTestResponse(
                MediaListCollectionQuery(userIdOpt, MediaType.ANIME),
                null,
            )

            // THEN
            animeSource.animeCollection.test(5.seconds) {
                awaitItem().shouldBeRight().lists.shouldBeEmpty()
                awaitComplete()
            }
            coVerify(exactly = 1) { userIdManager.getId() }
        }

        @Test
        @DisplayName("WHEN an error occurs THEN the error should be propagated")
        fun `an error occurs`() = runTest {
            // GIVEN
            client.registerTestNetworkError(MediaListCollectionQuery(userIdOpt, MediaType.ANIME))

            // THEN
            animeSource.animeCollection.test(5.seconds) {
                awaitItem().shouldBeLeft(ListsFailure.GetMediaCollection)
                cancelAndIgnoreRemainingEvents()
            }
            coVerify(exactly = 1) { userIdManager.getId() }
        }
    }

    @Nested
    @DisplayName("GIVEN a manga collection")
    inner class Manga {
        @Test
        @DisplayName("WHEN the collection has no lists THEN the result list should be also empty")
        fun `the collection has no lists`() = runTest {
            // GIVEN
            val query = MediaListCollectionQuery.Data {
                this["collection"] = buildMediaListCollection {
                    lists = emptyList()
                }
            }

            // WHEN
            client.registerTestResponse(
                MediaListCollectionQuery(userIdOpt, MediaType.MANGA),
                query,
            )

            // THEN
            mangaSource.mangaCollection.test(5.seconds) {
                awaitItem().shouldBeRight().lists.shouldBeEmpty()
                awaitComplete()
            }
            coVerify(exactly = 1) { userIdManager.getId() }
        }

        @Test
        @DisplayName("WHEN the entries are empty THEN the result lists' entries should also be empty")
        fun `the entries are empty`() = runTest {
            // GIVEN
            val query = MediaListCollectionQuery.Data {
                this["collection"] = buildMediaListCollection {
                    lists = listOf(
                        buildMediaListGroup {
                            name = "Rereading"
                            entries = emptyList()
                        },
                        buildMediaListGroup {
                            name = "Reading"
                            entries = emptyList()
                        },
                        buildMediaListGroup {
                            name = "Custom List"
                            entries = emptyList()
                        },
                        buildMediaListGroup {
                            name = null
                            entries = emptyList()
                        },
                    )
                    user = buildUser {
                        this["mediaListOptions"] = buildMediaListOptions {
                            mangaList = buildMediaListTypeOptions {
                                sectionOrder = listOf("Custom List", "Reading", "Rereading")
                            }
                        }
                    }
                }
            }

            // WHEN
            client.registerTestResponse(
                MediaListCollectionQuery(userIdOpt, MediaType.MANGA),
                query,
            )

            // THEN
            mangaSource.mangaCollection.test(5.seconds) {
                awaitItem().shouldBeRight().lists
                    .shouldHaveSize(4)
                    .also { lists ->
                        with(lists.first()) {
                            entries.shouldBeEmpty()
                            name shouldBe "Custom List"
                        }
                    }
                awaitComplete()
            }
            coVerify(exactly = 1) { userIdManager.getId() }
        }

        @Test
        @DisplayName("WHEN the entry has null values THEN the result entry should have the default values")
        fun `the entry has null values`() = runTest {
            // GIVEN
            val query = MediaListCollectionQuery.Data {
                this["collection"] = buildMediaListCollection {
                    lists = listOf(
                        buildMediaListGroup {
                            name = "Reading"
                            entries = listOf(
                                buildMediaList {
                                    id = Int.zero
                                    score = null
                                    progress = null
                                    progressVolumes = null
                                    repeat = null
                                    private = null
                                    notes = null
                                    hiddenFromStatusLists = null
                                    startedAt = null
                                    completedAt = null
                                    media = buildMedia {
                                        id = Int.zero
                                        title = null
                                        chapters = null
                                        volumes = null
                                        format = null
                                        coverImage = null
                                        nextAiringEpisode = null
                                    }
                                },
                            )
                        },
                    )
                }
            }

            // WHEN
            client.registerTestResponse(
                MediaListCollectionQuery(userIdOpt, MediaType.MANGA),
                query,
            )

            // THEN
            mangaSource.mangaCollection.test(5.seconds) {
                awaitItem().shouldBeRight().lists.also { lists ->
                    val entry = lists.first().entries.shouldHaveSize(1).first()

                    with(entry.list) {
                        id shouldBe Int.zero
                        score shouldBe Double.zero
                        progress shouldBe Int.zero
                        progressVolumes.shouldBeNull()
                        repeat shouldBe Int.zero
                        private.shouldBeFalse()
                        notes.shouldBeEmpty()
                        hiddenFromStatusLists.shouldBeFalse()
                        startedAt.shouldBeNull()
                        completedAt.shouldBeNull()
                    }

                    with(entry.entry) {
                        shouldBeTypeOf<MediaEntry.Manga>()
                        shouldNotBeTypeOf<MediaEntry>()
                        shouldNotBeTypeOf<MediaEntry.Anime>()

                        id shouldBe Int.zero
                        title.shouldBeEmpty()
                        coverImage.shouldBeEmpty()
                        format shouldBe CommonMediaEntry.Format.UNKNOWN
                        chapters.shouldBeNull()
                        volumes.shouldBeNull()
                    }
                }
                awaitComplete()
            }
            coVerify(exactly = 1) { userIdManager.getId() }
        }

        @Test
        @DisplayName("WHEN the entry has values THEN the result entry should have the default values")
        @Suppress("LongMethod")
        fun `the entry has values`() = runTest {
            // GIVEN
            val query = MediaListCollectionQuery.Data {
                this["collection"] = buildMediaListCollection {
                    lists = listOf(
                        buildMediaListGroup {
                            name = "Reading"
                            entries = listOf(
                                buildMediaList {
                                    id = 100
                                    score = 7.3
                                    progress = 12
                                    progressVolumes = 1
                                    repeat = 2
                                    private = true
                                    notes = "My notes :)"
                                    hiddenFromStatusLists = true
                                    startedAt = buildFuzzyDate {
                                        day = 23
                                        month = 12
                                        year = 1999
                                    }
                                    completedAt = buildFuzzyDate {
                                        day = 5
                                        month = 5
                                        year = 2009
                                    }
                                    media = buildMedia {
                                        id = 100
                                        title = buildMediaTitle {
                                            userPreferred = "My manga entry"
                                        }
                                        chapters = 23
                                        volumes = 2
                                        format = MediaFormat.NOVEL
                                        coverImage = buildMediaCoverImage {
                                            large = "https://placehold.co/128x256"
                                        }
                                        nextAiringEpisode = null
                                    }
                                },
                            )
                        },
                    )
                }
            }

            // WHEN
            client.registerTestResponse(
                MediaListCollectionQuery(userIdOpt, MediaType.MANGA),
                query,
            )

            // THEN
            mangaSource.mangaCollection.test(5.seconds) {
                awaitItem().shouldBeRight().lists.also { lists ->
                    val entry = lists.first().entries.shouldHaveSize(1).first()

                    with(entry.list) {
                        id shouldBe 100
                        score shouldBe 7.3
                        progress shouldBe 12
                        progressVolumes shouldBe 1
                        repeat shouldBe 2
                        private.shouldBeTrue()
                        notes shouldBe "My notes :)"
                        hiddenFromStatusLists.shouldBeTrue()
                        startedAt?.shouldBeEqualComparingTo(LocalDate.of(1999, 12, 23))
                        completedAt?.shouldBeEqualComparingTo(LocalDate.of(2009, 5, 5))
                    }

                    with(entry.entry) {
                        shouldBeTypeOf<MediaEntry.Manga>()
                        shouldNotBeTypeOf<MediaEntry>()
                        shouldNotBeTypeOf<MediaEntry.Anime>()

                        id shouldBe 100
                        title shouldBe "My manga entry"
                        coverImage shouldBe "https://placehold.co/128x256"
                        format shouldBe CommonMediaEntry.Format.NOVEL
                        chapters shouldBe 23
                        volumes shouldBe 2
                    }
                }
                awaitComplete()
            }
            coVerify(exactly = 1) { userIdManager.getId() }
        }

        @Test
        @DisplayName("WHEN the returned data is null THEN the result list should be empty")
        fun `the returned data is null`() = runTest {
            // GIVEN
            client.registerTestResponse(
                MediaListCollectionQuery(userIdOpt, MediaType.MANGA),
                null,
            )

            // THEN
            mangaSource.mangaCollection.test(5.seconds) {
                awaitItem().shouldBeRight().lists.shouldBeEmpty()
                awaitComplete()
            }
            coVerify(exactly = 1) { userIdManager.getId() }
        }

        @Test
        @DisplayName("WHEN an error occurs THEN the error should be propagated")
        fun `an error occurs`() = runTest {
            // GIVEN
            client.registerTestNetworkError(MediaListCollectionQuery(userIdOpt, MediaType.MANGA))

            // THEN
            mangaSource.mangaCollection.test(5.seconds) {
                awaitItem().shouldBeLeft(ListsFailure.GetMediaCollection)
                cancelAndIgnoreRemainingEvents()
            }
            coVerify(exactly = 1) { userIdManager.getId() }
        }
    }
}
