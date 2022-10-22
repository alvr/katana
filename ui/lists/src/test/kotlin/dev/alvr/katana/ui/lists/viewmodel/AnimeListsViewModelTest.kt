package dev.alvr.katana.ui.lists.viewmodel

import androidx.lifecycle.SavedStateHandle
import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.core.empty
import dev.alvr.katana.common.core.zero
import dev.alvr.katana.common.tests.coEitherJustRun
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.lists.failures.ListsFailure
import dev.alvr.katana.domain.lists.models.MediaCollection
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.models.lists.MediaList
import dev.alvr.katana.domain.lists.models.lists.MediaListGroup
import dev.alvr.katana.domain.lists.usecases.ObserveAnimeListUseCase
import dev.alvr.katana.domain.lists.usecases.UpdateListUseCase
import dev.alvr.katana.ui.lists.entities.MediaListItem
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.mockk.Ordering
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

private typealias AnimeState = ListState<MediaListItem.AnimeListItem>

internal class AnimeListsViewModelTest : BehaviorSpec() {
    private val stateHandle = spyk(SavedStateHandle())
    private val observeAnime = mockk<ObserveAnimeListUseCase>()
    private val updateList = mockk<UpdateListUseCase>()

    init {
        given("an AnimeListViewModel") {
            val viewModel = AnimeListsViewModel(stateHandle, updateList, observeAnime).test(ListState())

            `when`("initializing the viewModel") {
                and("the collections are empty") {
                    val initialState: Array<AnimeState.() -> AnimeState> = arrayOf(
                        { copy(isLoading = true) },
                        {
                            copy(
                                isLoading = false,
                                isEmpty = true,
                                items = persistentListOf(),
                            )
                        },
                    )

                    every { observeAnime.flow } returns flowOf(
                        MediaCollection<MediaEntry.Anime>(lists = emptyList()).right(),
                    )
                    coJustRun { observeAnime() }

                    viewModel.runOnCreate()

                    then("it should update its state") {
                        viewModel.assert(AnimeState()) {
                            states(*initialState)
                        }
                    }

                    and("try to fetch a non-existent anime list") {
                        viewModel.testIntent { selectList("NonExistent Anime List") }

                        then("the state should be the same") {
                            viewModel.assert(AnimeState()) {
                                states(
                                    *initialState + emptyArray(), // No more state updates
                                )
                            }

                            verify(exactly = 2) {
                                stateHandle.get<Collection<MediaListItem.AnimeListItem>>("collection")
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

                    and("the collection of anime is non-existent") {
                        every { stateHandle.get<Collection<MediaListItem.AnimeListItem>>(any()) } returns null

                        then("the state should be the same") {
                            viewModel.testIntent { selectList("MyCustomAnimeList") }

                            viewModel.assert(AnimeState()) {
                                states(*initialState)
                            }
                        }
                    }

                    and("the array of anime list names is non-existent") {
                        every { stateHandle.get<Array<String>>("listNames") } returns null

                        then("the array should be empty") {
                            viewModel.testIntent { listNames.shouldBeEmpty() }
                        }
                    }
                }

                and("the anime collection has entries") {
                    mockAnimeFlow()

                    viewModel.runOnCreate()

                    then("it should update its state with the mapped entry") {
                        viewModel.assert(AnimeState()) {
                            states(*initialStateWithLists)
                        }

                        coVerify(exactly = 1) { observeAnime() }
                        verify(exactly = 1) { observeAnime.flow }
                        verify(ordering = Ordering.ORDERED) {
                            stateHandle["listNames"] = arrayOf("MyCustomAnimeList", "MyCustomAnimeList2")

                            stateHandle["collection"] = mapOf(
                                "MyCustomAnimeList" to listOf(animeListItem1),
                                "MyCustomAnimeList2" to listOf(animeListItem2),
                            )
                        }
                    }

                    and("try to fetch a existent anime list") {
                        viewModel.testIntent { selectList("MyCustomAnimeList2") }

                        then("the state should be updated") {
                            viewModel.assert(AnimeState()) {
                                states(
                                    *initialStateWithLists,
                                    {
                                        copy(
                                            name = "MyCustomAnimeList2",
                                            items = persistentListOf(animeListItem2),
                                        )
                                    },
                                )
                            }

                            verify(exactly = 2) {
                                stateHandle.get<Collection<MediaListItem.AnimeListItem>>("collection")
                            }
                        }
                    }

                    and("getting the listNames") {
                        then("the list should contain one element") {
                            viewModel.testIntent {
                                listNames
                                    .shouldHaveSize(2)
                                    .shouldContainInOrder("MyCustomAnimeList", "MyCustomAnimeList2")
                            }

                            verify(exactly = 1) {
                                stateHandle.get<Array<String>>("listNames")
                            }
                        }
                    }
                }

                and("something went wrong collecting") {
                    every { observeAnime.flow } returns flowOf(ListsFailure.GetMediaCollection.left())
                    coJustRun { observeAnime() }

                    viewModel.runOnCreate()

                    then("update it state with isError = true") {
                        viewModel.assert(AnimeState()) {
                            states(
                                { copy(isLoading = true) },
                                { copy(isError = true, isLoading = false, isEmpty = true) },
                            )
                        }
                    }
                }
            }

            `when`("adding a +1 to an entry") {
                coEitherJustRun { updateList(any()) }

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
            }

            `when`("searching for entries") {
                and("the entry does not exist") {
                    mockAnimeFlow()

                    viewModel.runOnCreate()
                    viewModel.testIntent { search("non-existent entry") }

                    viewModel.assert(AnimeState()) {
                        states(
                            *initialStateWithLists,
                            {
                                copy(
                                    isLoading = false,
                                    isEmpty = true,
                                    items = persistentListOf(),
                                )
                            },
                        )
                    }
                }

                and("the entry exists") {
                    mockAnimeFlow()

                    viewModel.runOnCreate()
                    viewModel.testIntent { search("OnE PiEcE") }

                    viewModel.assert(AnimeState()) {
                        states(
                            *initialStateWithLists,
                            {
                                copy(
                                    isLoading = false,
                                    isEmpty = false,
                                    items = persistentListOf(animeListItem1),
                                )
                            },
                        )
                    }
                }
            }
        }
    }

    private val initialStateWithLists: Array<AnimeState.() -> AnimeState>
        get() = arrayOf(
            { copy(isLoading = true) },
            {
                copy(
                    isLoading = false,
                    isEmpty = false,
                    name = "MyCustomAnimeList",
                    items = persistentListOf(animeListItem1),
                    isError = false,
                )
            },
        )

    private fun mockAnimeFlow() {
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
            ).right(),
        )

        coJustRun { observeAnime() }
    }
}
