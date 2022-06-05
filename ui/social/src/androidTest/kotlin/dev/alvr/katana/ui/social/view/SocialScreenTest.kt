package dev.alvr.katana.ui.social.view

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
import dev.alvr.katana.ui.social.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class SocialScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun init() {
        composeTestRule.setContent {
            Social()
        }
    }

    @Test
    fun test_FollowingTabSelectedByDefault() {
        composeTestRule
            .onAllNodesWithText(text = context.getString(R.string.tab_following))
            .onFirst()
            .assertIsSelected()
            .assertIsDisplayed()

        composeTestRule
            .onAllNodesWithText(text = context.getString(R.string.tab_global))
            .onFirst()
            .assertIsNotSelected()
            .assertIsDisplayed()
    }

    @Test
    fun test_GlobalTabSelectedWhenSwiping() {
        composeTestRule.onRoot().performTouchInput {
            swipeLeft()
        }

        composeTestRule
            .onAllNodesWithText(text = context.getString(R.string.tab_following))
            .onFirst()
            .assertIsNotSelected()
            .assertIsDisplayed()

        composeTestRule
            .onAllNodesWithText(text = context.getString(R.string.tab_global))
            .onFirst()
            .assertIsSelected()
            .assertIsDisplayed()
    }

    @Test
    fun test_GlobalTabSelectedWhenClicking() {
        composeTestRule
            .onAllNodesWithText(text = context.getString(R.string.tab_global))
            .onFirst()
            .performClick()
            .assertIsSelected()
            .assertIsDisplayed()

        composeTestRule
            .onAllNodesWithText(text = context.getString(R.string.tab_following))
            .onFirst()
            .assertIsNotSelected()
            .assertIsDisplayed()
    }

    @Test
    fun test_FollowingTabSelectedWhenSwipingLeftAndRight() {
        composeTestRule.onRoot().performTouchInput {
            swipeLeft()
            swipeRight()
        }

        composeTestRule
            .onAllNodesWithText(text = context.getString(R.string.tab_following))
            .onFirst()
            .assertIsSelected()
            .assertIsDisplayed()

        composeTestRule
            .onAllNodesWithText(text = context.getString(R.string.tab_global))
            .onFirst()
            .assertIsNotSelected()
            .assertIsDisplayed()
    }
}
