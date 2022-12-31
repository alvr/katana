package dev.alvr.katana.ui.lists.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.unit.dp
import dev.alvr.katana.common.core.dot
import dev.alvr.katana.common.core.empty
import dev.alvr.katana.common.core.unknown
import dev.alvr.katana.common.core.zero
import dev.alvr.katana.common.tests.ComposeTest
import dev.alvr.katana.ui.lists.R
import dev.alvr.katana.ui.lists.entities.MediaListItem
import dev.alvr.katana.ui.lists.view.components.ITEM_PLUSONE_TAG
import dev.alvr.katana.ui.lists.view.components.ITEM_SCORE_TAG
import dev.alvr.katana.ui.lists.view.components.ITEM_SUBTITLE_TAG
import dev.alvr.katana.ui.lists.view.components.ITEM_TITLE_TAG
import dev.alvr.katana.ui.lists.view.components.MediaList
import dev.alvr.katana.ui.lists.viewmodel.ListState
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.localDate
import io.kotest.property.arbitrary.localDateTime
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.string
import io.mockk.coJustRun
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDate
import java.time.LocalDateTime
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalMaterialApi
@ExperimentalCoroutinesApi
@ExperimentalFoundationApi
internal class MediaListComponentTest : ComposeTest() {
    private val mockedAnimeList = persistentListOf(
        MediaListItem.AnimeListItem(
            entryId = Arb.int().next(),
            mediaId = Arb.int().next(),
            title = "One Piece",
            score = 9.8,
            format = MediaListItem.Format.Tv,
            cover = Arb.string().next(),
            progress = 2000,
            total = null,
            repeat = Arb.int().next(),
            private = Arb.boolean().next(),
            notes = Arb.string().next(),
            hiddenFromStatusLists = Arb.boolean().next(),
            startDate = Arb.localDate().next(),
            endDate = Arb.localDate().next(),
            startedAt = Arb.localDate().next(),
            completedAt = Arb.localDate().next(),
            updatedAt = Arb.localDateTime().next(),
            nextEpisode = MediaListItem.AnimeListItem.NextEpisode(
                number = 2001,
                date = LocalDateTime.of(2022, 8, 14, 9, 0),
            ),
        ),
        MediaListItem.AnimeListItem(
            entryId = Arb.int().next(),
            mediaId = Arb.int().next(),
            title = "Monster",
            score = Double.zero,
            format = MediaListItem.Format.Tv,
            cover = Arb.string().next(),
            progress = 16,
            total = 74,
            repeat = Arb.int().next(),
            private = Arb.boolean().next(),
            notes = Arb.string().next(),
            hiddenFromStatusLists = Arb.boolean().next(),
            startDate = Arb.localDate().next(),
            endDate = Arb.localDate().next(),
            startedAt = Arb.localDate().next(),
            completedAt = Arb.localDate().next(),
            updatedAt = Arb.localDateTime().next(),
            nextEpisode = null,
        ),
        MediaListItem.AnimeListItem(
            entryId = Arb.int().next(),
            mediaId = Arb.int().next(),
            title = "Gintama",
            score = 10.0,
            format = MediaListItem.Format.Special,
            cover = Arb.string().next(),
            progress = 234,
            total = 234,
            repeat = Arb.int().next(),
            private = Arb.boolean().next(),
            notes = Arb.string().next(),
            hiddenFromStatusLists = Arb.boolean().next(),
            startDate = Arb.localDate().next(),
            endDate = Arb.localDate().next(),
            startedAt = Arb.localDate().next(),
            completedAt = Arb.localDate().next(),
            updatedAt = Arb.localDateTime().next(),
            nextEpisode = null,
        ),
    )

    @Test
    fun `swipe down the list should trigger the refresh`() = runTest {
        val refresh = mockk<() -> Unit>()
        coJustRun { refresh() }

        composeTestRule.setContent {
            MediaList(
                listState = ListState(
                    items = persistentListOf(),
                    isEmpty = true,
                ),
                onRefresh = refresh,
                onAddPlusOne = { mockk() },
                onEditEntry = { mockk() },
                onEntryDetails = { mockk() },
            )
        }

        verify(exactly = 0) { refresh() }
        composeTestRule.onRoot().performTouchInput { swipeDown() }
        verify(exactly = 1) { refresh() }
    }

    @Test
    fun `list with three items, both are displayed on the screen`() = runTest {
        val list = Arb.list(
            Arb.bind<MediaListItem.AnimeListItem>(
                mapOf(
                    LocalDate::class to Arb.localDate(),
                    LocalDateTime::class to Arb.localDateTime(),
                ),
            ),
            range = NUMBER_OF_ITEMS..NUMBER_OF_ITEMS,
        ).next().toImmutableList()

        composeTestRule.setContent {
            MediaList(
                listState = ListState(
                    items = list,
                    isEmpty = list.isEmpty(),
                ),
                onRefresh = { mockk() },
                onAddPlusOne = { mockk() },
                onEditEntry = { mockk() },
                onEntryDetails = { mockk() },
            )
        }

        composeTestRule
            .mediaItems()
            .assertCountEquals(NUMBER_OF_ITEMS)
    }

    @Test
    @Suppress("CognitiveComplexMethod", "LongMethod")
    fun `an anime item should paint the correct data`() = runTest {
        val addPlusOne = mockk<(Int) -> Unit>()
        val editEntry = mockk<(MediaListItem) -> Unit>()
        val mediaDetails = mockk<(Int) -> Unit>()

        coJustRun { addPlusOne(any()) }
        coJustRun { editEntry(any()) }
        coJustRun { mediaDetails(any()) }

        composeTestRule.setContent {
            MediaList(
                listState = ListState(
                    items = mockedAnimeList,
                    isEmpty = mockedAnimeList.isEmpty(),
                ),
                onRefresh = { mockk() },
                onAddPlusOne = addPlusOne,
                onEditEntry = editEntry,
                onEntryDetails = mediaDetails,
            )
        }

        mockedAnimeList.forEachIndexed { index, item ->
            composeTestRule.mediaList().onChildAt(index).run {
                assertHeightIsEqualTo(144.dp)
            }

            composeTestRule.mediaItems()[index].onChildAt(0).run {
                onChildren().run {
                    filterToOne(hasTestTag(ITEM_TITLE_TAG)).assertTextEquals(item.title)

                    filterToOne(hasTestTag(ITEM_SUBTITLE_TAG)).run {
                        assertIsDisplayed()
                        assertTextContains(context.getString(item.format.value), substring = true)

                        item.nextEpisode?.let { episode ->
                            assertTextContains(episode.number.toString(), substring = true)
                            assertTextContains(episode.date.year.toString(), substring = true)
                        } ?: run {
                            assert(hasText(context.getString(R.string.lists_entry_next_episode_separator)).not())
                        }
                    }

                    if (item.score == Double.zero) {
                        filter(hasTestTag(ITEM_SCORE_TAG)).assertCountEquals(0)
                    } else {
                        filterToOne(hasTestTag(ITEM_SCORE_TAG)).onChild().run {
                            assertIsDisplayed()
                            assertTextContains(
                                item.score.toString()
                                    .replace("${String.dot}${Int.zero}", String.empty),
                            )
                        }
                    }

                    if (item.progress == item.total) {
                        filter(hasTestTag(ITEM_PLUSONE_TAG)).assertCountEquals(0)
                    } else {
                        filterToOne(hasTestTag(ITEM_PLUSONE_TAG)).run {
                            assertIsDisplayed()

                            verify(exactly = 0) { addPlusOne(item.entryId) }
                            performClick()
                            verify(exactly = 1) { addPlusOne(item.entryId) }

                            onChild().run {
                                val total = item.total ?: String.unknown
                                assertTextContains(
                                    context.getString(R.string.lists_entry_progress, item.progress, total),
                                    substring = true,
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    fun `a manga item should paint the correct data`() = runTest {
        val addPlusOne = mockk<(Int) -> Unit>()
        val editEntry = mockk<(MediaListItem) -> Unit>()
        val mediaDetails = mockk<(Int) -> Unit>()

        val list = persistentListOf<MediaListItem.MangaListItem>()

        composeTestRule.setContent {
            MediaList(
                listState = ListState(
                    items = list,
                    isEmpty = list.isEmpty(),
                ),
                onRefresh = { mockk() },
                onAddPlusOne = addPlusOne,
                onEditEntry = editEntry,
                onEntryDetails = mediaDetails,
            )
        }
    }

    private fun ComposeContentTestRule.mediaList() = onRoot(useUnmergedTree = true).onChildAt(0)
    private fun ComposeContentTestRule.mediaItems() = mediaList().onChildren()

    private companion object {
        const val NUMBER_OF_ITEMS = 3
    }
}
