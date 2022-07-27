package dev.alvr.katana.ui.login.view

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dev.alvr.katana.common.tests.ComposeTest
import dev.alvr.katana.ui.login.GET_STARTED_BUTTON_TAG
import dev.alvr.katana.ui.login.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalCoroutinesApi
internal class LoginScreenTest : ComposeTest() {
    @Test
    fun `Logo and Description should be displayed`() {
        composeTestRule.setContent {
            Header()
            Bottom()
        }

        composeTestRule
            .onNodeWithContentDescription(label = context.getString(R.string.content_description_katana_logo))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(text = context.getString(R.string.header_katana_description))
            .assertIsDisplayed()
    }

    @Test
    fun `GetStarted should be displayed`() {
        composeTestRule.setContent {
            Header()
            Bottom()
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
    fun `Begin block should be displayed after clicking`() {
        composeTestRule.setContent {
            Header()
            Bottom()
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
