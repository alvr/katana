package dev.alvr.katana.core.tests.coroutines

import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@SingleIn(AppScope::class)
@OptIn(ExperimentalCoroutinesApi::class)
internal class TestKatanaDispatcher : KatanaDispatcher {
    override val main = UnconfinedTestDispatcher(name = "main")
    override val immediate = UnconfinedTestDispatcher(name = "immediate")
    override val io = UnconfinedTestDispatcher(name = "io")
    override val default = UnconfinedTestDispatcher(name = "default")
}
