package dev.alvr.katana.common.tests

import androidx.annotation.CallSuper
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Before
import org.junit.Rule
import org.robolectric.annotation.Config

@Config(application = HiltTestApplication::class)
abstract class HiltTest : RoboTest() {
    @get:Rule
    @Suppress("LeakingThis")
    internal val hiltRule = HiltAndroidRule(this)

    @Before
    @CallSuper
    open fun setUp() {
        hiltRule.inject()
    }
}
