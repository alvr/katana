package dev.alvr.katana.core.tests.coroutines

import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.tests.di.TestAppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@Inject
@SingleIn(TestAppScope::class)
@OptIn(ExperimentalCoroutinesApi::class)
class TestKatanaDispatcher internal constructor() : KatanaDispatcher {
    override val main = UnconfinedTestDispatcher(name = "main")
    override val immediate = UnconfinedTestDispatcher(name = "immediate")
    override val io = UnconfinedTestDispatcher(name = "io")
    override val default = UnconfinedTestDispatcher(name = "default")
}
