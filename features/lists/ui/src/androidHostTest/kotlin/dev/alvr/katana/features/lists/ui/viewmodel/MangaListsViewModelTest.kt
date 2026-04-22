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
import dev.alvr.katana.features.lists.domain.usecases.ObserveMangaListUseCase
import dev.alvr.katana.features.lists.domain.usecases.UpdateListUseCase
import dev.alvr.katana.features.lists.ui.di.createListsUiTestGraph
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

private typealias MangaState = ListsState<MediaListItem.MangaListItem>

internal class MangaListsViewModelTest : FreeSpec() {
    private val observeManga = mockk<ObserveMangaListUseCase>()
    private val updateList = mockk<UpdateListUseCase>()

    private lateinit var viewModel: MangaListsViewModel

    init {
        "initializing viewModel" -
            {
                "the collections are empty" -
                    {
                        "the collections observed should are empty" {
                            every { observeManga.flow } returns
                                flowOf(MediaCollection<MediaEntry.Manga>(lists = emptyList()).right())
                            coJustRun { observeManga() }

                            viewModel.test {
                                expectState {
                                    empty.shouldBeTrue()
                                    items.shouldBeEmpty()

                                    copy(loading = false)
                                }
                            }

                            coVerify(exactly = 1) { observeManga() }
                            verify(exactly = 1) { observeManga.flow }
                        }
                    }

                "the manga collection has entries" {
                    mockMangaFlow()

                    viewModel.test {
                        expectStateWithLists()
                        currentState.empty.shouldBeFalse()
                    }

                    coVerify(exactly = 1) { observeManga() }
                    verify(exactly = 1) { observeManga.flow }
                }

                "the manga collection has entries AND getting the userLists" {
                    mockMangaFlow()

                    viewModel.test {
                        expectStateWithLists()
                        currentState.empty.shouldBeFalse()

                        currentState.lists
                            .shouldHaveSize(2)
                            .shouldContainInOrder(
                                UserList("MyCustomMangaList" to 1),
                                UserList("MyCustomMangaList2" to 1),
                            )
                    }

                    coVerify(exactly = 1) { observeManga() }
                    verify(exactly = 1) { observeManga.flow }
                }

                "something went wrong collecting" {
                    every { observeManga.flow } returns flowOf(ListsFailure.GetMediaCollection.left())
                    coJustRun { observeManga() }

                    viewModel.test {
                        expectState {
                            empty.shouldBeTrue()
                            copy(error = true, loading = false)
                        }

                        expectEffect(ListsEffect.LoadingListsFailure)
                    }

                    coVerify(exactly = 1) { observeManga() }
                    verify(exactly = 1) { observeManga.flow }
                }
            }

        "plus one" -
            {
                "is successful" {
                    mockMangaFlow()
                    coEitherJustRun { updateList(any()) }

                    viewModel.test {
                        expectState { currentState }
                        intent(ListsIntent.AddPlusOne(mangaListItem1.entryId))
                        expectEffect(ListsEffect.AddPlusOneSuccess)
                    }

                    coVerify(exactly = 1) { observeManga() }
                    verify(exactly = 1) { observeManga.flow }

                    coVerify(exactly = 1) {
                        updateList(mangaListItem1.copy(progress = mangaListItem1.progress.inc()).toMediaList())
                    }
                }

                "is failure" {
                    mockMangaFlow()
                    coEvery { updateList(any()) } returns ListsFailure.UpdatingList.left()

                    viewModel.test {
                        expectState { currentState }
                        intent(ListsIntent.AddPlusOne(mangaListItem1.entryId))
                        expectEffect(ListsEffect.AddPlusOneFailure)
                    }

                    coVerify(exactly = 1) {
                        updateList(mangaListItem1.copy(progress = mangaListItem1.progress.inc()).toMediaList())
                    }
                }

                "the element is not found" {
                    mockMangaFlow()

                    viewModel.test {
                        skipItems(1)
                        intent(ListsIntent.AddPlusOne(ItemEntryId(1)))
                    }

                    coVerify(exactly = 1) { observeManga() }
                    verify(exactly = 1) { observeManga.flow }
                    coVerify(exactly = 0) { updateList(any()) }
                }
            }

        "searching" -
            {
                "the manga collection has entries" {
                    mockMangaFlow()

                    viewModel.test {
                        expectStateWithLists()
                        currentState.empty.shouldBeFalse()
                        intent(ListsIntent.SelectList("MyCustomMangaList2"))
                        expectState {
                            items shouldBe persistentListOf(mangaListItem2)

                            copy(selectedList = "MyCustomMangaList2")
                        }
                    }

                    coVerify(exactly = 1) { observeManga() }
                    verify(exactly = 1) { observeManga.flow }
                }

                "try to select a non-existent manga list" {
                    mockMangaFlow()

                    viewModel.test {
                        expectStateWithLists()
                        intent(ListsIntent.SelectList("NonExistent Manga List"))

                        expectState {
                            empty.shouldBeTrue()
                            items.shouldBeEmpty()

                            copy(loading = false, selectedList = "NonExistent Manga List")
                        }
                    }

                    coVerify(exactly = 1) { observeManga() }
                    verify(exactly = 1) { observeManga.flow }
                }

                listOf(Triple("non-existent entry", true, persistentListOf<MediaListItem.MangaListItem>())).forEach {
                    (text, empty, result) ->
                    "searching $text an entry should return $result" {
                        mockMangaFlow()

                        viewModel.test {
                            expectStateWithLists()
                            intent(ListsIntent.Search(text))
                            expectState {
                                empty.shouldBeTrue()
                                items shouldBe result

                                copy(loading = false, searchQuery = text)
                            }
                        }

                        coVerify(exactly = 1) { observeManga() }
                        verify(exactly = 1) { observeManga.flow }
                    }
                }
            }

        "refreshing" -
            {
                "is successful" {
                    mockMangaFlow()

                    viewModel.test(finalizationType = FinalizationType.Drop) {
                        expectStateWithLists()
                        intent(ListsIntent.Refresh)
                    }

                    coVerify(exactly = 2) { observeManga() }
                    verify(exactly = 2) { observeManga.flow }
                }

                "is failure" {
                    coJustRun { observeManga() }
                    every { observeManga.flow } returnsMany
                        listOf(
                            flowOf(
                                MediaCollection(
                                        lists =
                                            listOf(
                                                MediaListGroup(
                                                    name = "MyCustomMangaList",
                                                    entries = listOf(mangaMediaEntry1),
                                                ),
                                                MediaListGroup(
                                                    name = "MyCustomMangaList2",
                                                    entries = listOf(mangaMediaEntry2),
                                                ),
                                            )
                                    )
                                    .right()
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

                    coVerify(exactly = 2) { observeManga() }
                    verify(exactly = 2) { observeManga.flow }
                }
            }
    }

    override suspend fun beforeEach(testCase: TestCase) {
        clearAllMocks()
        viewModel = createListsUiTestGraph(updateList, mockk(), observeManga).mangaListsViewModel
    }

    private fun mockMangaFlow() {
        every { observeManga.flow } returns
            flowOf(
                MediaCollection(
                        lists =
                            listOf(
                                MediaListGroup(name = "MyCustomMangaList", entries = listOf(mangaMediaEntry1)),
                                MediaListGroup(name = "MyCustomMangaList2", entries = listOf(mangaMediaEntry2)),
                            )
                    )
                    .right()
            )

        coJustRun { observeManga() }
    }

    private suspend fun TestKatanaBaseViewModelScope<MangaState, *, *>.expectStateWithLists() {
        expectState {
            items shouldBe persistentListOf(mangaListItem1)

            copy(
                loading = false,
                selectedList = "MyCustomMangaList",
                collection =
                    persistentMapOf(
                        "MyCustomMangaList" to persistentMapOf(ItemEntryId(Int.zero) to mangaListItem1),
                        "MyCustomMangaList2" to persistentMapOf(ItemEntryId(Int.zero) to mangaListItem2),
                    ),
                error = false,
            )
        }
    }
}
