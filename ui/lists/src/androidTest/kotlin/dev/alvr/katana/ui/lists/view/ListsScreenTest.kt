package dev.alvr.katana.ui.lists.view

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.swipeRight
import androidx.test.platform.app.InstrumentationRegistry
import dev.alvr.katana.ui.lists.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class ListsScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun init() {
        composeTestRule.setContent {
            Lists()
        }
    }

    @Test
    fun test_AnimeTabSelectedByDefault() {
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
    fun test_MangaTabSelectedWhenSwiping() {
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
    fun test_MangaTabSelectedWhenClicking() {
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
    fun test_AnimeTabSelectedWhenSwipingLeftAndRight() {
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
