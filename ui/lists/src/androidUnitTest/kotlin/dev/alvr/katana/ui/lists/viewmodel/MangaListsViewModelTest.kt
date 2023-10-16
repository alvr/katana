package dev.alvr.katana.ui.lists.viewmodel

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
import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import java.util.stream.Stream
import korlibs.time.Date
import korlibs.time.DateTimeTz
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.orbitmvi.orbit.test
import org.orbitmvi.orbit.test.test

private typealias MangaState = ListState<MediaListItem.MangaListItem>

@ExperimentalCoroutinesApi
internal class MangaListsViewModelTest : TestBase() {
    @MockK
    private lateinit var observeManga: ObserveMangaListUseCase
    @MockK
    private lateinit var updateList: UpdateListUseCase

    private lateinit var viewModel: MangaListsViewModel

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
        viewModel = MangaListsViewModel(updateList, observeManga)
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
                viewModel.test(this) {
                    runOnCreate()
                    expectInitialState()
                    initialState.forEach { expectState(it) }
                }

                // THEN
                verify(exactly = 1) { observeManga() }
                verify(exactly = 1) { observeManga.flow }
            }
        }

        @Test
        @DisplayName("AND the manga collection has entries THEN it should update its state with the mapped entry")
        fun `the manga collection has entries`() = runTest {
            // GIVEN
            mockMangaFlow()

            // WHEN
            viewModel.test(this) {
                runOnCreate()
                expectInitialState()
                initialStateWithLists.forEach { expectState(it) }
            }

            // THEN
            verify(exactly = 1) { observeManga() }
            verify(exactly = 1) { observeManga.flow }
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
            viewModel.test(this) {
                runOnCreate()
                expectInitialState()
                initialStateWithLists.forEach { expectState(it) }

                containerHost.userLists
                    .shouldHaveSize(2)
                    .shouldContainInOrder(
                        UserList("MyCustomMangaList" to 1),
                        UserList("MyCustomMangaList2" to 1),
                    )
            }

            // THEN
            verify(exactly = 1) { observeManga() }
            verify(exactly = 1) { observeManga.flow }
        }

        @Test
        @DisplayName("AND something went wrong collecting THEN update it state with isError = true")
        fun `something went wrong collecting`() = runTest {
            // GIVEN
            every { observeManga.flow } returns flowOf(ListsFailure.GetMediaCollection.left())
            coJustRun { observeManga() }

            // WHEN
            viewModel.test(this) {
                runOnCreate()
                expectInitialState()
                expectState { copy(isLoading = true) }
                expectState { copy(isError = true, isLoading = false, isEmpty = true) }
            }

            // THEN
            verify(exactly = 1) { observeManga() }
            verify(exactly = 1) { observeManga.flow }
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
            viewModel.test(this) {
                runOnCreate()
                expectInitialState()
                expectState { copy(isLoading = true) }
                containerHost.addPlusOne(mangaListItem1.entryId)
                cancelAndIgnoreRemainingItems()
            }

            // THEN
            verify(exactly = 1) { observeManga() }
            verify(exactly = 1) { observeManga.flow }

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
            viewModel.test(this) {
                runOnCreate()
                expectInitialState()
                expectState { copy(isLoading = true) }
                shouldThrowExactlyUnit<NoSuchElementException> { containerHost.addPlusOne(234) }
                cancelAndIgnoreRemainingItems()
            }

            // THEN
            verify(exactly = 1) { observeManga() }
            verify(exactly = 1) { observeManga.flow }
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
            viewModel.test(this) {
                runOnCreate()
                expectInitialState()
                containerHost.selectList("MyCustomMangaList2")
                initialStateWithLists.forEach { expectState(it) }
                expectState {
                    copy(
                        name = "MyCustomMangaList2",
                        items = persistentListOf(mangaListItem2),
                    )
                }
            }

            // THEN
            verify(exactly = 1) { observeManga() }
            verify(exactly = 1) { observeManga.flow }
        }

        @Test
        @DisplayName("AND try to select a non-existent manga list THEN the state should be the same")
        fun `try to select a non-existent manga list`() = runTest {
            // GIVEN
            mockMangaFlow()

            // WHEN
            viewModel.test(this) {
                runOnCreate()
                expectInitialState()
                containerHost.selectList("NonExistent Manga List")
                (initialStateWithLists + emptyArray()).forEach { expectState(it) }
            }

            // THEN
            verify(exactly = 1) { observeManga() }
            verify(exactly = 1) { observeManga.flow }
        }

        @Disabled
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
            viewModel.test(this) {
                runOnCreate()
                expectInitialState()
                containerHost.search(text)
                initialStateWithLists.forEach { expectState(it) }
                expectState {
                    copy(
                        isLoading = false,
                        isEmpty = empty,
                        items = result,
                    )
                }
            }

            // THEN
            verify(exactly = 1) { observeManga() }
            verify(exactly = 1) { observeManga.flow }
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

    private class SearchArguments : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<Arguments> =
            Stream.of(
                Arguments.of("non-existent entry", true, persistentListOf<MediaListItem.MangaListItem>()),
                Arguments.of("OnE PiEcE", false, persistentListOf(mangaListItem1)),
            )
    }
}
