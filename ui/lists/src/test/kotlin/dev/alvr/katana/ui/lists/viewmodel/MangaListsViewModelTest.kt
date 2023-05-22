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
import dev.alvr.katana.domain.lists.usecases.ObserveMangaListUseCase
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
import java.util.stream.Stream
import korlibs.time.Date
import korlibs.time.DateTimeTz
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

private typealias MangaState = ListState<MediaListItem.MangaListItem>

@ExperimentalCoroutinesApi
internal class MangaListsViewModelTest : TestBase() {
    @MockK
    private lateinit var observeManga: ObserveMangaListUseCase
    @MockK
    private lateinit var updateList: UpdateListUseCase
    @SpyK
    private var stateHandle = SavedStateHandle()

    private lateinit var viewModel:
        SuspendingTestContainerHost<ListState<MediaListItem.MangaListItem>, Nothing, MangaListsViewModel>

    private val initialStateWithLists: Array<MangaState.() -> MangaState>
        get() = arrayOf(
            { copy(isLoading = true) },
            {
                copy(
                    isLoading = false,
                    isEmpty = false,
                    name = "MyCustomMangaList",
                    items = persistentListOf(mangaListItem1),
                    isError = false,
                )
            },
        )

    override suspend fun beforeEach() {
        viewModel = MangaListsViewModel(stateHandle, updateList, observeManga).test(ListState())
    }

    @Nested
    @DisplayName("WHEN initializing the viewModel")
    inner class Init {
        @Nested
        @DisplayName("AND the collections are empty")
        inner class Empty {
            @Test
            @DisplayName("THEN the collections observed should are empty")
            fun `the collections are empty`() = runTest {
                // GIVEN
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

                every { observeManga.flow } returns flowOf(
                    MediaCollection<MediaEntry.Manga>(lists = emptyList()).right(),
                )
                coJustRun { observeManga() }

                // WHEN
                viewModel.runOnCreate()

                // THEN
                viewModel.assert(MangaState()) { states(*initialState) }

                verify(exactly = 1) { observeManga() }
                verify(exactly = 1) { observeManga.flow }
                verify(ordering = Ordering.ORDERED) {
                    stateHandle["collection"] = emptyMap<String, List<MediaListItem>>()
                    stateHandle["userLists"] = emptyArray<String>()

                    stateHandle.get<String>("collection")
                }
            }
        }

        @Test
        @DisplayName("AND the manga collection has entries THEN it should update its state with the mapped entry")
        fun `the manga collection has entries`() = runTest {
            // GIVEN
            mockMangaFlow()

            // WHEN
            viewModel.runOnCreate()

            // THEN
            viewModel.assert(MangaState()) { states(*initialStateWithLists) }

            verify(exactly = 1) { observeManga() }
            verify(exactly = 1) { observeManga.flow }
            verify(ordering = Ordering.ORDERED) {
                verifyStateHandle()

                stateHandle.get<String>("collection")
            }
        }

        @Test
        @DisplayName(
            """
            AND the manga collection has entries
            AND getting the userLists
            THEN the list should contain one element
            """,
        )
        fun `the manga collection has entries AND getting the userLists`() = runTest {
            // GIVEN
            mockMangaFlow()

            // WHEN
            viewModel.runOnCreate()
            viewModel.testIntent {
                userLists
                    .shouldHaveSize(2)
                    .shouldContainInOrder(UserList("MyCustomMangaList" to 1), UserList("MyCustomMangaList2" to 1))
            }

            // THEN
            viewModel.assert(MangaState()) { states(*initialStateWithLists) }
            verify(exactly = 1) { observeManga() }
            verify(exactly = 1) { observeManga.flow }
            verify(ordering = Ordering.ORDERED) {
                verifyStateHandle()

                stateHandle.get<String>("collection")
                stateHandle.get<Array<String>>("userLists")
            }
        }

        @Test
        @DisplayName("AND something went wrong collecting THEN update it state with isError = true")
        fun `something went wrong collecting`() = runTest {
            // GIVEN
            every { observeManga.flow } returns flowOf(ListsFailure.GetMediaCollection.left())
            coJustRun { observeManga() }

            // WHEN
            viewModel.runOnCreate()

            // THEN
            viewModel.assert(MangaState()) {
                states(
                    { copy(isLoading = true) },
                    { copy(isError = true, isLoading = false, isEmpty = true) },
                )
            }

            verify(exactly = 1) { observeManga() }
            verify(exactly = 1) { observeManga.flow }
        }

        @Test
        @DisplayName("AND the array of manga list names is non-existent THEN the array should be empty")
        fun `the array of manga list names is non-existent`() = runTest {
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
            mockMangaFlow()
            coEitherJustRun { updateList(any()) }

            // WHEN
            viewModel.runOnCreate()
            viewModel.testIntent { addPlusOne(mangaListItem1.entryId) }

            // THEN
            verify(exactly = 1) { observeManga() }
            verify(exactly = 1) { observeManga.flow }
            verify(ordering = Ordering.ORDERED) {
                verifyStateHandle()

                stateHandle.get<String>("collection")
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
                        startedAt = Date(Int.MAX_VALUE),
                        completedAt = Date(Int.MAX_VALUE),
                        updatedAt = DateTimeTz.fromUnix(Long.MAX_VALUE),
                    ),
                )
            }
        }

        @Test
        @DisplayName("AND the element is not found THEN it should throw `NoSuchElementException`")
        fun `the element is not found`() = runTest {
            // GIVEN
            mockMangaFlow()

            // WHEN
            viewModel.runOnCreate()
            viewModel.testIntent {
                // THEN
                shouldThrowExactlyUnit<NoSuchElementException> { addPlusOne(234) }
            }

            // THEN
            verify(exactly = 1) { observeManga() }
            verify(exactly = 1) { observeManga.flow }
            verify(ordering = Ordering.ORDERED) {
                verifyStateHandle()

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
            AND the manga collection has entries
            AND try to fetch a existent manga list
            THEN the state should be updated
            """,
        )
        fun `the manga collection has entries AND try to fetch a existent manga list`() = runTest {
            // GIVEN
            mockMangaFlow()

            // WHEN
            viewModel.runOnCreate()
            viewModel.testIntent { selectList("MyCustomMangaList2") }

            // THEN
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

            verify(exactly = 1) { observeManga() }
            verify(exactly = 1) { observeManga.flow }
            verify(ordering = Ordering.ORDERED) { verifyStateHandle() }
            verify(exactly = 2) {
                stateHandle.get<ListsCollection<MediaListItem.MangaListItem>>("collection")
            }
        }

        @Test
        @DisplayName("AND try to select a non-existent manga list THEN the state should be the same")
        fun `try to select a non-existent manga list`() = runTest {
            // GIVEN
            mockMangaFlow()

            // WHEN
            viewModel.runOnCreate()
            viewModel.testIntent { selectList("NonExistent Manga List") }

            // THEN
            viewModel.assert(MangaState()) {
                states(
                    *initialStateWithLists + emptyArray(), // No more state updates
                )
            }

            verify(exactly = 1) { observeManga() }
            verify(exactly = 1) { observeManga.flow }
            verify(ordering = Ordering.ORDERED) { verifyStateHandle() }
            verify(exactly = 2) {
                stateHandle.get<ListsCollection<MediaListItem.MangaListItem>>("collection")
            }
        }

        @Test
        @DisplayName("AND the collection of manga is non-existent THEN the state should be the same")
        fun `the collection of manga is non-existent`() = runTest {
            // GIVEN
            every { stateHandle.get<ListsCollection<MediaListItem.MangaListItem>>(any()) } returns null

            // WHEN
            viewModel.testIntent { selectList("MyCustomMangaList") }

            // THEN
            viewModel.assert(MangaState())

            verify(exactly = 1) {
                stateHandle.get<ListsCollection<MediaListItem.MangaListItem>>("collection")
            }
        }

        @ArgumentsSource(SearchArguments::class)
        @ParameterizedTest(name = "AND searching for {0} THEN the result should be {2}")
        fun `searching an entry`(
            text: String,
            empty: Boolean,
            result: ImmutableList<MediaListItem.MangaListItem>,
        ) = runTest {
            // GIVEN
            mockMangaFlow()

            // WHEN
            viewModel.runOnCreate()
            viewModel.testIntent { search(text) }

            // THEN
            viewModel.assert(MangaState()) {
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

            verify(exactly = 1) { observeManga() }
            verify(exactly = 1) { observeManga.flow }
            verify(ordering = Ordering.ORDERED) {
                verifyStateHandle()

                stateHandle.get<String>("collection")
            }
        }
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

    private fun verifyStateHandle() {
        stateHandle["collection"] = mapOf(
            "MyCustomMangaList" to listOf(mangaListItem1),
            "MyCustomMangaList2" to listOf(mangaListItem2),
        )
        stateHandle["userLists"] = arrayOf(
            UserList("MyCustomMangaList" to 1), UserList("MyCustomMangaList2" to 1),
        )
    }

    private class SearchArguments : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<Arguments> =
            Stream.of(
                Arguments.of("non-existent entry", true, persistentListOf<MediaListItem.MangaListItem>()),
                Arguments.of("OnE PiEcE", false, persistentListOf(mangaListItem1)),
            )
    }
}
