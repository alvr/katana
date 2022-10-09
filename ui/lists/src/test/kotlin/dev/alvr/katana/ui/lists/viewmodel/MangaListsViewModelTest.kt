package dev.alvr.katana.ui.lists.viewmodel

import androidx.lifecycle.SavedStateHandle
import arrow.core.right
import dev.alvr.katana.common.core.empty
import dev.alvr.katana.common.core.zero
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.lists.models.MediaCollection
import dev.alvr.katana.domain.lists.models.lists.MediaList
import dev.alvr.katana.domain.lists.models.lists.MediaListGroup
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

private typealias MangaState = ListState<MediaListItem.MangaListItem>

internal class MangaListViewModelTest : BehaviorSpec() {
    private val stateHandle = spyk(SavedStateHandle())
    private val observeManga = mockk<ObserveMangaListUseCase>()
    private val updateList = mockk<UpdateListUseCase>()

    init {
        given("a MangaListViewModel") {
            val viewModel = MangaListsViewModel(stateHandle, updateList, observeManga).test(ListState())

            `when`("initializing the viewModel") {
                and("the collections are empty") {
                    val initialState: Array<MangaState.() -> MangaState> = arrayOf(
                        { copy(isLoading = true) },
                        {
                            copy(
                                isLoading = false,
                                isEmpty = true,
                                items = persistentListOf(),
                            )
                        },
                    )

                    every { observeManga.flow } returns flowOf(MediaCollection(lists = emptyList()))
                    coJustRun { observeManga() }

                    viewModel.runOnCreate()

                    then("it should update its state") {
                        viewModel.assert(MangaState()) {
                            states(*initialState)
                        }
                    }

                    and("try to fetch a non-existent manga list") {
                        viewModel.testIntent { selectList("NonExistent Manga List") }

                        then("the state should be the same") {
                            viewModel.assert(MangaState()) {
                                states(
                                    *initialState + emptyArray(), // No more state updates
                                )
                            }

                            verify(exactly = 2) {
                                stateHandle.get<Collection<MediaListItem.MangaListItem>>("collection")
                            }
                        }
                    }

                    and("getting the listNames") {
                        then("the list should be empty") {
                            viewModel.testIntent { listNames.shouldBeEmpty() }

                            verify(exactly = 1) {
                                stateHandle.get<Array<String>>("listNames")
                            }
                        }
                    }

                    and("the collection of manga is non-existent") {
                        every { stateHandle.get<Collection<MediaListItem.MangaListItem>>(any()) } returns null

                        then("the state should be the same") {
                            viewModel.testIntent { selectList("MyCustomMangaList") }

                            viewModel.assert(MangaState()) {
                                states(*initialState)
                            }
                        }
                    }

                    and("the array of manga list names is non-existent") {
                        every { stateHandle.get<Array<String>>("listNames") } returns null

                        then("the array should be empty") {
                            viewModel.testIntent { listNames.shouldBeEmpty() }
                        }
                    }
                }

                and("the manga collection has entries") {
                    mockMangaFlow()

                    viewModel.runOnCreate()

                    then("it should update its state with the mapped entry") {
                        viewModel.assert(MangaState()) {
                            states(*initialStateWithLists)
                        }

                        coVerify(exactly = 1) { observeManga() }
                        verify(exactly = 1) { observeManga.flow }
                        verify(ordering = Ordering.ORDERED) {
                            stateHandle["listNames"] = arrayOf("MyCustomMangaList", "MyCustomMangaList2")

                            stateHandle["collection"] = mapOf(
                                "MyCustomMangaList" to listOf(mangaListItem1),
                                "MyCustomMangaList2" to listOf(mangaListItem2),
                            )
                        }
                    }

                    and("try to fetch a existent manga list") {
                        viewModel.testIntent { selectList("MyCustomMangaList2") }

                        then("the state should be updated") {
                            viewModel.assert(MangaState()) {
                                states(
                                    *initialStateWithLists,
                                    {
                                        copy(
                                            name = "MyCustomMangaList2",
                                            items = persistentListOf(mangaListItem2),
                                        )
                                    },
                                )
                            }

                            verify(exactly = 2) {
                                stateHandle.get<Collection<MediaListItem.MangaListItem>>("collection")
                            }
                        }
                    }

                    and("getting the listNames") {
                        then("the list should contain two element") {
                            viewModel.testIntent {
                                listNames
                                    .shouldHaveSize(2)
                                    .shouldContainInOrder("MyCustomMangaList", "MyCustomMangaList2")
                            }

                            verify(exactly = 1) {
                                stateHandle.get<Array<String>>("listNames")
                            }
                        }
                    }
                }
            }

            `when`("adding a +1 to an entry") {
                coEvery { updateList(any()) } returns Unit.right()

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

            `when`("searching for entries") {
                and("the entry does not exist") {
                    mockMangaFlow()

                    viewModel.runOnCreate()
                    viewModel.testIntent { search("non-existent entry") }

                    viewModel.assert(MangaState()) {
                        states(
                            *initialStateWithLists,
                            {
                                copy(
                                    isLoading = false,
                                    isEmpty = true,
                                    items = persistentListOf(),
                                    search = "non-existent entry",
                                )
                            },
                        )
                    }
                }

                and("the entry exists") {
                    mockMangaFlow()

                    viewModel.runOnCreate()
                    viewModel.testIntent { search("OnE PiEcE") }

                    viewModel.assert(MangaState()) {
                        states(
                            *initialStateWithLists,
                            {
                                copy(
                                    isLoading = false,
                                    isEmpty = false,
                                    items = persistentListOf(mangaListItem1),
                                    search = "OnE PiEcE",
                                )
                            },
                        )
                    }
                }
            }
        }
    }

    private val initialStateWithLists: Array<MangaState.() -> MangaState>
        get() = arrayOf(
            { copy(isLoading = true) },
            {
                copy(
                    isLoading = false,
                    isEmpty = false,
                    name = "MyCustomMangaList",
                    items = persistentListOf(mangaListItem1),
                )
            },
        )

    private fun mockMangaFlow() {
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

        coJustRun { observeManga() }
    }
}
