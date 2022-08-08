package dev.alvr.katana.ui.lists.viewmodel

import androidx.lifecycle.SavedStateHandle
import arrow.core.right
import dev.alvr.katana.common.core.empty
import dev.alvr.katana.common.core.zero
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.lists.models.MediaCollection
import dev.alvr.katana.domain.lists.models.entries.CommonMediaEntry
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.models.lists.MediaList
import dev.alvr.katana.domain.lists.models.lists.MediaListEntry
import dev.alvr.katana.domain.lists.models.lists.MediaListGroup
import dev.alvr.katana.domain.lists.usecases.ObserveAnimeListUseCase
import dev.alvr.katana.domain.lists.usecases.ObserveMangaListUseCase
import dev.alvr.katana.domain.lists.usecases.UpdateListUseCase
import dev.alvr.katana.ui.lists.entities.MediaListItem
import dev.alvr.katana.ui.lists.entities.MediaListItem.Format.OneShot
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.Ordering
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDate
import java.time.LocalDateTime
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flowOf
import org.orbitmvi.orbit.test

internal class ListsViewModelTest : BehaviorSpec({
    val stateHandle = mockk<SavedStateHandle>(relaxed = true)
    val observeAnime = mockk<ObserveAnimeListUseCase>()
    val observeManga = mockk<ObserveMangaListUseCase>()
    val updateList = mockk<UpdateListUseCase>()

    given("a ListsBaseViewModel") {
        val viewModel = ListsViewModel(stateHandle, observeAnime, observeManga, updateList).test(ListsState())

        `when`("initializing the viewModel") {
            and("the collections are empty") {
                every { observeAnime.flow } returns flowOf(MediaCollection(lists = emptyList()))
                every { observeManga.flow } returns flowOf(MediaCollection(lists = emptyList()))
                coJustRun { observeAnime() }
                coJustRun { observeManga() }

                viewModel.runOnCreate()

                then("it should update it's state") {
                    viewModel.assert(ListsState()) {
                        states(
                            { copy(animeList = animeList.copy(isLoading = true)) },
                            { copy(mangaList = mangaList.copy(isLoading = true)) },
                            {
                                copy(
                                    animeList = animeList.copy(
                                        isLoading = false,
                                        isEmpty = true,
                                        items = persistentListOf(),
                                    ),
                                )
                            },
                            {
                                copy(
                                    mangaList = mangaList.copy(
                                        isLoading = false,
                                        isEmpty = true,
                                        items = persistentListOf(),
                                    ),
                                )
                            },
                        )
                    }
                }
            }

            and("the anime collection has entries") {
                every { observeAnime.flow } returns flowOf(
                    MediaCollection(
                        lists = listOf(
                            MediaListGroup(
                                name = "MyCustomAnimeList",
                                entries = listOf(
                                    MediaListEntry(
                                        list = MediaList(
                                            id = Int.zero,
                                            score = Double.zero,
                                            progress = 234,
                                            progressVolumes = Int.zero,
                                            repeat = Int.zero,
                                            private = false,
                                            notes = String.empty,
                                            hiddenFromStatusLists = false,
                                            startedAt = LocalDate.MAX,
                                            completedAt = LocalDate.MAX,
                                            updatedAt = LocalDateTime.MAX,
                                        ),
                                        entry = MediaEntry.Anime(
                                            entry = CommonMediaEntry(
                                                id = Int.zero,
                                                title = String.empty,
                                                coverImage = String.empty,
                                                format = CommonMediaEntry.Format.ONE_SHOT,
                                            ),
                                            episodes = null,
                                            nextEpisode = null,
                                        ),
                                    ),
                                ),
                            ),
                        ),
                    ),
                )
                every { observeManga.flow } returns flowOf(MediaCollection(lists = emptyList()))
                coJustRun { observeAnime() }
                coJustRun { observeManga() }

                viewModel.runOnCreate()

                then("it should update it's state with the mapped entry") {
                    viewModel.assert(ListsState()) {
                        states(
                            { copy(animeList = animeList.copy(isLoading = true)) },
                            { copy(mangaList = mangaList.copy(isLoading = true)) },
                            {
                                copy(
                                    animeList = animeList.copy(
                                        isLoading = false,
                                        isEmpty = false,
                                        name = "MyCustomAnimeList",
                                        items = persistentListOf(
                                            MediaListItem.AnimeListItem(
                                                entryId = Int.zero,
                                                mediaId = Int.zero,
                                                title = String.empty,
                                                score = Double.zero,
                                                format = OneShot,
                                                cover = String.empty,
                                                progress = 234,
                                                total = null,
                                                repeat = Int.zero,
                                                private = false,
                                                notes = String.empty,
                                                hiddenFromStatusLists = false,
                                                nextEpisode = null,
                                                startedAt = LocalDate.MAX,
                                                completedAt = LocalDate.MAX,
                                                updatedAt = LocalDateTime.MAX,
                                            ),
                                        ),
                                    ),
                                )
                            },
                            {
                                copy(
                                    mangaList = mangaList.copy(
                                        isLoading = false,
                                        isEmpty = true,
                                        items = persistentListOf(),
                                    ),
                                )
                            },
                        )
                    }

                    coVerify(exactly = 1) { observeAnime() }
                    verify(exactly = 1) { observeAnime.flow }
                    verify(ordering = Ordering.ORDERED) {
                        stateHandle["animeListNames"] = arrayOf("MyCustomAnimeList")

                        stateHandle["animeCollection"] = mapOf(
                            "MyCustomAnimeList" to listOf(
                                MediaListItem.AnimeListItem(
                                    entryId = Int.zero,
                                    mediaId = Int.zero,
                                    title = String.empty,
                                    score = Double.zero,
                                    format = OneShot,
                                    cover = String.empty,
                                    progress = 234,
                                    total = null,
                                    repeat = Int.zero,
                                    private = false,
                                    notes = String.empty,
                                    hiddenFromStatusLists = false,
                                    nextEpisode = null,
                                    startedAt = LocalDate.MAX,
                                    completedAt = LocalDate.MAX,
                                    updatedAt = LocalDateTime.MAX,
                                ),
                            ),
                        )
                    }
                }
            }

            and("the manga collection has entries") {
                every { observeManga.flow } returns flowOf(
                    MediaCollection(
                        lists = listOf(
                            MediaListGroup(
                                name = "MyCustomMangaList",
                                entries = listOf(
                                    MediaListEntry(
                                        list = MediaList(
                                            id = Int.zero,
                                            score = Double.zero,
                                            progress = 234,
                                            progressVolumes = Int.zero,
                                            repeat = Int.zero,
                                            private = false,
                                            notes = String.empty,
                                            hiddenFromStatusLists = false,
                                            startedAt = LocalDate.MAX,
                                            completedAt = LocalDate.MAX,
                                            updatedAt = LocalDateTime.MAX,
                                        ),
                                        entry = MediaEntry.Manga(
                                            entry = CommonMediaEntry(
                                                id = Int.zero,
                                                title = String.empty,
                                                coverImage = String.empty,
                                                format = CommonMediaEntry.Format.ONE_SHOT,
                                            ),
                                            chapters = null,
                                            volumes = null,
                                        ),
                                    ),
                                ),
                            ),
                        ),
                    ),
                )
                every { observeAnime.flow } returns flowOf(MediaCollection(lists = emptyList()))
                coJustRun { observeAnime() }
                coJustRun { observeManga() }

                viewModel.runOnCreate()

                then("it should update it's state with the mapped entry") {
                    viewModel.assert(ListsState()) {
                        states(
                            { copy(animeList = animeList.copy(isLoading = true)) },
                            { copy(mangaList = mangaList.copy(isLoading = true)) },
                            {
                                copy(
                                    animeList = animeList.copy(
                                        isLoading = false,
                                        isEmpty = true,
                                        items = persistentListOf(),
                                    ),
                                )
                            },
                            {
                                copy(
                                    mangaList = mangaList.copy(
                                        isLoading = false,
                                        isEmpty = false,
                                        name = "MyCustomMangaList",
                                        items = persistentListOf(
                                            MediaListItem.MangaListItem(
                                                entryId = Int.zero,
                                                mediaId = Int.zero,
                                                title = String.empty,
                                                score = Double.zero,
                                                format = OneShot,
                                                cover = String.empty,
                                                progress = 234,
                                                volumesProgress = Int.zero,
                                                total = null,
                                                volumesTotal = null,
                                                repeat = Int.zero,
                                                private = false,
                                                notes = String.empty,
                                                hiddenFromStatusLists = false,
                                                startedAt = LocalDate.MAX,
                                                completedAt = LocalDate.MAX,
                                                updatedAt = LocalDateTime.MAX,
                                            ),
                                        ),
                                    ),
                                )
                            },
                        )
                    }

                    coVerify(exactly = 1) { observeManga() }
                    verify(exactly = 1) { observeManga.flow }
                    verify(ordering = Ordering.ORDERED) {
                        stateHandle["mangaListNames"] = arrayOf("MyCustomMangaList")

                        stateHandle["mangaCollection"] = mapOf(
                            "MyCustomMangaList" to listOf(
                                MediaListItem.MangaListItem(
                                    entryId = Int.zero,
                                    mediaId = Int.zero,
                                    title = String.empty,
                                    score = Double.zero,
                                    format = OneShot,
                                    cover = String.empty,
                                    progress = 234,
                                    volumesProgress = Int.zero,
                                    total = null,
                                    volumesTotal = null,
                                    repeat = Int.zero,
                                    private = false,
                                    notes = String.empty,
                                    hiddenFromStatusLists = false,
                                    startedAt = LocalDate.MAX,
                                    completedAt = LocalDate.MAX,
                                    updatedAt = LocalDateTime.MAX,
                                ),
                            ),
                        )
                    }
                }
            }
        }

        `when`("adding a +1 to an entry") {
            coEvery { updateList(any()) } returns Unit.right()

            and("is an AnimeListItem") {
                then("it should call the updateList with the progress incremented by 1") {
                    viewModel.testIntent {
                        addPlusOne(
                            MediaListItem.AnimeListItem(
                                entryId = Int.zero,
                                mediaId = Int.zero,
                                title = String.empty,
                                score = Double.zero,
                                format = OneShot,
                                cover = String.empty,
                                progress = 233,
                                total = Int.zero,
                                repeat = Int.zero,
                                private = false,
                                notes = String.empty,
                                hiddenFromStatusLists = false,
                                startedAt = LocalDate.MAX,
                                completedAt = LocalDate.MAX,
                                updatedAt = LocalDateTime.MAX,
                                nextEpisode = null,
                            ),
                        )
                    }

                    coVerify(exactly = 1) {
                        updateList(
                            MediaList(
                                id = Int.zero,
                                score = Double.zero,
                                progress = 234,
                                progressVolumes = null,
                                repeat = Int.zero,
                                private = false,
                                notes = String.empty,
                                hiddenFromStatusLists = false,
                                startedAt = LocalDate.MAX,
                                completedAt = LocalDate.MAX,
                                updatedAt = LocalDateTime.MAX,
                            ),
                        )
                    }
                }
            }

            and("is a MangaListItem") {
                then("it should call the updateList with the progress incremented by 1") {
                    viewModel.testIntent {
                        addPlusOne(
                            MediaListItem.MangaListItem(
                                entryId = Int.zero,
                                mediaId = Int.zero,
                                title = String.empty,
                                score = Double.zero,
                                format = OneShot,
                                cover = String.empty,
                                progress = 233,
                                total = Int.zero,
                                volumesProgress = Int.zero,
                                volumesTotal = Int.zero,
                                repeat = Int.zero,
                                private = false,
                                notes = String.empty,
                                hiddenFromStatusLists = false,
                                startedAt = LocalDate.MAX,
                                completedAt = LocalDate.MAX,
                                updatedAt = LocalDateTime.MAX,
                            ),
                        )
                    }

                    coVerify(exactly = 1) {
                        updateList(
                            MediaList(
                                id = Int.zero,
                                score = Double.zero,
                                progress = 234,
                                progressVolumes = Int.zero,
                                repeat = Int.zero,
                                private = false,
                                notes = String.empty,
                                hiddenFromStatusLists = false,
                                startedAt = LocalDate.MAX,
                                completedAt = LocalDate.MAX,
                                updatedAt = LocalDateTime.MAX,
                            ),
                        )
                    }
                }
            }
        }
    }
},)
