package dev.alvr.katana.ui.login

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test

internal class LoginScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun test_logoAndDescriptionAreDisplayed() {
        composeTestRule.setContent {
            Login(token = null)
        }

        composeTestRule
            .onNodeWithContentDescription(label = context.getString(R.string.content_description_katana_logo))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(text = context.getString(R.string.header_katana_description))
            .assertIsDisplayed()
    }

    @Test
    fun test_getStartedBlockIsDisplayed() {
        composeTestRule.setContent {
            Login(token = null)
        }

        composeTestRule
            .onNodeWithText(text = context.getString(R.string.get_started_description))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(GET_STARTED_BUTTON_TAG)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(text = context.getString(R.string.begin_description))
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(text = context.getString(R.string.begin_login_button))
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(text = context.getString(R.string.begin_register_button))
            .assertDoesNotExist()
    }

    @Test
    fun test_beginBlockIsDisplayedAfterClick() {
        composeTestRule.setContent {
            Login(token = null)
        }

        composeTestRule
            .onNodeWithTag(GET_STARTED_BUTTON_TAG)
            .assertIsDisplayed()
            .performClick()

        composeTestRule
            .onNodeWithTag(GET_STARTED_BUTTON_TAG)
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(text = context.getString(R.string.begin_description))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(text = context.getString(R.string.begin_login_button))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(text = context.getString(R.string.begin_register_button))
            .assertIsDisplayed()
    }
}
