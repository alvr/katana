package dev.alvr.katana.features.lists.ui.viewmodel

import arrow.core.left
import arrow.core.right
import dev.alvr.katana.core.common.empty
import dev.alvr.katana.core.common.zero
import dev.alvr.katana.core.tests.ui.FinalizationType
import dev.alvr.katana.core.tests.ui.TestKatanaBaseViewModelScope
import dev.alvr.katana.core.tests.ui.test
import dev.alvr.katana.features.lists.domain.failures.ListsFailure
import dev.alvr.katana.features.lists.domain.models.ItemEntryId
import dev.alvr.katana.features.lists.domain.models.MediaCollection
import dev.alvr.katana.features.lists.domain.models.entries.MediaEntry
import dev.alvr.katana.features.lists.domain.models.lists.MediaListEntry
import dev.alvr.katana.features.lists.domain.models.lists.MediaListGroup
import dev.alvr.katana.features.lists.domain.models.lists.MediaListType
import dev.alvr.katana.features.lists.domain.usecases.ObserveListUseCase
import dev.alvr.katana.features.lists.domain.usecases.UpdateListUseCase
import dev.alvr.katana.features.lists.ui.di.createListsUiTestGraph
import dev.alvr.katana.features.lists.ui.entities.MediaListItem
import dev.alvr.katana.features.lists.ui.entities.UserList
import dev.alvr.katana.features.lists.ui.entities.mappers.toMediaList
import dev.mokkery.answering.returns
import dev.mokkery.answering.sequentiallyReturns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.resetAnswers
import dev.mokkery.resetCalls
import dev.mokkery.verify
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.test.TestCase
import io.kotest.engine.test.TestResult
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalCoroutinesApi::class)
internal class ListsViewModelTest : FreeSpec() {
    private val observeList = mock<ObserveListUseCase>()
    private val updateList = mock<UpdateListUseCase>()

    init {
        "common lists behavior" -
            {
                listScenarios.forEach { scenario ->
                    scenario.name -
                        {
                            "initializing viewModel" -
                                {
                                    "the collections observed are empty" {
                                        every { observeList.flow } returns
                                            flowOf(MediaCollection<MediaEntry>(lists = emptyList()).right())
                                        everySuspend { observeList(scenario.type) } returns Unit

                                        scenario.viewModel().test {
                                            expectState {
                                                empty.shouldBeTrue()
                                                items.shouldBeEmpty()

                                                copy(loading = false)
                                            }
                                        }

                                        verifySuspend(mode = VerifyMode.exactly(1)) { observeList(scenario.type) }
                                        verify(mode = VerifyMode.exactly(1)) { observeList.flow }
                                    }

                                    "the collection has entries" {
                                        scenario.mockFlow()

                                        scenario.viewModel().test {
                                            expectStateWithLists(scenario)
                                            currentState.empty.shouldBeFalse()
                                        }

                                        verifySuspend(mode = VerifyMode.exactly(1)) { observeList(scenario.type) }
                                        verify(mode = VerifyMode.exactly(1)) { observeList.flow }
                                    }

                                    "the collection has entries AND getting the userLists" {
                                        scenario.mockFlow()

                                        scenario.viewModel().test {
                                            expectStateWithLists(scenario)
                                            currentState.empty.shouldBeFalse()

                                            currentState.lists
                                                .shouldHaveSize(2)
                                                .shouldContainInOrder(
                                                    UserList(scenario.listName1 to 1),
                                                    UserList(scenario.listName2 to 1),
                                                )
                                        }

                                        verifySuspend(mode = VerifyMode.exactly(1)) { observeList(scenario.type) }
                                        verify(mode = VerifyMode.exactly(1)) { observeList.flow }
                                    }

                                    "something went wrong collecting" {
                                        every { observeList.flow } returns
                                            flowOf(ListsFailure.GetMediaCollection.left())
                                        everySuspend { observeList(scenario.type) } returns Unit

                                        scenario.viewModel().test {
                                            expectState {
                                                empty.shouldBeTrue()
                                                copy(error = true, loading = false)
                                            }

                                            expectEffect(ListsEffect.LoadingListsFailure)
                                        }

                                        verifySuspend(mode = VerifyMode.exactly(1)) { observeList(scenario.type) }
                                        verify(mode = VerifyMode.exactly(1)) { observeList.flow }
                                    }
                                }

                            "plus one" -
                                {
                                    "is successful" {
                                        scenario.mockFlow()
                                        everySuspend { updateList(any()) } returns Unit.right()

                                        scenario.viewModel().test {
                                            expectStateWithLists(scenario)
                                            intent(ListsIntent.AddPlusOne(scenario.item1.entryId))
                                            expectEffect(ListsEffect.AddPlusOneSuccess)
                                        }

                                        verifySuspend(mode = VerifyMode.exactly(1)) { observeList(scenario.type) }
                                        verify(mode = VerifyMode.exactly(1)) { observeList.flow }

                                        verifySuspend(mode = VerifyMode.exactly(1)) {
                                            updateList(
                                                scenario.item1
                                                    .copyWithProgress(scenario.item1.progress.inc())
                                                    .toMediaList()
                                            )
                                        }
                                    }

                                    "is failure" {
                                        scenario.mockFlow()
                                        everySuspend { updateList(any()) } returns ListsFailure.UpdatingList.left()

                                        scenario.viewModel().test {
                                            expectStateWithLists(scenario)
                                            intent(ListsIntent.AddPlusOne(scenario.item1.entryId))
                                            expectEffect(ListsEffect.AddPlusOneFailure)
                                        }

                                        verifySuspend(mode = VerifyMode.exactly(1)) {
                                            updateList(
                                                scenario.item1
                                                    .copyWithProgress(scenario.item1.progress.inc())
                                                    .toMediaList()
                                            )
                                        }
                                    }

                                    "the element is not found" {
                                        scenario.mockFlow()

                                        scenario.viewModel().test {
                                            expectStateWithLists(scenario)
                                            intent(ListsIntent.AddPlusOne(ItemEntryId(1)))
                                        }

                                        verifySuspend(mode = VerifyMode.exactly(1)) { observeList(scenario.type) }
                                        verify(mode = VerifyMode.exactly(1)) { observeList.flow }
                                        verifySuspend(mode = VerifyMode.exactly(0)) { updateList(any()) }
                                    }
                                }

                            "searching" -
                                {
                                    "selecting a list filters the current entries" {
                                        scenario.mockFlow()

                                        scenario.viewModel().test {
                                            expectStateWithLists(scenario)
                                            currentState.empty.shouldBeFalse()
                                            intent(ListsIntent.SelectList(scenario.listName2))
                                            expectState {
                                                items shouldBe persistentListOf(scenario.item2)

                                                copy(selectedList = scenario.listName2)
                                            }
                                        }

                                        verifySuspend(mode = VerifyMode.exactly(1)) { observeList(scenario.type) }
                                        verify(mode = VerifyMode.exactly(1)) { observeList.flow }
                                    }

                                    "try to select a non-existent list" {
                                        scenario.mockFlow()

                                        scenario.viewModel().test {
                                            expectStateWithLists(scenario)
                                            intent(ListsIntent.SelectList(scenario.nonExistentListName))

                                            expectState {
                                                empty.shouldBeTrue()
                                                items.shouldBeEmpty()

                                                copy(loading = false, selectedList = scenario.nonExistentListName)
                                            }
                                        }

                                        verifySuspend(mode = VerifyMode.exactly(1)) { observeList(scenario.type) }
                                        verify(mode = VerifyMode.exactly(1)) { observeList.flow }
                                    }

                                    "searching a non-existent entry should return an empty list" {
                                        scenario.mockFlow()

                                        scenario.viewModel().test {
                                            expectStateWithLists(scenario)
                                            intent(ListsIntent.Search("non-existent entry"))

                                            expectState {
                                                empty.shouldBeTrue()
                                                items.shouldBeEmpty()

                                                copy(loading = false, searchQuery = "non-existent entry")
                                            }
                                        }

                                        verifySuspend(mode = VerifyMode.exactly(1)) { observeList(scenario.type) }
                                        verify(mode = VerifyMode.exactly(1)) { observeList.flow }
                                    }
                                }

                            "refreshing" -
                                {
                                    "is successful" {
                                        scenario.mockFlow()

                                        scenario.viewModel().test(finalizationType = FinalizationType.Drop) {
                                            expectStateWithLists(scenario)
                                            intent(ListsIntent.Refresh)
                                        }

                                        verifySuspend(mode = VerifyMode.exactly(2)) { observeList(scenario.type) }
                                        verify(mode = VerifyMode.exactly(2)) { observeList.flow }
                                    }

                                    "is failure" {
                                        everySuspend { observeList(scenario.type) } returns Unit
                                        every { observeList.flow } sequentiallyReturns
                                            listOf(
                                                flowOf(scenario.collection().right()),
                                                flowOf(ListsFailure.GetMediaCollection.left()),
                                            )

                                        scenario.viewModel().test {
                                            expectStateWithLists(scenario)

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

                                        verifySuspend(mode = VerifyMode.exactly(2)) { observeList(scenario.type) }
                                        verify(mode = VerifyMode.exactly(2)) { observeList.flow }
                                    }
                                }
                        }
                }
            }

        "anime-specific behavior" -
            {
                "maps next episode data" {
                    animeScenario.mockFlow()

                    animeScenario.viewModel().test {
                        expectStateWithLists(animeScenario)
                        intent(ListsIntent.SelectList(animeScenario.listName2))

                        expectState {
                            val item = items.single() as MediaListItem.AnimeListItem
                            item.nextEpisode shouldBe animeListItem2.nextEpisode

                            copy(selectedList = animeScenario.listName2)
                        }
                    }
                }
            }

        "manga-specific behavior" -
            {
                "maps volume data" {
                    mangaScenario.mockFlow()

                    mangaScenario.viewModel().test {
                        expectStateWithLists(mangaScenario)

                        val item = currentState.items.single() as MediaListItem.MangaListItem
                        item.volumesProgress shouldBe mangaListItem1.volumesProgress
                        item.volumesTotal shouldBe mangaListItem1.volumesTotal
                    }
                }
            }
    }

    override suspend fun afterEach(testCase: TestCase, result: TestResult) {
        resetCalls(observeList, updateList)
        resetAnswers(observeList, updateList)
    }

    private fun ListScenario.viewModel() =
        createListsUiTestGraph(observeListUseCase = observeList, updateListUseCase = updateList)
            .listsViewModelFactory
            .create(type)

    private fun ListScenario.mockFlow() {
        every { observeList.flow } returns flowOf(collection().right())
        everySuspend { observeList(type) } returns Unit
    }

    private fun ListScenario.collection() =
        MediaCollection(
            lists =
                listOf(
                    MediaListGroup(name = listName1, entries = listOf(mediaEntry1)),
                    MediaListGroup(name = listName2, entries = listOf(mediaEntry2)),
                )
        )

    private suspend fun TestKatanaBaseViewModelScope<ListsState, ListsEffect, ListsIntent>.expectStateWithLists(
        scenario: ListScenario
    ) {
        expectState {
            items shouldBe persistentListOf(scenario.item1)

            copy(
                loading = false,
                selectedList = scenario.listName1,
                collection =
                    persistentMapOf(
                        scenario.listName1 to persistentMapOf(ItemEntryId(Int.zero) to scenario.item1),
                        scenario.listName2 to persistentMapOf(ItemEntryId(Int.zero) to scenario.item2),
                    ),
                error = false,
            )
        }
    }
}

private data class ListScenario(
    val name: String,
    val type: MediaListType,
    val listName1: String,
    val listName2: String,
    val nonExistentListName: String,
    val mediaEntry1: MediaListEntry<MediaEntry>,
    val mediaEntry2: MediaListEntry<MediaEntry>,
    val item1: MediaListItem,
    val item2: MediaListItem,
)

private val animeScenario =
    ListScenario(
        name = "anime",
        type = MediaListType.Anime,
        listName1 = "MyCustomAnimeList",
        listName2 = "MyCustomAnimeList2",
        nonExistentListName = "NonExistent Anime List",
        mediaEntry1 = animeMediaEntry1,
        mediaEntry2 = animeMediaEntry2,
        item1 = animeListItem1,
        item2 = animeListItem2,
    )

private val mangaScenario =
    ListScenario(
        name = "manga",
        type = MediaListType.Manga,
        listName1 = "MyCustomMangaList",
        listName2 = "MyCustomMangaList2",
        nonExistentListName = "NonExistent Manga List",
        mediaEntry1 = mangaMediaEntry1,
        mediaEntry2 = mangaMediaEntry2,
        item1 = mangaListItem1,
        item2 = mangaListItem2,
    )

private val listScenarios = listOf(animeScenario, mangaScenario)

private fun MediaListItem.copyWithProgress(progress: Int) =
    when (this) {
        is MediaListItem.AnimeListItem -> copy(progress = progress)
        is MediaListItem.MangaListItem -> copy(progress = progress)
    }
