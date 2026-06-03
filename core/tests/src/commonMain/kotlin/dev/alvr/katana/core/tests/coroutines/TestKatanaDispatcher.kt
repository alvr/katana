package dev.alvr.katana.core.tests.coroutines

import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.tests.KatanaTestMainDispatcherExtension
import dev.alvr.katana.core.tests.di.TestAppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@Inject
@SingleIn(TestAppScope::class)
@OptIn(ExperimentalCoroutinesApi::class)
class TestKatanaDispatcher internal constructor() : KatanaDispatcher {
    private val scheduler = KatanaTestMainDispatcherExtension.scheduler

    override val main = UnconfinedTestDispatcher(scheduler, name = "main")
    override val immediate = UnconfinedTestDispatcher(scheduler, name = "immediate")
    override val io = UnconfinedTestDispatcher(scheduler, name = "io")
    override val default = UnconfinedTestDispatcher(scheduler, name = "default")
}
