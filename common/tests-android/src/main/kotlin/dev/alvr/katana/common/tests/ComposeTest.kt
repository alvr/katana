package dev.alvr.katana.common.tests

import android.content.Context
import androidx.annotation.CallSuper
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.Rule
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog

@Config(instrumentedPackages = ["androidx.loader.content"])
abstract class ComposeTest : RoboTest() {
    @get:Rule
    val composeTestRule = createComposeRule()

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    @CallSuper
    @Throws(Exception::class)
    open fun setUp() {
        ShadowLog.stream = System.out
    }
}
