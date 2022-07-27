package dev.alvr.katana.common.tests

import androidx.compose.ui.test.junit4.createComposeRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog

@ExperimentalCoroutinesApi
@Config(instrumentedPackages = ["androidx.loader.content"])
abstract class ComposeTest : RoboTest() {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        ShadowLog.stream = System.out
    }
}
