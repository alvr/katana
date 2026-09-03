package dev.alvr.katana.core.ui.navigation

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Consumes an incoming deep link URI. Handlers are contributed per feature via Metro's
 * `@IntoSet` binding and dispatched from the root composable.
 */
fun interface DeepLinkHandler {
    /** @return true if this handler consumed [uri]. */
    fun handle(uri: String, navigator: KatanaNavigator): Boolean
}

/** Holds the last deep link URI received and the login token extracted from it. */
@Inject
@SingleIn(AppScope::class)
class DeepLinkState {
    private val _uri = MutableStateFlow<String?>(null)
    val uri: StateFlow<String?> = _uri.asStateFlow()

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token.asStateFlow()

    fun onDeepLink(uri: String) {
        _uri.value = uri
    }

    fun onToken(token: String) {
        _token.value = token
    }

    fun consumeUri() {
        _uri.value = null
    }

    fun consumeToken() {
        _token.value = null
    }
}
