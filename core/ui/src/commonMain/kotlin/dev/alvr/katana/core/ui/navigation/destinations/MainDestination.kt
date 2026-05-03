package dev.alvr.katana.core.ui.navigation.destinations

import kotlinx.serialization.Serializable

sealed interface MainDestination : KatanaDestination {

    @Serializable data class Home(val token: String? = null) : MainDestination

    @Serializable data object Anime : MainDestination

    @Serializable data object Manga : MainDestination

    @Serializable data object Explore : MainDestination

    @Serializable data object Account : MainDestination
}
