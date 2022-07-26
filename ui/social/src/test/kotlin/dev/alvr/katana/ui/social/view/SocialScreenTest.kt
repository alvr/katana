package dev.alvr.katana.ui.social.view

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
import dev.alvr.katana.ui.social.R
import org.junit.Before
import org.junit.Test

internal class SocialScreenTest : ComposeTest() {
    @Before
    override fun setUp() {
        super.setUp()
        composeTestRule.setContent {
            Social()
        }
    }

    @Test
    fun test_followingTabSelectedByDefault() {
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
    fun test_globalTabSelectedWhenSwiping() {
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
    fun test_globalTabSelectedWhenClicking() {
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
    fun test_followingTabSelectedWhenSwipingLeftAndRight() {
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
