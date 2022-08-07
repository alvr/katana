package dev.alvr.katana.ui.lists.view

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
import androidx.compose.ui.test.onNodeWithText
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

@ExperimentalCoroutinesApi
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
            startedAt = Arb.localDate().next(),
            completedAt = Arb.localDate().next(),
            updatedAt = Arb.localDateTime().next(),
            nextEpisode = null,
        ),
    )

    @Test
    fun `when the list is empty, the emptyStateRes should be displayed`() {
        composeTestRule.setContent {
            MediaList(
                items = persistentListOf(),
                isEmpty = true,
                isLoading = false,
                emptyStateRes = R.string.empty_anime_list,
                onRefresh = { mockk() },
                addPlusOne = { mockk() },
                editEntry = { mockk() },
                mediaDetails = { mockk() },
            )
        }

        composeTestRule
            .onNodeWithText(text = context.getString(R.string.empty_anime_list))
            .assertIsDisplayed()
    }

    @Test
    fun `swipe down the list should trigger the refresh`() {
        val refresh = mockk<() -> Unit>()
        coJustRun { refresh() }

        composeTestRule.setContent {
            MediaList(
                items = persistentListOf(),
                isEmpty = true,
                isLoading = false,
                emptyStateRes = R.string.empty_anime_list,
                onRefresh = refresh,
                addPlusOne = { mockk() },
                editEntry = { mockk() },
                mediaDetails = { mockk() },
            )
        }

        verify(exactly = 0) { refresh() }
        composeTestRule.onRoot().performTouchInput { swipeDown() }
        verify(exactly = 1) { refresh() }
    }

    @Test
    fun `list with three items, both are displayed on the screen`() {
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
                items = list,
                isEmpty = list.isEmpty(),
                isLoading = false,
                emptyStateRes = R.string.empty_anime_list,
                onRefresh = { mockk() },
                addPlusOne = { mockk() },
                editEntry = { mockk() },
                mediaDetails = { mockk() },
            )
        }

        composeTestRule
            .mediaItems()
            .assertCountEquals(NUMBER_OF_ITEMS)
    }

    @Test
    @Suppress("LongMethod")
    fun `an anime item should paint the correct data`() {
        val addPlusOne = mockk<(MediaListItem) -> Unit>()
        val editEntry = mockk<(Int) -> Unit>()
        val mediaDetails = mockk<(Int) -> Unit>()

        coJustRun { addPlusOne(any()) }
        coJustRun { editEntry(any()) }
        coJustRun { mediaDetails(any()) }

        composeTestRule.setContent {
            MediaList(
                items = mockedAnimeList,
                isEmpty = mockedAnimeList.isEmpty(),
                isLoading = false,
                emptyStateRes = R.string.empty_anime_list,
                onRefresh = { mockk() },
                addPlusOne = addPlusOne,
                editEntry = editEntry,
                mediaDetails = mediaDetails,
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

                        if (item.nextEpisode != null) {
                            with(item.nextEpisode) {
                                assertTextContains(number.toString(), substring = true)
                                assertTextContains(date.year.toString(), substring = true)
                            }
                        } else {
                            assert(hasText(context.getString(R.string.next_episode_separator)).not())
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

                            verify(exactly = 0) { addPlusOne(item) }
                            performClick()
                            verify(exactly = 1) { addPlusOne(item) }

                            onChild().run {
                                val total = item.total ?: String.unknown
                                assertTextContains(
                                    context.getString(R.string.progress, item.progress, total),
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
    fun `a manga item should paint the correct data`() {
        val addPlusOne = mockk<(MediaListItem) -> Unit>()
        val editEntry = mockk<(Int) -> Unit>()
        val mediaDetails = mockk<(Int) -> Unit>()

        val list = persistentListOf<MediaListItem.MangaListItem>()

        composeTestRule.setContent {
            MediaList(
                items = list,
                isEmpty = list.isEmpty(),
                isLoading = false,
                emptyStateRes = R.string.empty_anime_list,
                onRefresh = { mockk() },
                addPlusOne = addPlusOne,
                editEntry = editEntry,
                mediaDetails = mediaDetails,
            )
        }
    }

    private fun ComposeContentTestRule.mediaList() = onRoot(useUnmergedTree = true).onChildAt(0)
    private fun ComposeContentTestRule.mediaItems() = mediaList().onChildren()

    private companion object {
        const val NUMBER_OF_ITEMS = 3
    }
}
