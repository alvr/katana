package dev.alvr.katana.ui.explore.view

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.swipeRight
import dev.alvr.katana.common.tests.ComposeTest
import dev.alvr.katana.ui.explore.R
import org.junit.Before
import org.junit.Test

internal class ExploreScreenTest : ComposeTest() {
    @Before
    override fun setUp() {
        super.setUp()
        composeTestRule.setContent {
            Explore()
        }
    }

    @Test
    fun test_animeTabSelectedByDefault() {
        composeTestRule
            .onAllNodesWithText(text = context.getString(R.string.tab_anime))
            .onFirst()
            .assertIsSelected()
            .assertIsDisplayed()

        composeTestRule
            .onAllNodesWithText(text = context.getString(R.string.tab_manga))
            .onFirst()
            .assertIsNotSelected()
            .assertIsDisplayed()
    }

    @Test
    fun test_mangaTabSelectedWhenSwiping() {
        composeTestRule.onRoot().performTouchInput {
            swipeLeft()
        }

        composeTestRule
            .onAllNodesWithText(text = context.getString(R.string.tab_anime))
            .onFirst()
            .assertIsNotSelected()
            .assertIsDisplayed()

        composeTestRule
            .onAllNodesWithText(text = context.getString(R.string.tab_manga))
            .onFirst()
            .assertIsSelected()
            .assertIsDisplayed()
    }

    @Test
    fun test_mangaTabSelectedWhenClicking() {
        composeTestRule
            .onAllNodesWithText(text = context.getString(R.string.tab_manga))
            .onFirst()
            .performClick()
            .assertIsSelected()
            .assertIsDisplayed()

        composeTestRule
            .onAllNodesWithText(text = context.getString(R.string.tab_anime))
            .onFirst()
            .assertIsNotSelected()
            .assertIsDisplayed()
    }

    @Test
    fun test_animeTabSelectedWhenSwipingLeftAndRight() {
        composeTestRule.onRoot().performTouchInput {
            swipeLeft()
            swipeRight()
        }

        composeTestRule
            .onAllNodesWithText(text = context.getString(R.string.tab_anime))
            .onFirst()
            .assertIsSelected()
            .assertIsDisplayed()

        composeTestRule
            .onAllNodesWithText(text = context.getString(R.string.tab_manga))
            .onFirst()
            .assertIsNotSelected()
            .assertIsDisplayed()
    }
}
