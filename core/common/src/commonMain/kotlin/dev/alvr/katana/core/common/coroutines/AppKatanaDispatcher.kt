package dev.alvr.katana.core.common.coroutines

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
internal class AppKatanaDispatcher : KatanaDispatcher {
    override val main = Dispatchers.Main
    override val immediate = Dispatchers.Main.immediate
    override val io = Dispatchers.IO
    override val default = Dispatchers.Default
}
