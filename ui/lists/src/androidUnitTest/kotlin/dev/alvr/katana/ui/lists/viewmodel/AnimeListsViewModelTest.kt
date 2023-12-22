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
import dev.alvr.katana.domain.lists.usecases.ObserveAnimeListUseCase
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
import org.orbitmvi.orbit.test.test

private typealias AnimeState = ListState<MediaListItem.AnimeListItem>

@ExperimentalCoroutinesApi
internal class AnimeListsViewModelTest : TestBase() {
    @MockK
    private lateinit var observeAnime: ObserveAnimeListUseCase
    @MockK
    private lateinit var updateList: UpdateListUseCase

    private lateinit var viewModel: AnimeListsViewModel

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
        viewModel = AnimeListsViewModel(updateList, observeAnime)
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
                viewModel.test(this) {
                    runOnCreate()
                    expectInitialState()
                    initialState.forEach { expectState(it) }
                }

                // THEN
                verify(exactly = 1) { observeAnime() }
                verify(exactly = 1) { observeAnime.flow }
            }
        }

        @Test
        @DisplayName("AND the anime collection has entries THEN it should update its state with the mapped entry")
        fun `the anime collection has entries`() = runTest {
            // GIVEN
            mockAnimeFlow()

            // WHEN
            viewModel.test(this) {
                runOnCreate()
                expectInitialState()
                initialStateWithLists.forEach { expectState(it) }
            }

            verify(exactly = 1) { observeAnime() }
            verify(exactly = 1) { observeAnime.flow }
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
            viewModel.test(this) {
                runOnCreate()
                expectInitialState()
                initialStateWithLists.forEach { expectState(it) }

                containerHost.userLists
                    .shouldHaveSize(2)
                    .shouldContainInOrder(
                        UserList("MyCustomAnimeList" to 1),
                        UserList("MyCustomAnimeList2" to 1),
                    )
            }

            // THEN
            verify(exactly = 1) { observeAnime() }
            verify(exactly = 1) { observeAnime.flow }
        }

        @Test
        @DisplayName("AND something went wrong collecting THEN update it state with isError = true")
        fun `something went wrong collecting`() = runTest {
            // GIVEN
            every { observeAnime.flow } returns flowOf(ListsFailure.GetMediaCollection.left())
            coJustRun { observeAnime() }

            // WHEN
            viewModel.test(this) {
                runOnCreate()
                expectInitialState()
                expectState { copy(isLoading = true) }
                expectState { copy(isError = true, isLoading = false, isEmpty = true) }
            }

            // THEN
            verify(exactly = 1) { observeAnime() }
            verify(exactly = 1) { observeAnime.flow }
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
            viewModel.test(this) {
                runOnCreate()
                expectInitialState()
                expectState { copy(isLoading = true) }
                containerHost.addPlusOne(animeListItem1.entryId)
                cancelAndIgnoreRemainingItems()
            }

            // THEN
            verify(exactly = 1) { observeAnime() }
            verify(exactly = 1) { observeAnime.flow }

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
            mockAnimeFlow()

            // WHEN
            viewModel.test(this) {
                runOnCreate()
                expectInitialState()
                expectState { copy(isLoading = true) }
                shouldThrowExactlyUnit<NoSuchElementException> { containerHost.addPlusOne(234) }
                cancelAndIgnoreRemainingItems()
            }

            // THEN
            verify(exactly = 1) { observeAnime() }
            verify(exactly = 1) { observeAnime.flow }
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
            viewModel.test(this) {
                runOnCreate()
                expectInitialState()
                containerHost.selectList("MyCustomAnimeList2")
                initialStateWithLists.forEach { expectState(it) }
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

        @Test
        @DisplayName("AND try to select a non-existent anime list THEN the state should be the same")
        fun `try to select a non-existent anime list`() = runTest {
            // GIVEN
            mockAnimeFlow()

            // WHEN
            viewModel.test(this) {
                runOnCreate()
                expectInitialState()
                containerHost.selectList("NonExistent Anime List")
                (initialStateWithLists + emptyArray()).forEach { expectState(it) }
            }

            // THEN
            verify(exactly = 1) { observeAnime() }
            verify(exactly = 1) { observeAnime.flow }
        }

        @Disabled
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
            verify(exactly = 1) { observeAnime() }
            verify(exactly = 1) { observeAnime.flow }
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
                Arguments.of(
                    "non-existent entry",
                    true,
                    persistentListOf<MediaListItem.AnimeListItem>(),
                ),
                Arguments.of("OnE PiEcE", false, persistentListOf(animeListItem1)),
            )
    }
}
