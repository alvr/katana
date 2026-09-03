package dev.alvr.katana.features.home.ui.navigation

import dev.alvr.katana.core.ui.navigation.DeepLinkHandler
import dev.alvr.katana.core.ui.navigation.DeepLinkState
import dev.alvr.katana.core.ui.navigation.KatanaNavigator
import dev.zacsweers.metro.Inject

/** Handles `katana://app#access_token=` (Anilist OAuth redirect). */
@Inject
class HomeDeepLinkHandler(private val deepLinkState: DeepLinkState) : DeepLinkHandler {
    override fun handle(uri: String, navigator: KatanaNavigator): Boolean {
        val token = uri.substringAfter(AccessTokenParam, "").takeUnless(String::isEmpty) ?: return false

        deepLinkState.onToken(token)
        return true
    }

    private companion object {
        const val AccessTokenParam = "#access_token="
    }
}
