package dev.alvr.katana.ui.lists.viewmodel

import androidx.lifecycle.SavedStateHandle
import arrow.core.left
import arrow.core.right
import dev.alvr.katana.common.core.empty
import dev.alvr.katana.common.core.zero
import dev.alvr.katana.common.tests.TestBase
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
import dev.alvr.katana.ui.lists.entities.UserList
import io.kotest.assertions.throwables.shouldThrowExactlyUnit
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.mockk.Ordering
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.verify
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.stream.Stream
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.orbitmvi.orbit.SuspendingTestContainerHost
import org.orbitmvi.orbit.test

private typealias AnimeState = ListState<MediaListItem.AnimeListItem>

@ExperimentalCoroutinesApi
internal class AnimeListsViewModelTest : TestBase() {
    @MockK
    private lateinit var observeAnime: ObserveAnimeListUseCase
    @MockK
    private lateinit var updateList: UpdateListUseCase
    @SpyK
    private var stateHandle = SavedStateHandle()

    private lateinit var viewModel:
        SuspendingTestContainerHost<ListState<MediaListItem.AnimeListItem>, Nothing, AnimeListsViewModel>

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

    override suspend fun beforeEach() {
        viewModel = AnimeListsViewModel(stateHandle, updateList, observeAnime).test(ListState())
    }

    @Nested
    @DisplayName("WHEN initializing the viewModel")
    inner class Init {
        @Nested
        @DisplayName("AND the collections are empty")
        inner class Empty {
            @Test
            @DisplayName("AND the collections are empty")
            fun `the collections are empty`() = runTest {
                // GIVEN
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

                // WHEN
                viewModel.runOnCreate()

                // THEN
                viewModel.assert(AnimeState()) {
                    states(*initialState)
                }

                verify(exactly = 1) { observeAnime() }
                verify(exactly = 1) { observeAnime.flow }
                verify(ordering = Ordering.ORDERED) {
                    stateHandle["userLists"] = emptyArray<String>()
                    stateHandle["collection"] = emptyMap<String, List<MediaListItem>>()

                    stateHandle.get<String>("collection")
                }
            }
        }

        @Test
        @DisplayName("AND the anime collection has entries THEN it should update its state with the mapped entry")
        fun `the anime collection has entries`() = runTest {
            // GIVEN
            mockAnimeFlow()

            // WHEN
            viewModel.runOnCreate()

            // THEN
            viewModel.assert(AnimeState()) {
                states(*initialStateWithLists)
            }

            verify(exactly = 1) { observeAnime() }
            verify(exactly = 1) { observeAnime.flow }
            verify(ordering = Ordering.ORDERED) {
                stateHandle["userLists"] = arrayOf(UserList("MyCustomAnimeList", 1), UserList("MyCustomAnimeList2", 1))
                stateHandle["collection"] = mapOf(
                    "MyCustomAnimeList" to listOf(animeListItem1),
                    "MyCustomAnimeList2" to listOf(animeListItem2),
                )

                stateHandle.get<String>("collection")
            }
        }

        @Test
        @DisplayName(
            """
            AND the anime collection has entries
            AND getting the userLists
            THEN the list should contain one element
            """,
        )
        fun `the anime collection has entries AND getting the userLists`() = runTest {
            // GIVEN
            mockAnimeFlow()

            // WHEN
            viewModel.runOnCreate()
            viewModel.testIntent {
                userLists
                    .shouldHaveSize(2)
                    .shouldContainInOrder(UserList("MyCustomAnimeList", 1), UserList("MyCustomAnimeList2", 1))
            }

            // THEN
            viewModel.assert(AnimeState()) { states(*initialStateWithLists) }
            verify(exactly = 1) { observeAnime() }
            verify(exactly = 1) { observeAnime.flow }
            verify(ordering = Ordering.ORDERED) {
                stateHandle["userLists"] = arrayOf(UserList("MyCustomAnimeList", 1), UserList("MyCustomAnimeList2", 1))
                stateHandle["collection"] = mapOf(
                    "MyCustomAnimeList" to listOf(animeListItem1),
                    "MyCustomAnimeList2" to listOf(animeListItem2),
                )

                stateHandle.get<String>("collection")
                stateHandle.get<Array<String>>("userLists")
            }
        }

        @Test
        @DisplayName("AND something went wrong collecting THEN update it state with isError = true")
        fun `something went wrong collecting`() = runTest {
            // GIVEN
            every { observeAnime.flow } returns flowOf(ListsFailure.GetMediaCollection.left())
            coJustRun { observeAnime() }

            // WHEN
            viewModel.runOnCreate()

            // THEN
            viewModel.assert(AnimeState()) {
                states(
                    { copy(isLoading = true) },
                    { copy(isError = true, isLoading = false, isEmpty = true) },
                )
            }

            verify(exactly = 1) { observeAnime() }
            verify(exactly = 1) { observeAnime.flow }
        }

        @Test
        @DisplayName("AND the array of anime list names is non-existent THEN the array should be empty")
        fun `the array of anime list names is non-existent`() = runTest {
            // GIVEN
            every { stateHandle.get<Array<String>>("userLists") } returns null

            // WHEN
            viewModel.testIntent { userLists.shouldBeEmpty() }

            // THEN
            verify(exactly = 1) { stateHandle.get<Array<String>>("userLists") }
        }
    }

    @Nested
    @DisplayName("WHEN adding a +1 to an entry")
    inner class PlusOne {
        @Test
        @DisplayName("AND is successful THEN it should call the updateList with the progress incremented by 1")
        fun `is successful`() = runTest {
            // GIVEN
            mockAnimeFlow()
            coEitherJustRun { updateList(any()) }

            // WHEN
            viewModel.runOnCreate()
            viewModel.testIntent { addPlusOne(animeListItem1.entryId) }

            // THEN
            verify(exactly = 1) { observeAnime() }
            verify(exactly = 1) { observeAnime.flow }
            verify(ordering = Ordering.ORDERED) {
                stateHandle["userLists"] = arrayOf(UserList("MyCustomAnimeList", 1), UserList("MyCustomAnimeList2", 1))
                stateHandle["collection"] = mapOf(
                    "MyCustomAnimeList" to listOf(animeListItem1),
                    "MyCustomAnimeList2" to listOf(animeListItem2),
                )

                stateHandle.get<String>("collection")
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

        @Test
        @DisplayName("AND the element is not found THEN it should throw `NoSuchElementException`")
        fun `the element is not found`() = runTest {
            // GIVEN
            mockAnimeFlow()

            // WHEN
            viewModel.runOnCreate()
            viewModel.testIntent {
                // THEN
                shouldThrowExactlyUnit<NoSuchElementException> { addPlusOne(234) }
            }

            // THEN
            verify(exactly = 1) { observeAnime() }
            verify(exactly = 1) { observeAnime.flow }
            verify(ordering = Ordering.ORDERED) {
                stateHandle["userLists"] = arrayOf(UserList("MyCustomAnimeList", 1), UserList("MyCustomAnimeList2", 1))
                stateHandle["collection"] = mapOf(
                    "MyCustomAnimeList" to listOf(animeListItem1),
                    "MyCustomAnimeList2" to listOf(animeListItem2),
                )

                stateHandle.get<String>("collection")
            }
        }
    }

    @Nested
    @DisplayName("WHEN searching")
    inner class Searching {
        @Test
        @DisplayName(
            """
            AND the anime collection has entries
            AND try to fetch a existent anime list
            THEN the state should be updated
            """,
        )
        fun `the anime collection has entries AND try to fetch a existent anime list`() = runTest {
            // GIVEN
            mockAnimeFlow()

            // WHEN
            viewModel.runOnCreate()
            viewModel.testIntent { selectList("MyCustomAnimeList2") }

            // THEN
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

            verify(exactly = 1) { observeAnime() }
            verify(exactly = 1) { observeAnime.flow }
            verify(ordering = Ordering.ORDERED) {
                stateHandle["userLists"] = arrayOf(UserList("MyCustomAnimeList", 1), UserList("MyCustomAnimeList2", 1))
                stateHandle["collection"] = mapOf(
                    "MyCustomAnimeList" to listOf(animeListItem1),
                    "MyCustomAnimeList2" to listOf(animeListItem2),
                )
            }
            verify(exactly = 2) {
                stateHandle.get<Collection<MediaListItem.AnimeListItem>>("collection")
            }
        }

        @Test
        @DisplayName("AND try to select a non-existent anime list THEN the state should be the same")
        fun `try to select a non-existent anime list`() = runTest {
            // GIVEN
            mockAnimeFlow()

            // WHEN
            viewModel.runOnCreate()
            viewModel.testIntent { selectList("NonExistent Anime List") }

            // THEN
            viewModel.assert(AnimeState()) {
                states(
                    *initialStateWithLists + emptyArray(), // No more state updates
                )
            }

            verify(exactly = 1) { observeAnime() }
            verify(exactly = 1) { observeAnime.flow }
            verify(ordering = Ordering.ORDERED) {
                stateHandle["userLists"] = arrayOf(UserList("MyCustomAnimeList", 1), UserList("MyCustomAnimeList2", 1))
                stateHandle["collection"] = mapOf(
                    "MyCustomAnimeList" to listOf(animeListItem1),
                    "MyCustomAnimeList2" to listOf(animeListItem2),
                )
            }
            verify(exactly = 2) {
                stateHandle.get<Collection<MediaListItem.AnimeListItem>>("collection")
            }
        }

        @Test
        @DisplayName("AND the collection of anime is non-existent THEN the state should be the same")
        fun `the collection of anime is non-existent`() = runTest {
            // GIVEN
            every { stateHandle.get<Collection<MediaListItem.AnimeListItem>>(any()) } returns null

            // WHEN
            viewModel.testIntent { selectList("MyCustomAnimeList") }

            // THEN
            viewModel.assert(AnimeState())

            verify(exactly = 1) {
                stateHandle.get<Collection<MediaListItem.AnimeListItem>>("collection")
            }
        }

        @ArgumentsSource(SearchArguments::class)
        @ParameterizedTest(name = "AND searching for {0} THEN the result should be {2}")
        fun `searching an entry`(
            text: String,
            empty: Boolean,
            result: ImmutableList<MediaListItem.AnimeListItem>,
        ) = runTest {
            // GIVEN
            mockAnimeFlow()

            // WHEN
            viewModel.runOnCreate()
            viewModel.testIntent { search(text) }

            // THEN
            viewModel.assert(AnimeState()) {
                states(
                    *initialStateWithLists,
                    {
                        copy(
                            isLoading = false,
                            isEmpty = empty,
                            items = result,
                        )
                    },
                )
            }

            verify(exactly = 1) { observeAnime() }
            verify(exactly = 1) { observeAnime.flow }
            verify(ordering = Ordering.ORDERED) {
                stateHandle["userLists"] = arrayOf(UserList("MyCustomAnimeList", 1), UserList("MyCustomAnimeList2", 1))
                stateHandle["collection"] = mapOf(
                    "MyCustomAnimeList" to listOf(animeListItem1),
                    "MyCustomAnimeList2" to listOf(animeListItem2),
                )

                stateHandle.get<String>("collection")
            }
        }
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

    private class SearchArguments : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<Arguments> =
            Stream.of(
                Arguments.of("non-existent entry", true, persistentListOf<MediaListItem.AnimeListItem>()),
                Arguments.of("OnE PiEcE", false, persistentListOf(animeListItem1)),
            )
    }
}
