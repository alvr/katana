package dev.alvr.katana.common.tests

import android.content.Context
import androidx.annotation.CallSuper
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@Config(minSdk = 28, maxSdk = 32)
@Suppress("UnnecessaryAbstractClass")
@RunWith(RobolectricTestRunner::class)
abstract class RoboTest {
    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    private val dispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(dispatcher)

    @Before
    @CallSuper
    open fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    @CallSuper
    open fun tearDown() {
        Dispatchers.resetMain()
    }

    fun runTest(test: suspend TestScope.() -> Unit) {
        testScope.runTest(testBody = test)
    }
}
