package dev.alvr.katana.core.common.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal expect val ioDispatcher: CoroutineDispatcher

internal class AppKatanaDispatcher : KatanaDispatcher {
    override val main = Dispatchers.Main
    override val immediate = Dispatchers.Main.immediate
    override val io = ioDispatcher
    override val default = Dispatchers.Default
}
