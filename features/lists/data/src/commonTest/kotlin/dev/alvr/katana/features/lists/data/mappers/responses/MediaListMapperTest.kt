package dev.alvr.katana.features.lists.data.mappers.responses

import dev.alvr.katana.core.common.empty
import dev.alvr.katana.core.common.zero
import dev.alvr.katana.core.remote.type.MediaFormat
import dev.alvr.katana.core.remote.type.MediaType
import dev.alvr.katana.core.remote.type.buildAiringSchedule
import dev.alvr.katana.core.remote.type.buildFuzzyDate
import dev.alvr.katana.core.remote.type.buildMedia
import dev.alvr.katana.core.remote.type.buildMediaCoverImage
import dev.alvr.katana.core.remote.type.buildMediaList
import dev.alvr.katana.core.remote.type.buildMediaListCollection
import dev.alvr.katana.core.remote.type.buildMediaListGroup
import dev.alvr.katana.core.remote.type.buildMediaListOptions
import dev.alvr.katana.core.remote.type.buildMediaListTypeOptions
import dev.alvr.katana.core.remote.type.buildMediaTitle
import dev.alvr.katana.core.remote.type.buildUser
import dev.alvr.katana.features.lists.data.MediaListCollectionQuery
import dev.alvr.katana.features.lists.domain.models.entries.CommonMediaEntry
import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import io.kotest.core.spec.style.FreeSpec
import io.kotest.inspectors.forAll
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
import korlibs.time.Date
import korlibs.time.DateTime
import korlibs.time.DateTimeTz
import korlibs.time.TimezoneOffset

@Suppress("LargeClass")
internal class MediaListMapperTest : FreeSpec({
    "a null response from server" {
        val data: MediaListCollectionQuery.Data? = null
        data?.invoke<MediaEntry.Anime>(MediaType.ANIME).shouldBeNull()
        data?.invoke<MediaEntry.Manga>(MediaType.MANGA).shouldBeNull()
    }

    "a collection without lists" {
        MediaListCollectionQuery.Data {
            this["MediaListCollection"] = buildMediaListCollection {
                lists = emptyList()
                user = buildUser { }
            }
        }.run {
            this<MediaEntry.Anime>(MediaType.ANIME).shouldBeEmpty()
            this<MediaEntry.Manga>(MediaType.MANGA).shouldBeEmpty()
        }
    }

    "a list without name" {
        MediaListCollectionQuery.Data {
            this["MediaListCollection"] = buildMediaListCollection {
                lists = listOf(
                    buildMediaListGroup {
                        name = String.empty
                        entries = emptyList()
                    },
                )
                user = buildUser {
                    mediaListOptions = buildMediaListOptions {
                        animeList = buildMediaListTypeOptions {
                            sectionOrder = emptyList()
                        }
                        mangaList = buildMediaListTypeOptions {
                            sectionOrder = emptyList()
                        }
                    }
                }
            }
        }.run {
            this<MediaEntry.Anime>(MediaType.ANIME).forAll { list -> list.name.shouldBeEmpty() }
            this<MediaEntry.Manga>(MediaType.MANGA).forAll { list -> list.name.shouldBeEmpty() }
        }
    }

    "anime" - {
        "a collection of animes without entries" {
            MediaListCollectionQuery.Data {
                this["MediaListCollection"] = buildMediaListCollection {
                    lists = listOf(
                        buildMediaListGroup {
                            name = "Rewatching"
                            entries = emptyList()
                        },
                        buildMediaListGroup {
                            name = "Watching"
                            entries = listOf(null)
                        },
                        buildMediaListGroup {
                            name = "Paused"
                            entries = emptyList()
                        },
                        buildMediaListGroup {
                            name = "Completed TV"
                            entries = emptyList()
                        },
                    )
                    user = buildUser {
                        mediaListOptions = buildMediaListOptions {
                            animeList = buildMediaListTypeOptions {
                                sectionOrder =
                                    listOf("Watching", "Rewatching", "Completed TV", "Paused")
                            }
                        }
                    }
                }
            }.invoke<MediaEntry.Anime>(MediaType.ANIME).also { list ->
                list[0].name shouldBe "Watching"
                list[1].name shouldBe "Rewatching"
                list[2].name shouldBe "Completed TV"
                list[3].name shouldBe "Paused"
            }.shouldHaveSize(4)
        }

        "a collection of animes without entries and sectionOrder" {
            MediaListCollectionQuery.Data {
                this["MediaListCollection"] = buildMediaListCollection {
                    lists = listOf(
                        buildMediaListGroup {
                            name = "Rewatching"
                            entries = emptyList()
                        },
                        buildMediaListGroup {
                            name = "Watching"
                            entries = listOf(null)
                        },
                        buildMediaListGroup {
                            name = "Paused"
                            entries = emptyList()
                        },
                        buildMediaListGroup {
                            name = "Completed TV"
                            entries = emptyList()
                        },
                    )
                    user = buildUser {
                        mediaListOptions = buildMediaListOptions {
                            animeList = buildMediaListTypeOptions {
                                sectionOrder = emptyList()
                            }
                        }
                    }
                }
            }.invoke<MediaEntry.Anime>(MediaType.ANIME).also { list ->
                list[0].name shouldBe "Rewatching"
                list[1].name shouldBe "Watching"
                list[2].name shouldBe "Paused"
                list[3].name shouldBe "Completed TV"
            }.shouldHaveSize(4)
        }

        "a collection of animes without entries and mediaListOptions" {
            MediaListCollectionQuery.Data {
                this["MediaListCollection"] = buildMediaListCollection {
                    lists = listOf(
                        buildMediaListGroup {
                            name = "Rewatching"
                            entries = emptyList()
                        },
                        buildMediaListGroup {
                            name = "Watching"
                            entries = listOf(null)
                        },
                        buildMediaListGroup {
                            name = "Paused"
                            entries = emptyList()
                        },
                        buildMediaListGroup {
                            name = "Completed TV"
                            entries = emptyList()
                        },
                    )
                    user = buildUser { mediaListOptions = buildMediaListOptions { } }
                }
            }.invoke<MediaEntry.Anime>(MediaType.ANIME).also { list ->
                list[0].name shouldBe "Rewatching"
                list[1].name shouldBe "Watching"
                list[2].name shouldBe "Paused"
                list[3].name shouldBe "Completed TV"
            }.shouldHaveSize(4)
        }

        "a collection of animes without entries and animeListSorting" {
            MediaListCollectionQuery.Data {
                this["MediaListCollection"] = buildMediaListCollection {
                    lists = listOf(
                        buildMediaListGroup {
                            name = "Rewatching"
                            entries = emptyList()
                        },
                        buildMediaListGroup {
                            name = "Watching"
                            entries = listOf(null)
                        },
                        buildMediaListGroup {
                            name = "Paused"
                            entries = emptyList()
                        },
                        buildMediaListGroup {
                            name = "Completed TV"
                            entries = emptyList()
                        },
                    )
                    user = buildUser {
                        mediaListOptions = buildMediaListOptions {
                            animeList = buildMediaListTypeOptions { }
                        }
                    }
                }
            }.invoke<MediaEntry.Anime>(MediaType.ANIME).also { list ->
                list[0].name shouldBe "Rewatching"
                list[1].name shouldBe "Watching"
                list[2].name shouldBe "Paused"
                list[3].name shouldBe "Completed TV"
            }.shouldHaveSize(4)
        }

        "a collection of animes with valid entries" {
            MediaListCollectionQuery.Data {
                this["MediaListCollection"] = buildMediaListCollection {
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
                                    updatedAt = 1_649_790_000
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
            }.invoke<MediaEntry.Anime>(MediaType.ANIME).forAll { list ->
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
                        startedAt.shouldNotBeNull() shouldBeEqualComparingTo Date(1999, 12, 23)
                        completedAt.shouldNotBeNull() shouldBeEqualComparingTo Date(2009, 5, 5)
                        updatedAt.shouldNotBeNull() shouldBeEqualComparingTo DateTimeTz.local(
                            DateTime(2022, 4, 12, 19, 0, 0),
                            TimezoneOffset.UTC,
                        )
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
                            at shouldBeEqualComparingTo DateTimeTz.local(
                                DateTime(2022, 4, 12, 19, 0, 0),
                                TimezoneOffset.UTC,
                            )
                            number shouldBe 13
                        }
                    }
                }
            }
        }

        "a collection of animes with invalid entries" {
            MediaListCollectionQuery.Data {
                this["MediaListCollection"] = buildMediaListCollection {
                    lists = listOf(
                        buildMediaListGroup {
                            name = "Watching"
                            entries = listOf(
                                buildMediaList {
                                    id = Int.zero
                                    score = Double.zero
                                    progress = null
                                    progressVolumes = null
                                    repeat = null
                                    private = null
                                    notes = null
                                    hiddenFromStatusLists = null
                                    startedAt = null
                                    completedAt = null
                                    updatedAt = null
                                    media = buildMedia {
                                        id = Int.zero
                                        title = buildMediaTitle { userPreferred = String.empty }
                                        episodes = null
                                        format = null
                                        coverImage = buildMediaCoverImage { large = String.empty }
                                        nextAiringEpisode = null
                                    }
                                },
                            )
                        },
                    )
                }
            }.invoke<MediaEntry.Anime>(MediaType.ANIME).forAll { list ->
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
        }
    }

    "manga" - {
        "a collection of mangas without entries" {
            MediaListCollectionQuery.Data {
                this["MediaListCollection"] = buildMediaListCollection {
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
                            name = "Paused"
                            entries = listOf(null)
                        },
                        buildMediaListGroup {
                            name = "Completed Novel"
                            entries = emptyList()
                        },
                    )
                    user = buildUser {
                        mediaListOptions = buildMediaListOptions {
                            mangaList = buildMediaListTypeOptions {
                                sectionOrder = listOf("Reading", "Rereading", "Completed Novel", "Paused")
                            }
                        }
                    }
                }
            }.invoke<MediaEntry.Manga>(MediaType.MANGA).also { list ->
                list[0].name shouldBe "Reading"
                list[1].name shouldBe "Rereading"
                list[2].name shouldBe "Completed Novel"
                list[3].name shouldBe "Paused"
            }.shouldHaveSize(4)
        }

        "a collection of mangas without entries and sectionOrder" {
            MediaListCollectionQuery.Data {
                this["MediaListCollection"] = buildMediaListCollection {
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
                            name = "Paused"
                            entries = listOf(null)
                        },
                        buildMediaListGroup {
                            name = "Completed Novel"
                            entries = emptyList()
                        },
                    )
                    user = buildUser {
                        mediaListOptions = buildMediaListOptions {
                            mangaList = buildMediaListTypeOptions {
                                sectionOrder = emptyList()
                            }
                        }
                    }
                }
            }.invoke<MediaEntry.Manga>(MediaType.MANGA).also { list ->
                list[0].name shouldBe "Rereading"
                list[1].name shouldBe "Reading"
                list[2].name shouldBe "Paused"
                list[3].name shouldBe "Completed Novel"
            }.shouldHaveSize(4)
        }

        "a collection of mangas without entries and mediaListOptions" {
            MediaListCollectionQuery.Data {
                this["MediaListCollection"] = buildMediaListCollection {
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
                            name = "Paused"
                            entries = listOf(null)
                        },
                        buildMediaListGroup {
                            name = "Completed Novel"
                            entries = emptyList()
                        },
                    )
                    user = buildUser { mediaListOptions = buildMediaListOptions { } }
                }
            }.invoke<MediaEntry.Manga>(MediaType.MANGA).also { list ->
                list[0].name shouldBe "Rereading"
                list[1].name shouldBe "Reading"
                list[2].name shouldBe "Paused"
                list[3].name shouldBe "Completed Novel"
            }.shouldHaveSize(4)
        }

        "a collection of mangas without entries and mangaListSorting" {
            MediaListCollectionQuery.Data {
                this["MediaListCollection"] = buildMediaListCollection {
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
                            name = "Paused"
                            entries = listOf(null)
                        },
                        buildMediaListGroup {
                            name = "Completed Novel"
                            entries = emptyList()
                        },
                    )
                    user = buildUser {
                        mediaListOptions = buildMediaListOptions {
                            mangaList = buildMediaListTypeOptions { }
                        }
                    }
                }
            }.invoke<MediaEntry.Manga>(MediaType.MANGA).also { list ->
                list[0].name shouldBe "Rereading"
                list[1].name shouldBe "Reading"
                list[2].name shouldBe "Paused"
                list[3].name shouldBe "Completed Novel"
            }.shouldHaveSize(4)
        }

        "a collection of mangas with valid entries" {
            MediaListCollectionQuery.Data {
                this["MediaListCollection"] = buildMediaListCollection {
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
                                    updatedAt = 1_649_790_000
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
            }.invoke<MediaEntry.Manga>(MediaType.MANGA).forAll { list ->
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
                        startedAt.shouldNotBeNull() shouldBeEqualComparingTo Date(1999, 12, 23)
                        completedAt.shouldNotBeNull() shouldBeEqualComparingTo Date(2009, 5, 5)
                        updatedAt.shouldNotBeNull() shouldBeEqualComparingTo DateTimeTz.local(
                            DateTime(2022, 4, 12, 19, 0, 0),
                            TimezoneOffset.UTC,
                        )
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
        }

        "a collection of mangas with invalid entries" {
            MediaListCollectionQuery.Data {
                this["MediaListCollection"] = buildMediaListCollection {
                    lists = listOf(
                        buildMediaListGroup {
                            name = "Reading"
                            entries = listOf(
                                buildMediaList {
                                    id = Int.zero
                                    score = Double.zero
                                    progress = null
                                    progressVolumes = null
                                    repeat = null
                                    private = null
                                    notes = null
                                    hiddenFromStatusLists = null
                                    startedAt = null
                                    completedAt = null
                                    updatedAt = null
                                    media = buildMedia {
                                        id = Int.zero
                                        title = buildMediaTitle { userPreferred = String.empty }
                                        chapters = null
                                        volumes = null
                                        format = null
                                        coverImage = buildMediaCoverImage { large = String.empty }
                                        nextAiringEpisode = null
                                    }
                                },
                            )
                        },
                    )
                }
            }.invoke<MediaEntry.Manga>(MediaType.MANGA).forAll { list ->
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
        }
    }
})
