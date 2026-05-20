package dev.alvr.katana.features.lists.data.sources

import app.cash.turbine.test
import arrow.core.right
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.annotations.ApolloExperimental
import com.apollographql.apollo.interceptor.ApolloInterceptor
import com.apollographql.apollo.testing.MapTestNetworkTransport
import com.apollographql.apollo.testing.registerTestNetworkError
import com.apollographql.apollo.testing.registerTestResponse
import dev.alvr.katana.common.user.domain.managers.UserIdManager
import dev.alvr.katana.core.common.empty
import dev.alvr.katana.core.common.zero
import dev.alvr.katana.core.remote.builder.Data
import dev.alvr.katana.core.remote.builder.buildAiringSchedule
import dev.alvr.katana.core.remote.builder.buildFuzzyDate
import dev.alvr.katana.core.remote.builder.buildMedia
import dev.alvr.katana.core.remote.builder.buildMediaCoverImage
import dev.alvr.katana.core.remote.builder.buildMediaList
import dev.alvr.katana.core.remote.builder.buildMediaListCollection
import dev.alvr.katana.core.remote.builder.buildMediaListGroup
import dev.alvr.katana.core.remote.builder.buildMediaListOptions
import dev.alvr.katana.core.remote.builder.buildMediaListTypeOptions
import dev.alvr.katana.core.remote.builder.buildMediaTitle
import dev.alvr.katana.core.remote.builder.buildUser
import dev.alvr.katana.core.remote.optional
import dev.alvr.katana.core.remote.type.MediaFormat
import dev.alvr.katana.core.remote.type.MediaType
import dev.alvr.katana.core.tests.shouldBeLeft
import dev.alvr.katana.core.tests.shouldBeRight
import dev.alvr.katana.features.lists.data.MediaListCollectionQuery
import dev.alvr.katana.features.lists.data.di.createListsRemoteSourceTestGraph
import dev.alvr.katana.features.lists.domain.failures.ListsFailure
import dev.alvr.katana.features.lists.domain.models.ItemEntryId
import dev.alvr.katana.features.lists.domain.models.ItemMediaId
import dev.alvr.katana.features.lists.domain.models.entries.CommonMediaEntry
import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.test.TestCase
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
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

@OptIn(ApolloExperimental::class)
internal class ApolloListsRemoteSourceTest : FreeSpec() {
    private val userIdManager = mock<UserIdManager>()
    private val reloadInterceptor = mock<ApolloInterceptor>()

    private val client = ApolloClient.Builder().networkTransport(MapTestNetworkTransport()).build()
    private val userId = 37_384.right()
    private val userIdOpt = userId.getOrNull().optional

    private lateinit var source: ListsRemoteSource

    init {
        beforeEach { everySuspend { userIdManager.getId() } returns userId }

        "anime" -
            {
                "the collection has no lists" {
                    val query =
                        MediaListCollectionQuery.Data {
                            this["MediaListCollection"] = buildMediaListCollection {
                                lists = emptyList()
                                user = buildUser {
                                    id = 37_384
                                    mediaListOptions = buildMediaListOptions {
                                        animeList = buildMediaListTypeOptions { sectionOrder = emptyList() }
                                        mangaList = buildMediaListTypeOptions { sectionOrder = emptyList() }
                                    }
                                }
                            }
                        }

                    client.registerTestResponse(MediaListCollectionQuery(userIdOpt, MediaType.ANIME), query)

                    source.animeCollection.test {
                        awaitItem().shouldBeRight().lists.shouldBeEmpty()
                        awaitComplete()
                    }
                    verifySuspend { userIdManager.getId() }
                }

                "the entries are empty" {
                    val query =
                        MediaListCollectionQuery.Data {
                            this["MediaListCollection"] = buildMediaListCollection {
                                lists =
                                    listOf(
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
                                    )
                                user = buildUser {
                                    id = 37_384
                                    mediaListOptions = buildMediaListOptions {
                                        animeList = buildMediaListTypeOptions {
                                            sectionOrder = listOf("Watching", "Completed TV", "Custom List")
                                        }
                                        mangaList = buildMediaListTypeOptions { sectionOrder = emptyList() }
                                    }
                                }
                            }
                        }

                    client.registerTestResponse(MediaListCollectionQuery(userIdOpt, MediaType.ANIME), query)

                    source.animeCollection.test {
                        awaitItem().shouldBeRight().lists.shouldHaveSize(3).also { lists ->
                            with(lists.first()) {
                                entries.shouldBeEmpty()
                                name shouldBe "Watching"
                            }
                        }
                        awaitComplete()
                    }
                    verifySuspend { userIdManager.getId() }
                }

                "the entry has null values" {
                    val query =
                        MediaListCollectionQuery.Data {
                            this["MediaListCollection"] = buildMediaListCollection {
                                lists =
                                    listOf(
                                        buildMediaListGroup {
                                            name = "Watching"
                                            entries =
                                                listOf(
                                                    buildMediaList {
                                                        id = Int.zero
                                                        mediaId = 100
                                                        userId = 37_384
                                                        score = Double.zero
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
                                                            title = buildMediaTitle { userPreferred = String.empty }
                                                            episodes = null
                                                            format = null
                                                            coverImage = buildMediaCoverImage { large = String.empty }
                                                            nextAiringEpisode = null
                                                        }
                                                    }
                                                )
                                        }
                                    )
                                user = buildUser {
                                    id = 37_384
                                    mediaListOptions = buildMediaListOptions {
                                        animeList = buildMediaListTypeOptions { sectionOrder = emptyList() }
                                        mangaList = buildMediaListTypeOptions { sectionOrder = emptyList() }
                                    }
                                }
                            }
                        }

                    client.registerTestResponse(MediaListCollectionQuery(userIdOpt, MediaType.ANIME), query)

                    source.animeCollection.test {
                        awaitItem().shouldBeRight().lists.also { lists ->
                            val entry = lists.first().entries.shouldHaveSize(1).first()

                            with(entry.list) {
                                id shouldBe ItemEntryId(Int.zero)
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

                                id shouldBe ItemMediaId(Int.zero)
                                title.shouldBeEmpty()
                                coverImage.shouldBeEmpty()
                                format shouldBe CommonMediaEntry.Format.UNKNOWN
                                episodes.shouldBeNull()
                                nextEpisode.shouldBeNull()
                            }
                        }
                        awaitComplete()
                    }
                    verifySuspend { userIdManager.getId() }
                }

                "the entry has values" {
                    val query =
                        MediaListCollectionQuery.Data {
                            this["MediaListCollection"] = buildMediaListCollection {
                                lists =
                                    listOf(
                                        buildMediaListGroup {
                                            name = "Watching"
                                            entries =
                                                listOf(
                                                    buildMediaList {
                                                        id = 100
                                                        mediaId = 100
                                                        userId = 37_384
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
                                                            title = buildMediaTitle { userPreferred = "My anime entry" }
                                                            episodes = 23
                                                            format = MediaFormat.TV
                                                            coverImage = buildMediaCoverImage {
                                                                large = "https://placehold.co/128x256"
                                                            }
                                                            nextAiringEpisode = buildAiringSchedule {
                                                                id = 1
                                                                airingAt = 1_649_790_000
                                                                episode = 13
                                                                mediaId = 100
                                                            }
                                                        }
                                                    }
                                                )
                                        }
                                    )
                                user = buildUser {
                                    id = 37_384
                                    mediaListOptions = buildMediaListOptions {
                                        animeList = buildMediaListTypeOptions { sectionOrder = emptyList() }
                                        mangaList = buildMediaListTypeOptions { sectionOrder = emptyList() }
                                    }
                                }
                            }
                        }

                    client.registerTestResponse(MediaListCollectionQuery(userIdOpt, MediaType.ANIME), query)

                    source.animeCollection.test {
                        awaitItem().shouldBeRight().lists.also { lists ->
                            val entry = lists.first().entries.shouldHaveSize(1).first()

                            with(entry.list) {
                                id shouldBe ItemEntryId(100)
                                score shouldBe 7.3
                                progress shouldBe 12
                                progressVolumes.shouldBeNull()
                                repeat shouldBe 2
                                private.shouldBeTrue()
                                notes shouldBe "My notes :)"
                                hiddenFromStatusLists.shouldBeTrue()
                                startedAt?.shouldBeEqualComparingTo(LocalDate(1999, 12, 23))
                                completedAt?.shouldBeEqualComparingTo(LocalDate(2009, 5, 5))
                            }

                            with(entry.entry) {
                                shouldBeTypeOf<MediaEntry.Anime>()
                                shouldNotBeTypeOf<MediaEntry>()
                                shouldNotBeTypeOf<MediaEntry.Manga>()

                                id shouldBe ItemMediaId(100)
                                title shouldBe "My anime entry"
                                coverImage shouldBe "https://placehold.co/128x256"
                                format shouldBe CommonMediaEntry.Format.TV
                                episodes shouldBe 23
                                with(nextEpisode.shouldNotBeNull()) {
                                    at shouldBeEqualComparingTo LocalDateTime(2022, 4, 12, 19, 0, 0)
                                    number shouldBe 13
                                }
                            }
                        }
                        awaitComplete()
                    }
                    verifySuspend { userIdManager.getId() }
                }

                "the returned data is null" {
                    client.registerTestResponse(MediaListCollectionQuery(userIdOpt, MediaType.ANIME), null)

                    source.animeCollection.test {
                        awaitItem().shouldBeLeft(ListsFailure.GetMediaCollection)
                        awaitComplete()
                    }

                    verifySuspend { userIdManager.getId() }
                }

                "an error occurs" {
                    client.registerTestNetworkError(MediaListCollectionQuery(userIdOpt, MediaType.ANIME))

                    source.animeCollection.test {
                        awaitItem().shouldBeLeft(ListsFailure.GetMediaCollection)
                        awaitComplete()
                    }

                    verifySuspend { userIdManager.getId() }
                }
            }

        "manga" -
            {
                "the collection has no lists" {
                    val query =
                        MediaListCollectionQuery.Data {
                            this["MediaListCollection"] = buildMediaListCollection {
                                lists = emptyList()
                                user = buildUser {
                                    id = 37_384
                                    mediaListOptions = buildMediaListOptions {
                                        animeList = buildMediaListTypeOptions { sectionOrder = emptyList() }
                                        mangaList = buildMediaListTypeOptions { sectionOrder = emptyList() }
                                    }
                                }
                            }
                        }

                    client.registerTestResponse(MediaListCollectionQuery(userIdOpt, MediaType.MANGA), query)

                    source.mangaCollection.test {
                        awaitItem().shouldBeRight().lists.shouldBeEmpty()
                        awaitComplete()
                    }

                    verifySuspend { userIdManager.getId() }
                }

                "the entries are empty" {
                    val query =
                        MediaListCollectionQuery.Data {
                            this["MediaListCollection"] = buildMediaListCollection {
                                lists =
                                    listOf(
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
                                    )
                                user = buildUser {
                                    id = 37_384
                                    mediaListOptions = buildMediaListOptions {
                                        animeList = buildMediaListTypeOptions { sectionOrder = emptyList() }
                                        mangaList = buildMediaListTypeOptions {
                                            sectionOrder = listOf("Custom List", "Reading", "Rereading")
                                        }
                                    }
                                }
                            }
                        }

                    client.registerTestResponse(MediaListCollectionQuery(userIdOpt, MediaType.MANGA), query)

                    source.mangaCollection.test {
                        awaitItem().shouldBeRight().lists.shouldHaveSize(3).also { lists ->
                            with(lists.first()) {
                                entries.shouldBeEmpty()
                                name shouldBe "Custom List"
                            }
                        }
                        awaitComplete()
                    }
                    verifySuspend { userIdManager.getId() }
                }

                "the entry has null values" {
                    val query =
                        MediaListCollectionQuery.Data {
                            this["MediaListCollection"] = buildMediaListCollection {
                                lists =
                                    listOf(
                                        buildMediaListGroup {
                                            name = "Reading"
                                            entries =
                                                listOf(
                                                    buildMediaList {
                                                        id = Int.zero
                                                        mediaId = 100
                                                        userId = 37_384
                                                        score = Double.zero
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
                                                            title = buildMediaTitle { userPreferred = String.empty }
                                                            chapters = null
                                                            volumes = null
                                                            format = null
                                                            coverImage = buildMediaCoverImage { large = String.empty }
                                                            nextAiringEpisode = null
                                                        }
                                                    }
                                                )
                                        }
                                    )
                                user = buildUser {
                                    id = 37_384
                                    mediaListOptions = buildMediaListOptions {
                                        animeList = buildMediaListTypeOptions { sectionOrder = emptyList() }
                                        mangaList = buildMediaListTypeOptions { sectionOrder = emptyList() }
                                    }
                                }
                            }
                        }

                    client.registerTestResponse(MediaListCollectionQuery(userIdOpt, MediaType.MANGA), query)

                    source.mangaCollection.test {
                        awaitItem().shouldBeRight().lists.also { lists ->
                            val entry = lists.first().entries.shouldHaveSize(1).first()

                            with(entry.list) {
                                id shouldBe ItemEntryId(Int.zero)
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

                                id shouldBe ItemMediaId(Int.zero)
                                title.shouldBeEmpty()
                                coverImage.shouldBeEmpty()
                                format shouldBe CommonMediaEntry.Format.UNKNOWN
                                chapters.shouldBeNull()
                                volumes.shouldBeNull()
                            }
                        }
                        awaitComplete()
                    }
                    verifySuspend { userIdManager.getId() }
                }

                "the entry has values" {
                    val query =
                        MediaListCollectionQuery.Data {
                            this["MediaListCollection"] = buildMediaListCollection {
                                lists =
                                    listOf(
                                        buildMediaListGroup {
                                            name = "Reading"
                                            entries =
                                                listOf(
                                                    buildMediaList {
                                                        id = 100
                                                        mediaId = 100
                                                        userId = 37_384
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
                                                            title = buildMediaTitle { userPreferred = "My manga entry" }
                                                            chapters = 23
                                                            volumes = 2
                                                            format = MediaFormat.NOVEL
                                                            coverImage = buildMediaCoverImage {
                                                                large = "https://placehold.co/128x256"
                                                            }
                                                            nextAiringEpisode = null
                                                        }
                                                    }
                                                )
                                        }
                                    )
                                user = buildUser {
                                    id = 37_384
                                    mediaListOptions = buildMediaListOptions {
                                        animeList = buildMediaListTypeOptions { sectionOrder = emptyList() }
                                        mangaList = buildMediaListTypeOptions { sectionOrder = emptyList() }
                                    }
                                }
                            }
                        }

                    client.registerTestResponse(MediaListCollectionQuery(userIdOpt, MediaType.MANGA), query)

                    source.mangaCollection.test {
                        awaitItem().shouldBeRight().lists.also { lists ->
                            val entry = lists.first().entries.shouldHaveSize(1).first()

                            with(entry.list) {
                                id shouldBe ItemEntryId(100)
                                score shouldBe 7.3
                                progress shouldBe 12
                                progressVolumes shouldBe 1
                                repeat shouldBe 2
                                private.shouldBeTrue()
                                notes shouldBe "My notes :)"
                                hiddenFromStatusLists.shouldBeTrue()
                                startedAt?.shouldBeEqualComparingTo(LocalDate(1999, 12, 23))
                                completedAt?.shouldBeEqualComparingTo(LocalDate(2009, 5, 5))
                            }

                            with(entry.entry) {
                                shouldBeTypeOf<MediaEntry.Manga>()
                                shouldNotBeTypeOf<MediaEntry>()
                                shouldNotBeTypeOf<MediaEntry.Anime>()

                                id shouldBe ItemMediaId(100)
                                title shouldBe "My manga entry"
                                coverImage shouldBe "https://placehold.co/128x256"
                                format shouldBe CommonMediaEntry.Format.NOVEL
                                chapters shouldBe 23
                                volumes shouldBe 2
                            }
                        }
                        awaitComplete()
                    }
                    verifySuspend { userIdManager.getId() }
                }

                "the returned data is null" {
                    client.registerTestResponse(MediaListCollectionQuery(userIdOpt, MediaType.MANGA), null)

                    source.mangaCollection.test {
                        awaitItem().shouldBeLeft(ListsFailure.GetMediaCollection)
                        awaitComplete()
                    }

                    verifySuspend { userIdManager.getId() }
                }

                "an error occurs" {
                    client.registerTestNetworkError(MediaListCollectionQuery(userIdOpt, MediaType.MANGA))

                    source.mangaCollection.test {
                        awaitItem().shouldBeLeft(ListsFailure.GetMediaCollection)
                        awaitComplete()
                    }

                    verifySuspend { userIdManager.getId() }
                }
            }
    }

    override suspend fun beforeEach(testCase: TestCase) {
        source = createListsRemoteSourceTestGraph(client, userIdManager, reloadInterceptor).listsRemoteSource
    }
}
