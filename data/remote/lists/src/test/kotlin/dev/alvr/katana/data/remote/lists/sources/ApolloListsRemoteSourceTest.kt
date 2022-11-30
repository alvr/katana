package dev.alvr.katana.data.remote.lists.sources

import app.cash.turbine.test
import arrow.core.right
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.annotations.ApolloExperimental
import com.apollographql.apollo3.testing.MapTestNetworkTransport
import com.apollographql.apollo3.testing.registerTestNetworkError
import com.apollographql.apollo3.testing.registerTestResponse
import dev.alvr.katana.common.core.zero
import dev.alvr.katana.data.remote.base.extensions.optional
import dev.alvr.katana.data.remote.base.interceptors.ReloadInterceptor
import dev.alvr.katana.data.remote.base.type.MediaType
import dev.alvr.katana.data.remote.lists.MediaListCollectionQuery
import dev.alvr.katana.data.remote.lists.sources.anime.AnimeListsRemoteSource
import dev.alvr.katana.data.remote.lists.sources.anime.AnimeListsRemoteSourceImpl
import dev.alvr.katana.data.remote.lists.sources.manga.MangaListsRemoteSource
import dev.alvr.katana.data.remote.lists.sources.manga.MangaListsRemoteSourceImpl
import dev.alvr.katana.data.remote.lists.test.MediaListCollectionQuery_TestBuilder.Data
import dev.alvr.katana.domain.lists.failures.ListsFailure
import dev.alvr.katana.domain.lists.models.entries.CommonMediaEntry
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.user.managers.UserIdManager
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.BehaviorSpec
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
import io.mockk.mockk
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.time.Duration.Companion.seconds

@OptIn(ApolloExperimental::class)
internal class ApolloListsRemoteSourceTest : BehaviorSpec() {
    private val userId = 37_384.right()
    private val userIdOpt = userId.optional()

    private val client = ApolloClient.Builder().networkTransport(MapTestNetworkTransport()).build()
    private val userIdManager = mockk<UserIdManager>()
    private val reloadInterceptor = mockk<ReloadInterceptor>()

    private val source: CommonListsRemoteSource = CommonListsRemoteSourceImpl(client, userIdManager, reloadInterceptor)
    private val animeSource: AnimeListsRemoteSource = AnimeListsRemoteSourceImpl(source)
    private val mangaSource: MangaListsRemoteSource = MangaListsRemoteSourceImpl(source)

    init {
        xgiven("an Apollo client with responses") {
            coEvery { userIdManager.getId() } returns userId

            and("an anime collection") {
                `when`("the collection has no lists") {
                    val query = MediaListCollectionQuery.Data {
                        collection = collection {
                            lists = emptyList()
                        }
                    }

                    client.registerTestResponse(
                        MediaListCollectionQuery(userIdOpt, MediaType.ANIME),
                        query,
                    )

                    then("the result list should be also empty") {
                        animeSource.animeCollection.test(5.seconds) {
                            awaitItem().shouldBeRight().lists.shouldBeEmpty()
                            awaitComplete()
                        }
                    }
                }

                `when`("the entries are empty") {
                    val query = MediaListCollectionQuery.Data {
                        collection = collection {
                            lists = listOf(
                                list {
                                    name = "Watching"
                                    entries = emptyList()
                                },
                                list {
                                    name = "Completed TV"
                                    entries = emptyList()
                                },
                                list {
                                    name = "Custom List"
                                    entries = emptyList()
                                },
                                list {
                                    name = null
                                    entries = emptyList()
                                },
                            )
                            user = user {
                                mediaListOptions = mediaListOptions {
                                    animeList = animeList {
                                        sectionOrder = listOf("Watching", "Completed TV", "Custom List")
                                    }
                                }
                            }
                        }
                    }

                    client.registerTestResponse(
                        MediaListCollectionQuery(userIdOpt, MediaType.ANIME),
                        query,
                    )

                    then("the result lists' entries should also be empty") {
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
                    }
                }

                `when`("the entry has null values") {
                    val query = MediaListCollectionQuery.Data {
                        collection = collection {
                            lists = listOf(
                                list {
                                    name = "Watching"
                                    entries = listOf(
                                        entry {
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
                                            media = media {
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

                    client.registerTestResponse(
                        MediaListCollectionQuery(userIdOpt, MediaType.ANIME),
                        query,
                    )

                    then("the result entry should have the default values") {
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
                    }
                }

                `when`("the entry has values") {
                    val query = MediaListCollectionQuery.Data {
                        collection = collection {
                            lists = listOf(
                                list {
                                    name = "Watching"
                                    entries = listOf(
                                        entry {
                                            id = 100
                                            score = 7.3
                                            progress = 12
                                            progressVolumes = null
                                            repeat = 2
                                            private = true
                                            notes = "My notes :)"
                                            hiddenFromStatusLists = true
                                            startedAt = startedAt {
                                                day = 23
                                                month = 12
                                                year = 1999
                                            }
                                            completedAt = completedAt {
                                                day = 5
                                                month = 5
                                                year = 2009
                                            }
                                            media = media {
                                                id = 100
                                                title = title {
                                                    userPreferred = "My anime entry"
                                                }
                                                episodes = 23
                                                format = "TV"
                                                coverImage = coverImage {
                                                    large = "https://www.fillmurray.com/128/256"
                                                }
                                                nextAiringEpisode = nextAiringEpisode {
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

                    client.registerTestResponse(
                        MediaListCollectionQuery(userIdOpt, MediaType.ANIME),
                        query,
                    )

                    then("the result entry should have the default values") {
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
                                    coverImage shouldBe "https://www.fillmurray.com/128/256"
                                    format shouldBe CommonMediaEntry.Format.TV
                                    episodes shouldBe 23
                                    with(nextEpisode.shouldNotBeNull()) {
                                        at shouldBeEqualComparingTo LocalDateTime.of(2022, 4, 12, 21, 0, 0)
                                        number shouldBe 13
                                    }
                                }
                            }
                            awaitComplete()
                        }
                    }
                }

                `when`("the returned data is null") {
                    client.registerTestResponse(
                        MediaListCollectionQuery(userIdOpt, MediaType.ANIME),
                        null,
                    )

                    then("the result list should be empty") {
                        animeSource.animeCollection.test(5.seconds) {
                            awaitItem().shouldBeRight().lists.shouldBeEmpty()
                            awaitComplete()
                        }
                    }
                }

                `when`("an error occurs") {
                    client.registerTestNetworkError(MediaListCollectionQuery(userIdOpt, MediaType.ANIME))

                    then("the error should be propagated") {
                        animeSource.animeCollection.test(5.seconds) {
                            awaitItem().shouldBeLeft(ListsFailure.GetMediaCollection)
                            cancelAndIgnoreRemainingEvents()
                        }
                    }
                }
            }

            and("a manga collection") {
                `when`("the collection has no lists") {
                    val query = MediaListCollectionQuery.Data {
                        collection = collection {
                            lists = emptyList()
                        }
                    }

                    client.registerTestResponse(
                        MediaListCollectionQuery(userIdOpt, MediaType.MANGA),
                        query,
                    )

                    then("the result list should be also empty") {
                        mangaSource.mangaCollection.test(5.seconds) {
                            awaitItem().shouldBeRight().lists.shouldBeEmpty()
                            awaitComplete()
                        }
                    }
                }

                `when`("the entries are empty") {
                    val query = MediaListCollectionQuery.Data {
                        collection = collection {
                            lists = listOf(
                                list {
                                    name = "Rereading"
                                    entries = emptyList()
                                },
                                list {
                                    name = "Reading"
                                    entries = emptyList()
                                },
                                list {
                                    name = "Custom List"
                                    entries = emptyList()
                                },
                                list {
                                    name = null
                                    entries = emptyList()
                                },
                            )
                            user = user {
                                mediaListOptions = mediaListOptions {
                                    mangaList = mangaList {
                                        sectionOrder = listOf("Custom List", "Reading", "Rereading")
                                    }
                                }
                            }
                        }
                    }

                    client.registerTestResponse(
                        MediaListCollectionQuery(userIdOpt, MediaType.MANGA),
                        query,
                    )

                    then("the result lists' entries should also be empty") {
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
                    }
                }

                `when`("the entry has null values") {
                    val query = MediaListCollectionQuery.Data {
                        collection = collection {
                            lists = listOf(
                                list {
                                    name = "Reading"
                                    entries = listOf(
                                        entry {
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
                                            media = media {
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

                    client.registerTestResponse(
                        MediaListCollectionQuery(userIdOpt, MediaType.MANGA),
                        query,
                    )

                    then("the result entry should have the default values") {
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
                    }
                }

                `when`("the entry has values") {
                    val query = MediaListCollectionQuery.Data {
                        collection = collection {
                            lists = listOf(
                                list {
                                    name = "Reading"
                                    entries = listOf(
                                        entry {
                                            id = 100
                                            score = 7.3
                                            progress = 12
                                            progressVolumes = 1
                                            repeat = 2
                                            private = true
                                            notes = "My notes :)"
                                            hiddenFromStatusLists = true
                                            startedAt = startedAt {
                                                day = 23
                                                month = 12
                                                year = 1999
                                            }
                                            completedAt = completedAt {
                                                day = 5
                                                month = 5
                                                year = 2009
                                            }
                                            media = media {
                                                id = 100
                                                title = title {
                                                    userPreferred = "My manga entry"
                                                }
                                                chapters = 23
                                                volumes = 2
                                                format = "NOVEL"
                                                coverImage = coverImage {
                                                    large = "https://www.fillmurray.com/128/256"
                                                }
                                                nextAiringEpisode = null
                                            }
                                        },
                                    )
                                },
                            )
                        }
                    }

                    client.registerTestResponse(
                        MediaListCollectionQuery(userIdOpt, MediaType.MANGA),
                        query,
                    )

                    then("the result entry should have the default values") {
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
                                    coverImage shouldBe "https://www.fillmurray.com/128/256"
                                    format shouldBe CommonMediaEntry.Format.NOVEL
                                    chapters shouldBe 23
                                    volumes shouldBe 2
                                }
                            }
                            awaitComplete()
                        }
                    }
                }

                `when`("the returned data is null") {
                    client.registerTestResponse(
                        MediaListCollectionQuery(userIdOpt, MediaType.MANGA),
                        null,
                    )

                    then("the result list should be empty") {
                        mangaSource.mangaCollection.test(5.seconds) {
                            awaitItem().shouldBeRight().lists.shouldBeEmpty()
                            awaitComplete()
                        }
                    }
                }

                `when`("an error occurs") {
                    client.registerTestNetworkError(MediaListCollectionQuery(userIdOpt, MediaType.MANGA))

                    then("the error should be propagated") {
                        mangaSource.mangaCollection.test(5.seconds) {
                            awaitItem().shouldBeLeft(ListsFailure.GetMediaCollection)
                            cancelAndIgnoreRemainingEvents()
                        }
                    }
                }
            }
        }
    }
}
