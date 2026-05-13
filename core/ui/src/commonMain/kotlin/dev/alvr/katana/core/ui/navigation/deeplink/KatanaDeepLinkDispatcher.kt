package dev.alvr.katana.core.ui.navigation.deeplink

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface KatanaDeepLinkDispatcher {
    val pendingUrl: StateFlow<String?>
    fun dispatch(url: String)
    fun consume()
}

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
internal class KatanaDeepLinkDispatcherImpl : KatanaDeepLinkDispatcher {
    override val pendingUrl: StateFlow<String?>
        field = MutableStateFlow<String?>(null)

    override fun dispatch(url: String) {
        pendingUrl.update { url }
    }

    override fun consume() {
        pendingUrl.update { null }
    }
}
