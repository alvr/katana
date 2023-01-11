package dev.alvr.katana.data.remote.lists.mappers.responses

import com.apollographql.apollo3.annotations.ApolloExperimental
import dev.alvr.katana.common.core.empty
import dev.alvr.katana.common.core.zero
import dev.alvr.katana.common.tests.TestBase
import dev.alvr.katana.data.remote.base.type.MediaType
import dev.alvr.katana.data.remote.lists.MediaListCollectionQuery
import dev.alvr.katana.data.remote.lists.test.MediaListCollectionQuery_TestBuilder.Data
import dev.alvr.katana.domain.lists.models.entries.CommonMediaEntry
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import io.kotest.assertions.throwables.shouldThrowExactlyUnit
import io.kotest.inspectors.forAll
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldExist
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.comparables.shouldBeEqualComparingTo
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEmpty
import io.kotest.matchers.types.shouldBeTypeOf
import io.kotest.matchers.types.shouldNotBeTypeOf
import java.time.LocalDate
import java.time.LocalDateTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@ApolloExperimental
@ExperimentalCoroutinesApi
internal class MediaListMapperTest : TestBase() {
    @Test
    @DisplayName("WHEN a null response from server THEN it should return a null medialist")
    fun `a null response from server`() = runTest {
        // GIVEN
        val data: MediaListCollectionQuery.Data? = null

        // WHEN
        val anime = data?.mediaList<MediaEntry.Anime>(MediaType.ANIME)
        val manga = data?.mediaList<MediaEntry.Manga>(MediaType.MANGA)

        // THEN
        anime.shouldBeNull()
        manga.shouldBeNull()
    }

    @Test
    @DisplayName("WHEN a collection without lists THEN it should return an empty list")
    fun `a collection without lists`() = runTest {
        // GIVEN
        val data = MediaListCollectionQuery.Data {
            collection = collection {
                lists = emptyList()
                user = null
            }
        }

        // WHEN
        val anime = data.mediaList<MediaEntry.Anime>(MediaType.ANIME)
        val manga = data.mediaList<MediaEntry.Manga>(MediaType.MANGA)

        // THEN
        anime.shouldBeEmpty()
        manga.shouldBeEmpty()
    }

    @Test
    @DisplayName("WHEN a list without name THEN it should return a CUSTOM list without name")
    fun `a list without name`() = runTest {
        // GIVEN
        val data = MediaListCollectionQuery.Data {
            collection = collection {
                lists = listOf(
                    list {
                        name = null
                        entries = emptyList()
                    },
                    list {
                        name = String.empty
                        entries = emptyList()
                    },
                    null,
                )
                user = user {
                    mediaListOptions = mediaListOptions {
                        animeList = animeList {
                            sectionOrder = emptyList()
                        }
                        mangaList = animeList {
                            sectionOrder = emptyList()
                        }
                    }
                }
            }
        }

        // WHEN
        val anime = data.mediaList<MediaEntry.Anime>(MediaType.ANIME)
        val manga = data.mediaList<MediaEntry.Manga>(MediaType.MANGA)

        // THEN
        anime.forAll { list -> list.name.shouldBeEmpty() }
        manga.forAll { list -> list.name.shouldBeEmpty() }
    }

    @Nested
    @DisplayName("GIVEN a collection of animes")
    inner class Anime {
        @Test
        @DisplayName(
            """
            WHEN a collection of animes without entries
            THEN should return a sorted media list
            """,
        )
        fun `a collection of animes without entries`() = runTest {
            // GIVEN
            val data = MediaListCollectionQuery.Data {
                collection = collection {
                    lists = listOf(
                        list {
                            name = "Rewatching"
                            entries = emptyList()
                        },
                        list {
                            name = "Watching"
                            entries = listOf(null)
                        },
                        list {
                            name = "Paused"
                            entries = emptyList()
                        },
                        list {
                            name = "Completed TV"
                            entries = emptyList()
                        },
                    )
                    user = user {
                        mediaListOptions = mediaListOptions {
                            animeList = animeList {
                                sectionOrder = listOf("Watching", "Rewatching", "Completed TV", "Paused")
                            }
                        }
                    }
                }
            }

            // WHEN
            val result = data.mediaList<MediaEntry.Anime>(MediaType.ANIME)

            // THEN
            result.also { list ->
                list[0].name shouldBe "Watching"
                list[1].name shouldBe "Rewatching"
                list[2].name shouldBe "Completed TV"
                list[3].name shouldBe "Paused"
            }.shouldHaveSize(4)
        }

        @Test
        @DisplayName(
            """
            WHEN a collection of animes without entries and sectionOrder
            THEN should return a sorted media list
            """,
        )
        fun `a collection of animes without entries and sectionOrder`() = runTest {
            // GIVEN
            val data = MediaListCollectionQuery.Data {
                collection = collection {
                    lists = listOf(
                        list {
                            name = "Rewatching"
                            entries = emptyList()
                        },
                        list {
                            name = "Watching"
                            entries = listOf(null)
                        },
                        list {
                            name = "Paused"
                            entries = emptyList()
                        },
                        list {
                            name = "Completed TV"
                            entries = emptyList()
                        },
                    )
                    user = user {
                        mediaListOptions = mediaListOptions {
                            animeList = animeList {
                                sectionOrder = null
                            }
                        }
                    }
                }
            }

            // WHEN
            val result = data.mediaList<MediaEntry.Anime>(MediaType.ANIME)

            // THEN
            result.also { list ->
                list[0].name shouldBe "Rewatching"
                list[1].name shouldBe "Watching"
                list[2].name shouldBe "Paused"
                list[3].name shouldBe "Completed TV"
            }.shouldHaveSize(4)
        }

        @Test
        @DisplayName(
            """
            WHEN a collection of animes without entries and mediaListOptions
            THEN should return a sorted media list
            """,
        )
        fun `a collection of animes without entries and mediaListOptions`() = runTest {
            // GIVEN
            val data = MediaListCollectionQuery.Data {
                collection = collection {
                    lists = listOf(
                        list {
                            name = "Rewatching"
                            entries = emptyList()
                        },
                        list {
                            name = "Watching"
                            entries = listOf(null)
                        },
                        list {
                            name = "Paused"
                            entries = emptyList()
                        },
                        list {
                            name = "Completed TV"
                            entries = emptyList()
                        },
                    )
                    user = user {
                        mediaListOptions = null
                    }
                }
            }

            // WHEN
            val result = data.mediaList<MediaEntry.Anime>(MediaType.ANIME)

            // THEN
            result.also { list ->
                list[0].name shouldBe "Rewatching"
                list[1].name shouldBe "Watching"
                list[2].name shouldBe "Paused"
                list[3].name shouldBe "Completed TV"
            }.shouldHaveSize(4)
        }

        @Test
        @DisplayName(
            """
            WHEN a collection of animes without entries and animeListSorting
            THEN should return a sorted media list
            """,
        )
        fun `a collection of animes without entries and animeListSorting`() = runTest {
            // GIVEN
            val data = MediaListCollectionQuery.Data {
                collection = collection {
                    lists = listOf(
                        list {
                            name = "Rewatching"
                            entries = emptyList()
                        },
                        list {
                            name = "Watching"
                            entries = listOf(null)
                        },
                        list {
                            name = "Paused"
                            entries = emptyList()
                        },
                        list {
                            name = "Completed TV"
                            entries = emptyList()
                        },
                    )
                    user = user {
                        mediaListOptions = mediaListOptions {
                            animeList = null
                        }
                    }
                }
            }

            // WHEN
            val result = data.mediaList<MediaEntry.Anime>(MediaType.ANIME)

            // THEN
            result.also { list ->
                list[0].name shouldBe "Rewatching"
                list[1].name shouldBe "Watching"
                list[2].name shouldBe "Paused"
                list[3].name shouldBe "Completed TV"
            }.shouldHaveSize(4)
        }

        @Test
        @DisplayName(
            """
            WHEN a collection of animes with valid entries
            THEN the media should be type of MediaEntry.Anime
            """,
        )
        @Suppress("LongMethod")
        fun `a collection of animes with valid entries`() = runTest {
            // GIVEN
            val data = MediaListCollectionQuery.Data {
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
                                    updatedAt = 1_649_790_000
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
                                            large = "https://placehold.co/128x256"
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

            // WHEN
            val result = data.mediaList<MediaEntry.Anime>(MediaType.ANIME)

            // THEN
            result.forAll { list ->
                list.entries.forAll { entry ->
                    with(entry.list) {
                        id shouldBe 100
                        score shouldBe 7.3
                        progress shouldBe 12
                        progressVolumes.shouldBeNull()
                        repeat shouldBe 2
                        private.shouldBeTrue()
                        notes shouldBe "My notes :)"
                        hiddenFromStatusLists.shouldBeTrue()
                        startedAt.shouldNotBeNull() shouldBeEqualComparingTo LocalDate.of(1999, 12, 23)
                        completedAt.shouldNotBeNull() shouldBeEqualComparingTo LocalDate.of(2009, 5, 5)
                        updatedAt.shouldNotBeNull() shouldBeEqualComparingTo LocalDateTime.of(2022, 4, 12, 19, 0, 0)
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
            }

            shouldThrowExactlyUnit<IllegalStateException> {
                data.mediaList<MediaEntry>(MediaType.UNKNOWN__)
            }
        }

        @Test
        @DisplayName(
            """
            WHEN a collection of animes with invalid entries
            THEN the media should be type of MediaEntry.Anime
            """,
        )
        fun `a collection of animes with invalid entries`() = runTest {
            // GIVEN
            val data = MediaListCollectionQuery.Data {
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
                                    updatedAt = null
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

            // WHEN
            val result = data.mediaList<MediaEntry.Anime>(MediaType.ANIME)

            // THEN
            result shouldExist { it.name == "Watching" }
            result.forAll { list ->
                list.entries.forAll { entry ->
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
                        updatedAt.shouldBeNull()
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
            }

            shouldThrowExactlyUnit<IllegalStateException> {
                data.mediaList<MediaEntry>(MediaType.UNKNOWN__)
            }
        }
    }

    @Nested
    @DisplayName("GIVEN a collection of mangas")
    inner class Manga {
        @Test
        @DisplayName(
            """
            WHEN a collection of mangas without entries
            THEN it should return a sorted media list
            """,
        )
        fun `a collection of mangas without entries`() = runTest {
            // GIVEN
            val data = MediaListCollectionQuery.Data {
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
                            name = "Paused"
                            entries = listOf(null)
                        },
                        list {
                            name = "Completed Novel"
                            entries = emptyList()
                        },
                    )
                    user = user {
                        mediaListOptions = mediaListOptions {
                            mangaList = mangaList {
                                sectionOrder = listOf("Reading", "Rereading", "Completed Novel", "Paused")
                            }
                        }
                    }
                }
            }

            // WHEN
            val result = data.mediaList<MediaEntry.Manga>(MediaType.MANGA)

            // THEN
            result.also { list ->
                list[0].name shouldBe "Reading"
                list[1].name shouldBe "Rereading"
                list[2].name shouldBe "Completed Novel"
                list[3].name shouldBe "Paused"
            }.shouldHaveSize(4)
        }

        @Test
        @DisplayName(
            """
            WHEN a collection of mangas without entries and sectionOrder
            THEN it should return a sorted media list
            """,
        )
        fun `a collection of mangas without entries and sectionOrder`() = runTest {
            // GIVEN
            val data = MediaListCollectionQuery.Data {
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
                            name = "Paused"
                            entries = listOf(null)
                        },
                        list {
                            name = "Completed Novel"
                            entries = emptyList()
                        },
                    )
                    user = user {
                        mediaListOptions = mediaListOptions {
                            mangaList = mangaList {
                                sectionOrder = null
                            }
                        }
                    }
                }
            }

            // WHEN
            val result = data.mediaList<MediaEntry.Manga>(MediaType.MANGA)

            // THEN
            result.also { list ->
                list[0].name shouldBe "Rereading"
                list[1].name shouldBe "Reading"
                list[2].name shouldBe "Paused"
                list[3].name shouldBe "Completed Novel"
            }.shouldHaveSize(4)
        }

        @Test
        @DisplayName(
            """
            WHEN a collection of mangas without entries and mediaListOptions
            THEN it should return a sorted media list
            """,
        )
        fun `a collection of mangas without entries and mediaListOptions`() = runTest {
            // GIVEN
            val data = MediaListCollectionQuery.Data {
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
                            name = "Paused"
                            entries = listOf(null)
                        },
                        list {
                            name = "Completed Novel"
                            entries = emptyList()
                        },
                    )
                    user = user {
                        mediaListOptions = null
                    }
                }
            }

            // WHEN
            val result = data.mediaList<MediaEntry.Manga>(MediaType.MANGA)

            // THEN
            result.also { list ->
                list[0].name shouldBe "Rereading"
                list[1].name shouldBe "Reading"
                list[2].name shouldBe "Paused"
                list[3].name shouldBe "Completed Novel"
            }.shouldHaveSize(4)
        }

        @Test
        @DisplayName(
            """
            WHEN a collection of mangas without entries and mangaListSorting
            THEN it should return a sorted media list
            """,
        )
        fun `a collection of mangas without entries and mangaListSorting`() = runTest {
            // GIVEN
            val data = MediaListCollectionQuery.Data {
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
                            name = "Paused"
                            entries = listOf(null)
                        },
                        list {
                            name = "Completed Novel"
                            entries = emptyList()
                        },
                    )
                    user = user {
                        mediaListOptions = mediaListOptions {
                            mangaList = null
                        }
                    }
                }
            }

            // WHEN
            val result = data.mediaList<MediaEntry.Manga>(MediaType.MANGA)

            // THEN
            result.also { list ->
                list[0].name shouldBe "Rereading"
                list[1].name shouldBe "Reading"
                list[2].name shouldBe "Paused"
                list[3].name shouldBe "Completed Novel"
            }.shouldHaveSize(4)
        }

        @Test
        @DisplayName(
            """
            WHEN a collection of mangas with valid entries
            THEN the media should be type of MediaEntry.Manga
            """,
        )
        fun `a collection of mangas with valid entries`() = runTest {
            // GIVEN
            val data = MediaListCollectionQuery.Data {
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
                                    updatedAt = 1_649_790_000
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
            val result = data.mediaList<MediaEntry.Manga>(MediaType.MANGA)

            // THEN
            result.forAll { list ->
                list.entries.forAll { entry ->
                    with(entry.list) {
                        id shouldBe 100
                        score shouldBe 7.3
                        progress shouldBe 12
                        progressVolumes shouldBe 1
                        repeat shouldBe 2
                        private.shouldBeTrue()
                        notes shouldBe "My notes :)"
                        hiddenFromStatusLists.shouldBeTrue()
                        startedAt.shouldNotBeNull() shouldBeEqualComparingTo LocalDate.of(1999, 12, 23)
                        completedAt.shouldNotBeNull() shouldBeEqualComparingTo LocalDate.of(2009, 5, 5)
                        updatedAt.shouldNotBeNull() shouldBeEqualComparingTo LocalDateTime.of(2022, 4, 12, 19, 0, 0)
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
            }
            shouldThrowExactlyUnit<IllegalStateException> {
                data.mediaList<MediaEntry>(MediaType.UNKNOWN__)
            }
        }

        @Test
        @DisplayName(
            """
            WHEN a collection of mangas with invalid entries
            THEN the media should be type of MediaEntry.Manga
            """,
        )
        fun `a collection of mangas with invalid entries`() = runTest {
            // GIVEN
            val data = MediaListCollectionQuery.Data {
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
                                    updatedAt = null
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

            // WHEN
            val result = data.mediaList<MediaEntry.Manga>(MediaType.MANGA)

            // THEN
            result shouldExist { it.name == "Reading" }
            result.forAll { list ->
                list.entries.forAll { entry ->
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
                        updatedAt.shouldBeNull()
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
            }

            shouldThrowExactlyUnit<IllegalStateException> {
                data.mediaList<MediaEntry>(MediaType.UNKNOWN__)
            }
        }
    }
}
