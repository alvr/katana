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
import dev.alvr.katana.domain.lists.usecases.ObserveAnimeListUseCase
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

private typealias AnimeState = ListState<MediaListItem.AnimeListItem>

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

                    verify(exactly = 1) { observeAnime() }
                    verify(exactly = 1) { observeAnime.flow }
                }
            }

            "the anime collection has entries" {
                mockAnimeFlow()

                viewModel.test(orbitTestScope) {
                    runOnCreate()
                    expectInitialState()
                    expectStateWithLists()
                }

                verify(exactly = 1) { observeAnime() }
                verify(exactly = 1) { observeAnime.flow }
            }

            "the anime collection has entries AND getting the userLists" {
                mockAnimeFlow()

                viewModel.test(orbitTestScope) {
                    runOnCreate()
                    expectInitialState()
                    expectStateWithLists()

                    containerHost.userLists
                        .shouldHaveSize(2)
                        .shouldContainInOrder(
                            UserList("MyCustomAnimeList" to 1),
                            UserList("MyCustomAnimeList2" to 1),
                        )
                }

                verify(exactly = 1) { observeAnime() }
                verify(exactly = 1) { observeAnime.flow }
            }

            "something went wrong collecting" {
                every { observeAnime.flow } returns flowOf(ListsFailure.GetMediaCollection.left())
                coJustRun { observeAnime() }

                viewModel.test(orbitTestScope) {
                    runOnCreate()
                    expectInitialState()
                    expectState { copy(isLoading = true) }
                    expectState { copy(isError = true, isLoading = false, isEmpty = true) }
                }

                verify(exactly = 1) { observeAnime() }
                verify(exactly = 1) { observeAnime.flow }
            }
        }

        "plus one" - {
            "is successful" {
                mockAnimeFlow()
                coEitherJustRun { updateList(any()) }

                viewModel.test(orbitTestScope) {
                    runOnCreate()
                    expectInitialState()
                    expectState { copy(isLoading = true) }
                    containerHost.addPlusOne(animeListItem1.entryId)
                    cancelAndIgnoreRemainingItems()
                }

                verify(exactly = 1) { observeAnime() }
                verify(exactly = 1) { observeAnime.flow }

                coVerify(exactly = 1) {
                    updateList(animeListItem1.copy(progress = animeListItem1.progress.inc()).toMediaList())
                }
            }

            "the element is not found" {
                mockAnimeFlow()

                viewModel.test(orbitTestScope) {
                    runOnCreate()
                    expectInitialState()
                    expectState { copy(isLoading = true) }
                    shouldThrowExactlyUnit<NoSuchElementException> { containerHost.addPlusOne(234) }
                    cancelAndIgnoreRemainingItems()
                }

                verify(exactly = 1) { observeAnime() }
                verify(exactly = 1) { observeAnime.flow }
            }
        }

        "searching" - {
            "the anime collection has entries" {
                mockAnimeFlow()

                viewModel.test(orbitTestScope) {
                    runOnCreate()
                    expectInitialState()
                    expectStateWithLists()
                    containerHost.selectList("MyCustomAnimeList2")
                    expectState {
                        copy(
                            name = "MyCustomAnimeList2",
                            items = persistentListOf(animeListItem2),
                        )
                    }
                }

                verify(exactly = 1) { observeAnime() }
                verify(exactly = 1) { observeAnime.flow }
            }

            "try to select a non-existent anime list" {
                mockAnimeFlow()

                viewModel.test(orbitTestScope) {
                    runOnCreate()
                    expectInitialState()
                    containerHost.selectList("NonExistent Anime List")
                    expectStateWithLists()
                }

                verify(exactly = 1) { observeAnime() }
                verify(exactly = 1) { observeAnime.flow }
            }

            listOf(
                Triple("non-existent entry", true, persistentListOf<MediaListItem.AnimeListItem>()),
            ).forEach { (text, empty, result) ->
                "searching $text an entry should return $result" {
                    mockAnimeFlow()

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

                    verify(exactly = 1) { observeAnime() }
                    verify(exactly = 1) { observeAnime.flow }
                }
            }
        }
    }

    override suspend fun beforeTest(testCase: TestCase) {
        viewModel = AnimeListsViewModel(updateList, observeAnime)
    }

    override suspend fun afterAny(testCase: TestCase, result: TestResult) {
        clearAllMocks()
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

    private suspend fun OrbitTestContext<AnimeState, *, *>.expectStateWithLists() {
        expectState { copy(isLoading = true) }
        expectState {
            copy(
                isLoading = false,
                isEmpty = false,
                name = "MyCustomAnimeList",
                items = persistentListOf(animeListItem1),
                isError = false,
            )
        }
    }
}
