package dev.alvr.katana.features.lists.ui.viewmodel

import arrow.core.left
import arrow.core.right
import dev.alvr.katana.core.common.empty
import dev.alvr.katana.core.common.zero
import dev.alvr.katana.core.domain.usecases.invoke
import dev.alvr.katana.core.tests.coEitherJustRun
import dev.alvr.katana.core.tests.ui.FinalizationType
import dev.alvr.katana.core.tests.ui.TestKatanaBaseViewModelScope
import dev.alvr.katana.core.tests.ui.test
import dev.alvr.katana.features.lists.domain.failures.ListsFailure
import dev.alvr.katana.features.lists.domain.models.ItemEntryId
import dev.alvr.katana.features.lists.domain.models.MediaCollection
import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import dev.alvr.katana.features.lists.domain.models.lists.MediaListGroup
import dev.alvr.katana.features.lists.domain.usecases.ObserveAnimeListUseCase
import dev.alvr.katana.features.lists.domain.usecases.UpdateListUseCase
import dev.alvr.katana.features.lists.ui.entities.MediaListItem
import dev.alvr.katana.features.lists.ui.entities.UserList
import dev.alvr.katana.features.lists.ui.entities.mappers.toMediaList
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.test.TestCase
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.coroutines.flow.flowOf

private typealias AnimeState = ListsState<MediaListItem.AnimeListItem>

internal class AnimeListsViewModelTest : FreeSpec() {
    private val observeAnime = mockk<ObserveAnimeListUseCase>()
    private val updateList = mockk<UpdateListUseCase>()

    private lateinit var viewModel: AnimeListsViewModel

    init {
        "initializing viewModel" - {
            "the collections are empty" - {
                "the collections observed should are empty" {
                    every { observeAnime.flow } returns flowOf(
                        MediaCollection<MediaEntry.Anime>(lists = emptyList()).right(),
                    )
                    coJustRun { observeAnime() }

                    viewModel.test {
                        expectState {
                            empty.shouldBeTrue()
                            items.shouldBeEmpty()

                            copy(loading = false)
                        }
                    }

                    coVerify(exactly = 1) { observeAnime() }
                    verify(exactly = 1) { observeAnime.flow }
                }
            }

            "the anime collection has entries" {
                mockAnimeFlow()

                viewModel.test {
                    expectStateWithLists()
                    currentState.empty.shouldBeFalse()
                }

                coVerify(exactly = 1) { observeAnime() }
                verify(exactly = 1) { observeAnime.flow }
            }

            "the anime collection has entries AND getting the userLists" {
                mockAnimeFlow()

                viewModel.test {
                    expectStateWithLists()
                    currentState.empty.shouldBeFalse()

                    currentState.lists
                        .shouldHaveSize(2)
                        .shouldContainInOrder(
                            UserList("MyCustomAnimeList" to 1),
                            UserList("MyCustomAnimeList2" to 1),
                        )
                }

                coVerify(exactly = 1) { observeAnime() }
                verify(exactly = 1) { observeAnime.flow }
            }

            "something went wrong collecting" {
                every { observeAnime.flow } returns flowOf(ListsFailure.GetMediaCollection.left())
                coJustRun { observeAnime() }

                viewModel.test {
                    expectState {
                        empty.shouldBeTrue()
                        copy(error = true, loading = false)
                    }

                    expectEffect(ListsEffect.LoadingListsFailure)
                }

                coVerify(exactly = 1) { observeAnime() }
                verify(exactly = 1) { observeAnime.flow }
            }
        }

        "plus one" - {
            "is successful" {
                mockAnimeFlow()
                coEitherJustRun { updateList(any()) }

                viewModel.test {
                    skipItems(1)
                    intent(ListsIntent.AddPlusOne(animeListItem1.entryId))
                    expectEffect(ListsEffect.AddPlusOneSuccess)
                }

                coVerify(exactly = 1) { observeAnime() }
                verify(exactly = 1) { observeAnime.flow }

                coVerify(exactly = 1) {
                    updateList(animeListItem1.copy(progress = animeListItem1.progress.inc()).toMediaList())
                }
            }

            "is failure" {
                mockAnimeFlow()
                coEvery { updateList(any()) } returns ListsFailure.UpdatingList.left()

                viewModel.test {
                    skipItems(1)
                    intent(ListsIntent.AddPlusOne(animeListItem1.entryId))
                    expectEffect(ListsEffect.AddPlusOneFailure)
                }

                coVerify(exactly = 1) {
                    updateList(animeListItem1.copy(progress = animeListItem1.progress.inc()).toMediaList())
                }
            }

            "the element is not found" {
                mockAnimeFlow()

                viewModel.test {
                    skipItems(1)
                    intent(ListsIntent.AddPlusOne(ItemEntryId(1)))
                }

                coVerify(exactly = 1) { observeAnime() }
                verify(exactly = 1) { observeAnime.flow }
                coVerify(exactly = 0) { updateList(any()) }
            }
        }

        "searching" - {
            "the anime collection has entries" {
                mockAnimeFlow()

                viewModel.test {
                    expectStateWithLists()
                    currentState.empty.shouldBeFalse()
                    intent(ListsIntent.SelectList("MyCustomAnimeList2"))
                    expectState {
                        items shouldBe persistentListOf(animeListItem2)

                        copy(selectedList = "MyCustomAnimeList2")
                    }
                }

                coVerify(exactly = 1) { observeAnime() }
                verify(exactly = 1) { observeAnime.flow }
            }

            "try to select a non-existent anime list" {
                mockAnimeFlow()

                viewModel.test {
                    expectStateWithLists()
                    intent(ListsIntent.SelectList("NonExistent Anime List"))

                    expectState {
                        empty.shouldBeTrue()
                        items.shouldBeEmpty()

                        copy(
                            loading = false,
                            selectedList = "NonExistent Anime List",
                        )
                    }
                }

                coVerify(exactly = 1) { observeAnime() }
                verify(exactly = 1) { observeAnime.flow }
            }

            listOf(
                Triple("non-existent entry", true, persistentListOf<MediaListItem.AnimeListItem>()),
            ).forEach { (text, empty, result) ->
                "searching $text an entry should return $result" {
                    mockAnimeFlow()

                    viewModel.test {
                        expectStateWithLists()
                        intent(ListsIntent.Search(text))
                        expectState {
                            empty.shouldBeTrue()
                            items shouldBe result

                            copy(
                                loading = false,
                                searchQuery = text,
                            )
                        }
                    }

                    coVerify(exactly = 1) { observeAnime() }
                    verify(exactly = 1) { observeAnime.flow }
                }
            }
        }

        "refreshing" - {
            "is successful" {
                mockAnimeFlow()

                viewModel.test(
                    finalizationType = FinalizationType.Drop,
                ) {
                    expectStateWithLists()
                    intent(ListsIntent.Refresh)
                }

                coVerify(exactly = 2) { observeAnime() }
                verify(exactly = 2) { observeAnime.flow }
            }

            "is failure" {
                coJustRun { observeAnime() }
                every { observeAnime.flow } returnsMany listOf(
                    flowOf(
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
                    ),
                    flowOf(ListsFailure.GetMediaCollection.left()),
                )

                viewModel.test {
                    expectStateWithLists()

                    intent(ListsIntent.Refresh)

                    expectState { copy(loading = true) }
                    expectState {
                        empty.shouldBeTrue()
                        items.shouldBeEmpty()

                        copy(
                            collection = persistentMapOf(),
                            selectedList = String.empty,
                            error = true,
                            loading = false,
                        )
                    }
                    expectEffect(ListsEffect.LoadingListsFailure)
                }

                coVerify(exactly = 2) { observeAnime() }
                verify(exactly = 2) { observeAnime.flow }
            }
        }
    }

    override suspend fun beforeEach(testCase: TestCase) {
        clearAllMocks()
        viewModel = AnimeListsViewModel(updateList, observeAnime)
    }

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

    private suspend fun TestKatanaBaseViewModelScope<AnimeState, *, *>.expectStateWithLists() {
        expectState {
            items shouldBe persistentListOf(animeListItem1)

            copy(
                loading = false,
                selectedList = "MyCustomAnimeList",
                collection = persistentMapOf(
                    "MyCustomAnimeList" to persistentMapOf(ItemEntryId(Int.zero) to animeListItem1),
                    "MyCustomAnimeList2" to persistentMapOf(ItemEntryId(Int.zero) to animeListItem2),
                ),
                error = false,
            )
        }
    }
}
