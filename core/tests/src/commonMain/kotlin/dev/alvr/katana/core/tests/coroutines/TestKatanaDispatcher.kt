package dev.alvr.katana.core.tests.coroutines

import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
internal class TestKatanaDispatcher : KatanaDispatcher {
    override val main = StandardTestDispatcher(name = "main")
    override val io = UnconfinedTestDispatcher(name = "io")
    override val default = UnconfinedTestDispatcher(name = "default")
}
