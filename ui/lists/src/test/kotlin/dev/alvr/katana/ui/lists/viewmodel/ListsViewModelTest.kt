package dev.alvr.katana.ui.lists.viewmodel

import androidx.lifecycle.SavedStateHandle
import arrow.core.right
import dev.alvr.katana.common.core.empty
import dev.alvr.katana.common.core.zero
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.lists.models.MediaCollection
import dev.alvr.katana.domain.lists.models.lists.MediaList
import dev.alvr.katana.domain.lists.models.lists.MediaListGroup
import dev.alvr.katana.domain.lists.usecases.ObserveAnimeListUseCase
import dev.alvr.katana.domain.lists.usecases.ObserveMangaListUseCase
import dev.alvr.katana.domain.lists.usecases.UpdateListUseCase
import dev.alvr.katana.ui.lists.entities.MediaListItem
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.mockk.Ordering
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import java.time.LocalDate
import java.time.LocalDateTime
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flowOf
import org.orbitmvi.orbit.test

internal class ListsViewModelTest : BehaviorSpec() {
    private val stateHandle = spyk(SavedStateHandle())
    private val observeAnime = mockk<ObserveAnimeListUseCase>()
    private val observeManga = mockk<ObserveMangaListUseCase>()
    private val updateList = mockk<UpdateListUseCase>()

    init {
        given("a ListsBaseViewModel") {
            val viewModel = ListsViewModel(stateHandle, observeAnime, observeManga, updateList).test(ListsState())

            `when`("initializing the viewModel") {
                and("the collections are empty") {
                    val initialState: Array<ListsState.() -> ListsState> = arrayOf(
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

                    every { observeAnime.flow } returns flowOf(MediaCollection(lists = emptyList()))
                    every { observeManga.flow } returns flowOf(MediaCollection(lists = emptyList()))
                    coJustRun { observeAnime() }
                    coJustRun { observeManga() }

                    viewModel.runOnCreate()

                    then("it should update its state") {
                        viewModel.assert(ListsState()) {
                            states(*initialState)
                        }
                    }

                    and("try to fetch a non-existent anime list") {
                        viewModel.testIntent { selectAnimeList("NonExistent Anime List") }

                        then("the state should be the same") {
                            viewModel.assert(ListsState()) {
                                states(
                                    *initialState + emptyArray(), // No more state updates
                                )
                            }

                            verify(exactly = 2) {
                                stateHandle.get<Collection<MediaListItem.AnimeListItem>>("animeCollection")
                            }
                        }
                    }

                    and("try to fetch a non-existent manga list") {
                        viewModel.testIntent { selectMangaList("NonExistent Manga List") }

                        then("the state should be the same") {
                            viewModel.assert(ListsState()) {
                                states(
                                    *initialState + emptyArray(), // No more state updates
                                )
                            }

                            verify(exactly = 2) {
                                stateHandle.get<Collection<MediaListItem.MangaListItem>>("mangaCollection")
                            }
                        }
                    }

                    and("getting the animeListNames") {
                        then("the list should be empty") {
                            viewModel.testIntent { animeListNames.shouldBeEmpty() }

                            verify(exactly = 1) {
                                stateHandle.get<Array<String>>("animeListNames")
                            }
                        }
                    }

                    and("getting the mangaListNames") {
                        then("the list should be empty") {
                            viewModel.testIntent { mangaListNames.shouldBeEmpty() }

                            verify(exactly = 1) {
                                stateHandle.get<Array<String>>("mangaListNames")
                            }
                        }
                    }

                    and("the collection of anime is non-existent") {
                        every { stateHandle.get<Collection<MediaListItem.AnimeListItem>>(any()) } returns null

                        then("the state should be the same") {
                            viewModel.testIntent { selectAnimeList("MyCustomAnimeList") }

                            viewModel.assert(ListsState()) {
                                states(*initialState)
                            }
                        }
                    }

                    and("the collection of manga is non-existent") {
                        every { stateHandle.get<Collection<MediaListItem.MangaListItem>>(any()) } returns null

                        then("the state should be the same") {
                            viewModel.testIntent { selectMangaList("MyCustomMangaList") }

                            viewModel.assert(ListsState()) {
                                states(*initialState)
                            }
                        }
                    }

                    and("the array of anime list names is non-existent") {
                        every { stateHandle.get<Array<String>>("animeListNames") } returns null

                        then("the array should be empty") {
                            viewModel.testIntent { animeListNames.shouldBeEmpty() }
                        }
                    }

                    and("the array of manga list names is non-existent") {
                        every { stateHandle.get<Array<String>>("mangaListNames") } returns null

                        then("the array should be empty") {
                            viewModel.testIntent { mangaListNames.shouldBeEmpty() }
                        }
                    }
                }

                and("the anime collection has entries") {
                    val initialState: Array<ListsState.() -> ListsState> = arrayOf(
                        { copy(animeList = animeList.copy(isLoading = true)) },
                        { copy(mangaList = mangaList.copy(isLoading = true)) },
                        {
                            copy(
                                animeList = animeList.copy(
                                    isLoading = false,
                                    isEmpty = false,
                                    name = "MyCustomAnimeList",
                                    items = persistentListOf(animeListItem1),
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

                    every { observeAnime.flow } returns flowOf(
                        MediaCollection(
                            lists = listOf(
                                MediaListGroup(
                                    name = "MyCustomAnimeList",
                                    entries = listOf(animeMediaEntry1),
                                ),
                                MediaListGroup(
                                    name = "MyCustomAnimeList2",
                                    entries = listOf(animeMediaEntry2),
                                ),
                            ),
                        ),
                    )
                    every { observeManga.flow } returns flowOf(MediaCollection(lists = emptyList()))
                    coJustRun { observeAnime() }
                    coJustRun { observeManga() }

                    viewModel.runOnCreate()

                    then("it should update its state with the mapped entry") {
                        viewModel.assert(ListsState()) {
                            states(*initialState)
                        }

                        coVerify(exactly = 1) { observeAnime() }
                        verify(exactly = 1) { observeAnime.flow }
                        verify(ordering = Ordering.ORDERED) {
                            stateHandle["animeListNames"] = arrayOf("MyCustomAnimeList", "MyCustomAnimeList2")

                            stateHandle["animeCollection"] = mapOf(
                                "MyCustomAnimeList" to listOf(animeListItem1),
                                "MyCustomAnimeList2" to listOf(animeListItem2),
                            )
                        }
                    }

                    and("try to fetch a existent anime list") {
                        viewModel.testIntent { selectAnimeList("MyCustomAnimeList2") }

                        then("the state should be updated") {
                            viewModel.assert(ListsState()) {
                                states(
                                    *initialState + arrayOf(
                                        {
                                            copy(
                                                animeList = animeList.copy(
                                                    name = "MyCustomAnimeList2",
                                                    items = persistentListOf(animeListItem2),
                                                ),
                                            )
                                        },
                                    ),
                                )
                            }

                            verify(exactly = 2) {
                                stateHandle.get<Collection<MediaListItem.AnimeListItem>>("animeCollection")
                            }
                        }
                    }

                    and("getting the animeListNames") {
                        then("the list should contain one element") {
                            viewModel.testIntent {
                                animeListNames
                                    .shouldHaveSize(2)
                                    .shouldContainInOrder("MyCustomAnimeList", "MyCustomAnimeList2")
                            }

                            verify(exactly = 1) {
                                stateHandle.get<Array<String>>("animeListNames")
                            }
                        }
                    }
                }

                and("the manga collection has entries") {
                    val initialState: Array<ListsState.() -> ListsState> = arrayOf(
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
                                    items = persistentListOf(mangaListItem1),
                                ),
                            )
                        },
                    )

                    every { observeManga.flow } returns flowOf(
                        MediaCollection(
                            lists = listOf(
                                MediaListGroup(
                                    name = "MyCustomMangaList",
                                    entries = listOf(mangaMediaEntry1),
                                ),
                                MediaListGroup(
                                    name = "MyCustomMangaList2",
                                    entries = listOf(mangaMediaEntry2),
                                ),
                            ),
                        ),
                    )
                    every { observeAnime.flow } returns flowOf(MediaCollection(lists = emptyList()))
                    coJustRun { observeAnime() }
                    coJustRun { observeManga() }

                    viewModel.runOnCreate()

                    then("it should update its state with the mapped entry") {
                        viewModel.assert(ListsState()) {
                            states(*initialState)
                        }

                        coVerify(exactly = 1) { observeManga() }
                        verify(exactly = 1) { observeManga.flow }
                        verify(ordering = Ordering.ORDERED) {
                            stateHandle["mangaListNames"] = arrayOf("MyCustomMangaList", "MyCustomMangaList2")

                            stateHandle["mangaCollection"] = mapOf(
                                "MyCustomMangaList" to listOf(mangaListItem1),
                                "MyCustomMangaList2" to listOf(mangaListItem2),
                            )
                        }
                    }

                    and("try to fetch a existent manga list") {
                        viewModel.testIntent { selectMangaList("MyCustomMangaList2") }

                        then("the state should be updated") {
                            viewModel.assert(ListsState()) {
                                states(
                                    *initialState + arrayOf(
                                        {
                                            copy(
                                                mangaList = mangaList.copy(
                                                    name = "MyCustomMangaList2",
                                                    items = persistentListOf(mangaListItem2),
                                                ),
                                            )
                                        },
                                    ),
                                )
                            }

                            verify(exactly = 2) {
                                stateHandle.get<Collection<MediaListItem.MangaListItem>>("mangaCollection")
                            }
                        }
                    }

                    and("getting the mangaListNames") {
                        then("the list should contain two element") {
                            viewModel.testIntent {
                                mangaListNames
                                    .shouldHaveSize(2)
                                    .shouldContainInOrder("MyCustomMangaList", "MyCustomMangaList2")
                            }

                            verify(exactly = 1) {
                                stateHandle.get<Array<String>>("mangaListNames")
                            }
                        }
                    }
                }
            }

            `when`("adding a +1 to an entry") {
                coEvery { updateList(any()) } returns Unit.right()

                and("is an AnimeListItem") {
                    then("it should call the updateList with the progress incremented by 1") {
                        viewModel.testIntent {
                            addPlusOne(animeListItem1)
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
                            addPlusOne(mangaListItem1)
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
    }
}
