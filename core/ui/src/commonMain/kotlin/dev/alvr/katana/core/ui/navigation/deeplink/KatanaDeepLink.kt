package dev.alvr.katana.core.ui.navigation.deeplink

import dev.alvr.katana.core.ui.navigation.destinations.KatanaDestination
import kotlinx.serialization.Serializable

sealed interface KatanaDeepLink {
    data class Login(val token: String) : KatanaDeepLink
    data object Home : KatanaDeepLink

    @Serializable data class AnimeDetail(val mediaId: Int) : KatanaDeepLink, KatanaDestination
    @Serializable data class MangaDetail(val mediaId: Int) : KatanaDeepLink, KatanaDestination
}
