package dev.alvr.katana.ui.explore.view

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.swipeRight
import dev.alvr.katana.common.tests.ComposeTest
import dev.alvr.katana.ui.explore.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalCoroutinesApi
internal class ExploreScreenTest : ComposeTest() {
    override fun setUp() {
        super.setUp()
        composeTestRule.setContent {
            Explore()
        }
    }

    @Test
    fun `Anime tab should be selected by default`() {
        composeTestRule
            .onAllNodesWithText(text = context.getString(R.string.tab_anime))
            .filter(hasClickAction())
            .onFirst()
            .assertIsSelected()
            .assertIsDisplayed()

        composeTestRule
            .onAllNodesWithText(text = context.getString(R.string.tab_manga))
            .filter(hasClickAction())
            .onFirst()
            .assertIsNotSelected()
            .assertIsDisplayed()
    }

    @Test
    fun `Manga tab should be selected when swiping left`() {
        composeTestRule.onRoot().performTouchInput {
            swipeLeft()
        }

        composeTestRule
            .onAllNodesWithText(text = context.getString(R.string.tab_anime))
            .filter(hasClickAction())
            .onFirst()
            .assertIsNotSelected()
            .assertIsDisplayed()

        composeTestRule
            .onAllNodesWithText(text = context.getString(R.string.tab_manga))
            .filter(hasClickAction())
            .onFirst()
            .assertIsSelected()
            .assertIsDisplayed()
    }

    @Test
    fun `Manga tab should be selected when clicking its tab`() {
        composeTestRule
            .onAllNodesWithText(text = context.getString(R.string.tab_manga))
            .filter(hasClickAction())
            .onFirst()
            .performClick()
            .assertIsSelected()
            .assertIsDisplayed()

        composeTestRule
            .onAllNodesWithText(text = context.getString(R.string.tab_anime))
            .filter(hasClickAction())
            .onFirst()
            .assertIsNotSelected()
            .assertIsDisplayed()
    }

    @Test
    fun `Anime tab should be selected when swiping left and right`() {
        composeTestRule.onRoot().performTouchInput {
            swipeLeft()
            swipeRight()
        }

        composeTestRule
            .onAllNodesWithText(text = context.getString(R.string.tab_anime))
            .filter(hasClickAction())
            .onFirst()
            .assertIsSelected()
            .assertIsDisplayed()

        composeTestRule
            .onAllNodesWithText(text = context.getString(R.string.tab_manga))
            .filter(hasClickAction())
            .onFirst()
            .assertIsNotSelected()
            .assertIsDisplayed()
    }
}
