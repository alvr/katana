package dev.alvr.katana.data.remote.lists.mappers

import com.apollographql.apollo3.annotations.ApolloExperimental
import dev.alvr.katana.common.core.empty
import dev.alvr.katana.common.core.zero
import dev.alvr.katana.data.remote.lists.MediaListCollectionQuery
import dev.alvr.katana.data.remote.lists.mappers.responses.mediaList
import dev.alvr.katana.data.remote.lists.test.MediaListCollectionQuery_TestBuilder.Data
import dev.alvr.katana.domain.lists.models.entries.CommonMediaEntry
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.WordSpec
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

@OptIn(ApolloExperimental::class)
internal class MediaListMapperTest : WordSpec({
    "a null response from server" should {
        val data: MediaListCollectionQuery.Data? = null

        "return a null anime medialist" {
            data?.mediaList<MediaEntry.Anime>().shouldBeNull()
        }

        "return a null manga medialist" {
            data?.mediaList<MediaEntry.Manga>().shouldBeNull()
        }
    }

    "a collection without lists" should {
        val data = MediaListCollectionQuery.Data {
            collection = collection {
                lists = emptyList()
            }
        }

        "return an empty list of animes" {
            data.mediaList<MediaEntry.Anime>().shouldBeEmpty()
        }

        "return an empty list of mangas" {
            data.mediaList<MediaEntry.Manga>().shouldBeEmpty()
        }
    }

    "a list without name" should {
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
                    }
                }
            }
        }

        "return a CUSTOM list without name" {
            data.mediaList<MediaEntry.Anime>().forAll { list ->
                list.name.shouldBeEmpty()
            }
        }
    }

    "a collection of animes without entries" should {
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

        "return a sorted media list" {
            data.mediaList<MediaEntry.Anime>()
                .also { list ->
                    list[0].name shouldBe "Watching"
                    list[1].name shouldBe "Rewatching"
                    list[2].name shouldBe "Completed TV"
                    list[3].name shouldBe "Paused"
                }
                .shouldHaveSize(4)
        }
    }

    "a collection of mangas without entries" should {
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

        "return a sorted media list" {
            data.mediaList<MediaEntry.Manga>()
                .also { list ->
                    list[0].name shouldBe "Reading"
                    list[1].name shouldBe "Rereading"
                    list[2].name shouldBe "Completed Novel"
                    list[3].name shouldBe "Paused"
                }
                .shouldHaveSize(4)
        }
    }

    "a collection of animes with valid entries" should {
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

        "the media should be type of MediaEntry.Anime" {
            data.mediaList<MediaEntry.Anime>().forAll { list ->
                list.entries.forAll { entry ->
                    with(entry) {
                        id shouldBe 100
                        score shouldBe 7.3
                        progress shouldBe 12
                        progressVolumes.shouldBeNull()
                        repeat shouldBe 2
                        private.shouldBeTrue()
                        notes shouldBe "My notes :)"
                        hiddenFromStatusLists.shouldBeTrue()
                        startedAt.shouldNotBeNull().shouldBeEqualComparingTo(LocalDate.of(1999, 12, 23))
                        completedAt.shouldNotBeNull().shouldBeEqualComparingTo(LocalDate.of(2009, 5, 5))
                        updatedAt.shouldNotBeNull().shouldBeEqualComparingTo(LocalDateTime.of(2022, 4, 12, 21, 0, 0))
                        with(media) {
                            shouldBeTypeOf<MediaEntry.Anime>()
                            shouldNotBeTypeOf<MediaEntry>()
                            shouldNotBeTypeOf<MediaEntry.Manga>()

                            id shouldBe 100
                            title shouldBe "My anime entry"
                            coverImage shouldBe "https://www.fillmurray.com/128/256"
                            format shouldBe CommonMediaEntry.Format.TV
                            episodes shouldBe 23
                            with(nextEpisode.shouldNotBeNull()) {
                                at.shouldBeEqualComparingTo(LocalDateTime.of(2022, 4, 12, 21, 0, 0))
                                number shouldBe 13
                            }
                        }
                    }
                }
            }
        }

        "throw an exception when using wrong MediaEntry" {
            shouldThrowExactly<IllegalStateException> {
                data.mediaList<MediaEntry>()
            }
        }
    }

    "a collection of mangas with valid entries" should {
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

        "the media should be type of MediaEntry.Manga" {
            data.mediaList<MediaEntry.Manga>().forAll { list ->
                list.entries.forAll { entry ->
                    with(entry) {
                        id shouldBe 100
                        score shouldBe 7.3
                        progress shouldBe 12
                        progressVolumes shouldBe 1
                        repeat shouldBe 2
                        private.shouldBeTrue()
                        notes shouldBe "My notes :)"
                        hiddenFromStatusLists.shouldBeTrue()
                        startedAt.shouldNotBeNull().shouldBeEqualComparingTo(LocalDate.of(1999, 12, 23))
                        completedAt.shouldNotBeNull().shouldBeEqualComparingTo(LocalDate.of(2009, 5, 5))
                        updatedAt.shouldNotBeNull().shouldBeEqualComparingTo(LocalDateTime.of(2022, 4, 12, 21, 0, 0))
                        with(media) {
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
                }
            }
        }

        "throw an exception when using wrong MediaEntry" {
            shouldThrowExactly<IllegalStateException> {
                data.mediaList<MediaEntry>()
            }
        }
    }

    "a collection of animes with invalid entries" should {
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

        "return a sorted media list" {
            data.mediaList<MediaEntry.Anime>().shouldExist { it.name == "Watching" }
        }

        "the media should be type of MediaEntry.Anime" {
            data.mediaList<MediaEntry.Anime>().forAll { list ->
                list.entries.forAll { entry ->
                    with(entry) {
                        id shouldBe 0
                        score shouldBe 0.0
                        progress shouldBe 0
                        progressVolumes.shouldBeNull()
                        repeat shouldBe 0
                        private.shouldBeFalse()
                        notes.shouldBeEmpty()
                        hiddenFromStatusLists.shouldBeFalse()
                        startedAt.shouldBeNull()
                        completedAt.shouldBeNull()
                        updatedAt.shouldBeNull()
                        with(media) {
                            shouldBeTypeOf<MediaEntry.Anime>()
                            shouldNotBeTypeOf<MediaEntry>()
                            shouldNotBeTypeOf<MediaEntry.Manga>()

                            id shouldBe 0
                            title.shouldBeEmpty()
                            coverImage.shouldBeEmpty()
                            format shouldBe CommonMediaEntry.Format.UNKNOWN
                            episodes.shouldBeNull()
                            nextEpisode.shouldBeNull()
                        }
                    }
                }
            }
        }

        "throw an exception when using wrong MediaEntry" {
            shouldThrowExactly<IllegalStateException> {
                data.mediaList<MediaEntry>()
            }
        }
    }

    "a collection of mangas with invalid entries" should {
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

        "return a sorted media list" {
            data.mediaList<MediaEntry.Manga>().shouldExist { it.name == "Reading" }
        }

        "the media should be type of MediaEntry.Manga" {
            data.mediaList<MediaEntry.Manga>().forAll { list ->
                list.entries.forAll { entry ->
                    with(entry) {
                        id shouldBe 0
                        score shouldBe 0.0
                        progress shouldBe 0
                        progressVolumes.shouldBeNull()
                        repeat shouldBe 0
                        private.shouldBeFalse()
                        notes.shouldBeEmpty()
                        hiddenFromStatusLists.shouldBeFalse()
                        startedAt.shouldBeNull()
                        completedAt.shouldBeNull()
                        updatedAt.shouldBeNull()
                        with(media) {
                            shouldBeTypeOf<MediaEntry.Manga>()
                            shouldNotBeTypeOf<MediaEntry>()
                            shouldNotBeTypeOf<MediaEntry.Anime>()

                            id shouldBe 0
                            title.shouldBeEmpty()
                            coverImage.shouldBeEmpty()
                            format shouldBe CommonMediaEntry.Format.UNKNOWN
                            chapters.shouldBeNull()
                            volumes.shouldBeNull()
                        }
                    }
                }
            }
        }

        "throw an exception when using wrong MediaEntry" {
            shouldThrowExactly<IllegalStateException> {
                data.mediaList<MediaEntry>()
            }
        }
    }
},)
