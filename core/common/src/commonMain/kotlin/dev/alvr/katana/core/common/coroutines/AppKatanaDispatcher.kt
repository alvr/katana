package dev.alvr.katana.core.common.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

internal class AppKatanaDispatcher : KatanaDispatcher {
    override val main = Dispatchers.Main
    override val io = Dispatchers.IO
    override val default = Dispatchers.Default
}
