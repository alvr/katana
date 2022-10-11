package dev.alvr.katana.common.tests

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@Config(application = HiltTestApplication::class)
abstract class HiltTest : RoboTest() {
    @get:Rule
    internal val hiltRule by lazy { HiltAndroidRule(this) }

    override fun setUp() {
        super.setUp()
        hiltRule.inject()
    }
}
