package dev.alvr.katana.data.remote.lists.mappers

import com.apollographql.apollo3.annotations.ApolloExperimental
import dev.alvr.katana.data.remote.lists.MediaListCollectionQuery
import dev.alvr.katana.data.remote.lists.mappers.responses.mediaList
import dev.alvr.katana.data.remote.lists.test.MediaListCollectionQuery_TestBuilder.Data
import dev.alvr.katana.domain.lists.models.entries.CommonMediaEntry
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.models.lists.MediaList
import io.kotest.assertions.throwables.shouldThrowExactlyUnit
import io.kotest.core.spec.style.WordSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldBeSortedWith
import io.kotest.matchers.collections.shouldContainAll
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
                        name = ""
                        entries = emptyList()
                    },
                    null
                )
            }
        }

        "return a CUSTOM list without name" {
            data.mediaList<MediaEntry.Anime>().forAll { list ->
                list.name.shouldBeEmpty()
                list.listType shouldBe MediaList.ListType.CUSTOM
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
            }
        }

        "return a sorted media list" {
            data.mediaList<MediaEntry.Anime>()
                .shouldBeSortedWith { m1, m2 -> m1.listType.compareTo(m2.listType) }
                .also { list ->
                    list[0].listType shouldBe MediaList.ListType.WATCHING
                    list[1].listType shouldBe MediaList.ListType.REWATCHING
                    list[2].listType shouldBe MediaList.ListType.COMPLETED_TV
                    list[3].listType shouldBe MediaList.ListType.PAUSED
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
            }
        }

        "return a sorted media list" {
            data.mediaList<MediaEntry.Anime>()
                .shouldBeSortedWith { m1, m2 -> m1.listType.compareTo(m2.listType) }
                .also { list ->
                    list[0].listType shouldBe MediaList.ListType.READING
                    list[1].listType shouldBe MediaList.ListType.REREADING
                    list[2].listType shouldBe MediaList.ListType.COMPLETED_NOVEL
                    list[3].listType shouldBe MediaList.ListType.PAUSED
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
                                priority = 2
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
                                    duration = 24
                                    format = "TV"
                                    coverImage = coverImage {
                                        large = "https://www.fillmurray.com/128/256"
                                    }
                                    updatedAt = 1_649_790_000
                                    genres = listOf("Action", "Adventure")
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
                        priority shouldBe 2
                        private.shouldBeTrue()
                        notes shouldBe "My notes :)"
                        hiddenFromStatusLists.shouldBeTrue()
                        startedAt?.shouldBeEqualComparingTo(LocalDate.of(1999, 12, 23))
                        completedAt?.shouldBeEqualComparingTo(LocalDate.of(2009, 5, 5))
                        with(media) {
                            shouldBeTypeOf<MediaEntry.Anime>()
                            shouldNotBeTypeOf<MediaEntry>()
                            shouldNotBeTypeOf<MediaEntry.Manga>()

                            id shouldBe 100
                            title shouldBe "My anime entry"
                            coverImage shouldBe "https://www.fillmurray.com/128/256"
                            format shouldBe CommonMediaEntry.Format.TV
                            genres.shouldContainAll("Action", "Adventure")
                            episodes shouldBe 23
                            updatedAt.shouldNotBeNull()
                                .shouldBeEqualComparingTo(LocalDateTime.of(2022, 4, 12, 21, 0, 0))
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
            shouldThrowExactlyUnit<IllegalStateException> {
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
                                priority = 2
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
                                    updatedAt = 1_649_790_000
                                    genres = listOf("Action", "Adventure")
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
                        priority shouldBe 2
                        private.shouldBeTrue()
                        notes shouldBe "My notes :)"
                        hiddenFromStatusLists.shouldBeTrue()
                        startedAt?.shouldBeEqualComparingTo(LocalDate.of(1999, 12, 23))
                        completedAt?.shouldBeEqualComparingTo(LocalDate.of(2009, 5, 5))
                        with(media) {
                            shouldBeTypeOf<MediaEntry.Manga>()
                            shouldNotBeTypeOf<MediaEntry>()
                            shouldNotBeTypeOf<MediaEntry.Anime>()

                            id shouldBe 100
                            title shouldBe "My manga entry"
                            coverImage shouldBe "https://www.fillmurray.com/128/256"
                            format shouldBe CommonMediaEntry.Format.NOVEL
                            genres.shouldContainAll("Action", "Adventure")
                            updatedAt.shouldNotBeNull()
                                .shouldBeEqualComparingTo(LocalDateTime.of(2022, 4, 12, 21, 0, 0))
                            chapters shouldBe 23
                            volumes shouldBe 2
                        }
                    }
                }
            }
        }

        "throw an exception when using wrong MediaEntry" {
            shouldThrowExactlyUnit<IllegalStateException> {
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
                                id = 0
                                score = null
                                progress = null
                                progressVolumes = null
                                repeat = null
                                priority = null
                                private = null
                                notes = null
                                hiddenFromStatusLists = null
                                startedAt = null
                                completedAt = null
                                media = media {
                                    id = 0
                                    title = null
                                    episodes = null
                                    duration = null
                                    format = null
                                    coverImage = null
                                    genres = null
                                    updatedAt = null
                                    nextAiringEpisode = null
                                }
                            },
                        )
                    },
                )
            }
        }

        "return a sorted media list" {
            data.mediaList<MediaEntry.Anime>()
                .shouldExist { it.listType == MediaList.ListType.WATCHING }
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
                        priority shouldBe 0
                        private.shouldBeFalse()
                        notes.shouldBeEmpty()
                        hiddenFromStatusLists.shouldBeFalse()
                        startedAt.shouldBeNull()
                        completedAt.shouldBeNull()
                        with(media) {
                            shouldBeTypeOf<MediaEntry.Anime>()
                            shouldNotBeTypeOf<MediaEntry>()
                            shouldNotBeTypeOf<MediaEntry.Manga>()

                            id shouldBe 0
                            title.shouldBeEmpty()
                            coverImage.shouldBeEmpty()
                            format shouldBe CommonMediaEntry.Format.UNKNOWN
                            genres.shouldBeEmpty()
                            updatedAt.shouldBeNull()
                            episodes.shouldBeNull()
                            nextEpisode.shouldBeNull()
                        }
                    }
                }
            }
        }

        "throw an exception when using wrong MediaEntry" {
            shouldThrowExactlyUnit<IllegalStateException> {
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
                                id = 0
                                score = null
                                progress = null
                                progressVolumes = null
                                repeat = null
                                priority = null
                                private = null
                                notes = null
                                hiddenFromStatusLists = null
                                startedAt = null
                                completedAt = null
                                media = media {
                                    id = 0
                                    title = null
                                    chapters = null
                                    volumes = null
                                    format = null
                                    coverImage = null
                                    genres = null
                                    updatedAt = null
                                    nextAiringEpisode = null
                                }
                            },
                        )
                    },
                )
            }
        }

        "return a sorted media list" {
            data.mediaList<MediaEntry.Manga>()
                .shouldExist { it.listType == MediaList.ListType.READING }
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
                        priority shouldBe 0
                        private.shouldBeFalse()
                        notes.shouldBeEmpty()
                        hiddenFromStatusLists.shouldBeFalse()
                        startedAt.shouldBeNull()
                        completedAt.shouldBeNull()
                        with(media) {
                            shouldBeTypeOf<MediaEntry.Manga>()
                            shouldNotBeTypeOf<MediaEntry>()
                            shouldNotBeTypeOf<MediaEntry.Anime>()

                            id shouldBe 0
                            title.shouldBeEmpty()
                            coverImage.shouldBeEmpty()
                            format shouldBe CommonMediaEntry.Format.UNKNOWN
                            genres.shouldBeEmpty()
                            updatedAt.shouldBeNull()
                            chapters.shouldBeNull()
                            volumes.shouldBeNull()
                        }
                    }
                }
            }
        }

        "throw an exception when using wrong MediaEntry" {
            shouldThrowExactlyUnit<IllegalStateException> {
                data.mediaList<MediaEntry>()
            }
        }
    }
})
