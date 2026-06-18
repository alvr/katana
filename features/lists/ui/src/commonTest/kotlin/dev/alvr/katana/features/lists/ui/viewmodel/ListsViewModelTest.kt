package dev.alvr.katana.features.lists.ui.viewmodel

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.media.domain.models.ItemEntryId
import dev.alvr.katana.common.media.domain.models.MediaCollection
import dev.alvr.katana.common.media.domain.models.entries.MediaEntry
import dev.alvr.katana.common.media.domain.models.lists.MediaListEntry
import dev.alvr.katana.common.media.domain.models.lists.MediaListGroup
import dev.alvr.katana.common.media.domain.models.lists.MediaListStatus
import dev.alvr.katana.common.media.domain.models.lists.MediaListType
import dev.alvr.katana.common.media.domain.usecases.ObserveMediaCollectionUseCase
import dev.alvr.katana.core.common.empty
import dev.alvr.katana.core.common.zero
import dev.alvr.katana.core.tests.ui.FinalizationType
import dev.alvr.katana.core.tests.ui.TestKatanaBaseViewModelScope
import dev.alvr.katana.core.tests.ui.test
import dev.alvr.katana.features.lists.domain.failures.ListsFailure
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
import dev.mokkery.verify
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withContexts
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlin.to
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalCoroutinesApi::class)
internal class ListsViewModelTest : FreeSpec() {
    init {
        "common lists behavior" -
            {
                withContexts(listScenarios) { scenario ->
                    "initializing viewModel" -
                        {
                            "the collections observed are empty" {
                                val scope =
                                    createListTestScope(
                                        scenario,
                                        flowOf(MediaCollection<MediaEntry>(lists = emptyList()).right()),
                                    )

                                scope.viewModel.test {
                                    expectState {
                                        empty.shouldBeTrue()
                                        items.shouldBeEmpty()

                                        copy(loading = false)
                                    }
                                }

                                verifySuspend(mode = VerifyMode.exactly(1)) { scope.observeList(scenario.params) }
                                verify(mode = VerifyMode.exactly(1)) { scope.observeList.flow }
                            }

                            "the collection has entries" {
                                val scope = createListTestScope(scenario)

                                scope.viewModel.test {
                                    expectStateWithLists(scenario)
                                    currentState.empty.shouldBeFalse()
                                }

                                verifySuspend(mode = VerifyMode.exactly(1)) { scope.observeList(scenario.params) }
                                verify(mode = VerifyMode.exactly(1)) { scope.observeList.flow }
                            }

                            "the collection has entries AND getting the userLists" {
                                val scope = createListTestScope(scenario)

                                scope.viewModel.test {
                                    expectStateWithLists(scenario)
                                    currentState.empty.shouldBeFalse()

                                    currentState.lists
                                        .shouldHaveSize(2)
                                        .shouldContainInOrder(
                                            UserList(scenario.listName1 to 1),
                                            UserList(scenario.listName2 to 1),
                                        )
                                }

                                verifySuspend(mode = VerifyMode.exactly(1)) { scope.observeList(scenario.params) }
                                verify(mode = VerifyMode.exactly(1)) { scope.observeList.flow }
                            }

                            "something went wrong collecting" {
                                val scope =
                                    createListTestScope(scenario, flowOf(ListsFailure.GetMediaCollection.left()))

                                scope.viewModel.test {
                                    expectState {
                                        empty.shouldBeTrue()
                                        copy(error = true, loading = false)
                                    }

                                    expectEffect(ListsEffect.LoadingListsFailure)
                                }

                                verifySuspend(mode = VerifyMode.exactly(1)) { scope.observeList(scenario.params) }
                                verify(mode = VerifyMode.exactly(1)) { scope.observeList.flow }
                            }
                        }

                    "plus one" -
                        {
                            "is successful" {
                                val scope = createListTestScope(scenario)
                                everySuspend { scope.updateList(any()) } returns Unit.right()

                                scope.viewModel.test {
                                    expectStateWithLists(scenario)
                                    intent(ListsIntent.AddPlusOne(scenario.item1.entryId))
                                    expectEffect(ListsEffect.AddPlusOneSuccess)
                                }

                                verifySuspend(mode = VerifyMode.exactly(1)) { scope.observeList(scenario.params) }
                                verify(mode = VerifyMode.exactly(1)) { scope.observeList.flow }

                                verifySuspend(mode = VerifyMode.exactly(1)) {
                                    scope.updateList(
                                        scenario.item1.copyWithProgress(scenario.item1.progress.inc()).toMediaList()
                                    )
                                }
                            }

                            "is failure" {
                                val scope = createListTestScope(scenario)
                                everySuspend { scope.updateList(any()) } returns ListsFailure.UpdatingList.left()

                                scope.viewModel.test {
                                    expectStateWithLists(scenario)
                                    intent(ListsIntent.AddPlusOne(scenario.item1.entryId))
                                    expectEffect(ListsEffect.AddPlusOneFailure)
                                }

                                verifySuspend(mode = VerifyMode.exactly(1)) {
                                    scope.updateList(
                                        scenario.item1.copyWithProgress(scenario.item1.progress.inc()).toMediaList()
                                    )
                                }
                            }

                            "the element is not found" {
                                val scope = createListTestScope(scenario)

                                scope.viewModel.test {
                                    expectStateWithLists(scenario)
                                    intent(ListsIntent.AddPlusOne(ItemEntryId(1)))
                                }

                                verifySuspend(mode = VerifyMode.exactly(1)) { scope.observeList(scenario.params) }
                                verify(mode = VerifyMode.exactly(1)) { scope.observeList.flow }
                                verifySuspend(mode = VerifyMode.exactly(0)) { scope.updateList(any()) }
                            }
                        }

                    "searching" -
                        {
                            "selecting a list filters the current entries" {
                                val scope = createListTestScope(scenario)

                                scope.viewModel.test {
                                    expectStateWithLists(scenario)
                                    currentState.empty.shouldBeFalse()
                                    intent(ListsIntent.SelectList(scenario.listName2))
                                    expectState {
                                        items shouldBe persistentListOf(scenario.item2)

                                        copy(selectedList = scenario.listName2)
                                    }
                                }

                                verifySuspend(mode = VerifyMode.exactly(1)) { scope.observeList(scenario.params) }
                                verify(mode = VerifyMode.exactly(1)) { scope.observeList.flow }
                            }

                            "try to select a non-existent list" {
                                val scope = createListTestScope(scenario)

                                scope.viewModel.test {
                                    expectStateWithLists(scenario)
                                    intent(ListsIntent.SelectList(scenario.nonExistentListName))

                                    expectState {
                                        empty.shouldBeTrue()
                                        items.shouldBeEmpty()

                                        copy(loading = false, selectedList = scenario.nonExistentListName)
                                    }
                                }

                                verifySuspend(mode = VerifyMode.exactly(1)) { scope.observeList(scenario.params) }
                                verify(mode = VerifyMode.exactly(1)) { scope.observeList.flow }
                            }

                            "searching a non-existent entry should return an empty list" {
                                val scope = createListTestScope(scenario)

                                scope.viewModel.test(finalizationType = FinalizationType.Drop) {
                                    expectStateWithLists(scenario)
                                    intent(ListsIntent.Search("non-existent entry"))
                                }

                                verifySuspend(mode = VerifyMode.exactly(1)) { scope.observeList(scenario.params) }
                                verify(mode = VerifyMode.exactly(1)) { scope.observeList.flow }
                            }
                        }

                    "refreshing" -
                        {
                            "is successful" {
                                val scope = createListTestScope(scenario)

                                scope.viewModel.test(finalizationType = FinalizationType.Drop) {
                                    expectStateWithLists(scenario)
                                    intent(ListsIntent.Refresh)
                                }

                                verifySuspend(mode = VerifyMode.exactly(2)) { scope.observeList(scenario.params) }
                                verify(mode = VerifyMode.exactly(2)) { scope.observeList.flow }
                            }

                            "is failure" {
                                val observeList = mock<ObserveMediaCollectionUseCase>()
                                val updateList = mock<UpdateListUseCase>()

                                everySuspend { observeList(scenario.params) } returns Unit
                                every { observeList.flow } sequentiallyReturns
                                    listOf(
                                        flowOf(scenario.collection().right()),
                                        flowOf(ListsFailure.GetMediaCollection.left()),
                                    )

                                val viewModel =
                                    createListsUiTestGraph(
                                            observeListUseCase = observeList,
                                            updateListUseCase = updateList,
                                        )
                                        .listsViewModelFactory
                                        .create(scenario.type)

                                viewModel.test {
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

                                verifySuspend(mode = VerifyMode.exactly(2)) { observeList(scenario.params) }
                                verify(mode = VerifyMode.exactly(2)) { observeList.flow }
                            }
                        }
                }
            }

        "anime-specific behavior" -
            {
                "maps next episode data" {
                    val scope = createListTestScope(animeScenario)

                    scope.viewModel.test {
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
                    val scope = createListTestScope(mangaScenario)

                    scope.viewModel.test {
                        expectStateWithLists(mangaScenario)

                        val item = currentState.items.single() as MediaListItem.MangaListItem
                        item.volumesProgress shouldBe mangaListItem1.volumesProgress
                        item.volumesTotal shouldBe mangaListItem1.volumesTotal
                    }
                }
            }
    }

    private fun createListTestScope(
        scenario: ListScenario,
        flow: Flow<Either<ListsFailure, MediaCollection<MediaEntry>>> = flowOf(scenario.collection().right()),
    ): ListsTestScope {
        val observeList = mock<ObserveMediaCollectionUseCase>()
        val updateList = mock<UpdateListUseCase>()

        every { observeList.flow } returns flow
        everySuspend { observeList(scenario.params) } returns Unit

        val viewModel =
            createListsUiTestGraph(observeListUseCase = observeList, updateListUseCase = updateList)
                .listsViewModelFactory
                .create(scenario.type)

        return ListsTestScope(viewModel, observeList, updateList)
    }

    private data class ListsTestScope(
        val viewModel: ListsViewModel,
        val observeList: ObserveMediaCollectionUseCase,
        val updateList: UpdateListUseCase,
    )

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
) {
    val params = ObserveMediaCollectionUseCase.Params(type, MediaListStatus.All)
}

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
