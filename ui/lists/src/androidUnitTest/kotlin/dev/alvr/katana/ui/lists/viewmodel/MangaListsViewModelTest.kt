package dev.alvr.katana.ui.lists.viewmodel

import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.tests.coEitherJustRun
import dev.alvr.katana.common.tests.orbitTestScope
import dev.alvr.katana.domain.base.usecases.invoke
import dev.alvr.katana.domain.lists.failures.ListsFailure
import dev.alvr.katana.domain.lists.models.MediaCollection
import dev.alvr.katana.domain.lists.models.entries.MediaEntry
import dev.alvr.katana.domain.lists.models.lists.MediaListGroup
import dev.alvr.katana.domain.lists.usecases.ObserveMangaListUseCase
import dev.alvr.katana.domain.lists.usecases.UpdateListUseCase
import dev.alvr.katana.ui.lists.entities.MediaListItem
import dev.alvr.katana.ui.lists.entities.UserList
import dev.alvr.katana.ui.lists.entities.mappers.toMediaList
import io.kotest.assertions.throwables.shouldThrowExactlyUnit
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.mockk.clearAllMocks
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flowOf
import org.orbitmvi.orbit.test.OrbitTestContext
import org.orbitmvi.orbit.test.test

private typealias MangaState = ListState<MediaListItem.MangaListItem>

internal class MangaListsViewModelTest : FreeSpec() {
    private val observeManga = mockk<ObserveMangaListUseCase>()
    private val updateList = mockk<UpdateListUseCase>()
    private lateinit var viewModel: MangaListsViewModel

    init {
        "initializing viewModel" - {
            "the collections are empty" - {
                "the collections observed should are empty" {
                    every { observeManga.flow } returns flowOf(
                        MediaCollection<MediaEntry.Manga>(lists = emptyList()).right(),
                    )
                    coJustRun { observeManga() }

                    viewModel.test(orbitTestScope) {
                        runOnCreate()
                        expectInitialState()
                        expectState { copy(isLoading = true) }
                        expectState {
                            copy(
                                isLoading = false,
                                isEmpty = true,
                                items = persistentListOf(),
                            )
                        }
                        cancelAndIgnoreRemainingItems()
                    }

                    verify(exactly = 1) { observeManga() }
                    verify(exactly = 1) { observeManga.flow }
                }
            }

            "the manga collection has entries" {
                mockMangaFlow()

                viewModel.test(orbitTestScope) {
                    runOnCreate()
                    expectInitialState()
                    expectStateWithLists()
                }

                verify(exactly = 1) { observeManga() }
                verify(exactly = 1) { observeManga.flow }
            }

            "the manga collection has entries AND getting the userLists" {
                mockMangaFlow()

                viewModel.test(orbitTestScope) {
                    runOnCreate()
                    expectInitialState()
                    expectStateWithLists()

                    containerHost.userLists
                        .shouldHaveSize(2)
                        .shouldContainInOrder(
                            UserList("MyCustomMangaList" to 1),
                            UserList("MyCustomMangaList2" to 1),
                        )
                }

                verify(exactly = 1) { observeManga() }
                verify(exactly = 1) { observeManga.flow }
            }

            "something went wrong collecting" {
                every { observeManga.flow } returns flowOf(ListsFailure.GetMediaCollection.left())
                coJustRun { observeManga() }

                viewModel.test(orbitTestScope) {
                    runOnCreate()
                    expectInitialState()
                    expectState { copy(isLoading = true) }
                    expectState { copy(isError = true, isLoading = false, isEmpty = true) }
                }

                verify(exactly = 1) { observeManga() }
                verify(exactly = 1) { observeManga.flow }
            }
        }

        "plus one" - {
            "is successful" {
                mockMangaFlow()
                coEitherJustRun { updateList(any()) }

                viewModel.test(orbitTestScope) {
                    runOnCreate()
                    expectInitialState()
                    expectState { copy(isLoading = true) }
                    containerHost.addPlusOne(mangaListItem1.entryId)
                    cancelAndIgnoreRemainingItems()
                }

                verify(exactly = 1) { observeManga() }
                verify(exactly = 1) { observeManga.flow }

                coVerify(exactly = 1) {
                    updateList(mangaListItem1.copy(progress = mangaListItem1.progress.inc()).toMediaList())
                }
            }

            "the element is not found" {
                mockMangaFlow()

                viewModel.test(orbitTestScope) {
                    runOnCreate()
                    expectInitialState()
                    expectState { copy(isLoading = true) }
                    shouldThrowExactlyUnit<NoSuchElementException> { containerHost.addPlusOne(234) }
                    cancelAndIgnoreRemainingItems()
                }

                verify(exactly = 1) { observeManga() }
                verify(exactly = 1) { observeManga.flow }
            }
        }

        "searching" - {
            "the manga collection has entries" {
                mockMangaFlow()

                viewModel.test(orbitTestScope) {
                    runOnCreate()
                    expectInitialState()
                    expectStateWithLists()
                    containerHost.selectList("MyCustomMangaList2")
                    expectState {
                        copy(
                            name = "MyCustomMangaList2",
                            items = persistentListOf(mangaListItem2),
                        )
                    }
                }

                verify(exactly = 1) { observeManga() }
                verify(exactly = 1) { observeManga.flow }
            }

            "try to select a non-existent manga list" {
                mockMangaFlow()

                viewModel.test(orbitTestScope) {
                    runOnCreate()
                    expectInitialState()
                    containerHost.selectList("NonExistent Manga List")
                    expectStateWithLists()
                }

                verify(exactly = 1) { observeManga() }
                verify(exactly = 1) { observeManga.flow }
            }

            listOf(
                Triple("non-existent entry", true, persistentListOf<MediaListItem.MangaListItem>()),
            ).forEach { (text, empty, result) ->
                "searching $text an entry should return $result" {
                    mockMangaFlow()

                    viewModel.test(orbitTestScope) {
                        runOnCreate()
                        expectInitialState()
                        expectStateWithLists()
                        containerHost.search(text)
                        expectState {
                            copy(
                                isLoading = false,
                                isEmpty = empty,
                                items = result,
                            )
                        }
                    }

                    verify(exactly = 1) { observeManga() }
                    verify(exactly = 1) { observeManga.flow }
                }
            }
        }
    }

    override suspend fun beforeTest(testCase: TestCase) {
        viewModel = MangaListsViewModel(updateList, observeManga)
    }

    override suspend fun afterAny(testCase: TestCase, result: TestResult) {
        clearAllMocks()
    }

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
            ).right(),
        )

        coJustRun { observeManga() }
    }

    private suspend fun OrbitTestContext<MangaState, *, *>.expectStateWithLists() {
        expectState { copy(isLoading = true) }
        expectState {
            copy(
                isLoading = false,
                isEmpty = false,
                name = "MyCustomMangaList",
                items = persistentListOf(mangaListItem1),
                isError = false,
            )
        }
    }
}
